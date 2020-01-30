import { Component, Input, ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { DrawerService } from './drawer.service';
import { UserFile } from './UserFile';
import { MatDialogConfig, MatDialog } from '@angular/material/dialog';
import { FileuploadComponent } from './fileupload/fileupload.component';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';
import { FormsModule, ReactiveFormsModule, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})


export class AppComponent {
  title = 'Easy Drive';
  userLoggedIn = false;
  public clickedFile: UserFile;

  @ViewChild('drawer', {static: true}) public drawer: MatSidenav;

  // tslint:disable-next-line: use-lifecycle-interface
  ngOnInit() {
    this.drawerService.setDrawer(this.drawer);
  }

  childFileClicked(userfile: UserFile) {
    this.clickedFile = userfile;
    this.drawer.open();
  }

  constructor(private drawerService: DrawerService,
              public matDialog: MatDialog,
              private authenticationService: AuthService,
              private router: Router
    ) {
    const currentUser = authenticationService.IsLoggedIn();
    if (currentUser) {
            this.userLoggedIn = true;
        } else {
          this.userLoggedIn = false;
        }
  }

  openUploadModal() {
    const dialogRef = this.matDialog.open(FileuploadComponent, {
    });
  }
}
