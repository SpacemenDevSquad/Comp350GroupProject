import { useSearchParams } from 'react-router-dom';

function SearchResults(){
  const [searchParams] = useSearchParams();
  const searchTerm = searchParams.get('q');
  console.log(searchTerm)

  return (
    <div>Search Results</div>
  )
}

export default SearchResults