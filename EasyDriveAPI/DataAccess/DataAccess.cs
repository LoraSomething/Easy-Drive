using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccess
{
    public class DataAccess
    {
        public List<Userfile> GetFiles()
        {
            using (var context = new EasyDriveEntities())
            {
                return context.Userfiles.ToList();
            }
        }

        public void UploadFile(Userfile u)
        {
            using (var context = new EasyDriveEntities())
            {
                context.Userfiles.Add(u);
                context.SaveChanges();
            }
        }
    }
}
