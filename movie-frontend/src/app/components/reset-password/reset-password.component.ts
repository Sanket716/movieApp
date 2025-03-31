import { Component } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { ResetPassword, ResetPasswordService } from 'src/app/services/reset-password.service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent {

  oldPassword = new FormControl<string>('', [Validators.required]);
  newPassword = new FormControl<string>('', [Validators.required]);
  repeatPassword = new FormControl<string>('', [Validators.required]);

  email : string |null = "";

  resetPasswordForm: FormGroup;

  errorMessage: string | null = null; 

  successMessage: string | null = null;

  constructor(private formBuilder: FormBuilder,
    private authService:AuthService,
    private router:Router, 
    private resetPasswordService: ResetPasswordService){

      this.resetPasswordForm = this.formBuilder.group({
          oldPassword: this.oldPassword,
          newPassword: this.newPassword,
          repeatPassword: this.repeatPassword
      });

  }

  resetPassword(){
    //console.log(this.resetPasswordForm.value)
    this.email = this.authService.getEmail();
    //console.log(this.email)
    const resetPassword: ResetPassword = {
      oldPassword: this.resetPasswordForm.get('oldPassword')?.value,
      newPassword: this.resetPasswordForm.get('newPassword')?.value,
      repeatPassword: this.resetPasswordForm.get('repeatPassword')?.value,
    }

    this.resetPasswordService.resetPassword(resetPassword,this.email).subscribe(
      {
        next:(res:any)=>{
           this.successMessage = res;
        },
        error:(err:any)=>{
          this.errorMessage = err.error.detail || 'Reset Password Failed. Please try again.';
        }
      }
    )

  }

  cancel(){
    this.router.navigate(['home'])
  }

}
