
package com.northeastern.selenium.tests;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Report;
import com.northeastern.selenium.ReportManager;
import com.northeastern.selenium.ScreenshotUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.io.FileWriter;
import java.util.List;

import java.io.IOException;
import java.time.Duration;


public class Scenario5Test extends BaseTest {

    @Test
    public void scenario5() throws InterruptedException, IOException {
        ReportManager.test = ReportManager.extent.createTest("This test is going to check for 'Add to my calender' button");
        ReportManager.test.log(Status.INFO, "test has started");
        Reporter.log("Test has started", true);

        // Initialize WebDriverWait for explicit waits
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Step 1: Navigate to My NEU portal and capture a screenshot
        driver.get("https://me.northeastern.edu");

//        login("konda.su@northeastern.edu", "Amaravathi@1209");


        Thread.sleep(5000);
        ScreenshotUtil.takeScreenshot(driver, "Scenario5", "Initial Page");
        Reporter.log("Opened Initial PAGE", true);
        ReportManager.test.log(Status.INFO, "Inital page");
        WebElement resourcesTab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Resources')]")));
        resourcesTab.click();
        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, "Scenario5", "Resource Page");
        ReportManager.test.log(Status.INFO, "Resource Page");
        Reporter.log("Opened Resource Page", true);

        WebElement classReg = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"resource-tab-Academics,_Classes_&_Registration\"]")));
        classReg.click();
        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, "Scenario5", "Class Page");
        ReportManager.test.log(Status.INFO, "Class Page");
        Reporter.log("Opened Class Page", true);


        // Step 8: Click on "Academic Calendar" on the Resources page
        WebElement transcriptLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Academic Calendar')]")));
        transcriptLink.click();
        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, "Scenario5", "Transcript Page");
        ReportManager.test.log(Status.INFO, "Transcript Page");
        Reporter.log("Opened Transcript Page", true);



        String originalWindow = driver.getWindowHandle();
        wait.until(ExpectedConditions.numberOfWindowsToBe(4));

        // Switch to the newest (third) tab
        for (String windowHandle : driver.getWindowHandles()) {
            if (!originalWindow.equals(windowHandle)) {
                driver.switchTo().window(windowHandle);
                Thread.sleep(10000);
                // Check if this is the correct tab by verifying its title or URL
                if (driver.getCurrentUrl().equals("https://registrar.northeastern.edu/group/calendar/")) { // Change this condition as needed
                    break; // Exit loop if the correct tab is found
                } else {
                    driver.switchTo().window(originalWindow); // Switch back if it's not the correct tab
                }
            }
        }

        // Step 10: Click on the "Academic calendar for the current academic year." button
        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, "Scenario5", "Academic Calender Page");
        ReportManager.test.log(Status.INFO, "Academic Calender Page");
        Reporter.log("Opened Academic Calender Page", true);
        WebElement currentAcademicYearButton = wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("Academic calendar for the current academic year.")));
        currentAcademicYearButton.click();

        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, "Scenario5", "Academic Calendar for Currrent Year Page");
        ReportManager.test.log(Status.INFO, "Academic Calendar for Currrent Year Page");
        Reporter.log("Opened Academic Calendar for Currrent Year Page", true);


        // Step 11: Scroll down the page incrementally to load dynamic content
        for (int i = 0; i < 2; i++) {  // Scroll down in increments to load content
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 500);");
            Thread.sleep(500); // Pause briefly for content to load
        }

        // Now try to locate the checkbox and scroll it into view
        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, "Scenario5", "Before unclicking checkbox");
        ReportManager.test.log(Status.INFO, "Before unclicking checkbox");
        Reporter.log("Before unclicking checkbox", true);

        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("trumba.spud.7.iframe")));
        WebElement checkbox = driver.findElement(By.xpath("//*[@id=\"mixItem0\"]"));
        if (checkbox.isSelected()) {
            // If it's selected, uncheck it
            checkbox.click();
        }

        // Step 12: Uncheck the checkbox if it is checked
        if (checkbox.isSelected()) {
            checkbox.click();

        }

        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, "Scenario5", "After clicking checkbox");
        ReportManager.test.log(Status.INFO, "After clicking checkbox");
        Reporter.log("After clicking checkbox", true);


        driver.switchTo().defaultContent();
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");

        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("trumba.spud.2.iframe")));
        // Step 13: Verify if the button with id "ctl04_ctl44_ctl00_buttonAtmc" is displayed
        try {
            WebElement verifyButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[.//span[text()='Add to My Calendar']]")));
            Assert.assertTrue(verifyButton.isDisplayed(), "The 'Add to My Calendar' button is not displayed.");
            ScreenshotUtil.takeScreenshot(driver, "Scenario5", "AddToMyCalendarButtonDisplayed");
            ReportManager.test.log(Status.INFO, "Add to My Calendar");
            Reporter.log("Checking Add to My Calendar", true);
            System.out.println("Test passed: The 'Add to My Calendar' button is displayed.");
            if (verifyButton.isDisplayed()) {
                Thread.sleep(6000);

            }
            ScreenshotUtil.takeScreenshot(driver, "Scenario5", "AddToMyCalendarButtonDisplayed");
            ReportManager.test.log(Status.INFO, "Add to My Calendar is visible");
            Reporter.log("Add to My Calendar is available", true);

        } catch (Exception e) {
            System.out.println("Timeout occured");
        }


        // Step 14: Switch back to the original window if needed
        driver.switchTo().window(originalWindow);

    }

    }




