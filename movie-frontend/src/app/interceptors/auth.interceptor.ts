import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, switchMap, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

@Injectable()
export class HttpInterceptorService implements HttpInterceptor {

  constructor(private authService: AuthService, private router: Router) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    console.log("Inside interceptor")

    if (
      req.url.includes('/login') ||
      req.url.includes('/register') ||
      req.url.includes('/refresh')
    ) {
      return next.handle(req);
    }

    let token = sessionStorage.getItem('accessToken');

    //console.log("Authentication:"+this.authService.isAuthenticated())
    //console.log("Token " + token)

    if (this.authService.isAuthenticated() && token) {
      req = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }

    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401 || error.status == 403) {
            this.authService.logout();
            this.router.navigate(['login']);
        }
        return throwError(() => error);
      })
    );
  }

  private handle401Error(req: HttpRequest<any>, next: HttpHandler) {
    return this.authService.refreshToken().pipe(
      switchMap((newToken: string) => {
        sessionStorage.setItem('accessToken', newToken);
        req = req.clone({
          setHeaders: { Authorization: `Bearer ${newToken}` }
        });
        return next.handle(req);
      }),
      catchError((err) => {
        this.authService.logout();
        this.router.navigate(['login']);
        return throwError(() => err);
      })
    );
  }
}
