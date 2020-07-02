using Firebase.Auth;
using FirebaseAdmin;
using FirebaseAdmin.Auth;
using Google.Apis.Auth.OAuth2;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices;
using System.Security;
using System.Text;
using System.Threading.Tasks;
using System.Windows;

namespace Desktop_app_easy_drive
{
    static class Authentication
    {
        static private FirebaseToken firebaseToken;
        
        static public async void AuthenticateUser(string email, SecureString password, Action<bool> callback)
        {
            try
            {
                using (var firebaseAuth = new FirebaseAuthProvider(new FirebaseConfig(Properties.Settings.Default.APIKEY)))
                {

                    var firebaseAuthLink = await firebaseAuth.SignInWithEmailAndPasswordAsync(email, ConvertToUnsecureString(password));

                    Properties.Settings.Default.FirebaseToken = firebaseAuthLink.FirebaseToken;
                    Properties.Settings.Default.Save();

                    callback(true);

                    
                }

            }

            catch (Exception ex)
            {
                callback(false);
            }

        }

        static public void Logout()
        {
            Properties.Settings.Default.FirebaseToken = "";
            Properties.Settings.Default.Save();
        }

        static public async void IsAuthenticatedAsync(Action<bool> callback)
        {
            try
            {
                FirebaseApp.Create(new AppOptions()
                {
                    Credential = GoogleCredential.GetApplicationDefault(),
                });

            }
            catch(Exception ex)
            {

            }

            try
            {
                string token = Properties.Settings.Default.FirebaseToken;

                firebaseToken = await FirebaseAdmin.Auth.FirebaseAuth.DefaultInstance.VerifyIdTokenAsync(token);

                callback(true);

            }
            catch(Exception ex)
            {
                MessageBox.Show(ex.Message);
                callback(false);
            }
            
        }


        static public string GetUserID()
        {
            if (firebaseToken != null)
            {
               return firebaseToken.Uid;
            }
            else
            {
                return string.Empty;
            }
        }

        static public string GetEmail()
        {
            if (firebaseToken != null)
            {
                return firebaseToken.Claims["email"].ToString();
            }
            else
            {
                return string.Empty;
            }
        }

        static public string GetToken()
        {
            if (firebaseToken != null)
            {
                return Properties.Settings.Default.FirebaseToken;
            }
            else
            {
                return string.Empty;
            }
        }

        private static string ConvertToUnsecureString(this SecureString securePassword)
        {
            if (securePassword == null)
                throw new ArgumentNullException("securePassword");

            IntPtr unmanagedString = IntPtr.Zero;
            try
            {
                unmanagedString = Marshal.SecureStringToGlobalAllocUnicode(securePassword);
                return Marshal.PtrToStringUni(unmanagedString);
            }
            finally
            {
                Marshal.ZeroFreeGlobalAllocUnicode(unmanagedString);
            }
        }
    }
}
