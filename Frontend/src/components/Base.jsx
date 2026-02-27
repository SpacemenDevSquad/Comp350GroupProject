import { Outlet } from 'react-router-dom'
import SearchBar from './SearchBar'

function Base() {
  return (
    <div className='app-layout'>
      {/* <Sidebar /> */}
      <div>
        Base-specific stuff
        <SearchBar />
      </div>
      <main className='main-content'>
        <Outlet />
      </main>
    </div>
  )
}

export default Base