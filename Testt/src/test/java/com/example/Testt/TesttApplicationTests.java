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
    void testFullSignupLoginAndAddPatient() {
        WebDriverManager.chromedriver().setup();
        ChromeDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            driver.manage().window().maximize();
            driver.get("https://doctor.vaccinationcentre.com/login");
            System.out.println("Website opened.");

            // Navigate to Signup Page
            WebElement signupLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@routerlink='/signup']")));
            signupLink.click();
            System.out.println("Navigated to Signup page.");
            Thread.sleep(1000); // Wait for animations

            // Fill Signup Form
            fillInputField(driver, wait, "Qualification", "Master in Computer Science");
            fillInputField(driver, wait, "AdditionalInfo", "Test");
            fillInputField(driver, wait, "FirstName", "Shahneela");
            fillInputField(driver, wait, "LastName", "Naheed");
            fillInputField(driver, wait, "DisplayName", "Shahneela Naheed");
            fillInputField(driver, wait, "Email", "shneelashahneela@gmail.com");
            fillInputField(driver, wait, "MobileNumber", "0876543211");
            fillInputField(driver, wait, "PhoneNo", "0876543211");

            // Enable and Click Sign Up Button
            WebElement signUpButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ion-button[contains(text(), 'Sign Up')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", signUpButton);
            Thread.sleep(1000);
            ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('disabled');", signUpButton);
            signUpButton.click();
            System.out.println("Clicked on Sign Up button.");

            Thread.sleep(5000); // Wait for signup process

            // Move to Login Page
            driver.get("https://doctor.vaccinationcentre.com/login");
            System.out.println("Moved to Login page.");

            // Login
            fillInputField(driver, wait, "MobileNumber", "3335196658");
            fillInputField(driver, wait, "Password", "AlphaBeta@12");

            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ion-button[contains(text(), 'Login')]")));
            loginButton.click();
            System.out.println("Clicked on Login button.");

            wait.until(ExpectedConditions.urlContains("/members/dashboard"));
            assertTrue(driver.getCurrentUrl().contains("/members/dashboard"), "Login failed!");
            System.out.println("Login successful, on Dashboard.");

            // Open Add Patient Page
            driver.get("https://doctor.vaccinationcentre.com/members/child/add");
            System.out.println("Opened Add Patient page.");

            Thread.sleep(4000); // Allow form to load

            // Fill Patient Form
            fillInputField(driver, wait, "Name", "Zoya");
            fillInputField(driver, wait, "FatherName", "Shafeeq");

            // Select Gender: Girl (By Parent Element)
            try {
                WebElement girlRadioButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//ion-radio[@value='Girl']/ancestor::ion-item")
                ));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", girlRadioButton);
                System.out.println("Selected Gender: Girl");
            } catch (Exception e) {
                System.out.println("Failed to select Girl radio button: " + e.getMessage());
            }

            // Select Date of Birth
            WebElement dobField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@formcontrolname='DOB']")));
            dobField.click();
            System.out.println("Clicked on DOB field.");
            Thread.sleep(1000);

            /*WebElement dateElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//td[@aria-label='April 28, 2025']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dateElement);
            System.out.println("Selected DOB: April 28, 2025");

            // Select Mode: Regular
            WebElement regularModeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ion-item[contains(.,'regular')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", regularModeButton);
            System.out.println("Selected Mode: Regular");*/

            // Fill other fields
            fillInputField(driver, wait, "Email", "yasir@gmail.com");
            fillInputField(driver, wait, "CountryCode", "+92 3098765432");
            fillInputField(driver, wait, "MobileNumber", "03098765432");
            fillInputField(driver, wait, "CNIC", "1122334455667");
            fillInputField(driver, wait, "city", "Bahawalpur");
            fillInputField(driver, wait, "City2", "Taxila");

            // Submit Patient Form
            WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ion-button[contains(text(), 'submit')]")));
            submitButton.click();
            System.out.println("Clicked on Submit button for Patient.");

            // Verify return to dashboard
            wait.until(ExpectedConditions.urlContains("/members/dashboard"));
            assertTrue(driver.getCurrentUrl().contains("/members/dashboard"), "Did not return to Dashboard after submit!");
            System.out.println("Patient added successfully and back on Dashboard.");

        } catch (Exception e) {
            System.out.println("Test failed: " + e.getMessage());
            fail("Test failed due to exception: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }

    private void fillInputField(WebDriver driver, WebDriverWait wait, String fieldName, String value) {
        try {
            WebElement inputField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@formcontrolname='" + fieldName + "']")));

            if (inputField.getTagName().equalsIgnoreCase("input") || inputField.getTagName().equalsIgnoreCase("textarea")) {
                inputField.clear();
                inputField.sendKeys(value);
            } else {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].value='" + value + "';", inputField);
            }
            System.out.println("Filled " + fieldName + " with: " + value);
        } catch (Exception e) {
            System.out.println("Failed to fill field: " + fieldName + " due to " + e.getMessage());
        }
    }
}
