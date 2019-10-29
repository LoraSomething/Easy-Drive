using DataAccess;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace EasyDriveAPI.Controllers
{
    public class GetAllFilesController : ApiController
    {
        [HttpGet]
        public List<Userfile> GetAll()
        {
            return DataAccess.DataAccess.getFiles();
        }
    }
}
