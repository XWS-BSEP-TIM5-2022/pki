import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TestComponent } from './test/test.component';
import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { AdminHomeComponent } from './admin-home/admin-home.component';
import { UserHomeComponent } from './user-home/user-home.component';
import { CertificateComponent } from './certificate/certificate.component';
import { NewAdminComponent } from './new-admin/new-admin.component';

const routes: Routes = [
  {path: "", component: TestComponent},
  {path: "login", component: LoginComponent},
  {path: "registration", component: RegistrationComponent},
  {path: "admin-home", component: AdminHomeComponent},
  {path: "user-home", component: UserHomeComponent},
  {path: "certificate-review", component: CertificateComponent},
  {path: "new-admin", component: NewAdminComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
