import { Component, OnInit } from '@angular/core'; 
import { Certificate } from '../model/certificate.model';

@Component({
  selector: 'app-admin-home',
  templateUrl: './admin-home.component.html',
  styleUrls: ['./admin-home.component.scss']
})
export class AdminHomeComponent implements OnInit {

  rootCert : Certificate[] = []
  caCert : Certificate[] = []
  endEntityCert : Certificate[] = []
 
  constructor() { }

  ngOnInit(): void {
    this.rootCert.push(new Certificate())

  }

}
