import { Injectable, Output } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DrawerService {

  constructor() { }

  private drawer: MatSidenav;

  @Output()
  drawerClosed;

  public setDrawer(drawer: MatSidenav) {
    this.drawer = drawer;
    this.drawerClosed = drawer.closedStart;
  }

  public open() {
    return this.drawer.open();
  }


  public close() {
    return this.drawer.close();
  }

  public toggle(): void {
    this.drawer.toggle();
  }
}
