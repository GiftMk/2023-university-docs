import * as Avatar from '@radix-ui/react-avatar'
import * as Menubar from '@radix-ui/react-menubar'
import { anonymousDropdownLinks } from './navData'
import { NavLink } from 'react-router-dom';

export interface Props {
    name?: string
    image?: string
}

const DEFAULT_NAME = 'Guest'

const ProfileMenu = ({ name = DEFAULT_NAME, image }: Props) => {
    return (
        <div className='flex items-center gap-x-5'>
            <p className='opacity-95 hidden md:block'>
                {name}
            </p>
            <Menubar.Root>
                <Menubar.Menu>
                    <Menubar.Trigger className='w-8 h-8 rounded-full bg-[var(--primary-accent-color)] hover:opacity-90'>
                        <Avatar.Root className='flex items-center justify-center h-full rounded-full'>
                            <Avatar.Image 
                                src={image} 
                                alt='profile-picture'
                            />
                            <Avatar.Fallback>
                                {name.charAt(0)}
                            </Avatar.Fallback>
                        </Avatar.Root>
                    </Menubar.Trigger>
                    <Menubar.Portal>
                        <Menubar.Content
                            id='menubar-content'
                            className='fade z-50 w-56 rounded-md backdrop-blur-2xl bg-stone-500/[80%] shadow-lg py-1 mt-4 mr-10'
                        >
                            {
                                anonymousDropdownLinks.map((link, i: number) => (
                                    <Menubar.Item key={i}>
                                        <NavLink 
                                            to={link.href} 
                                            className='opacity-95 block text-sm px-4 py-5 hover:bg-stone-400/30'
                                        >
                                            {link.name}
                                        </NavLink>
                                    </Menubar.Item>
                                ))
                            }
                        </Menubar.Content>
                    </Menubar.Portal>
                </Menubar.Menu>
            </Menubar.Root>
        </div>
    )
}

export default ProfileMenu