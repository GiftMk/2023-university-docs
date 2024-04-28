import AgeRating from './film/AgeRating'
import { AgeRatingProps } from './film/ageRatingData'
import { AiOutlineStar } from 'react-icons/ai'

export interface Props {
    title: string
    description: string
    image: string
    ageRating:  AgeRatingProps,
    rating: number
}

const truncate = (text: string, length: number, wholeWordsOnly=true): string => {
    if (text.length > length) {
        if (wholeWordsOnly) {
            while (text.charAt(length).trim() !== '')
                length--
        }
        return text.slice(0, length) + '...'
    }
    return text
}

const Hero = ({ title, description, image, ageRating, rating }: Props) => {
    return (
        <div className='flex hero-image min-h-[65vh] items-end overflow-hidden pt-[var(--navbar-height)]' style={{ backgroundImage: `url(${image})` }}>
            <div className='relative'>
                <div className='absolute w-full md:max-w-[70%] h-[96%] bg-black/[90%] blur-3xl'></div>
                <div className='flex flex-col gap-5 w-full md:max-w-[75%] p-10 justify-end relative'>
                    <h1 className='text-[var(--primary-accent-color)] text-5xl font-semibold'>
                        {title}
                    </h1>
                    <div className='flex font-bold text-xl items-center gap-12'>
                        <AgeRating name={ageRating.name} color={ageRating.color} />
                        <div className='flex gap-x-3 items-center'>
                            <AiOutlineStar className='w-6 h-6' />
                            <p className='text-[1rem]'>{rating} / 10</p>
                        </div>
                    </div>
                    <p>
                        {truncate(description, 150)}
                    </p>
                    <div className='block'>
                        <button className='bg-[var(--primary-accent-color)] rounded-md font-semibold h-11 w-40 px-5 hover:opacity-90'>
                            View
                        </button>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Hero