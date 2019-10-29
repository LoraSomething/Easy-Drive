using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccess
{
    static public class DataAccess
    {
        static public List<Userfile> getFiles()
        {
            using (var context = new EasyDriveEntities())
            {
                return context.Userfiles.ToList();
            }
        }

        static public Userfile getOneFile(int ID)
        {
            using (var context = new EasyDriveEntities())
            {
                return context.Userfiles.FirstOrDefault(x => x.ID == ID);
            }
        }

        static public void uploadFile(Userfile u)
        {
            using (var context = new EasyDriveEntities())
            {
                context.Userfiles.Add(u);
                context.SaveChanges();
            }
        }

        static public Userfile renameOneFile(Userfile file)
        {
            using (var context = new EasyDriveEntities())
            {
                Userfile u = context.Userfiles.FirstOrDefault(x => x.ID == file.ID);
                if(u!=null)
                {
                    u.filename = file.filename;
                    context.SaveChanges();
                }

                return u;
            }
        }

        static public void deleteFile(int ID)
        {
            using (var context = new EasyDriveEntities())
            {
                Userfile u = context.Userfiles.FirstOrDefault(x => x.ID == ID);
                context.Userfiles.Remove(u);
            }
        }
    }
}
