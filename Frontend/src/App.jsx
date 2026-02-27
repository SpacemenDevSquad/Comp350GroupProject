import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Base from './Base';
import Home from './views/Home';
import Schedule from './views/Schedule';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Base />}>
          <Route path='/' element={<Home />} />
          <Route path='/schedule/' element={<Schedule />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;