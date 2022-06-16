import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TestComponent } from './test/test.component';
import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { AdminHomeComponent } from './admin-home/admin-home.component';
import { UserHomeComponent } from './user-home/user-home.component';
import { CertificateComponent } from './certificate/certificate.component';
import { NewAdminComponent } from './new-admin/new-admin.component';
import { NewCertificateComponent } from './new-certificate/new-certificate.component';
import { NewCertificateAdminComponent } from './new-certificate-admin/new-certificate-admin.component';
import { AccountActivationComponent } from './account-activation/account-activation.component';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { AuthentificationGuard } from './authentification.guard'
import { RoleguardService as RoleGuard } from './service/roleguard.service';

const routes: Routes = [
  {path: "", redirectTo: '/login', pathMatch: 'full'},
  {path: "login", component: LoginComponent},
  {path: "registration", component: RegistrationComponent},
  {
    path: "admin-home", 
    component: AdminHomeComponent,
    canActivate: [RoleGuard], 
    data: { 
      expectedRole: 'ROLE_ADMIN'  
    }
  },
  {
    path: "user-home", 
    component: UserHomeComponent,
    canActivate: [RoleGuard], 
    data: { 
      expectedRole: 'ROLE_USER'  
    }
  },
  {
    path: "certificate-review/:id", 
    component: CertificateComponent,
    canActivate:[AuthentificationGuard]
  },
  {
    path: "new-admin", 
    component: NewAdminComponent,
    canActivate: [RoleGuard], 
    data: { 
      expectedRole: 'ROLE_ADMIN'  
    }
  },
  {
    path: "new-certificate", 
    component: NewCertificateComponent,
    canActivate: [RoleGuard], 
    data: { 
      expectedRole: 'ROLE_USER'  
    }
  },
  {
    path: "new-certificate-admin", 
    component: NewCertificateAdminComponent,
    canActivate: [RoleGuard], 
    data: { 
      expectedRole: 'ROLE_ADMIN'  
    }
  },
  {path: "activate-account/:token", component: AccountActivationComponent},
  {
    path: "change-password", 
    component: ChangePasswordComponent,
    canActivate:[AuthentificationGuard]
  },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
