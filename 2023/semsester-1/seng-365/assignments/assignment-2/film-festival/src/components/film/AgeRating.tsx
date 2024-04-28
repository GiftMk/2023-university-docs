import { AgeRatingProps } from './ageRatingData'

const AgeRating = ({ name, color }: AgeRatingProps) => {
    const styles = [
        color,
        'font-black',
        'text-[1rem]',
        'w-9',
        'h-7',
        'rounded-md',
        'flex items-center',
        'justify-center',
        'cursor-default',
    ].join(' ')
    return (
        <span className={styles}>
            {name}
        </span>
    )
}

export default AgeRating