import DesktopNavbar from './desktop/DesktopNavbar'
import MobileNavbar from './mobile/MobileNavbar'

const Navbar = () => {
    return (
        <>
            <MobileNavbar />
            <DesktopNavbar />
        </>
    )
}

export default Navbar