using DataAccess;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace EasyDriveAPI.Controllers
{
    public class GetOneFileController : ApiController
    {
        [HttpGet]
        public Userfile GetOneFile(int ID)
        {
            return DataAccess.DataAccess.getOneFile(ID);
        }
    }
}
