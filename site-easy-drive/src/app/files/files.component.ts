import { Component, OnInit } from '@angular/core';
import { UserFile } from '../file';
import { FileService } from '../file.service';

@Component({
  selector: 'app-files',
  templateUrl: './files.component.html',
  styleUrls: ['./files.component.scss']
})
export class FilesComponent implements OnInit {

  files: UserFile[];
  selectedFile: UserFile;

  ngOnInit() {
    this.getFiles();
  }
  constructor(private fileService: FileService) {
  }

  getFiles(): void {
    this.fileService.getFiles()
      .subscribe(files => this.files = files);
        
  }

  onSelect(file: UserFile){
    this.selectedFile = file;
  }

}
