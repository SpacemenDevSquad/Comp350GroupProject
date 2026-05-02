import { useState } from 'react';

/**
 * Reusable Rating Component
 * @param {number} rating - current rating value (1-5)
 * @param {number} maxStars - max icons (default 5 to match backend)
 * @param {boolean} interactive - if true, allows clicking to set rating
 * @param {function} onRate - callback when an icon is clicked (passes 1-5)
 * @param {number} size - font size in pixels
 * @param {'quality'|'difficulty'} variant - display style for icon + colors
 */
function StarRating({ rating = 0, maxStars = 5, interactive = false, onRate, size = 24, variant = 'quality' }) {
  const [hoverRating, setHoverRating] = useState(0);
  const activeValue = hoverRating || rating;

  const getIcon = () => (variant === 'difficulty' ? '\uD83C\uDF36' : '\u2605');

  const getDifficultyColor = (isFilled, i) => {
    if (isFilled) {
      const heatColors = ['#ffd166', '#ff9f1c', '#ff6b35', '#e63946', '#9d0208'];
      return heatColors[Math.min(i - 1, heatColors.length - 1)];
    }
    return '#d9d9d9';
  };

  const getQualityColor = (isFilled, level) => {
    if (!isFilled) {
      return '#d9d9d9';
    }
    const colorByLevel = {
      1: '#7a4f2b',
      2: '#b3772e',
      3: '#d4af37',
      4: '#f3d35f',
      5: '#ffe8a3',
    };
    return colorByLevel[Math.min(level, 5)] || colorByLevel[5];
  };

  const icons = [];
  for (let i = 1; i <= maxStars; i++) {
    const isFilled = i <= activeValue;
    const icon = getIcon();
    const color = variant === 'difficulty'
      ? getDifficultyColor(isFilled, i)
      : getQualityColor(isFilled, Math.ceil(activeValue));

    const textShadow = variant === 'quality' && isFilled
      ? (activeValue >= 3
          ? '0 0 2px rgba(255, 235, 130, 0.85), 0 0 8px rgba(255, 210, 70, 0.55), 0 0 14px rgba(255, 220, 120, 0.35)'
          : '0 0 2px rgba(190, 140, 70, 0.45)')
      : 'none';

    icons.push(
      <span
        key={i}
        style={{
          fontSize: size,
          cursor: interactive ? 'pointer' : 'default',
          color,
          textShadow,
          transition: 'color 0.15s ease',
          userSelect: 'none',
          marginRight: 2,
          filter: variant === 'difficulty' && isFilled ? 'saturate(1.2)' : 'none',
        }}
        onMouseEnter={() => interactive && setHoverRating(i)}
        onMouseLeave={() => interactive && setHoverRating(0)}
        onClick={() => interactive && onRate && onRate(i)}
      >
        {icon}
      </span>
    );
  }

  return <div style={{ display: 'inline-flex', alignItems: 'center', width: 'fit-content' }}>{icons}</div>;
}

export default StarRating;
