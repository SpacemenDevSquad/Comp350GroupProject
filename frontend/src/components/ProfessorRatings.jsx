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

  // Per-user averaging:
  // 1) average each user's ratings for this professor
  // 2) average across those user-level averages
  const ratingsByUser = new Map();
  for (const rating of ratings) {
    const userId = rating?.user?.id;
    if (!userId) continue;
    if (!ratingsByUser.has(userId)) {
      ratingsByUser.set(userId, []);
    }
    ratingsByUser.get(userId).push(rating);
  }

  const userAverages = Array.from(ratingsByUser.values()).map((userRatings) => {
    const qualityAvg = userRatings.reduce((acc, r) => acc + (r.quality || 0), 0) / userRatings.length;
    const difficultyAvg = userRatings.reduce((acc, r) => acc + (r.difficulty || 0), 0) / userRatings.length;
    return { qualityAvg, difficultyAvg };
  });

  if (userAverages.length === 0) {
    return <p>No ratings available for this professor.</p>;
  }

  const averageQuality = userAverages.reduce((acc, u) => acc + u.qualityAvg, 0) / userAverages.length;
  const averageDifficulty = userAverages.reduce((acc, u) => acc + u.difficultyAvg, 0) / userAverages.length;

  return (
    <div>
      <div style={{ marginBottom: '1rem' }}>
        <div style={{ margin: '0.25rem 0' }}>
          <strong>Average Quality:</strong>{' '}
          <StarRating rating={Math.round(averageQuality)} maxStars={5} size={20} variant="quality" />
          {' '}({averageQuality.toFixed(1)}/5)
        </div>
        <div style={{ margin: '0.25rem 0' }}>
          <strong>Average Difficulty:</strong>{' '}
          <StarRating rating={Math.round(averageDifficulty)} maxStars={5} size={20} variant="difficulty" />
          {' '}({averageDifficulty.toFixed(1)}/5)
        </div>
      </div>
    </div>
  );
}

