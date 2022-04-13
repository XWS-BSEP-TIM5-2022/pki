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
  userId: any;
  found: boolean;

  constructor(private route: ActivatedRoute,
    private router: Router,
    private http : HttpClient,
    private certificateService: CertificateService) { }

  ngOnInit(): void {

    let role = localStorage.getItem('role');
    if (role != "USER" && role!= "ADMIN"){
      this.router.navigate(['/login'])
      return;
    }

    this.id = +this.route.snapshot.paramMap.get('id')!;
    this.userId = localStorage.getItem('userId');

    if (role == "USER") {
      this.certificateService.findAllByUserId(this.userId).subscribe(
        (certificates: Certificate[]) => {

          for(let cer of certificates){
            if (cer.id == this.id){
              this.found = true;
            }
          }
    
          if (!this.found) {
              this.router.navigate(['/user-home'])
              return;
          }
        }
      );
    }  

    this.certificateService.findById(this.id).subscribe((data) => {
      this.certificate = data;
      //console.log(this.certificate)
    });
  }

  downloadCertificate() {
    // this.http.get('http://localhost:8080/api/certificate/downloadCertificate/' + this.id)
    this.certificateService.downloadCertificate(this.id)
    .subscribe(data => { 
      alert('Certificate is downloaded')
    },
    err => {
      alert('Certificate is revoked, cannot be downloaded')
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
