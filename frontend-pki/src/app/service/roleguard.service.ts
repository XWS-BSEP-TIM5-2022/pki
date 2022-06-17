import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router } from '@angular/router';
import { AuthguardService } from '././authguard.service';

@Injectable({
  providedIn: 'root'
})
export class RoleguardService {

  constructor(public authguardService : AuthguardService, public router: Router) {}
  canActivate(route: ActivatedRouteSnapshot): boolean {

    const expectedRole = route.data['expectedRole'];
    const token = localStorage.getItem('jwt');
    let currentRole : any = "";
    
    if (token != null){
      let jwtData = token.split('.')[1]
      let decodedJwtJsonData = window.atob(jwtData)
      let decodedJwtData = JSON.parse(decodedJwtJsonData)

      currentRole = localStorage.getItem('role')
    }


    if (!this.authguardService.gettoken() || !currentRole.includes(expectedRole)) {
      let role =  localStorage.getItem('role') 

      if(role === "ROLE_ADMIN"){

        this.router.navigate(['admin-home'])

      }else if(role === "ROLE_USER"){

        this.router.navigate(['user-home'])

      }else{ 

        this.router.navigate(['login']);
      }
      
      return false;
    }
    return true;
  }
  
}
