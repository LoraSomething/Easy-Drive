using DataAccess;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Web;
using System.Web.Http;

namespace EasyDriveAPI.Controllers
{
    public class DownloadController : ApiController
    {
        [HttpGet]
        public HttpResponseMessage Download(int id)
        {
            string localFilePath;

            Userfile u = DataAccess.DataAccess.getOneFile(id);

            localFilePath = HttpContext.Current.Server.MapPath("~/UploadedFiles" + "/" + u.storageID);          

            HttpResponseMessage response = new HttpResponseMessage(HttpStatusCode.OK);
            response.Content = new StreamContent(new FileStream(localFilePath, FileMode.Open, FileAccess.Read));
            response.Content.Headers.ContentDisposition = new System.Net.Http.Headers.ContentDispositionHeaderValue("attachment");
            response.Content.Headers.ContentDisposition.FileName = u.filename+"."+u.extension;
            response.Content.Headers.ContentType = new MediaTypeHeaderValue("application/"+u.extension);

            //byte[] fileBytes = System.IO.File.ReadAllBytes(localFilePath);
            //response.Content = new ByteArrayContent(fileBytes);
            //response.Content.Headers.ContentDisposition = new ContentDispositionHeaderValue("attachment");
            //response.Content.Headers.ContentDisposition.FileName = u.filename+"."+u.extension;
            //response.Content.Headers.ContentType = new MediaTypeHeaderValue("application/pdf");

            return response;
        }
    }
}
