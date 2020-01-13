import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-fileupload',
  templateUrl: './fileupload.component.html',
  styleUrls: ['./fileupload.component.scss']
})
export class FileuploadComponent implements OnInit {

  uploadedfile: File;

  constructor() { }

  ngOnInit() {
  }

  uploadFileToActivity() {
  }
}
