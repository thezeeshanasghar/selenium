package com.example.Testt;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest
class TesttApplicationTests {

    @Test
    void testSignupAndLogin() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            driver.manage().window().maximize();
            driver.get("https://doctor.vaccinationcentre.com/login");
            System.out.println("✅ Website opened.");

            // Navigate to Signup Page
            WebElement signupLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@routerlink='/signup']")
            ));
            signupLink.click();
            System.out.println("✅ Navigated to Signup page.");
            Thread.sleep(1000); // Wait for animations

            // Fill out form fields
            fillInputField(driver, wait, "Qualification", "Master in Computer Science");
            fillInputField(driver, wait, "AdditionalInfo", "Test");
            fillInputField(driver, wait, "FirstName", "Shahneela");
            fillInputField(driver, wait, "LastName", "Naheed");
            fillInputField(driver, wait, "DisplayName", "Shahneela Naheed");
            fillInputField(driver, wait, "Email", "shneelashahneela@gmail.com");
            fillInputField(driver, wait, "MobileNumber", "0876543211");
            fillInputField(driver, wait, "PhoneNo", "0876543211");

            // Enable & Click Sign Up Button
         // Wait for the button to be visible & enabled
            WebElement signUpButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//ion-button[contains(text(), 'Sign Up')]")
            ));

            // Scroll into view
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", signUpButton);
            Thread.sleep(1000); // Small delay for rendering

            // Remove 'disabled' attribute if present
            ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('disabled');", signUpButton);

            // Try clicking normally first
            try {
                signUpButton.click();
            } catch (ElementNotInteractableException e) {
                // If still not clickable, force click via JavaScript
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", signUpButton);
            }

            System.out.println("✅ Clicked on Sign Up button successfully.");
            Thread.sleep(5000); // Allow time for signup processing
            driver.get("https://doctor.vaccinationcentre.com/login");
            System.out.println("✅ Moved to Login page.");

            // Login
            fillInputField(driver, wait, "MobileNumber", "3335196658");
            fillInputField(driver, wait, "Password", "AlphaBeta@12");

            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//ion-button[contains(text(), 'Login')]")
            ));
            loginButton.click();
            System.out.println("✅ Clicked on Login button.");

            wait.until(ExpectedConditions.urlContains("dashboard"));
            assertTrue(driver.getCurrentUrl().contains("dashboard"), "❌ Login failed!");

            System.out.println("✅ Login successful!");

        } catch (Exception e) {
            System.out.println("❌ Test failed: " + e.getMessage());
            fail("Test failed due to exception: " + e.getMessage());
        } finally {
            //driver.quit();
        }
    }

    private void fillInputField(WebDriver driver, WebDriverWait wait, String fieldName, String value) {
        try {
            WebElement ionElement = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[@formcontrolname='" + fieldName + "']")
            ));

            WebElement inputField;
            try {
                inputField = ionElement.findElement(By.cssSelector("input, textarea"));
            } catch (NoSuchElementException e) {
                inputField = ionElement; // Fallback if input is the main element
            }

            // Check if field is interactable
            if (inputField.isDisplayed() && inputField.isEnabled()) {
                inputField.clear();
                inputField.sendKeys(value);
                System.out.println("✅ Filled " + fieldName + " with: " + value);
            } else {
                // Use JavaScript if the element is not interactable
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].removeAttribute('readonly');", inputField);
                js.executeScript("arguments[0].value='" + value + "';", inputField);
                System.out.println("✅ Filled " + fieldName + " with: " + value);
            }
        } catch (Exception e) {
            System.out.println("❌ Failed to fill " + fieldName + ": " + e.getMessage());
        }
    }
}
