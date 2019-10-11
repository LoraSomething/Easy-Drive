import { Component, OnInit } from '@angular/core';
import { FileuploadService } from '../fileupload.service';

@Component({
  selector: 'app-fileupload',
  templateUrl: './fileupload.component.html',
  styleUrls: ['./fileupload.component.scss']
})
export class FileuploadComponent implements OnInit {

  uploadedfile: File;

  constructor(private FileuploadService: FileuploadService) { }

  ngOnInit() {
  }

  onFileChange(upload) {
    this.uploadedfile = upload.target.files[0];
  }


  uploadFileToActivity() {
    this.FileuploadService.postFile(this.uploadedfile);
  }
}