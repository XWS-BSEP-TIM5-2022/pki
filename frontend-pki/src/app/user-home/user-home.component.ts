import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Certificate } from '../model/certificate.model';

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

    let idLocalStorage = localStorage.getItem('userId')
    this.http.get('http://localhost:8080/api/users/getById/' + idLocalStorage)
    .subscribe(data => {
      this.user = data
      this.email = this.user.email
    })

    this.getCertificates();
  }

  review(id: any){
    this.router.navigate(['/certificate-review/'+ id]);
  }

  getCertificates(){

    this.http.get<Certificate[]>('http://localhost:8080/api/certificate/getAllByUser/' + localStorage.getItem('userId'))
    .subscribe(data => {
      var allCertificates : Certificate[] = data
      for(var c of allCertificates){
        if(c.certificateType === "INTERMEDIATE"){
          this.caCert.push(c);
        }else if (c.certificateType === "END_ENTITY"){
          this.endEntityCert.push(c)
        }
      }
    });
  }

}
