import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Base from './components/Base';
import Home from './views/Home';
import Schedule from './views/Schedule';
import SearchResults from './components/SearchResults';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Base />}>
          <Route path='/' element={<Home />} />
          <Route path='/schedule/' element={<Schedule />} />
          <Route path='/search/' element={<SearchResults />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;