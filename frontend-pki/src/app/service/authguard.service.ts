import { Injectable } from '@angular/core';
import {JwtHelperService} from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class AuthguardService {

  constructor(private jwtHelper: JwtHelperService) { }

  gettoken(){  
    if (this.tokenIsPresent() && this.roleIsPresent() && !this.tokenIsExpired()){
      return true;
    }
    return false;
  }
 
  tokenIsPresent() {
    return localStorage.getItem("jwt") != undefined && localStorage.getItem("jwt") != null;
  }

  roleIsPresent(){
    return localStorage.getItem("role")!= undefined && localStorage.getItem("role") != null;
  }

  tokenIsExpired(){
    if (localStorage.getItem("jwt") != undefined && localStorage.getItem("jwt") != null)  {
      let locStorageToken = localStorage.getItem("jwt") 
      if (!locStorageToken){
        return true;
      } 
      return this.jwtHelper.isTokenExpired(locStorageToken);
    }
    return true;
  }
}
