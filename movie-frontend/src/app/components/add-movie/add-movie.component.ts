import { Component } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { MovieDto, MovieService } from 'src/app/services/movie.service';

@Component({
  selector: 'app-add-movie',
  templateUrl: './add-movie.component.html',
  styleUrls: ['./add-movie.component.css']
})
export class AddMovieComponent {

  title = new FormControl<String>('', Validators.required)
  director = new FormControl<String>('', Validators.required)
  studio = new FormControl<String>('', Validators.required)
  movieCast = new FormControl<String>('', Validators.required)
  releaseYear = new FormControl<String>('', Validators.required)

  selectedFile: File|null = null;

  inlineNotification = {
    show: false,
    type: '',
    text: '',
  };

  errorMessage: string | null = null; 

  addMovieForm: FormGroup;

  constructor(private formBuilder: FormBuilder,
    private authService:AuthService,
    private router:Router,
    private movieService:MovieService
  ) {
    this.addMovieForm = this.formBuilder.group({
      title: this.title,
      studio: this.studio,
      director: this.director,
      movieCast: this.movieCast,
      releaseYear: this.releaseYear,
      poster: [null, Validators.required],
    });
  }

  onFileSelected(event:any){
    this.selectedFile = event.target.files[0];
    this.addMovieForm.patchValue({file:this.selectedFile});
  }

  addMovie(){
    if(this.authService.isAuthenticated() && this.addMovieForm.valid)
    {
      let movieCast = this.addMovieForm.get('movieCast')?.value as string;
      const mvc = movieCast.split(",").map(e=>e.trim()).filter(e=>e.length>0);
      const movieData:MovieDto={
        title: this.addMovieForm.get('title')?.value,
        director: this.addMovieForm.get('director')?.value,
        studio: this.addMovieForm.get('studio')?.value,
        movieCast: mvc,
        releaseYear: this.addMovieForm.get('releaseYear')?.value
      }
      this.movieService.addMovie(movieData, this.selectedFile!).subscribe({
        next: (response) => {
          console.log("response = ", response);
          this.addMovieForm.reset();
          this.router.navigate(['home'])
        },
        error: (err) => {
          console.log("");
          this.router.navigate(['add-movie']);
          this.inlineNotification = {
            show: true,
            type: 'error',
            text: 'Some error while adding movie!',
          }
        }
      })
    }
    else if(!this.authService.isAuthenticated()) {
      this.authService.logout();
      this.router.navigate(['/login']);
      this.inlineNotification = {
        show: true,
        type: 'error',
        text: 'Session expired! Please login again.',
      }
    } else {
      this.inlineNotification = {
        show: true,
        type: 'error',
        text: 'Please Enter all mandatory form fields!',
      }
    }
  }


}
