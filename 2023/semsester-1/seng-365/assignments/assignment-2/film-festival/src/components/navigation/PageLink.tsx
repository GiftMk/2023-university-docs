import { NavLink } from 'react-router-dom'

interface Props {
    href: string,
    name: string,
    extraStyles?: string[]
}

const PageLink = ( { href, name, extraStyles }: Props ) => {
    const styles = [
        'rounded-md',
        'hover:text-[var(--text-color)]',
        'hover:opacity-100',
        'transition',
        'duration-50',
        'ease-in-out',
        'opacity-95',
        `${location.pathname === href ? 'font-semibold' : ''}`,
        ...(extraStyles || [])
    ].join(' ')
    return (
        <NavLink to={href} className={styles}>
            {name}
        </NavLink>
    )
}

export default PageLink