import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  email: string;
  password: string;
  message: string;

  constructor(private authService: AuthService ) { }

  ngOnInit() {
  }

  Register() {
    this.authService.Signup(this.email, this.password)
    .then(() => {
      // this.message = 'Y';
    })
    .catch(() => {
      this.message = 'Register failed. Please try again.';
    });

  }

}
