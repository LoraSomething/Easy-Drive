import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { AngularFireStorage, AngularFireUploadTask } from '@angular/fire/storage';
import { AngularFirestore } from '@angular/fire/firestore';
import { tap, finalize } from 'rxjs/operators';

@Component({
  selector: 'app-fileupload',
  templateUrl: './fileupload.component.html',
  styleUrls: ['./fileupload.component.scss']
})
export class FileuploadComponent implements OnInit {

  isHovering: boolean;
  files: File[] = [];

  constructor(public dialogRef: MatDialogRef<FileuploadComponent>, private storage: AngularFireStorage, private db: AngularFirestore) { }

  ngOnInit() {
  }

  uploadFileToActivity() {
  }

  closeModal() {
    this.dialogRef.close();
  }

  toggleHover(event: boolean) {
    this.isHovering = event;
  }

  onDrop(files: FileList) {
    for (let i = 0; i < files.length; i++) {
      this.files.push(files.item(i));
    }
  }

  upload(event) {
    this.files.push(event.target.files[0]);
  }
}
