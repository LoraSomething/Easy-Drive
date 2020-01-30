import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  email: string;
  password: string;
  message: string;

  constructor(private authenticationService: AuthService, private router: Router) { }

  ngOnInit() {
  }

  logout() {
    this.authenticationService.Logout();
    this.router.navigate(['/login']);
  }

  async Login() {
    this.authenticationService.Login(this.email, this.password)
    .then(() => {
      this.message = 'Y';
    })
    .catch(() => {
      this.message = 'N';
    });
  }
}
