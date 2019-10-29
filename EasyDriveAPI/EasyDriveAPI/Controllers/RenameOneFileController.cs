using DataAccess;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace EasyDriveAPI.Controllers
{
    public class RenameOneFileController : ApiController
    {
        [HttpPost]
        public Userfile RenameOneFile(Userfile file)
        {
            return DataAccess.DataAccess.renameOneFile(file);
        }
    }
}
