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
  constructor(private route: ActivatedRoute,
     private router: Router,
    private http : HttpClient,
    private certificateService: CertificateService) { }

  ngOnInit(): void {
    this.id = +this.route.snapshot.paramMap.get('id')!;

    this.certificateService.findById(this.id).subscribe((data) => {
      this.certificate = data;
      console.log(this.certificate)
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
