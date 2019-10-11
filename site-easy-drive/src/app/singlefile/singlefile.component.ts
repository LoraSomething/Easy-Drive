import { Component, OnInit, Input } from '@angular/core';
import { UserFile } from '../file';
import { FileService } from '../file.service';
import { FilesComponent } from '../files/files.component';

@Component({
  selector: 'app-singlefile',
  templateUrl: './singlefile.component.html',
  styleUrls: ['./singlefile.component.scss']
})

export class SinglefileComponent implements OnInit {

  @Input() file: UserFile;

  constructor(private fileService: FileService) { }

  ngOnInit() {
  }

}
