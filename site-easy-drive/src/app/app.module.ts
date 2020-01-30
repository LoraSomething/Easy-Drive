import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FilesComponent } from './files/files.component';
import { HttpClientModule } from '@angular/common/http';
import { SinglefileComponent } from './singlefile/singlefile.component';
import { FileuploadComponent } from './fileupload/fileupload.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material.module';
import { AngularFireAuthModule } from 'angularfire2/auth';
import { AngularFireStorageModule } from 'angularfire2/storage';
import { environment } from '../environments/environment';
import { AngularFirestoreModule } from 'angularfire2/firestore';
import { AngularFireModule } from 'angularfire2';
import { DrawerService } from './drawer.service';
import { AuthService } from './auth.service';
import { DropzoneDirective } from './dropzone.directive';
import { UploadtaskComponent } from './uploadtask/uploadtask.component';
import { RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { AuthGuard } from './AuthGuard';
import { RegisterComponent } from './register/register.component';

@NgModule({
  declarations: [
    AppComponent,
    FilesComponent,
    SinglefileComponent,
    FileuploadComponent,
    DropzoneDirective,
    UploadtaskComponent,
    LoginComponent,
    RegisterComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    MaterialModule,
    BrowserAnimationsModule,
    AngularFireModule.initializeApp(environment.firebase),
    AngularFirestoreModule,
    AngularFireAuthModule,
    AngularFireStorageModule,
    ReactiveFormsModule,
    FormsModule,
    RouterModule.forChild([{
        path: '', component: AppComponent, canActivate: [AuthGuard]
    }, {
      path: 'login', component: LoginComponent
    }, {
      path: 'register', component: RegisterComponent
    },  {
      path: '**', redirectTo: ''
    }])
  ],
  bootstrap: [AppComponent],
  providers: [DrawerService, AuthService],
  entryComponents: [FileuploadComponent]
})
export class AppModule { }
