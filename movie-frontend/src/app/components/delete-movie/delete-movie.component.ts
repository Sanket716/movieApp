import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AuthService } from 'src/app/services/auth.service';
import { MovieDto, MovieService } from 'src/app/services/movie.service';

@Component({
  selector: 'app-delete-movie',
  templateUrl: './delete-movie.component.html',
  styleUrls: ['./delete-movie.component.css']
})
export class DeleteMovieComponent {

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: {movie: MovieDto},
    private dialogRef: MatDialogRef<DeleteMovieComponent>,
    private authService: AuthService,
    private movieService: MovieService,
  ) {
  }

  delete() {
    if(this.authService.isAuthenticated()) {
      this.movieService.deleteMovie(this.data.movie.movieId!).subscribe({
        next: (response) => {
          console.log(response);
        },
        error: (err) => {
          console.log(err);
        },
        complete: () => {
          this.dialogRef.close(true);
        }
      });
    }
  }

  cancel() {
    this.dialogRef.close();
  }

}
