export interface BatchFilmResponse {
    filmId: number
    title: string
    genreId: number
    ageRating: string
    directorId: number
    directorFirstName: string
    directorLastName: number
    rating: number
    releaseDate: string
}

export interface SingleFilmResponse extends BatchFilmResponse {
    description: string
    runtime: number
    numRatings: number
}