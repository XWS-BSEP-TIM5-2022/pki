import { Component, OnInit } from '@angular/core'; 
import { Router } from '@angular/router';
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
 
  constructor(private router: Router) { }

  ngOnInit(): void {
    this.rootCert.push(new Certificate())

  }

  review(id: any){
    this.router.navigate(['/certificate-review/'+ id]);
  }
  addAdmin() {
    this.router.navigate(['/new-admin'])
  }

}
