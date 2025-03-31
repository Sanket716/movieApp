import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ResetPasswordService {

  public BASE_URL = "http://localhost:8080";

  http = inject(HttpClient);

  constructor() { }

  resetPassword(resetPassword: ResetPassword, email: string|null): Observable<string> {
    return this.http.post(`${this.BASE_URL}/resetPassword/${email}`, resetPassword, {
      responseType: 'text'
    });
  }


}

export type ResetPassword = {

  oldPassword: string,
  newPassword: string,
  repeatPassword: string

}
