import { Component, signal } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {

  
  isLoggedIn = signal<boolean>(false)
  

  constructor(private authService: AuthService, private router : Router){
    
  }

  getName(): string|null {
    return sessionStorage.getItem('name');
  }

  ngOnInit():void{
     this.isLoggedIn = this.authService.getLoggedIn();
  }

  logout(){
     this.authService.logout();
     this.authService.setLoggedIn(false);
     this.router.navigate(['login'])
  }

  isAdmin():boolean{
    return this.authService.hasRole('ADMIN');
  }

}
