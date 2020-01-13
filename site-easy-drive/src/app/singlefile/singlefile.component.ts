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
    // multiplied by 1000 so that the argument is in milliseconds, not seconds.
    const date = new Date((this.userfile.lastModified) * 1000);
    return date;
    // Hours part from the timestamp
    const hours = date.getHours();
    // Minutes part from the timestamp
    const minutes = '0' + date.getMinutes();
    // Seconds part from the timestamp
    const seconds = '0' + date.getSeconds();

    // Will display time in 10:30:23 format
    // return hours + ':' + minutes.substr(-2) + ':' + seconds.substr(-2);
  }

}
