import { HttpClient } from '@angular/common/http';
import { Injectable, signal, WritableSignal } from '@angular/core';
import { jwtDecode } from 'jwt-decode';
import { catchError, Observable, tap, throwError } from 'rxjs';

const BASE_URL = "http://localhost:8080";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private loggedIn = signal<boolean>(this.isAuthenticated())

  constructor(private httpClient : HttpClient) { }

  register(registerRequest: RegisterRequest): Observable<AuthResponse>{
    return this.httpClient.post<AuthResponse>(`${BASE_URL}/api/v1/auth/register`,registerRequest);
  }

  login(loginRequest: LoginRequest): Observable<AuthResponse>{
    return this.httpClient.post<AuthResponse>(`${BASE_URL}/api/v1/auth/login`,loginRequest)
    .pipe(tap(res=>{
      if(res && res.accessToken)
      {
        sessionStorage.setItem('accessToken',res.accessToken);
        sessionStorage.setItem('refreshToken',res.refreshToken);
        sessionStorage.setItem('name',res.name);
        sessionStorage.setItem('username',res.username);
        sessionStorage.setItem('email',res.email);

        const decodedToken: any = jwtDecode(res.accessToken);
        sessionStorage.setItem('role',decodedToken.role[0].authority)
      }
    }))
  }

  isAuthenticated():boolean{

    return !!sessionStorage.getItem('accessToken');

  }

  getEmail(): string|null{

    return sessionStorage.getItem('email');

  }

  logout():void{
    sessionStorage.removeItem('accessToken');
    sessionStorage.removeItem('refreshToken');
    sessionStorage.removeItem('name');
    sessionStorage.removeItem('email');
    sessionStorage.removeItem('username');
  }

  setLoggedIn(value:boolean)
  {
    this.loggedIn.set(value);
  }

  getLoggedIn():WritableSignal<boolean>{
    return this.loggedIn;
  }

  isTokenExpired(token: string): boolean{
    const decodedToken:any = jwtDecode(token);
    return (decodedToken.exp * 1000) < Date.now();
  }

  refreshToken():Observable<any>{
    const refreshToken = sessionStorage.getItem('refreshToken');
    const refreshTokObj = {
      refreshToken: refreshToken
    }
    return this.httpClient.post(`${BASE_URL}/api/v1/auth/refresh`,{refreshTokObj})
                          .pipe(tap((res:any) => sessionStorage.setItem('accessToken',res.accessToken)),
                          catchError((err:any)=>{
                            this.logout();
                            return throwError(()=>err);
                          }))

  }

  hasRole(role:string):boolean{
    const token = sessionStorage.getItem('accessToken');
    if(token){
      const decodedToken: any = jwtDecode(token);
      return decodedToken?.role[0]?.authority.includes(role);
    }

    return false;

  }
}


export type RegisterRequest = {
  name: string,
  email: string,
  username: string,
  password: string

}

export type LoginRequest = {
  email: string,
  password: string
}

export type AuthResponse = {
  accessToken: string,
  refreshToken: string,
  name: string,
  email: string,
  username: string

}

export type RefreshTokenRequest = {
  refreshToken: string|null
}
