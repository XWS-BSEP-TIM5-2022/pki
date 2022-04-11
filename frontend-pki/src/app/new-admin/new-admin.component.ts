import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-new-admin',
  templateUrl: './new-admin.component.html',
  styleUrls: ['./new-admin.component.scss']
})
export class NewAdminComponent implements OnInit {

  constructor(private http: HttpClient, private router: Router) { }

  ngOnInit(): void {
  }

  email = new FormControl('', [Validators.required, Validators.email]);
  getErrorMessage() {
    if (this.email.hasError('required')) {
      return 'You must enter a value';
    }

    return this.email.hasError('email') ? 'Not a valid email' : '';
  }

  password = new FormControl('', [Validators.required]);
  getPasswordErrorMessage() {
    if (this.password.hasError('required')) {
      return 'You must enter a value';
    }
    return;
  }

  password2 = new FormControl('', [Validators.required]);
  getPassword2ErrorMessage() {
    if (this.password2.hasError('required')) {
      return 'You must enter a value';
    }
    if(this.password.value != this.password2.value) {
      return 'Passwords do not match'
    }
    return;
  }

  isErrorPassword2: boolean = false;
  signUp() {
    if(this.password.value != this.password2.value) {
      alert('Passwords do not match')
      this.password.setValue('')
      this.password2.setValue('')
      return;
    }
    let user = {
      'email': this.email.value,
      'password': this.password.value,
      'userType': "ADMIN",
      'authorityType': "ROOT" // da li je admin i root?
    }
    let body = JSON.stringify(user)
    const headers = { 'content-type': 'application/json'} 
    this.http.post('http://localhost:8080/api/users/addAdmin', body, {'headers': headers })
    .subscribe(data => {
      this.router.navigate(['admin-home'])
    })
  }
}
