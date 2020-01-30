import { Component, OnInit, Output, EventEmitter  } from '@angular/core';
import { UserFile } from '../UserFile';
import { FileService } from '../file.service';
import { DrawerService } from '../drawer.service';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-files',
  templateUrl: './files.component.html',
  styleUrls: ['./files.component.scss']
})
export class FilesComponent implements OnInit {

  files: UserFile[];
  clickedFile: UserFile;

  @Output() fileClicked = new EventEmitter<UserFile>();

  ngOnInit() {
    this.drawer.drawerClosed.subscribe(() => {
      this.clickedFile = null;
    });

  }

  constructor(private fileService: FileService, private drawer: DrawerService, private authService: AuthService) {
    const context = this;
    authService.IsLoggedIn().subscribe({
      next(user) {
        if (user) {
          context.fileService.GetFiles().subscribe(doc => {
            context.files = doc;
            // console.log('Files: ', doc);
          });
        } else {
          context.files = null;
        }
      },
      error(err) {
        console.log('Error received', err);
       }
    });
  }

  onClick(userfile: UserFile) {
    this.fileClicked.emit(userfile);
    this.clickedFile = userfile;
  }

  getImage(fileExtension: string): string {
    if (fileExtension === 'jpg') {
      return 'assets/img/img-icon.png';
    } else if (fileExtension === 'pdf') {
      return 'assets/img/pdf-icon.png';
    } else {
      return 'assets/img/unknown-icon.png';
    }
  }

}

