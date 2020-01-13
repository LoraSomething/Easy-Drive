using System;
using System.Collections.Generic;
using System.ComponentModel;
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
        private System.Windows.Forms.NotifyIcon _notifyIcon;
        private bool _isExit;


        private void OnStartup(object sender, StartupEventArgs e)
        {
            vm = new ViewM(this);
            var win = new MainWindow();
            win.Closing += MainWindow_Closing;

            win.DataContext = vm;
            win.Show();

            _notifyIcon = new System.Windows.Forms.NotifyIcon();
            _notifyIcon.DoubleClick += (s, args) => ShowMainWindow();
            _notifyIcon.Icon = Desktop_app_easy_drive.Properties.Resources.DriveIcon;
            _notifyIcon.Visible = true;

            Authentication.IsAuthenticatedAsync((Action<bool>)((res) =>
            {
                if (res)
                {
                    InitializeApp();
                    vm.LoginOverlayVisibility = Visibility.Hidden;
                }
                else
                {
                    vm.LoginOverlayVisibility = Visibility.Visible;
                    MessageBox.Show("Auth check failed");
                }
            }));

            CreateContextMenu();
            
            // settings to synch folder
        }

        private void CreateContextMenu()
        {
            _notifyIcon.ContextMenuStrip =
              new System.Windows.Forms.ContextMenuStrip();
            _notifyIcon.ContextMenuStrip.Items.Add("Open").Click += (s, e) => ShowMainWindow();
            _notifyIcon.ContextMenuStrip.Items.Add("Exit").Click += (s, e) => ExitApplication();
        }

        private void ExitApplication()
        {
            _isExit = true;
            MainWindow.Close();
            _notifyIcon.Dispose();
            _notifyIcon = null;
        }

        private void ShowMainWindow()
        {
            if (MainWindow.IsVisible)
            {
                if (MainWindow.WindowState == WindowState.Minimized)
                {
                    MainWindow.WindowState = WindowState.Normal;
                }
                MainWindow.Activate();
            }
            else
            {
                MainWindow.Show();
            }
        }

        private void MainWindow_Closing(object sender, CancelEventArgs e)
        {
            if (!_isExit)
            {
                e.Cancel = true;
                MainWindow.Hide(); // A hidden window can be shown again, a closed one not
            }
        }



        public void GetAllFiles()
        {
            StatusDisplay.Show("Synchronizing with server");
            DataAcces.GetAllFilesAsync((Action<List<Userfile>>)((res) =>
            {
                    vm.Userfiles = res;

            }));

        }

        public void CreateNewFile(string fileURL)
        {
            DataAcces.CreateFileAsync(fileURL, (Action<bool>)((res) =>
            {
                if (res)
                {
                    StatusDisplay.Show("File uploaded succesfully");
                }
                else
                {
                    StatusDisplay.Show("File upload failed");
                }
               
            }));

        }

        public void RenameOneFile(Userfile userfile)
        {
            DataAcces.RenameOneFileAsync(userfile);
            StatusDisplay.Show("Renaming file");
        }

        public void DownloadOneFile(Userfile userfile,string folderName)
        {
            DataAcces.DownloadOneFileAsync(userfile, (Action<string>)((res) =>
            {
                using (WebClient wc = new WebClient())
                {
                    string filename = userfile.name.Replace(":", "-");
                    string savepath = folderName + "\\" + filename + "." + userfile.fileExtension;
                    wc.DownloadFile(res, savepath);
                    StatusDisplay.Show("File downloaded");
                }
            }));
        }

        public void DeleteFile(Userfile userfile)
        {
            DataAcces.DeleteAsync(userfile, (Action<bool>)((res) =>
            {
                if (res)
                {
                    StatusDisplay.Show("File deleted succesfully");
                }
                else
                {
                    StatusDisplay.Show("File delete failed");
                }
            }));
        }
        
        public void InitializeApp()
        {
            vm.UserEmail = Authentication.GetEmail();
            DataAcces.SetChangeListner(snapshot =>
            {

                var a = snapshot.Changes;
                GetAllFiles();

            });

            GetAllFiles();

            FileSyncher.Instance.Initialize(vm);
            FileSyncher.Instance.Start();
        }

        public void Login(string email, SecureString password)
        {
            Authentication.AuthenticateUser(email, password, (Action<bool>)((res) =>
            {
                if (res)
                {
                    Authentication.IsAuthenticatedAsync((Action<bool>)((ress) =>
                    {
                        if (ress)
                        {
                            InitializeApp();
                            vm.LoginOverlayVisibility = Visibility.Hidden;
                        }
                        else
                        {
                            vm.LoginOverlayVisibility = Visibility.Visible;
                            MessageBox.Show("Auth check failed");
                        }
                    }));

                    //InitializeApp();
                    //vm.LoginOverlayVisibility = Visibility.Hidden;
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
            FileSyncher.Instance.Stop();
            vm.Userfiles = null;
            Authentication.Logout();
        }        
    }
}
