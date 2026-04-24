import { useState } from 'react';

/**
 * Reusable Star Rating Component
 * @param {number} rating - current rating value (1-3)
 * @param {number} maxStars - max stars (default 3 to match backend)
 * @param {boolean} interactive - if true, allows clicking to set rating
 * @param {function} onRate - callback when a star is clicked (passes 1-3)
 * @param {number} size - font size in pixels
 */
function StarRating({ rating = 0, maxStars = 3, interactive = false, onRate, size = 24 }) {
  const [hoverRating, setHoverRating] = useState(0);

  const stars = [];
  for (let i = 1; i <= maxStars; i++) {
    const isFilled = i <= (hoverRating || rating);
    stars.push(
      <span
        key={i}
        style={{
          fontSize: size,
          cursor: interactive ? 'pointer' : 'default',
          color: isFilled ? '#FFD700' : '#CCCCCC',
          transition: 'color 0.15s ease',
          userSelect: 'none',
          marginRight: 2,
        }}
        onMouseEnter={() => interactive && setHoverRating(i)}
        onMouseLeave={() => interactive && setHoverRating(0)}
        onClick={() => interactive && onRate && onRate(i)}
      >
        ★
      </span>
    );
  }

  return <div style={{ display: 'inline-flex', alignItems: 'center' }}>{stars}</div>;
}

export default StarRating;
