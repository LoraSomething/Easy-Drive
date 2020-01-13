using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Timers;

namespace Desktop_app_easy_drive
{
    public class StatusDisplay
    {
        public static ViewM _vm;

        public static void SetVM(ViewM vm)
        {
            _vm = vm;
        }

        public static void Show(string message)
        {
            Task.Run(() =>
            {
                _vm.Status = message;
                Thread.Sleep(2000);
                _vm.Status = "Idle";
            });
            
        }
    }
}
