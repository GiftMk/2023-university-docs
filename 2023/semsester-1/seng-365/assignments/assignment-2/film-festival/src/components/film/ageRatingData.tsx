export type AgeRatingName =  'G' | 'PG' | 'M' | 'R13' | 'R16' | 'R18' | 'TBC'

export interface AgeRatingProps {
    name: AgeRatingName
    color: string
}

export const ageRatings: AgeRatingProps[] = [
    { 'name': 'G', 'color': 'bg-green-600' },
    { 'name': 'PG', 'color': 'bg-yellow-600' },
    { 'name': 'M', 'color': 'bg-yellow-600' },
    { 'name': 'R13', 'color': 'bg-red-600' },
    { 'name': 'R16', 'color': 'bg-red-600' },
    { 'name': 'R18', 'color': 'bg-red-600' },
    { 'name': 'TBC', 'color': 'bg-stone-950' },
]

const getProps = (name: string): AgeRatingProps => {
    const props = ageRatings.find(r => r.name === name)
    if (props) return props
    throw Error('Unsupported age rating ' + name)
}

export default getProps