using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Linq;
using System.Threading.Tasks;
using System.Windows;

namespace Desktop_app_easy_drive
{
    /// <summary>
    /// Interaction logic for App.xaml
    /// </summary>
    public partial class App : Application
    {
        private void OnStartup(object sender, StartupEventArgs e)
        {
            var vm = new ViewM(this);
            var win = new MainWindow();
            win.DataContext = vm;
            win.Show();
        }

        public void ShowMSG(string s)
        {
            MessageBox.Show(s);
        }
    }
}
