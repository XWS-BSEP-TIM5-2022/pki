import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Certificate } from '../model/certificate.model';

@Component({
  selector: 'app-new-certificate',
  templateUrl: './new-certificate.component.html',
  styleUrls: ['./new-certificate.component.scss']
})
export class NewCertificateComponent implements OnInit {

  constructor(private http: HttpClient) { }

  certificate: Certificate = new Certificate();

  ngOnInit(): void {
  }

  newCertificate(){
    console.log(this.certificate);
  }

  create() {
    let cert = {
      'id': 1,
      'revoked': false,
      'validFrom': new Date(),
      'validTo': new Date()
    }
    this.http.post('http://localhost:8080/api/certificate/issueCertificate', cert)
      .subscribe( data => {
        console.log(data)
      })
  }
}
