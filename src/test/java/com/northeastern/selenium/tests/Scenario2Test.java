package com.northeastern.selenium.tests;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.northeastern.selenium.ReportManager;
import com.northeastern.selenium.ScreenshotUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Scenario2Test extends BaseTest {


    @Test
    public void addTwoToDoTasks() throws Exception {

        driver.get("https://canvas.northeastern.edu/");
        Thread.sleep(2000);

        ReportManager.test = ReportManager.extent.createTest( "This test is going create 2 events in the calender");
        ReportManager.test.info( "test has started");
        Reporter.log("Test has started", true);

        ScreenshotUtil.takeScreenshot(driver, "Scenario2", "InitialPage");
        ReportManager.test.info( "Initial Page");
        Reporter.log("Initial page has been Opened");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        login("username", "password");
        try {
            WebElement noButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("idBtn_Back")));
            noButton.click();
        } catch (TimeoutException e) {
            System.out.println("'Stay signed in?' prompt did not appear. Continuing...");
        }
        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, "Scenario2", "AfterLogin");
        ReportManager.test.info("After Login");
        Reporter.log("After Login", true);



        // Navigate to Calendar
        WebElement calendarLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("global_nav_calendar_link")));
        calendarLink.click();
        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, "Scenario2", "CalendarPage");
        ReportManager.test.info( "Calendar Page");
        Reporter.log("Calendar Page", true);
        String excelFilePath = "/Users/sumanayanakonda/Desktop/Selenium_Assignment/Scenario_2.xlsx";
        FileInputStream inputStream = new FileInputStream(excelFilePath);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheet("Sheet1");

        List<String> addedTitles = new ArrayList<>(); // List to store added event titles

        for (int i = 1; i <= 2; i++) {
            Row r = sheet.getRow(i);
            String title = r.getCell(0).getStringCellValue();
            addedTitles.add(title);
            System.out.println(title);
            String date = r.getCell(1).getStringCellValue();
            System.out.println(date);
            String time = r.getCell(2).getNumericCellValue() + "";
            System.out.println(time);
            String Details = r.getCell(3).getStringCellValue();
            System.out.println(Details);

            // Click on the "+" button to add a new event
            WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("create_new_event_link")));
            addButton.click();
            // Wait for the "Edit Event" popup to appear
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("edit_event_tabs")));

            Thread.sleep(2000);
            ScreenshotUtil.takeScreenshot(driver, "Scenario2", "CreateNewEvent");
            ReportManager.test.info( "Create New Event");
            Reporter.log("Trying to Create New Event", true);

            // Switch to the To-Do tab
            WebElement todoTab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href, '#edit_planner_note_form_holder')]")));
            todoTab.click();

            // Wait for the form to be visible
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit_planner_note_form_holder")));

            Thread.sleep(2000);
            ScreenshotUtil.takeScreenshot(driver, "Scenario2", "Before Entering Details");
            ReportManager.test.info("Before Entering Details");
            Reporter.log("Trying to Enter Details", true);

            // Fill in the event details
            WebElement titleInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("planner_note_title")));
            titleInput.sendKeys(title);

            WebElement dateInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("planner_note_date")));
            dateInput.clear();
            dateInput.sendKeys(date);

            WebElement timeInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("planner_note_time")));
            timeInput.sendKeys(time);

            WebElement detailsInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("details_textarea")));
            detailsInput.sendKeys(Details);

            // Take a screenshot before submitting
            Thread.sleep(2000);
            ScreenshotUtil.takeScreenshot(driver, "Scenario2", "After Entering Details");
            ReportManager.test.info( "After Entering Details");
            Reporter.log("After Entering Details", true);

            // Submit the event
            WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"edit_planner_note_form_holder\"]/form/div[2]/button")));
            submitButton.click();
            Reporter.log("Event is Submitted", true);

            // Wait for the event to be added and take a screenshot
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("edit_event_tabs")));
            Thread.sleep(2000);
            ScreenshotUtil.takeScreenshot(driver, "Scenario2", "After Submission");
        }

        verifyAddedEvents(wait, addedTitles);

        workbook.close();
        inputStream.close();
    }




    private List<String[]> readExcelData(String filePath, String sheetName) throws IOException {
        List<String[]> data = new ArrayList<>();
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet(sheetName);

        for (Row row : sheet) {
            String[] rowData = new String[5];
            for (int i = 0; i < 5; i++) {
                Cell cell = row.getCell(i);
                rowData[i] = cell == null ? "" : cell.toString();
            }
            data.add(rowData);
        }

        workbook.close();
        fis.close();
        return data;
    }

    private void verifyAddedEvents(WebDriverWait wait, List<String> addedTitles) {
        // Navigate back to the calendar view to verify added events
//        driver.get("https://northeastern.instructure.com/calendar#view_name=month&view_start=2024-11-14"); // Ensure you are on the calendar page

        for (String title : addedTitles) {
            try {
                // Wait until the event is visible in the calendar
                WebElement eventElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(), '" + title + "')]")));
                if (eventElement.isDisplayed()) {
                    System.out.println("Verified: Event '" + title + "' is displayed in the calendar.");
                }
                ReportManager.test.pass("Verified: Event '" + title + "' is displayed in the calendar.");
                Reporter.log("Verified: Event '" + title + "' is displayed in the calendar", true);
            } catch (TimeoutException e) {
                System.out.println("Event '" + title + "' not found in the calendar.");
                ReportManager.test.fail( "Event '" + title + "' not found in the calendar.");
                Reporter.log("Event '" + title + "' not found in the calendar", true);
                // You can log this or handle it as needed
            }
        }
    }

    private void login(String username, String password) throws InterruptedException, IOException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        try {
            WebElement loginToCanvasButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Log in to Canvas')]")));
            loginToCanvasButton.click();
            System.out.println("Clicked 'Log in to Canvas' button");
        } catch (TimeoutException e) {
            System.out.println("'Log in to Canvas' button not found. The page might have changed.");
            Thread.sleep(2000);
            ScreenshotUtil.takeScreenshot(driver, "Scenario1", "LoginToCanvasButtonNotFound");
            throw e;
        }



}

}