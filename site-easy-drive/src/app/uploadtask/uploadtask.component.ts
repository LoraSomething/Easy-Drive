import { Component, OnInit, Input } from '@angular/core';
import { AngularFireUploadTask, AngularFireStorage } from '@angular/fire/storage';
import { Observable } from 'rxjs';
import { AngularFirestore } from 'angularfire2/firestore';
import { tap, finalize } from 'rxjs/operators';
import { FileService } from '../file.service';
import { UserFile } from '../UserFile';

@Component({
  selector: 'app-uploadtask',
  templateUrl: './uploadtask.component.html',
  styleUrls: ['./uploadtask.component.scss']
})
export class UploadtaskComponent implements OnInit {

  @Input() file: File;

  task: AngularFireUploadTask;

  done = false;

  percentage: Observable<number>;
  snapshot: Observable<any>;
  downloadURL: string;

  constructor(private storage: AngularFireStorage, private db: AngularFirestore, private fileService: FileService) { }

  ngOnInit() {
    this.startUpload();
  }

  startUpload() {

    // The storage path
    const path = `test/${Date.now()}_${this.file.name}`;

    // Reference to storage bucket
    const ref = this.storage.ref(path);

    // The main task
    this.task = this.storage.upload(path, this.file);

    // Progress monitoring
    this.percentage = this.task.percentageChanges();

    this.snapshot = this.task.snapshotChanges().pipe(
      // tap(console.log),
      // The file's download URL
      finalize( async () =>  {
        this.done = true;
        const n = this.file.name.lastIndexOf('.');
        const name = this.file.name.slice(0, n);
        const ext = this.file.name.substring(n + 1);
        const ufile = new UserFile('placeholder', name, ext, 'MD5HASH', Math.round((new Date()).getTime() / 1000)) ;
        this.fileService.CreateFile(ufile);
      }),
    );
  }

  isActive(snapshot) {
    return snapshot.state === 'running' && snapshot.bytesTransferred < snapshot.totalBytes;
  }


}
