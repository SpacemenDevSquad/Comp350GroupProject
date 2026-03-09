import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function SearchBar() {
  const [query, setQuery] = useState('');
  const navigate = useNavigate();

  const handleSearch = (e) => {
    e.preventDefault(); // Prevents the page from reloading
    if (query.trim()) {
      // Redirects user to /search?q=your-query
      navigate(`/search?q=${encodeURIComponent(query)}`);
    }
  };

  return (
    <div>
      <h3>Search</h3>
      <form onSubmit={handleSearch}>
        <input 
          type="text" 
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder="Search..."
        />
        <button type="submit">Search</button>
      </form>
    </div>
  );
}

export default SearchBar;