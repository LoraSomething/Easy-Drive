import { Injectable } from '@angular/core';
import { UserFile } from './UserFile';
import { AngularFirestore, AngularFirestoreCollection, DocumentChangeAction } from 'angularfire2/firestore';
import { Observable, throwError } from 'rxjs';
import { map } from 'rxjs/operators';
import { AuthService } from './auth.service';
import { AngularFireStorage } from 'angularfire2/storage';

@Injectable({
  providedIn: 'root'
})
export class FileService {

  userfiles: Observable<UserFile[]>;
  userfile: UserFile ;
  uid = 'placeholder';
  isLoggedIn = false;
  collectionRef: AngularFirestoreCollection;

  constructor(private firestore: AngularFirestore, private authService: AuthService, private storage: AngularFireStorage) {
    const context = this;
    authService.IsLoggedIn().subscribe({
      next(user) {
        if (user) {
          context.uid = authService.GetUID();
          context.collectionRef = context.firestore.collection('Userfiles').doc(context.uid).collection('files');
          context.isLoggedIn = true;
        } else {
          context.uid = 'placeholder';
        }
      },
      error(err) {
        console.log('Error received', err);
       }
    });
  }

  GetFiles(): Observable<UserFile[]> {
       return this.collectionRef
        .snapshotChanges()
        .pipe(
          map((actions: DocumentChangeAction<UserFile>[]) => {
            return actions.map((a: DocumentChangeAction<UserFile>) => {
              const data: UserFile = a.payload.doc.data() as UserFile;
              return {...data };
            });
          }),
        );
  }

  CreateFile(userfile: UserFile): Promise<any> {
    const prom = new Promise((result, reject) => {
      this.collectionRef.add({
        _id: userfile._id,
        name: userfile.name,
        fileExtension: userfile.fileExtension,
        lastModified: userfile.lastModified,
        md5hash: userfile.md5hash}).then((docRef) => {
          this.collectionRef.doc(docRef.id).set({ _id: docRef.id }, { merge: true }).then(() => {
            result(docRef.id);
          }).catch(() => {
            reject();
          });
        });
      }
    );
    return prom;
  }

  UpdateFile(userfile: UserFile, newname: string) {
    this.collectionRef.doc(userfile._id).set({ name: newname }, { merge: true });
  }

  DeleteFile(userfile: UserFile) {
    this.collectionRef.doc(userfile._id).delete().then(() => {
    const path = `${this.authService.GetUID()}/${userfile._id}`;
    const ref = this.storage.ref(path).delete();
    }).catch(() => {
      console.log('Error occured on delete');
    });
  }

  GetDownloadLink(userfile: UserFile): Promise<any> {
    const path = `${this.authService.GetUID()}/${userfile._id}`;
    return this.storage.ref(path).getDownloadURL().toPromise();
  }
}
