import { CertificateService } from './../service/certificate.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Certificate } from './../model/certificate.model';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-certificate',
  templateUrl: './certificate.component.html',
  styleUrls: ['./certificate.component.scss']
})
export class CertificateComponent implements OnInit {

  certificate : any;
  id:any; 
  expired: boolean = false;
  serialNum : string;

  constructor(private route: ActivatedRoute,
     private router: Router,
    private http : HttpClient,
    private certificateService: CertificateService) { }

  ngOnInit(): void {
    this.id = +this.route.snapshot.paramMap.get('id')!;

    this.certificateService.findById(this.id).subscribe((data) => {
      this.certificate = data;
      this.serialNum = data.serialNumber
      console.log(this.certificate)

      
    this.certificateService.findBySerialNumber(this.serialNum).subscribe(
      (issuer: Certificate) => {

        let date = <string> issuer.validTo
        let dateFinal =  date.substring(6) + '-' + date.substring(3,5) + '-' + date.substring(0,2)
        let maxDate = new Date(dateFinal)  
        this.expired = (maxDate < new Date) 
      })
  
    });
   
  }

  downloadCertificate() {
    this.http.get('http://localhost:8080/api/certificate/downloadCertificate/' + this.id)
    .subscribe(data => { 
      alert('Certificate is downloaded')
    });
  }

  revokeCertificate(serialNumber){
    console.log(serialNumber)
    this.http.get('http://localhost:8080/api/certificate/revokeCerificate/' + serialNumber)
    .subscribe(data => { 
      alert('Certificate is revoked')
    });
  }
}
