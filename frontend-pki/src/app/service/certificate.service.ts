import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Certificate } from '../model/certificate.model';

@Injectable({
  providedIn: 'root'
})
export class CertificateService {

  constructor(private _http:HttpClient) { }
  private readonly userPath = 'http://localhost:8080/api/certificate';


  findById(id:any) {
    return this._http.get<Certificate>(`${this.userPath}/findById/` + id)  
  }
}
