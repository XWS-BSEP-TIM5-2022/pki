import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

  isAdmin(){
    if(localStorage.getItem('role') == "ADMIN"){
      return true;
    } else {
      return false;
    }
  }

  isClient(){
    if(localStorage.getItem('role') == "USER"){
      return true;
    } else {
      return false;
    }
    
  }

}
