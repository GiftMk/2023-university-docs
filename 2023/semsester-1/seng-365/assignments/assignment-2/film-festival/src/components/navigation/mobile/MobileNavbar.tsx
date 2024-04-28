import * as NavigationMenu from '@radix-ui/react-navigation-menu'
import ProfileMenu from '../ProfileMenu'
import { NavLink } from 'react-router-dom'
import Logo from '../Logo'
import HamburgerMenu from './HamburgerMenu'

const MobileNavbar = () => {
    const navStyles = [
        'flex',
        'flex-col',
        'justify-center',
        'px-10',
        'py-5',
        'fixed',
        'top-0',
        'w-full',
        'h-[var(--navbar-height)]',
        'z-10',
        'transition',
        'ease-out',
        'delay-150',
        'duration-100',
        'backdrop-blur-lg bg-stone-950/[90%]',
        'md:invisible'
    ].join(' ')

    return (
        <NavigationMenu.Root className={navStyles} >
            <NavigationMenu.List className='flex justify-between gap-x-10'>
                <NavigationMenu.Item className='flex items-center'>
                    <HamburgerMenu />
                </NavigationMenu.Item>
                <NavigationMenu.Item className='inline-flex items-center shrink-0'>
                    <NavLink to={'/'}>
                        <Logo />
                    </NavLink>
                </NavigationMenu.Item>
                <NavigationMenu.Item className='inline-flex'>
                    <ProfileMenu />
                </NavigationMenu.Item>
            </NavigationMenu.List>
        </NavigationMenu.Root>
    )
}

export default MobileNavbar