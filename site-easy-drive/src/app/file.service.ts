import { Injectable } from '@angular/core';
import { UserFile } from './file';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class FileService {

  // file1: File = {
  //   id: 1,
  //   name: 'spmething',
  //   location: 'something'
  // }

  
  constructor(private http: HttpClient) { }
  
  getFiles(): Observable<UserFile[]> {
    
    return this.http.get<UserFile[]>('http://localhost:3000/files');

  };

  getFilebyID(): Observable<UserFile[]> {

    return this.http.get<UserFile[]>('http://localhost:3000/files/fileID');

  };
}
