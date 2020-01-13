import { Component, Input, ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { DrawerService } from './drawer.service';
import { UserFile } from './UserFile';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})


export class AppComponent {
  title = 'Easy Drive';
  showFiller = false;

  public clickedFile: UserFile;

  @ViewChild('drawer', {static: true}) public drawer: MatSidenav;

  ngOnInit() {
    this.drawerService.setDrawer(this.drawer);
  }

  childFileClicked(userfile: UserFile) {
    this.clickedFile = userfile;
    this.drawer.open();
  }

  constructor(private drawerService: DrawerService) {
  }

}
