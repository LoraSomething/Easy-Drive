import { Component, OnInit } from '@angular/core';
import { FileuploadComponent } from '../fileupload/fileupload.component';
import { MatDialogRef } from '@angular/material/dialog';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss']
})
export class AccountComponent implements OnInit {
  username: string;
  email: string;

  constructor(public dialogRef: MatDialogRef<FileuploadComponent>, private authService: AuthService) {
    const context = this;
    authService.IsLoggedIn().subscribe({
      next(user) {
        if (user) {
          context.username = authService.GetUsername();
          context.email = authService.GetEmail();
        } else {
          context.username = '';
          context.email = '';
        }
      },
      error(err) {
        console.log('Error received', err);
       }
    });
   }

  ngOnInit() {
  }

  logOut() {
    this.authService.Logout();
    this.dialogRef.close();
  }

}
