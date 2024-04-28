import React from 'react'
import { FC } from 'react'
import * as NavigationMenu from '@radix-ui/react-navigation-menu'
import { navbarLinks } from '../navData'
import ProfileMenu from '../ProfileMenu'
import { NavLink } from 'react-router-dom'
import Logo from '../Logo'
import PageLink from '../PageLink'

const BACKGROUND_AT_TOP = 'bg-gradient-to-b from-stone-950/80 to-transparent'
const BACKGROUND_ON_SCROLL = 'backdrop-blur-lg bg-stone-950/[90%]'

const DesktopNavbar: FC  = () => {
    const [isAtTop, setIsAtTop] = React.useState(true);

    React.useEffect(() => {
        const handleScroll = () => {
          const position = window.pageYOffset || document.documentElement.scrollTop
          setIsAtTop(position <= 0)
        }
        window.addEventListener('scroll', handleScroll)
        return () => {
          window.removeEventListener('scroll', handleScroll)
        }
      }, [])

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
        'duration-150',
        isAtTop? BACKGROUND_AT_TOP : BACKGROUND_ON_SCROLL,
        'md:visible',
        'invisible',
    ].join(' ')
    
    return (
        <>
            <NavigationMenu.Root className={navStyles} >
                <NavigationMenu.List className='flex gap-x-10'>
                    <NavigationMenu.Item className='inline-flex items-center shrink-0'>
                        <NavLink to={'/'}>
                            <Logo />
                        </NavLink>
                    </NavigationMenu.Item>
                    {navbarLinks.map((link, i: number) => (
                        <NavigationMenu.Item className='inline-flex' key={i}>
                            <PageLink 
                                href={link.href} 
                                name={link.name} 
                                extraStyles={['py-2', 'px-3']} 
                            />
                        </NavigationMenu.Item>
                    ))}
                    <NavigationMenu.Item className='inline-flex ml-auto'>
                        <ProfileMenu />
                    </NavigationMenu.Item>
                </NavigationMenu.List>
            </NavigationMenu.Root>
        </>
    )
}

export default DesktopNavbar