using Google.Cloud.Firestore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Desktop_app_easy_drive
{
    [FirestoreData]
    public class Userfile
    {
        [FirestoreProperty]
        public string name { get; set; }

        [FirestoreProperty]
        public string fileExtension { get; set; }

        [FirestoreProperty]
        public string _id { get; set; }

        public override string ToString()
        {
            return name + "." + fileExtension;
        }
    }
    
    
}
