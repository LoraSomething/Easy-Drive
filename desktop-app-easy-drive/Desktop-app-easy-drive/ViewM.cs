using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;

namespace Desktop_app_easy_drive
{
    public class ViewM : INotifyPropertyChanged
    {
        public ViewM(App app)
        {
            _app = app;
            DoTheThing = new AppCommand(new Action<object>(CallThing));
        }

        public event PropertyChangedEventHandler PropertyChanged;
        private string _testStr = "sdfasdf";
        private App _app;

        public void Notify(string name)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
        }

        public ICommand DoTheThing { get; set; }

        public string TestStr
        {
            get
            {
                return _testStr;
            }

            set
            {
                _testStr = value;
                Notify(nameof(TestStr));
            }
        }

        public void CallThing(object param)
        {
            _app.ShowMSG(_testStr);
        }
    }
}
