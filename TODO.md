# Fix Credit Filter Always Sending 0

## Steps:
- [x] 1. Created this TODO.md
- [x] 2. Edit Frontend/src/components/Filters.jsx: Remove buggy useEffect, make select controlled (add value={credits}), update onChange to setCredits + immediately triggerSearch.
- [x] 3. Edit Backend/src/main/java/edu/gcc/prij/objects/search/SearchController.java: Add logging for received credits.
- [x] 4. Update this TODO.md with progress and complete task.
