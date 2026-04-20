import json
import time
from selenium import webdriver
from selenium.webdriver.firefox.service import Service
from selenium.webdriver.firefox.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException, NoSuchElementException
from webdriver_manager.firefox import GeckoDriverManager

def scrape_rmp(url):
    # Setup Firefox options
    options = Options()
    # options.add_argument('--headless') # Uncomment to run invisibly in the background

    # Initialize WebDriver
    print("Launching Firefox...")
    driver = webdriver.Firefox(service=Service(GeckoDriverManager().install()), options=options)
    
    # --- ADBLOCKER SETUP ---
    # To use an adblocker, download the .xpi file for uBlock Origin (or your preferred blocker)
    # You can download it from: https://addons.mozilla.org/firefox/downloads/file/4236440/ublock_origin-1.56.0.xpi
    # Save it in the same folder as this script and uncomment the line below:
    
    driver.install_addon('ublock_origin.xpi', temporary=True)
    
    driver.get(url)
    wait = WebDriverWait(driver, 10)

    # 1. Close the cookie banner if it appears
    try:
        cookie_button = wait.until(EC.element_to_be_clickable((By.XPATH, "//button[contains(text(), 'Close')]")))
        cookie_button.click()
        time.sleep(1)
    except TimeoutException:
        pass # No cookie banner found

    # 2. Click "Show More" until all professors are loaded
    print("Loading all professors (this may take a minute depending on school size)...")
    while True:
        try:
            show_more_btn = driver.find_element(By.XPATH, "//button[text()='Show More']")
            driver.execute_script("arguments[0].click();", show_more_btn)
            time.sleep(1.5) # Wait for new cards to load
        except NoSuchElementException:
            break

    print("Finished loading. Extracting data...")

    # 3. Find all Teacher Cards
    cards = driver.find_elements(By.XPATH, "//a[contains(@class, 'TeacherCard__StyledTeacherCard')]")
    professors_data = []

    for card in cards:
        prof_info = {}
        
        # Extract ID from href
        href = card.get_attribute("href")
        if href:
            prof_info["id"] = href.split("/")[-1]

        # Helper function to safely extract text
        def get_text_safe(xpath):
            try:
                return card.find_element(By.XPATH, xpath).text.strip()
            except NoSuchElementException:
                return None

        # Extract textual data
        prof_info["name"] = get_text_safe(".//div[contains(@class, 'CardName__StyledCardName')]")
        prof_info["department"] = get_text_safe(".//div[contains(@class, 'CardSchool__Department')]")
        prof_info["school"] = get_text_safe(".//div[contains(@class, 'CardSchool__School')]")
        prof_info["quality_rating"] = get_text_safe(".//div[contains(@class, 'CardNumRatingNumber')]")
        prof_info["rating_count"] = get_text_safe(".//div[contains(@class, 'CardNumRatingCount')]")

        # Extract "Would Take Again" and "Difficulty"
        feedback_numbers = card.find_elements(By.XPATH, ".//div[contains(@class, 'CardFeedbackNumber')]")
        
        if len(feedback_numbers) >= 2:
            prof_info["would_take_again"] = feedback_numbers[0].text.strip()
            prof_info["difficulty"] = feedback_numbers[1].text.strip()
        else:
            prof_info["would_take_again"] = None
            prof_info["difficulty"] = None

        professors_data.append(prof_info)

    # 4. Save to JSON
    with open('professors.json', 'w', encoding='utf-8') as f:
        json.dump(professors_data, f, indent=4, ensure_ascii=False)

    print(f"Successfully scraped {len(professors_data)} professors and saved to 'professors.json'")
    driver.quit()

if __name__ == "__main__":
    target_url = "https://www.ratemyprofessors.com/search/professors/384?q=*"
    scrape_rmp(target_url)