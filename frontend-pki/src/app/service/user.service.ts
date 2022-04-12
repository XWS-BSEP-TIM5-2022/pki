import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private _http: HttpClient) { }
  private readonly userPath = 'http://localhost:8080/api/users';

  findAll() {
    return this._http.get<User[]>(`${this.userPath}/findAll`)  
  }

  findById(id: number) {
    return this._http.get<User>(`${this.userPath}/getById/` + id)    
  }

  findByEmail(email: string) {
    return this._http.get<User>(`${this.userPath}/getByEmail/` + email)    
  }
}
