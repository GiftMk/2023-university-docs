export interface NavLinkProps {
    href: string
    name: string
}

export const navbarLinks: NavLinkProps[] = [
    { href: '/', name: 'Home' },
    { href: '/films', name: 'Films' },
]

export const anonymousDropdownLinks: NavLinkProps[] = [
    { href: '/login', name: 'Login' },
    { href: '/register', name: 'Register' },
]

export const authenticatedDropdownLinks: NavLinkProps[] = [
    { href: '/profile', name: 'My profile' },
    { href: '/profile/films', name: 'My films' },
    { href: '/logout', name: 'Logout' },
]