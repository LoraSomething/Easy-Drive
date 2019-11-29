using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Linq;
using System.Net;
using System.Security;
using System.Threading.Tasks;
using System.Windows;

namespace Desktop_app_easy_drive
{
    /// <summary>
    /// Interaction logic for App.xaml
    /// </summary>
    public partial class App : Application
    {
        ViewM vm;
        private void OnStartup(object sender, StartupEventArgs e)
        {
            vm = new ViewM(this);
            var win = new MainWindow();
            win.DataContext = vm;
            win.Show();
            
        }

        public void GetAllFiles()
        {
            DataAcces.GetAllFilesAsync((Action<List<Userfile>>)((res) =>
            {
                    vm.Userfiles = res;
            }));

        }

        public void CreateNewFile(string fileURL)
        {
            DataAcces.CreateFileAsync(fileURL, (Action<bool>)((res) =>
            {
               MessageBox.Show("upload done");
            }));

        }

        public void RenameOneFile(Userfile userfile)
        {
            DataAcces.RenameOneFileAsync(userfile);
        }

        public void DownloadOneFile(Userfile userfile,string folderName)
        {
            DataAcces.DownloadOneFileAsync(userfile, (Action<string>)((res) =>
            {
                using (WebClient wc = new WebClient())
                {
                    wc.DownloadFileAsync(new System.Uri(res),folderName);
                    wc.DownloadFile(res, folderName + "\\" + userfile.name + "." + userfile.fileExtension);
                }
            }));
        }

        public void Login(string email, SecureString password)
        {
            Authentication.AuthenticateUser(email, password, (Action<bool>)((res) =>
            {
                if (res)
                {
                    
                    GetAllFiles();
                    vm.UserEmail = Authentication.GetEmail();
                    DataAcces.SetChangeListner(snapshot =>
                    {

                        var a = snapshot.Changes;
                        GetAllFiles();

                    });
                    vm.LoginOverlayVisibility = Visibility.Hidden;
                }
                else
                {
                    vm.LoginOverlayVisibility = Visibility.Visible;
                    MessageBox.Show("login failed");
                }
            }));
        }

        public void Logout()
        {
            vm.LoginOverlayVisibility = Visibility.Visible;
            vm.Userfiles = null;
            Authentication.Logout();
        }
        
    }
}
