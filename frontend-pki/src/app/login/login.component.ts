import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

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

  user: any;
  login() {
    let u = {
      'email': this.email.value,
      'password': this.password.value
    }
    let body = JSON.stringify(u)
    const headers = { 'content-type': 'application/json'}
    this.http.post('http://localhost:9000/auth/login', body, {'headers': headers })
    .subscribe(data => {

      if(data == "NOT_FOUND"){
        alert('Invalid email and/or password')
      } 
      else{
        this.user = data
        localStorage.setItem('userId', this.user.id);
        localStorage.setItem('email', this.user.email)
        localStorage.setItem('role', this.user.userType);
        if(this.user.userType == "ADMIN"){
          this.router.navigate(['admin-home'])
        } else {
          this.router.navigate(['user-home'])
        }
      }
    },
    err => {
      alert('Invalid email and/or password')
    })

  }
}
