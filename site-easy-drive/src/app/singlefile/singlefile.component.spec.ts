import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SinglefileComponent } from './singlefile.component';

describe('SinglefileComponent', () => {
  let component: SinglefileComponent;
  let fixture: ComponentFixture<SinglefileComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SinglefileComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SinglefileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
