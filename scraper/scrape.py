from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import NoSuchElementException, TimeoutException
import json

# Initialize WebDriver (Chrome used here, ensure you have chromedriver installed/in PATH)
driver = webdriver.Chrome()

try:
    # 1. Navigate to the portal
    driver.get("https://my.gcc.edu")
    print("Browser opened. Please log in, perform your advanced course search, and hit search.")
    print("Waiting up to 5 minutes for the 'Search Results' page...")

    # 2 & 3. Custom Wait Condition for the manual phase
    class SearchResultsReady(object):
        def __call__(self, driver):
            # Check if the URL contains the required parameters
            url_target = "my.gcc.edu/ICS/My_Info/Academics.jnz" in driver.current_url and "screen=Advanced+Course+Search" in driver.current_url
            if not url_target:
                return False
            
            # Check if the main div exists and contains the specific text
            try:
                main_div = driver.find_element(By.ID, "pg0_V_divMain")
                return "Search Results" in main_div.text
            except NoSuchElementException:
                return False

    # Wait up to 300 seconds (5 minutes) for the user to reach the correct state
    WebDriverWait(driver, 300).until(SearchResultsReady())
    print("\nSearch results detected! Resuming automated control...")

    # 4 & 5. Extract data and paginate
    all_courses = []
    page_number = 1

    while True:
        print(f"Scraping page {page_number}...")
        
        # Locate the table
        table = driver.find_element(By.ID, "pg0_V_dgCourses")

        # Extract rows
        rows = table.find_elements(By.TAG_NAME, "tr")
        for row in rows:
            # We look for table data (td) to avoid scraping header rows (th)
            cols = row.find_elements(By.TAG_NAME, "td")
            if cols: 
                course_data = [col.text.strip() for col in cols]
                all_courses.append(course_data)

        # 6. Look for the "Next page -->" button
        try:
            # Using partial link text since the arrows might have unique spacing or formatting
            next_button = driver.find_element(By.XPATH, "//a[contains(text(), 'Next page -->')]")

            # 7. Click and wait for the old table to become stale
            next_button.click()
            
            # This is critical: Wait until the *old* table element is destroyed in the DOM
            WebDriverWait(driver, 10).until(EC.staleness_of(table))

            # Wait for the *new* table element to be rendered
            WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.ID, "pg0_V_dgCourses")))
            page_number += 1

        except NoSuchElementException:
            # The "Next page -->" button is no longer present
            print("\nNo more pages found. Scraping complete.")
            break
        except TimeoutException:
            print("\nTimed out waiting for the next page to load.")
            break

    # Output the data
    print(f"Successfully extracted {len(all_courses)} course records.")

    # Structuring the scraped output as JSON makes it trivial to ingest into the backend of your course scheduling application.
    with open("out/scraped_courses.json", "w") as f:
        json.dump(all_courses, f, indent=4)
    print("Data saved to out/scraped_courses.json")

finally:
    # Ensure the browser closes even if an error occurs
    driver.quit()