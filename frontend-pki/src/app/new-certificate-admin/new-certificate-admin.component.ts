import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Certificate } from '../model/certificate.model';
import { CertificateData } from '../model/certificateData.model';
import { CreateCertificate } from '../model/create-certificate';
import { CreateSelfSignedCertificate } from '../model/create-self-signed-certificate';
import { User } from '../model/user';
import { CertificateService } from '../service/certificate.service';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-new-certificate-admin',
  templateUrl: './new-certificate-admin.component.html',
  styleUrls: ['./new-certificate-admin.component.scss']
})
export class NewCertificateAdminComponent implements OnInit {

  constructor(private http: HttpClient, private userService: UserService, private certificateService: CertificateService) { }

  certificate: CreateCertificate = new CreateCertificate();
  selfSignedCertificate: CreateSelfSignedCertificate = new CreateSelfSignedCertificate();
  users: User[] = [];
  issuerCertificates: Certificate[]; 

  ngOnInit(): void {
    this.loadUsers();
    this.loadIssuerCertificates();
  }

  loadUsers(){
    this.userService.findAll().subscribe(
      (users: User[]) => {
        this.users = users;
      })
  }

  loadIssuerCertificates(){
    this.certificateService.findAllRootsAndCA().subscribe(
      (issuerCertificates: Certificate[]) => {
        this.issuerCertificates = issuerCertificates;
      })
  }

  selectSubject(){
    this.userService.findByEmail(this.certificate.subjectName).subscribe(
      (user: User) => {
        this.certificate.certificateDataDTO.emailAddress = this.certificate.subjectName
        this.certificate.certificateDataDTO.userId = user.id;
      })

      this.certificate.certificateDataDTO.emailAddress = this.certificate.subjectName

      // TODO:
      // this.allowedIssuerCertificates();
      // // ako se promeni subject (user) kome se sertifikat izdaje, mora opet proveriti lista sertifikata za potpisivanje
      // this.certificate.issuerName = "";
      // this.certificate.issuerSerialNumber = "";
  }

  allowedIssuerCertificates(){

  }

  selectIssuer(){
    console.log(this.certificate.issuerSerialNumber)
    this.certificateService.findUserByCertificateSerialNumber(this.certificate.issuerSerialNumber).subscribe(
      (issuer: User) => {
        this.certificate.issuerName = issuer.email;
        console.log(this.certificate.issuerName)
      })
  }

  createNewCertificate(){

    if(this.certificate.certificateType == "SELF_SIGNED"){
      this.selfSignedCertificate.certificateDataDTO = this.certificate.certificateDataDTO;
      this.selfSignedCertificate.certificateType = this.certificate.certificateType;
      this.selfSignedCertificate.certificateUsage = this.certificate.certificateUsage;
      this.selfSignedCertificate.issuerName = this.certificate.subjectName;   // jer je self-signed
      this.selfSignedCertificate.subjectName = this.certificate.subjectName;
      this.selfSignedCertificate.validFrom = this.certificate.validFrom;
      this.selfSignedCertificate.validTo = this.certificate.validTo;

      this.certificateService.issueSelfSignedCertificate(this.selfSignedCertificate).subscribe(
        (cer: CreateSelfSignedCertificate) => {}
      )
    } 
    else {
      console.log(this.certificate)

      this.certificateService.issueCertificate(this.certificate).subscribe(
        (cer: CreateCertificate) => {}
      )
    }
  }

}
