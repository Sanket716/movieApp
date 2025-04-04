import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
  })
  export class MovieService {

    public BASE_URL = "http://localhost:8080";

    http = inject(HttpClient);

    getAllMovies(): Observable<MovieDto[]> {
        return this.http.get<MovieDto[]>(`${this.BASE_URL}/api/movie`);
    }

    addMovie(movieDto: MovieDto, file: File):Observable<MovieDto>{
        const formData = new FormData();
        formData.append("movieData",JSON.stringify(movieDto))
        formData.append("file",file)

        return this.http.post<MovieDto>(`${this.BASE_URL}/api/movie`,formData)

    }

    updateMovie(movieId:number,movieDto: MovieDto, file?: File|null):Observable<MovieDto>{
        const formData = new FormData();
        formData.append("movieData",JSON.stringify(movieDto))
        if(file!=null){
        formData.append("file",file)
        }

        return this.http.put<MovieDto>(`${this.BASE_URL}/api/movie/${movieId}`,formData)

    }

    deleteMovie(movieId:number):Observable<string>{

        return this.http.delete(`${this.BASE_URL}/api/movie/${movieId}`,{
            responseType:"text"
        })

    }

  }


  export type MovieDto = {
    movieId?: number,
    title: string,
    director: string,
    studio: string,
    movieCast: string[],
    releaseYear: number,
    poster?: string,
    posterUrl?: string,
}
  