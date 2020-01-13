import { Injectable } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';

@Injectable({
  providedIn: 'root'
})
export class DrawerService {

  constructor() { }

  private drawer: MatSidenav;


  public setDrawer(drawer: MatSidenav) {
    this.drawer = drawer;
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
