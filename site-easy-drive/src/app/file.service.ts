import { Injectable } from '@angular/core';
import { UserFile } from './UserFile';
import { AngularFirestore, AngularFirestoreCollection, DocumentChangeAction } from 'angularfire2/firestore';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { User } from 'firebase';
import * as firebase from 'firebase/app';

@Injectable({
  providedIn: 'root'
})
export class FileService {

  userfiles: Observable<UserFile[]>;
  userfile: UserFile ;
  // collectionRef: AngularFirestoreCollection<UserFile>;

  constructor(private firestore: AngularFirestore) {

  }

  collectionRef = this.firestore.collection('Userfiles').doc('qGfGRfH6snT4ZaRbWHrEccpLT1x1').collection('files');

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

  CreateFile(userfile: UserFile) {
    this.collectionRef.add({
      _id: userfile._id,
      name: userfile.name,
      fileExtension: userfile.fileExtension,
      lastModified: userfile.lastModified,
      md5hash: userfile.md5hash}).then((docRef) => {
        this.collectionRef.doc(docRef.id).set({ _id: docRef.id }, { merge: true });
      });
  }

  UpdateFile(userfile: UserFile, newname: string) {
    this.collectionRef.doc(userfile._id).set({ name: newname }, { merge: true });
  }

  DeleteFile(userfile: UserFile) {
    this.collectionRef.doc(userfile._id).delete();
  }

}
