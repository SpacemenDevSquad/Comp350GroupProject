**Team PRIJ:** Peter Brumbach, Ryan Merrick, Isaiah Zimmerman, James Yoho
# 📦 Project Requirements

This document lists the requirements for the project.
It is expected to change as requirements are refined.

---
## 🚀 MVP Requirements

- [ ] **R1: Schedule add** A user should be able to add a course to a candidate schedule.
    - [ ] **R1-1** As a user, I don't want to accidentally add a course that conflicts with my current classes. I should be prevented from doing this.
    - [ ] **R1-2** As a user, I should be able to add classes that put me over 18 credits for a single semester, but I should receive a warning message when I add the class that pushes me over 18 credits.
- [ ] **R2: Schedule removal** As a user, I should be able to remove courses from my candidate schedule from the search view or from the calendar view
- [ ] **R3: Robust Search** As a user, I should be able to search for a class using various parameters including:
    - [ ] **R3-1** As a user, I should be able to search for a class using a course code.
    - [ ] **R3-2** As a user, I should be able to search for all classes whose name or description contains a key word or key words.
    - [ ] **R3-3** As a user, I should be able to search for a professor and see classes that are taught by that professor.
    - [ ] **R3-4** As a user, I should be able to search for all classes offered by a specific department.
    - [ ] **R3-5** As a user, I should be able to filter the course search results based on a range of credit hours that I define.
    - [ ] **R3-6** As a user, I should be able to filter the course search results based on which days the classes meet.
    - [ ] **R3-7** As a user, I should be able to filter the course search results based on the start and end time range.  
      - Ex: I should be able to find all classes that start after 1pm on M W or F, or 2 credit classes between 1 and 3 on Mondays. 
- [ ] **R4: Calendar view/schedule overview** As a user, I should be able to view a calendar which shows all of my currently scheduled classes. I should be able view more information about these classes.
    - [ ] **R4-1** As a user, I want to see some insights, like how many total credits I'm taking
    - [ ] **R4-2** As a user, I should be able to view this calendar in a weekly format that clearly displays when classes are back-to-back and when there are gaps in-between.
- [ ] **R5: Data persistence** As a user, my schedule should be available to me, even if I exit the web page. It should save my schedule.
- [ ] **R6: UI** A user should not have to use a clunky interface such as a console to accomplish tasks.  There should be an intuitive GUI.

---
## ✨ Additional Features
- [ ] **F1: Multiple Semesters** As a user, I should be able to create schedules for different semesters and easily move back and forth between them.
- [ ] **F2: User ratings** As a user, I should be able to see what others think about the class.
    - [ ] **F2-1** I should be able to rate a class that I took with a particular professor, knowing that some classes are taught by many professors
    - [ ] **F2-2** Rating a professor should be easy - I should be able to say a class is easy, average, or hard, and I should be able to rate a class on quality, from bad, to average, to good.
- [ ] **F3: Degree progress** As a user, I should be able to see which classes I have remaining for my major
    - NOTE: This is something that the user would probably have to enter themselves, unless we figured out a relatively simple script that could scrape degree requirements from the status sheet PDFs
    - [ ] **F3-1** As a user, I want to be able to see my progress on my minor as well, with credits taken and remaining, and eligible classes
    - NOTE: This would probably be accomplished through some combination of nested AND and OR statements for classes, and we'd want a way to make this clean and simple but still powerful.

---

> [!info] These requirements can only be modified with agreement between both the dev team and the customer.