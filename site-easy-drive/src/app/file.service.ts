import { Injectable } from '@angular/core';
import { UserFile } from './UserFile';
import { AngularFirestore, AngularFirestoreCollection, DocumentChangeAction } from 'angularfire2/firestore';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { User } from 'firebase';

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
}
