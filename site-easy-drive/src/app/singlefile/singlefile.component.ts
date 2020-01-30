import { Component, OnInit, Input, Injectable } from '@angular/core';
import { UserFile } from '../UserFile';
import { FileService } from '../file.service';
import { DrawerService } from '../drawer.service';

@Component({
  selector: 'app-singlefile',
  templateUrl: './singlefile.component.html',
  styleUrls: ['./singlefile.component.scss']

})


export class SinglefileComponent implements OnInit {

  @Input() userfile: UserFile;

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

}
