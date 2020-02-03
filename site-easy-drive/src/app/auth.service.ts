import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AngularFireAuth } from '@angular/fire/auth';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  authState: Observable<firebase.User>;

  constructor(private firebaseAuth: AngularFireAuth) {
    this.authState = firebaseAuth.authState;
   }

   Signup(email: string, password: string): Promise<any> {
    const prom = new Promise((result, reject) => {
    this.firebaseAuth
      .auth
      .createUserWithEmailAndPassword(email, password)
      .then(value => {
        console.log('Success!', value);
      })
      .catch(err => {
        console.log('Something went wrong:', err.message);
      });
    });
    return prom;
  }

  IsLoggedIn() {
    return this.authState;
 }

  Login(email: string, password: string): Promise<any> {
    const prom = new Promise((result, reject) => {
      this.firebaseAuth
      .auth
      .signInWithEmailAndPassword(email, password)
      .then(value => {
        result();
      })
      .catch(err => {
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

  GetUsername(): string {
    if (this.firebaseAuth.auth.currentUser.displayName != null) {
      return this.firebaseAuth.auth.currentUser.displayName;
    }
  }

  GetEmail(): string {
    if (this.firebaseAuth.auth.currentUser.email != null) {
      return this.firebaseAuth.auth.currentUser.email;
    }
  }

  Logout() {
    this.firebaseAuth
      .auth
      .signOut();
  }
}
