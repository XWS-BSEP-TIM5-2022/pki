import { CertificateService } from './../service/certificate.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Certificate } from './../model/certificate.model';
import { Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common'
import { ThisReceiver } from '@angular/compiler';

@Component({
  selector: 'app-certificate',
  templateUrl: './certificate.component.html',
  styleUrls: ['./certificate.component.scss']
})
export class CertificateComponent implements OnInit {

  certificate : any;
  id:any;
  issuer: any = "";
  dto = {
    "serialNumber": "",
    "certType": ""
  }
  constructor(private route: ActivatedRoute,
     private router: Router,
    private http : HttpClient,
    private certificateService: CertificateService) { }

  ngOnInit(): void {
    this.id = +this.route.snapshot.paramMap.get('id')!;

    this.certificateService.findById(this.id).subscribe((data) => {
      this.certificate = data;
      console.log(this.certificate)
      this.dto.certType = data.certificateType;
      this.dto.serialNumber = data.serialNumber;
      var dto = {
        'serialNumber': data.serialNumber,
        'certType': data.certificateType
      }

      if(this.certificate.certificateType == "SELF_SIGNED"){
        this.certificate.certificateType = "SELF SIGNED"
      }  
      else if(this.certificate.certificateType == "END_ENTITY"){
        this.certificate.certificateType = "END ENTITY"
      }
    
      // let body = JSON.stringify(dto) 
      this.certificateService.findIssuerEmailBySerialNumber(dto).subscribe((data) => {
        this.issuer = data;
      });


    });


  
    
  }

  downloadCertificate() {
    this.http.get('http://localhost:8080/api/certificate/downloadCertificate/' + this.id)
    .subscribe(data => { 
      alert('Certificate is downloaded')
    });
  }

  isAdmin(){
    if(localStorage.getItem('role') == "ADMIN"){
      return true;
    } else {
      return false;
    }
  }

  revokeCertificate(serialNumber){
    console.log(serialNumber)
    this.http.get('http://localhost:8080/api/certificate/revokeCerificate/' + serialNumber)
    .subscribe(data => { 
      alert('Certificate is revoked')
    });
    this.router.navigate(['admin-home'])
  }
}
