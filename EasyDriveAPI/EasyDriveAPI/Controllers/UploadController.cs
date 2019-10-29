using DataAccess;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web;
using System.Web.Http;

namespace EasyDriveAPI.Controllers
{
    public class UploadController : ApiController
    {
        public async Task<HttpResponseMessage> PostFormData()
        {
            // Check if the request contains multipart/form-data.
            if (!Request.Content.IsMimeMultipartContent())
            {
                throw new HttpResponseException(HttpStatusCode.UnsupportedMediaType);
            }

            string root = HttpContext.Current.Server.MapPath("~/UploadedFiles");
            var provider = new MultipartFormDataStreamProvider(root);

            try
            {
                // Read the form data.
                await Request.Content.ReadAsMultipartAsync(provider);

                // This illustrates how to get the file names.
                foreach (MultipartFileData file in provider.FileData)
                {
                    Trace.WriteLine(file.Headers.ContentDisposition.FileName);
                    Trace.WriteLine("Server file path: " + file.LocalFileName);

                }

                string local_filename = provider.FileData.ElementAt(0).LocalFileName; //returns the local filename of the first uploaded file in the form
                var f = new FileInfo(local_filename);
                
                string orig_filename = provider.FileData.ElementAt(0).Headers.ContentDisposition.FileName.Replace("\"", ""); //returns the original filename of the first uploaded file in the form
                string[] s = orig_filename.Split('.');

                string dbfilextension = s[s.Length - 1];
                string dbfilename = orig_filename.Remove(orig_filename.Length-(dbfilextension.Length+1));

                var u = new Userfile { filename = dbfilename,storageID=f.Name,extension=dbfilextension};

                DataAccess.DataAccess.uploadFile(u);

                return Request.CreateResponse(HttpStatusCode.OK, "File uploaded succefully");
            }
            catch (System.Exception e)
            {
                return Request.CreateErrorResponse(HttpStatusCode.InternalServerError, e);
            }
        }
    }
}
