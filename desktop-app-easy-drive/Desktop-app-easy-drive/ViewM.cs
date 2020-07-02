using Microsoft.Win32;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;
using System.Windows.Forms;
using System.Windows;
using System.Security;

namespace Desktop_app_easy_drive
{
    public class ViewM : INotifyPropertyChanged
    {
        public ViewM(App app)
        {
            _app = app;
            IGetFileURL = new AppCommand(new Action<object>(GetFileURL));
            IRename = new AppCommand(new Action<object>(Rename));
            IDownloadOne = new AppCommand(new Action<object>(DownloadOne));
            IDelete = new AppCommand(new Action<object>(DeleteFile));
            ILogin = new AppCommand(new Action<object>(Login));
            IRegister = new AppCommand(new Action<object>(Register));
            ILogout = new AppCommand(new Action<object>(Logout));
            ISetSynchFolder = new AppCommand(new Action<object>(SetSynchFolder));
            
            LoginOverlayVisibility = Visibility.Visible;
            StatusDisplay.SetVM(this);
            SynchFolder = Properties.Settings.Default.SynchFolder;

            Status = "Idle";
            
        }

        public event PropertyChangedEventHandler PropertyChanged;

        public void Notify(string name)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
        }

        public ICommand IGetFileURL { get; set; }
        public ICommand IRename { get; set; }
        public ICommand IDownloadOne { get; set; }
        public ICommand IDelete { get; set; }
        public ICommand ILogin { get; set; }
        public ICommand IRegister { get; set; }
        public ICommand ILogout { get; set; }
        public ICommand ISetSynchFolder { get; set; }

        private App _app;
        private List<Userfile> _userfiles = null;
        private Userfile _userfile;
        private Visibility _loginOverlayVisibility;
        private string _loginEmail;
        private string _userEmail;
        private string _synchFolder;
        private string _status;


        public SecureString SecurePassword { private get; set; }

        public List<Userfile> Userfiles
        {
            get
            {
                return _userfiles;
            }

            set
            {
                _userfiles = value;
                Notify(nameof(Userfiles));

            }
        }

        public Visibility LoginOverlayVisibility
        {
            get
            {
                return _loginOverlayVisibility;
            }

            set
            {
                _loginOverlayVisibility = value;
                Notify(nameof(LoginOverlayVisibility));

            }
        }

        public string Status
        {
            get
            {
                return _status;
            }

            set
            {
                _status = value;
                Notify(nameof(Status));

            }
        }

        public string UserEmail
        {
            get
            {
                return _userEmail;
            }

            set
            {
                _userEmail = value;
                Notify(nameof(UserEmail));

            }
        }

        public string LoginEmail
        {
            get
            {
                return _loginEmail;
            }

            set
            {
                _loginEmail = value;
                Notify(nameof(LoginEmail));

            }
        }


        public Userfile Userfile
        {
            get
            {
                return _userfile;
            }

            set
            {
                if (value != null)
                {
                    _userfile = new Userfile { _id = value._id, fileExtension = value.fileExtension, name = value.name };
                }
                else
                {
                    _userfile = null;
                }

                Notify(nameof(Userfile));
            }
        }

        public string SynchFolder
        {
            get
            {
                return _synchFolder;
            }

            set
            {
                _synchFolder = value;
                Notify(nameof(SynchFolder));

            }
        }
        

        public void GetFileURL(object param)
        {
            Microsoft.Win32.OpenFileDialog openFileDialog = new Microsoft.Win32.OpenFileDialog();
            if (openFileDialog.ShowDialog() == true)
                _app.CreateNewFile(openFileDialog.FileName);
        }

        public void SetSynchFolder(object param)
        {
            using (var dialog = new System.Windows.Forms.FolderBrowserDialog())
            {
                System.Windows.Forms.DialogResult result = dialog.ShowDialog();
                if (result == DialogResult.OK)
                {
                    SynchFolder = dialog.SelectedPath;
                    Properties.Settings.Default.SynchFolder = SynchFolder;
                    Properties.Settings.Default.Save();
                }
            }
        }

        public void Rename(object param)
        {
            if (_userfile != null)
            {
                _app.RenameOneFile(_userfile);
            }
        }

        public void DownloadOne(object param)
        {
            if (_userfile != null)
            {
                using (var dialog = new System.Windows.Forms.FolderBrowserDialog())
                {
                    System.Windows.Forms.DialogResult result = dialog.ShowDialog();
                    if (result == DialogResult.OK)
                    {
                        string folderName = dialog.SelectedPath;
                        
                        _app.DownloadOneFile(_userfile,folderName);
                    }
                }
            }
        }

        public void DeleteFile(object param)
        {
            if (_userfile != null)
            {
                _app.DeleteFile(_userfile);
            }
        }

        public void Login(object param)
        {
            _app.Login(_loginEmail, SecurePassword);
        }

        public void Register(object param)
        {
            System.Diagnostics.Process.Start("https://easy-drive-4251d.firebaseapp.com/");
        }

        public void Logout(object param)
        {
            _app.Logout();
            Properties.Settings.Default.SynchFolder = "";
            Properties.Settings.Default.Save();
            SynchFolder = "";
        }

    }
}
