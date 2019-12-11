
using Firebase.Auth;
using Firebase.Storage;
using Google.Apis.Auth.OAuth2;
using Google.Cloud.Firestore;
using Google.Cloud.Storage.V1;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;
using System.Windows;

namespace Desktop_app_easy_drive
{
    class DataAcces
    {
         

        static public async void GetAllFilesAsync(Action<List<Userfile>> callback)
        {
            string userID = Authentication.GetUserID();

            FirestoreDb db = FirestoreDb.Create("easy-drive-4251d");

            CollectionReference collection = db.Collection("Userfiles").Document(userID).Collection("files");

            QuerySnapshot allFiles = await collection.GetSnapshotAsync();

            List<Userfile> ul = new List<Userfile>();

            foreach (DocumentSnapshot document in allFiles.Documents)
            {
                Userfile u = document.ConvertTo<Userfile>();
                ul.Add(u);
            }
            callback(ul);
            
        }

        public static async void CreateFileAsync(string url, Action<bool> callback)
        {
            string userID = Authentication.GetUserID();

            FirestoreDb db = FirestoreDb.Create("easy-drive-4251d");

            CollectionReference collection = db.Collection("Userfiles").Document(userID).Collection("files");



            Userfile userfile = new Userfile
            {
                name = System.IO.Path.GetFileNameWithoutExtension(url),
                fileExtension = System.IO.Path.GetExtension(url).Substring(1),
                md5hash = CalculateMD5(url)
            };
            
            DocumentReference document = await collection.AddAsync(userfile);

            string docId = document.Id;

            await document.SetAsync(
            new { _id = docId },
            SetOptions.MergeAll);

            try
            {
                var stream = File.Open(@url, FileMode.Open);
            
                string token = Authentication.GetToken();

                var task = new FirebaseStorage("easy-drive-4251d.appspot.com", new FirebaseStorageOptions
                {
                    AuthTokenAsyncFactory = () => Task.FromResult(token)
                })
                .Child(userID)
                    .Child(docId)
                    .PutAsync(stream);

                var downloadUrl = await task;

                callback(true);
            }
            catch(Exception exep)
            {
                await document.DeleteAsync();
                callback(false);
            }
        }

        static string CalculateMD5(string filename)
        {
            using (MD5 md5 = new MD5CryptoServiceProvider())
            {
                using (var stream = File.OpenRead(filename))
                {
                    var hash = md5.ComputeHash(stream);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < hash.Length; i++)
                    {
                        sb.Append(hash[i].ToString("x2"));
                    }
                    return sb.ToString();
                }
            }
        }

        public static async void RenameOneFileAsync(Userfile userfile)
        {
            string userID = Authentication.GetUserID();

            FirestoreDb db = FirestoreDb.Create("easy-drive-4251d");

            DocumentReference document = db.Collection("Userfiles").Document(userID).Collection("files").Document(userfile._id);

            await document.SetAsync(
            new { name = userfile.name },
            SetOptions.MergeAll);

        }

        public static async void DownloadOneFileAsync(Userfile userfile, Action<String> callback)
        {
            string userID = Authentication.GetUserID();
            var task = new FirebaseStorage("easy-drive-4251d.appspot.com").Child(userID).Child(userfile._id).GetDownloadUrlAsync();
            var downloadUrl = await task;
            callback(downloadUrl);
        }

        public static async void DeleteAsync(Userfile userfile, Action<String> callback)
        {
            string userID = Authentication.GetUserID();

            FirestoreDb db = FirestoreDb.Create("easy-drive-4251d");

            DocumentReference document = db.Collection("Userfiles").Document(userID).Collection("files").Document(userfile._id);
            

            try
            {
                var task = new FirebaseStorage("easy-drive-4251d.appspot.com").Child(userID).Child(userfile._id).DeleteAsync();
                await task;

                try
                {
                    await document.DeleteAsync();
                }
                catch(Exception e)
                {

                }

            }
            catch(Exception e)
            {

            }
            
        }

        public static void SetChangeListner(Action<QuerySnapshot> callback)
        {
            string userID = Authentication.GetUserID();
            FirestoreDb db = FirestoreDb.Create("easy-drive-4251d");

            CollectionReference collection = db.Collection("Userfiles").Document(userID).Collection("files");

            collection.Listen(callback);

        }

    }

}
