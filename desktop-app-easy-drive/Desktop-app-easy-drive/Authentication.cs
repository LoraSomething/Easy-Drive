using Firebase.Auth;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices;
using System.Security;
using System.Text;
using System.Threading.Tasks;

namespace Desktop_app_easy_drive
{
    static class Authentication
    {
        static private FirebaseAuthLink firebaseAuthLink;
        
        static public async void AuthenticateUser(string email, SecureString password, Action<bool> callback)
        {
            try
            {
                using (var firebaseAuth = new FirebaseAuthProvider(new FirebaseConfig(Properties.Settings.Default.APIKEY)))
                {

                    firebaseAuthLink = await firebaseAuth.SignInWithEmailAndPasswordAsync(email, ConvertToUnsecureString(password));

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
            firebaseAuthLink = null;
            
        }

        static public bool IsAuthenticated()
        {
            if (firebaseAuthLink != null)
            {
                return true;
            }
            else
            {
                return false;
            }
        }


        static public string GetUserID()
        {
            if (firebaseAuthLink != null)
            {
               return firebaseAuthLink.User.LocalId;
            }
            else
            {
                return string.Empty;
            }
        }

        static public string GetEmail()
        {
            if (firebaseAuthLink != null)
            {
                return firebaseAuthLink.User.Email;
            }
            else
            {
                return string.Empty;
            }
        }

        static public string GetToken()
        {
            if (firebaseAuthLink != null)
            {
                return firebaseAuthLink.FirebaseToken;
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
