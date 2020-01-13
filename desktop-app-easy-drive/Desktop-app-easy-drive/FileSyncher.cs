using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Threading;
using System.Windows;
using System.IO;
using System.Net;

namespace Desktop_app_easy_drive
{
    public class FileSyncher
    {
        private static FileSyncher fileSyncher;
        private ViewM _vm;
        private bool isActive = false;

        private enum Action
        {
            ADD,
            DELETE
        }

        private List<Tuple<Action, Userfile>> actionsClient;
        private List<Tuple<Action, string>> actionsServer;

        private FileSyncher()
        {

        }

        public static FileSyncher Instance
        {
            get
            {
                if (fileSyncher == null)
                {
                    fileSyncher = new FileSyncher();
                }

                return fileSyncher;
            }
        }

        public void Initialize(ViewM vm)
        {
            if (!isActive)
            {
                _vm = vm;
                isActive = true;
            }
            else
            {
                throw new Exception("File sycher already running");
            }
        }

        public void Start()
        {
            Task.Run(() =>
            {
                while (isActive)
                {
                    SynchronizeFiles();

                    Thread.Sleep(10000);
                }
            });
        }

        public void Stop()
        {
            isActive = false;
        }

        private void SynchronizeFiles()
        {
            var folder = Properties.Settings.Default.SynchFolder;

            if (!Directory.Exists(folder))
            {
                return;
            }

            if (_vm.Userfiles == null)
            {
                return;
            }

            var clientfiles = _vm.Userfiles.ToArray(); //getting a local copy of the userfiles

            var windowsfiles = Directory.GetFiles(folder); //getting a list of the files in the synch folder

        }

        private void ClientFilesVsWindows(List<Userfile> clientfiles, string[] windowsfiles)
        {
            foreach (string wf in windowsfiles)
            {
                string hash = Utils.CalculateMD5(wf);
                foreach (Userfile cf in clientfiles)
                {
                    if (hash == cf.md5hash)
                    {
                        if (Path.GetFileName(wf) == $"{cf.name}.{cf.fileExtension}")
                        {
                            //do nothing
                        }
                        else
                        {
                            actionsServer.Add(new Tuple<Action, string>(Action.ADD, wf));
                        }
                    }
                    else
                    {
                        actionsServer.Add(new Tuple<Action, string>(Action.ADD, wf));
                    }
                }
            }
            foreach (Userfile cf in clientfiles)
            {
                foreach (string wf in windowsfiles)
                {
                    string hash = Utils.CalculateMD5(wf);
                    if (hash == cf.md5hash)
                    {
                        if (Path.GetFileName(wf) == $"{cf.name}.{cf.fileExtension}")
                        {
                            //do nothing
                        }
                        else
                        {
                            actionsClient.Add(new Tuple<Action, Userfile>(Action.ADD, cf));
                        }
                    }
                    else
                    {
                        actionsClient.Add(new Tuple<Action, Userfile>(Action.ADD, cf));
                    }
                }
            }
        }

        private void ClientFilesVsServer(List<Userfile> clientfiles)
        {
            List<Userfile> serverfiles = null;
            DataAcces.GetAllFilesAsync((Action<List<Userfile>>)((res) =>
            {
                serverfiles = res;

            }));

            foreach (var sf in serverfiles)
            {
                foreach (var cf in clientfiles)
                {
                    if (cf._id == sf._id)
                    {
                        //do nothing
                    }
                    else
                    {
                        actionsClient.Add(new Tuple<Action, Userfile>(Action.ADD, sf));
                    }

                }
            }
            
        }
    }
}