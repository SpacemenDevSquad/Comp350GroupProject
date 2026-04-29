import { useState, useEffect } from 'react';
import StarRating from './StarRating';

export default function ProfessorRatings({ professorName }) {
  const [ratings, setRatings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    async function fetchRatings() {
      try {
        const response = await fetch(`${import.meta.env.VITE_API_URL}/api/ratings/professor/${encodeURIComponent(professorName)}`);
        if (!response.ok) {
          throw new Error(`Error fetching ratings: ${response.statusText}`);
        }
        const data = await response.json();
        setRatings(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    }

    fetchRatings();
  }, [professorName]);

  if (loading) {
    return <p>Loading ratings...</p>;
  }
  if (error) {
    return <p>Error loading ratings: {error}</p>;
  }
  if (!ratings || ratings.length === 0) {
    return <p>No ratings available for this professor.</p>;
  }

  const averageQuality = ratings.reduce((acc, rating) => acc + rating.quality, 0) / ratings.length;
  const averageDifficulty = ratings.reduce((acc, rating) => acc + rating.difficulty, 0) / ratings.length;

  return (
    <div>
      <div style={{ marginBottom: '1rem' }}>
        <p style={{ margin: '0.25rem 0' }}>
          <strong>Average Quality:</strong>{' '}
          <StarRating rating={Math.round(averageQuality)} maxStars={3} size={20} />
          {' '}({averageQuality.toFixed(1)}/3)
        </p>
        <p style={{ margin: '0.25rem 0' }}>
          <strong>Average Difficulty:</strong>{' '}
          <StarRating rating={Math.round(averageDifficulty)} maxStars={3} size={20} />
          {' '}({averageDifficulty.toFixed(1)}/3)
        </p>
      </div>
    </div>
  );
}

