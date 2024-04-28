import React from 'react'
import axios from 'axios'
import Navbar from '../components/navigation/Navbar'
import Hero from '../components/Hero'
import { BatchFilmResponse, SingleFilmResponse } from '../types/Film'
import { Buffer } from 'buffer'
import getProps from '../components/film/ageRatingData'
import { FILM_AGE_RATING, FILM_DESCRIPTION, FILM_IMAGE, FILM_NAME, FILM_RATING } from './fakeData'

const NUM_FILMS = 6
const API_URL = 'http://localhost:4941/api/v1'

const Home = () => {
    const [ films, setFilms ] = React.useState<BatchFilmResponse[]>([])
    const [ topFilm, setTopFilm ] = React.useState<SingleFilmResponse>()
    const [ topFilmImage, setTopFilmImage ] = React.useState('')

    const useReal = false

    //TODO: Extract out image logic
    //TODO: Handle errors in axios calls
    //TODO: Possibly combine use effects?
    React.useEffect(() => {
        axios.get(API_URL + '/films', {
            params: {
              count: NUM_FILMS,
              sortBy: 'RATING_DESC'
            }
          }).then((response) => {
            setFilms(response.data.films)
        })
    }, [])

    React.useEffect(() => {
        if (films.length > 0) {
            axios.get(API_URL + '/films/' + films[2].filmId).then((response) => {
                setTopFilm(response.data)
            })
        }
    }, [films])

    React.useEffect(() => {
        if (topFilm) {
            axios.get(API_URL + '/films/' + topFilm.filmId + '/image', {
                responseType: 'arraybuffer'
            })
            .then((response) => {
                const arrayBuffer = Buffer.from(response.data)
                const base64String = arrayBuffer.toString('base64');
                let format: string;
                switch (response.headers['content-type']) {
                case 'image/png':
                    format = 'png';
                    break;
                case 'image/jpeg':
                    format = 'jpeg';
                    break;
                case 'image/bmp':
                    format = 'bmp';
                    break;
                case 'image/gif':
                    format = 'gif';
                    break;
                default:
                    throw new Error('Unsupported image format');
                }
                setTopFilmImage(`data:image/${format};base64,${base64String}`)
                console.log(topFilmImage)
            })
        }
    })

    if (useReal) {
        return (
            <>
                <Navbar />
                {
                    topFilm && 
                    <Hero 
                        title={topFilm.title} 
                        description={topFilm.description} 
                        image={topFilmImage} 
                        ageRating={getProps(topFilm.ageRating)} 
                        rating={topFilm.rating}
                    />
                }
                <div className='px-10 py-8'>
                    <h1 className='text-2xl font-medium'>Most Popular</h1>
                </div>
                <div className='h-[200vh]'></div>
            </> 
        )
    }
    return (
        <>
            <Navbar />
            <Hero 
                title={FILM_NAME} 
                description={FILM_DESCRIPTION} 
                image={FILM_IMAGE} 
                ageRating={FILM_AGE_RATING} 
                rating={FILM_RATING}
            />
            <div className='px-10 py-8'>
                <h1 className='text-2xl font-medium'>Most Popular</h1>
            </div>
            <div className='h-[200vh]'></div>
        </> 
    )
}

export default Home