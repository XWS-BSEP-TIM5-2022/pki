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
  
  constructor() { }

  ngOnInit(): void {
    this.caCert.push(new Certificate())
  }

}
