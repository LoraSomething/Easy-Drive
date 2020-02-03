import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadtaskComponent } from './uploadtask.component';

describe('UploadtaskComponent', () => {
  let component: UploadtaskComponent;
  let fixture: ComponentFixture<UploadtaskComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UploadtaskComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UploadtaskComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
