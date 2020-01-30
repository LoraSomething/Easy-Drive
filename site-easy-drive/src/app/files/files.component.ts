import { Component, OnInit, Output, EventEmitter  } from '@angular/core';
import { UserFile } from '../UserFile';
import { FileService } from '../file.service';
import { DrawerService } from '../drawer.service';
import { JsonpClientBackend } from '@angular/common/http';

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
    this.fileService.GetFiles().subscribe(doc => {
      this.files = doc;
    });

    this.drawer.drawerClosed.subscribe(() => {
      this.clickedFile = null;
      console.log(this.clickedFile);
    });
  }

  constructor(private fileService: FileService, private drawer: DrawerService) {
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

