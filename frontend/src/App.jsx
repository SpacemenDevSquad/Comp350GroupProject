import Home from './views/Home';
import Schedule from './views/Schedule';
import StatusSheet from './views/StatusSheet';
import TopBar from './components/TopBar';
import {useState} from 'react'

function App() {
  const [year, setYear] = useState(2025)
  const [term, setTerm] = useState('F')

  return (
    <div>
      <TopBar />
      <Home
        year={year}
        setYear={setYear}
        term={term}
        setTerm={setTerm}
      />
      <Schedule
        year={year}
        term={term}
      />

      <StatusSheet />
    </div>
  );
}

export default App;