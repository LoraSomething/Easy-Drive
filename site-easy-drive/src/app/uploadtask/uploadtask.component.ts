import { Component, OnInit, Input } from '@angular/core';
import { AngularFireUploadTask, AngularFireStorage } from '@angular/fire/storage';
import { Observable } from 'rxjs';
import { AngularFirestore } from 'angularfire2/firestore';
import { tap, finalize } from 'rxjs/operators';
import { FileService } from '../file.service';
import { UserFile } from '../UserFile';
import { AuthService } from '../auth.service';

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

  constructor(
    private storage: AngularFireStorage,
    private db: AngularFirestore,
    private fileService: FileService,
    private authService: AuthService) { }

  ngOnInit() {
    this.startUpload();
  }

  startUpload() {

    // The storage path

    const n = this.file.name.lastIndexOf('.');
    const name = this.file.name.slice(0, n);
    const ext = this.file.name.substring(n + 1);
    const ufile = new UserFile('placeholder', name, ext, 'MD5HASH', Math.round((new Date()).getTime() / 1000)) ;

    this.fileService.CreateFile(ufile).then((fileID) => {

    const path = `${this.authService.GetUID()}/${fileID}`;
    const ref = this.storage.ref(path);
    this.task = this.storage.upload(path, this.file);
    this.percentage = this.task.percentageChanges();
    this.snapshot = this.task.snapshotChanges().pipe(
      finalize( async () =>  {
        this.done = true;
      }),
    );
    }).catch(() => {console.log('File creation went wrong'); });
  }

  isActive(snapshot) {
    return snapshot.state === 'running' && snapshot.bytesTransferred < snapshot.totalBytes;
  }


}
