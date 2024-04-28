import './App.css'
import PublicRoutes from './routes/PublicRoutes'
import { publicRoutes } from './routes/routesData'

function App() {
  return (
    <div className='app'>
      <PublicRoutes routes={publicRoutes} />
    </div>
  )
}

export default App
