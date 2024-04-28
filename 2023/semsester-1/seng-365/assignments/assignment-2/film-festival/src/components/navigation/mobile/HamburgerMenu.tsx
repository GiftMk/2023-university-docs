import React from 'react'
import * as Menubar from '@radix-ui/react-menubar'
import { GiHamburgerMenu } from 'react-icons/gi'
import { navbarLinks } from '../navData'
import PageLink from '../PageLink'

const HamburgerMenu = () => {
    const [ isOpen, setIsOpen ] = React.useState(false)
    React.useEffect(() => {
        document.body.style.overflowY = isOpen? 'hidden' : 'auto'
    }, [isOpen])

    return (
        <Menubar.Root>
            <Menubar.Menu>
                <Menubar.Trigger 
                    className='flex items-center hover:opacity-90' 
                    onClick={() => setIsOpen(!isOpen)}
                >
                    <GiHamburgerMenu className='w-6 h-6' />
                </Menubar.Trigger>
                <Menubar.Portal>
                    <Menubar.Content 
                        id='menubar-content' 
                        className='md:hidden fade z-50 backdrop-blur-2xl bg-stone-950/[90%] min-h-screen w-screen mt-6'
                    >
                        {navbarLinks.map((link, i: number) => (
                            <Menubar.Item key={i}>
                                <PageLink 
                                    href={link.href} 
                                    name={link.name} 
                                    extraStyles={['block w-full px-10 py-4 text-base hover:bg-stone-600/[55%]']}
                                />
                            </Menubar.Item>
                        ))}
                    </Menubar.Content>
                </Menubar.Portal>
            </Menubar.Menu>
        </Menubar.Root>
    )
}

export default HamburgerMenu