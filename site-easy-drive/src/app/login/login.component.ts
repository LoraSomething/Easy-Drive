import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  email: string;
  password: string;
  message: string;
  register = false;

  constructor(private authenticationService: AuthService) { }

  ngOnInit() {
  }

  async Login() {
    this.authenticationService.Login(this.email, this.password)
    .then(() => {
      this.message = 'Y';
    })
    .catch(() => {
      this.message = 'Login failed. Please try again.';
    });
  }

  registerStart() {
    this.register = true;
  }
}
