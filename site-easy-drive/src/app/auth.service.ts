import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AngularFireAuth } from '@angular/fire/auth';
import { first } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private firebaseAuth: AngularFireAuth) {
    this.user = firebaseAuth.authState;
   }

  user: Observable<firebase.User>;
  succes = false;

  public myCallback: (succes: boolean) => boolean;

   Signup(email: string, password: string) {
    this.firebaseAuth
      .auth
      .createUserWithEmailAndPassword(email, password)
      .then(value => {
        console.log('Success!', value);
      })
      .catch(err => {
        console.log('Something went wrong:', err.message);
      });
  }

  IsLoggedIn() {
    return this.user.pipe(first()).toPromise();
 }

  Login(email: string, password: string): Promise<any> {
    const prom = new Promise((result, reject) => {
      this.firebaseAuth
      .auth
      .signInWithEmailAndPassword(email, password)
      .then(value => {
        console.log('Nice, it worked!');
        result();
      })
      .catch(err => {
        console.log('Something went wrong:', err.message);
        reject();
      });
    });
    return prom;
  }

  GetUID(): string {
    if (this.firebaseAuth.auth.currentUser.uid != null) {
      return this.firebaseAuth.auth.currentUser.uid;
    }
  }

  Logout() {
    this.firebaseAuth
      .auth
      .signOut();
  }
}
