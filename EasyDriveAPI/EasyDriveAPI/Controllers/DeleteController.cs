using DataAccess;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web;
using System.Web.Http;
using System.IO;

namespace EasyDriveAPI.Controllers
{
    public class DeleteController : ApiController
    {
        [HttpPost]
        public void Delete(Userfile uf)
        {
            Userfile u = DataAccess.DataAccess.getOneFile(uf.ID);

            string file = HttpContext.Current.Server.MapPath("~/UploadedFiles"+"/"+u.storageID);

            if (File.Exists(file))
            {
                File.Delete(file);
                DataAccess.DataAccess.deleteFile(uf.ID);
            }
            else
            {
                var responseMessage = new HttpResponseMessage(HttpStatusCode.NotFound);
                responseMessage.Content = new StringContent("The file path which you requested is not found");
                throw new HttpResponseException(responseMessage);
            }
        }
    }
}
