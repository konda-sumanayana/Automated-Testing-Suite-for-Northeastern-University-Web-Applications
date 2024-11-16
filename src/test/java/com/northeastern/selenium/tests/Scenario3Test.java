package com.northeastern.selenium.tests;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.northeastern.selenium.ReportManager;
import com.northeastern.selenium.ScreenshotUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.time.Duration;

public class Scenario3Test extends BaseTest {

    @Test
    public void downloadClassroomGuideWithInvalidID() throws Exception {
        ReportManager.test = ReportManager.extent.createTest("This test is going to check classrooms and their locations.");
        ReportManager.test.info( "test has started");
        Reporter.log("This test is going to check classrooms and their locations.", true);
        driver.get("https://service.northeastern.edu/tech?id=classrooms");
        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, "Scenario3", "InitialPage");
        ReportManager.test.info( "In the Initial page");
        Reporter.log("Started the test", true);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Increased wait time

        // Locate the search input field by its label or placeholder text
        WebElement classroomIDInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[contains(@placeholder, 'Search a classroom by name')]")));
        classroomIDInput.clear();
        classroomIDInput.sendKeys("INVALID_CLASSROOM_ID"); // Entering an invalid ID

        WebElement classroomLocationDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("classroomtype"))); // Update based on actual HTML
        Select dropdown = new Select(classroomLocationDropdown);
        dropdown.selectByVisibleText("NUflex Auto");

        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, "Scenario3", "InitialPage");
        Reporter.log("Opened the initial Page", true);

        WebElement searchButton = driver.findElement(By.xpath("//*[@id=\"classroomFilter\"]/form/input")); // Update based on actual HTML
        searchButton.click();

        WebElement errorMessageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'No classrooms found for the provided ID.') or contains(text(), 'No classrooms match your filter criteria.')]")));
        String actualErrorMessage = errorMessageElement.getText();
        String expectedErrorMessage = "No classrooms found for the provided ID.";

        ScreenshotUtil.takeScreenshot(driver, "Scenario3", "Outcome");
        ReportManager.test.info( "Outcome: " + actualErrorMessage);
        Reporter.log( "Expected: " + expectedErrorMessage, true);


        // Assertion to verify the result matches the scenario's description
        Assert.assertNotEquals(actualErrorMessage, expectedErrorMessage,
                "The error message matches the expected, so the scenario should fail.");
        System.out.println("Test failed as expected: Error messages do not match.");


        // Assertion to verify the result matches the scenario's description
        if (!actualErrorMessage.equals(expectedErrorMessage)) {
            System.out.println("Test failed as expected: Error messages do not match.");
            Assert.fail("""
                    Expected error message""");
            ReportManager.test.fail( "Test failed as expected: Error messages do not match.");
            Reporter.log("Test failed as expected: Error messages do not match.", false);
        } else {
            System.out.println("Test passed: Expected error message is displayed.");
//            ReportManager.test.pass( "Test passed: Expected error message is displayed.");
//            Reporter.log("Test passed: Expected error message is displayed.", false);
        }

            }
}
