import { Certificate } from './../model/certificate.model';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-certificate',
  templateUrl: './certificate.component.html',
  styleUrls: ['./certificate.component.scss']
})
export class CertificateComponent implements OnInit {

  certificate : Certificate;
  constructor() { }

  ngOnInit(): void {
  }

}
