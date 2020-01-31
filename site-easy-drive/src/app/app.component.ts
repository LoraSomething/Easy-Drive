import { Component, Input, ViewChild, ChangeDetectorRef } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { DrawerService } from './drawer.service';
import { UserFile } from './UserFile';
import { MatDialogConfig, MatDialog } from '@angular/material/dialog';
import { FileuploadComponent } from './fileupload/fileupload.component';
import { AuthService } from './auth.service';
import { AccountComponent } from './account/account.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})


export class AppComponent {
  userLoggedIn = false;
  public clickedFile: UserFile;

  @ViewChild('drawer', {static: true}) public drawer: MatSidenav;

  // tslint:disable-next-line: use-lifecycle-interface
  ngOnInit() {
    this.userLoggedIn = false;
    this.drawerService.setDrawer(this.drawer);
  }

  childFileClicked(userfile: UserFile) {
    this.clickedFile = userfile;
    this.drawer.open();
  }

  constructor(private drawerService: DrawerService,
              public matDialog: MatDialog,
              private authenticationService: AuthService
    ) {
    const context = this;
    authenticationService.IsLoggedIn().subscribe({
      next(user) {
        if (user) {
          context.userLoggedIn = true;
          // console.log('User is logged in');
        } else {
          context.userLoggedIn = false;
          // console.log('No user');
        }
      },
      error(err) {
        console.log('Error received', err);
       }
    });
  }

  openUploadModal() {
    const dialogRef = this.matDialog.open(FileuploadComponent);
  }

  openAccountModal() {
    const dialogRef = this.matDialog.open(AccountComponent);
  }
}
