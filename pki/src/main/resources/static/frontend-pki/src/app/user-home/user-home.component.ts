import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
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
  
  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.caCert.push(new Certificate())
    
    let urlArray = window.location.href.split('/')
    let id = urlArray[urlArray.length - 1]
    this.http.get('http://localhost:8080/api/users/getById/' + id)
    .subscribe(data => {
      this.user = data
      this.email = this.user.email
    })
  }

}
