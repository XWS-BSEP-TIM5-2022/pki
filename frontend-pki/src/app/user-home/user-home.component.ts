import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Certificate } from '../model/certificate.model';
import { User } from '../model/user';

@Component({
  selector: 'app-user-home',
  templateUrl: './user-home.component.html',
  styleUrls: ['./user-home.component.scss']
})
export class UserHomeComponent implements OnInit {

  caCert : Certificate[] = []
  endEntityCert : Certificate[] = []
  email: string = '';
  user: any;

  constructor(private router: Router, private http: HttpClient) { }


  ngOnInit(): void {

    let role = localStorage.getItem('role');
    if (role == "ROLE_ADMIN"){
      this.router.navigate(['/admin-home'])
      return;
    } 
    else if (role != "ROLE_USER" && role!= "ROLE_ADMIN"){
      this.router.navigate(['/login'])
      return;
    }

    this.email = localStorage.getItem('user') || ""
    this.http.get('https://localhost:9000/api/users/getByEmail/' + this.email)
    .subscribe(data => {
      this.user = data
      this.email = this.user.email
      // alert()
      this.getCertificates(this.user.id);
    })

    // this.getCertificates();s
  }

  review(id: any){
    this.router.navigate(['/certificate-review/'+ id]);
  }

  getCertificates(id: number){


    this.http.get<Certificate[]>('https://localhost:9000/api/certificate/getAllByUser/' + id)
    .subscribe(data => {
      var allCertificates : Certificate[] = data
      for(var c of allCertificates){
        if(c.certificateType === "INTERMEDIATE"){
          // console.log(c)
          this.caCert.push(c);
        }else if (c.certificateType === "END_ENTITY"){
          this.endEntityCert.push(c)
        }
      }
    });
  }

  changePassword() {
    this.router.navigate(['change-password'])
  }
}
