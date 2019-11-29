using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace Desktop_app_easy_drive
{
    /// <summary>
    /// Interaction logic for LoginOverlay.xaml
    /// </summary>
    public partial class LoginOverlay : UserControl
    {
        public LoginOverlay()
        {
            InitializeComponent();
        }

        private void PasswordBox_PasswordChanged(object sender, RoutedEventArgs e)
        {
            if (this.DataContext != null)
            { ((ViewM)this.DataContext).SecurePassword = ((PasswordBox)sender).SecurePassword; }
        }
        
    }
    
}
