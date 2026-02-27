import { Outlet } from 'react-router-dom'

function Base() {
  return (
    <div className='app-layout'>
      {/* <Sidebar /> */}
      <div>Base-specific stuff</div>
      <main className='main-content'>
        <Outlet />
      </main>
    </div>
  )
}

export default Base