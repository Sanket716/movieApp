import { Component } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService, LoginRequest } from 'src/app/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  email = new FormControl<string>('', [Validators.required, Validators.email]);
  password = new FormControl<string>('', [Validators.required, Validators.minLength(5)]);

  loginForm: FormGroup;

  inlineNotification = {
    show: false,
    type: '',
    text: '',
  };

  errorMessage: string | null = null; 

  constructor(private formBuilder: FormBuilder,
    private authService:AuthService,
    private router:Router
  ) {
    this.loginForm = this.formBuilder.group({
      email: this.email,
      password: this.password,
    });
  }

  login(){
    console.log(this.loginForm.value);
    if(this.loginForm.valid){
      const loginRequest: LoginRequest ={
        email: this.loginForm.get('email')?.value,
        password: this.loginForm.get('password')?.value
      }
      this.authService.login(loginRequest).subscribe({
        next:(res:any)=>{
          console.log(res);
          this.authService.setLoggedIn(true)
          this.router.navigate(['home'])
        },
        error:(err:any)=>{
          console.log(err);
          this.loginForm.reset();
          this.errorMessage = err.error.detail || 'Login failed. Invalid email or password.';
        }
      })
    } 
    else{
      this.inlineNotification = {
        show: true,
        type: 'error',
        text: 'Please fill all the required fields',
      };
    }   
  }

}
