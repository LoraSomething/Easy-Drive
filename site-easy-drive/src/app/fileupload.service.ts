import { Injectable } from '@angular/core';
import { UserFile } from './file';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class FileuploadService {

  constructor(private http: HttpClient) { }

  postFile(fileToUpload: File){
    const formData: FormData = new FormData();
    formData.append('fileKey', fileToUpload, fileToUpload.name);
    console.log(formData);
    this.http.post<File>('http://localhost:3000/files/upload', formData);
  }
}