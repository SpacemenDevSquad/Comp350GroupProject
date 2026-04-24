# TODO: Course/Professor Rating Display

## Plan

### Backend
- [x] Add `GET /api/ratings/course/{department}/{number}/professor/{professorName}` endpoint to RatingController.java

### Frontend — Section.jsx
- [x] Update fetch logic to use new dedicated endpoint for course+professor ratings
- [x] Always show rating section: stars if ratings exist, "No ratings yet — click to rate" otherwise
- [x] Clicking rating area opens the modal in both cases
- [x] Update ProfessorModal to fetch course+professor-specific ratings (not all professor ratings)
- [x] Move inline styles to CSS classes for consistent card layout

### Frontend — WeeklySchedule.jsx
- [x] Add rating display to calendar cards (inside `.calendar-card`)
- [x] Add rating display to no-time cards (inside `.no-time-card`)

### CSS
- [x] Add `.sectionRating` class to section.css with proper positioning
- [x] Add `.card-rating` class to weeklySchedule.css for compact calendar display

## Dependent Files
1. `Backend/src/main/java/edu/gcc/prij/objects/rating/RatingController.java`
2. `Frontend/src/components/Section.jsx`
3. `Frontend/src/components/WeeklySchedule.jsx`
4. `Frontend/src/css/section.css`
5. `Frontend/src/css/weeklySchedule.css`

