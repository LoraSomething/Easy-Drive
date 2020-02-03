import { Component, OnInit, Input, Injectable, ViewChild } from '@angular/core';
import { UserFile } from '../UserFile';
import { FileService } from '../file.service';
import { DrawerService } from '../drawer.service';
import { MatSidenav } from '@angular/material/sidenav';

@Component({
  selector: 'app-singlefile',
  templateUrl: './singlefile.component.html',
  styleUrls: ['./singlefile.component.scss']

})


export class SinglefileComponent implements OnInit {

  @Input() userfile: UserFile;
  editDisabled = true;
  downloadUrl: string;

  constructor(private fileService: FileService, private drawer: DrawerService) { }

  ngOnInit() {
  }

  getTime(): Date {
    const date = new Date((this.userfile.lastModified) * 1000);
    return date;
  }

  deleteFile() {
    this.fileService.DeleteFile(this.userfile);
    this.drawer.close();
  }

  editFileName() {
    this.editDisabled = false;
  }

  saveChanges() {
    this.editDisabled = true;
    this.fileService.UpdateFile(this.userfile, this.userfile.name);
  }

  getDownloadLink() {
    this.fileService.GetDownloadLink(this.userfile).then((link) => {
      // this.downloadUrl = link;
      const a = document.createElement('a');
      a.download = `${this.userfile.name}.${this.userfile.fileExtension}`;
      a.href = link;
      a.click();
    });
  }

}
