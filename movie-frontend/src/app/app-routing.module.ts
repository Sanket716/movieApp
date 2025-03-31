import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddMovieComponent } from './components/add-movie/add-movie.component';
import { ForgotPasswordComponent } from './components/forgot-password/forgot-password.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { ResetPasswordComponent } from './components/reset-password/reset-password.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' }, // Redirect root to login
  { path: 'login', title: "MovieApp - Login", component: LoginComponent },
  { path: 'register', title: "MovieApp - Register", component: RegisterComponent },
  { path: 'home', title: "MovieApp - Home", component: HomeComponent, canActivate: [authGuard] }, // Home route (optional)
  { path: 'forgot-password', title: "MovieApp - Forgot Password", component: ForgotPasswordComponent },
  { path: 'reset-password', title: "MovieApp - Reset Password", component: ResetPasswordComponent, canActivate: [authGuard] },
  { path: 'add-movie', title: "MovieApp - Add Movie", component: AddMovieComponent, canActivate: [authGuard] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
