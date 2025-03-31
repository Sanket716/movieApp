import { Component, inject, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { MovieDto, MovieService } from 'src/app/services/movie.service';
import {MatDialog} from "@angular/material/dialog";
import { UpdateMovieComponent } from '../update-movie/update-movie.component';
import { DeleteMovieComponent } from '../delete-movie/delete-movie.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit{
  

  movieService = inject(MovieService);
  authService = inject(AuthService)
  matDialog = inject(MatDialog);

  movies: MovieDto[] = []

  ngOnInit(): void {
    if(this.authService.isAuthenticated()){
    this.getAllMovies()
    }
  }

  getAllMovies()
  {
    this.movieService.getAllMovies().subscribe(
      {
        next: (res)=>{
          console.log("response " + res)
          this.movies = res;
        },
        error: (err)=>{
          console.log("err " + err)
        }
      }
    )
  }

  isAdmin():boolean{
    return this.authService.hasRole('ADMIN');
  }

  update(movie:MovieDto){
    console.log("update movie : ", movie);

    const dialogRef = this.matDialog.open(UpdateMovieComponent, {
      data: {movie: movie},
    });

    dialogRef.afterClosed().subscribe({
      next: (result: boolean) => {
        if(result) {
          this.getAllMovies();
        }
      },
      error: (err) => {
        console.log(err);
      }
    })
  }

  delete(movie:MovieDto){
    const dialogRef = this.matDialog.open(DeleteMovieComponent, {
      data: {movie: movie},
    });

    dialogRef.afterClosed().subscribe({
      next: (result: boolean) => {
        if(result) {
          this.getAllMovies();
        }
      },
      error: (err) => {
        console.log(err);
      }
    })
  }

}
