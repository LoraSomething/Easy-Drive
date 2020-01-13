import { Component, OnInit, Output, EventEmitter  } from '@angular/core';
import { UserFile } from '../UserFile';
import { FileService } from '../file.service';
import { DrawerService } from '../drawer.service';

@Component({
  selector: 'app-files',
  templateUrl: './files.component.html',
  styleUrls: ['./files.component.scss']
})
export class FilesComponent implements OnInit {

  files: UserFile[];

  @Output() fileClicked = new EventEmitter<UserFile>();

  ngOnInit() {
    this.fileService.GetFiles().subscribe(doc => {
      this.files = doc;
      console.log(doc);
    });
  }

  constructor(private fileService: FileService, private drawer: DrawerService) {
  }

  onClick(userfile: UserFile) {
    this.fileClicked.emit(userfile);
  }

  getImage(fileExtension: string): string {
    if (fileExtension === 'jpg') {
      return 'assets/img/img-icon.png';
    } else if (fileExtension === 'pdf') {
      return 'assets/img/pdf-icon.png';
    }
  }

}

