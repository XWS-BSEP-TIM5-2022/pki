import { Component, OnInit } from '@angular/core';
import { Certificate } from '../model/certificate.model';

@Component({
  selector: 'app-new-certificate',
  templateUrl: './new-certificate.component.html',
  styleUrls: ['./new-certificate.component.scss']
})
export class NewCertificateComponent implements OnInit {

  constructor() { }

  certificate: Certificate = new Certificate();

  ngOnInit(): void {
  }

  newCertificate(){
    console.log(this.certificate);
  }
}
