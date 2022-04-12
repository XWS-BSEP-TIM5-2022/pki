import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Certificate } from '../model/certificate.model';
import { CreateCertificate } from '../model/create-certificate';
import { CreateSelfSignedCertificate } from '../model/create-self-signed-certificate';
import { User } from '../model/user';
import { CertificateService } from '../service/certificate.service';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-new-certificate',
  templateUrl: './new-certificate.component.html',
  styleUrls: ['./new-certificate.component.scss']
})
export class NewCertificateComponent implements OnInit {

  constructor(private http: HttpClient, private userService: UserService, private certificateService: CertificateService) { }

  certificate: CreateCertificate = new CreateCertificate();
  users: User[] = [];
  issuerCertificates: Certificate[]; 
  selectedIssuerCertificate: Certificate;
  user: any = new User();
  userId: number; 
  loadedUser: boolean = false;

  ngOnInit(): void {

    let idLocalStorage = localStorage.getItem('userId')
    this.http.get('http://localhost:8080/api/users/getById/' + idLocalStorage)
    .subscribe(data => {
      this.user = data
      this.userId = this.user.id;
      this.loadedUser = true;

      this.loadUsers();
      this.loadIssuerCertificates();
    })
  }

  loadUsers(){
    this.userService.findAll().subscribe(
      (users: User[]) => {
        this.users = users;
      })
  }

  loadIssuerCertificates(){    
    this.certificateService.findAllRootAndCAByUser(this.userId).subscribe(    
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
  }

  selectIssuer(){
    this.certificate.validTo = undefined;
    this.certificate.validFrom = undefined;

    this.certificateService.findUserByCertificateSerialNumber(this.certificate.issuerSerialNumber).subscribe(
      (issuer: User) => {
        this.certificate.issuerName = issuer.email;
      })

    this.certificateService.findCertificateBySerialNumber(this.certificate.issuerSerialNumber).subscribe(
      (certificate: Certificate) => {
        this.selectedIssuerCertificate = certificate;
      })   
      
      this.checkIssuerCertificateDuration();  
  }

  checkDates(){
    if(this.certificate.validFrom != undefined && this.certificate.validTo != undefined){
      if(this.certificate.validFrom > this.certificate.validTo){
        this.certificate.validTo = undefined;
        this.certificate.validFrom = undefined;
      }
    }

    this.checkIssuerCertificateDuration();
  }

  checkIssuerCertificateDuration(){
    
    if (this.selectedIssuerCertificate != undefined &&
        this.certificate.validFrom != undefined && this.certificate.validTo != undefined){
     
        let issuerValidFrom = Number(this.selectedIssuerCertificate.validFrom);
        let issuerValidTo = Number(this.selectedIssuerCertificate.validTo);

        let validFrom: number = new Date(this.certificate.validFrom).getTime();
        let validTo: number = new Date(this.certificate.validTo).getTime();
        
        // console.log(validFrom);   
        // console.log(validTo);   

        // console.log(issuerValidFrom);  
        // console.log(issuerValidTo);  

        if ( validFrom < issuerValidFrom || validTo > issuerValidTo){
          this.certificate.validTo = undefined;
          this.certificate.validFrom = undefined;

          console.log('datumi od-kad do-kad je sertifikat validan nisu ispravni')
        }
    }
  }

  createNewCertificate(){

    // TODO: nisu pokrivene sve situacije, npr. ako je neko polje prazan string
    if(this.certificate.certificateType != undefined && this.certificate.certificateUsage != undefined &&
        this.certificate.issuerName != undefined && this.certificate.issuerSerialNumber != undefined &&
        this.certificate.subjectName != undefined && this.certificate.validFrom != undefined && 
        this.certificate.validTo != undefined && 
        this.certificate.certificateDataDTO.commonName != undefined &&
        this.certificate.certificateDataDTO.countryCode != undefined && this.certificate.certificateDataDTO.emailAddress != undefined &&
        this.certificate.certificateDataDTO.givenName != undefined && this.certificate.certificateDataDTO.organization != undefined &&
        this.certificate.certificateDataDTO.organizationalUnit != undefined && this.certificate.certificateDataDTO.surname != undefined &&
        this.certificate.certificateDataDTO.userId != undefined){

      this.certificateService.issueCertificate(this.certificate).subscribe(
        (cer: CreateCertificate) => {}
      )      
    } 
    else {
      console.log('nisu sva polja popunjena');
    }
  }
}
