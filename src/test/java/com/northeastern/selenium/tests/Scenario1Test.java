package com.northeastern.selenium.tests;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.northeastern.selenium.EncryptionUtil;
import com.northeastern.selenium.ReportManager;
import com.northeastern.selenium.ScreenshotUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Reporter;
import org.testng.annotations.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.Select;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.io.FileOutputStream;


public class Scenario1Test extends BaseTest {



    @Test
    public void downloadTranscript() throws Exception {

        String excelFilePath = "/Users/sumanayanakonda/Desktop/Selenium_Assignment/Scenario_1.xlsx";
        FileInputStream inputStream = new FileInputStream(excelFilePath);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheet("Sheet1");
        String username = sheet.getRow(1).getCell(0).getStringCellValue();
        String encryptedPassword = sheet.getRow(1).getCell(1).getStringCellValue();
        String password = EncryptionUtil.decrypt(encryptedPassword);
        String username1 = sheet.getRow(1).getCell(2).getStringCellValue();

        ReportManager.test = ReportManager.extent.createTest("Download Latest Transcript");

        driver.get("https://me.northeastern.edu");

        Thread.sleep(2000);
        driver.manage().window().maximize();

        ScreenshotUtil.takeScreenshot(driver, "Scenario1", "InitialPage");
        Reporter.log("Login Started", true);

        login(username, password);

        Thread.sleep(2000);

        ScreenshotUtil.takeScreenshot(driver, "Scenario1", "AfterLogin");
        ReportManager.test.info( "After Login");
        Reporter.log("Login Completed", true);


        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Wait for the Resources link and click it
        try {
            WebElement resourcesLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Resources')]")));
            resourcesLink.click();
        } catch (TimeoutException e) {
            // If the link is not clickable, try to find it and click using JavaScript
            WebElement resourcesLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(text(), 'Resources')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", resourcesLink);
        }

        Thread.sleep(2000);

        ScreenshotUtil.takeScreenshot(driver, "Scenario1", "ResourcesPage");
        ReportManager.test.info( "After ResourcesPage");
        Reporter.log("Resources Page is visited", true);


        // Step 8: Click on "My Transcript" on the Resources page
        WebElement transcriptLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'My Transcript')]")));
        transcriptLink.click();

        Thread.sleep(2000);

        ScreenshotUtil.takeScreenshot(driver, "Scenario1", "08_TranscriptLink");
        ReportManager.test.info( "In TranscriptLink");
        Reporter.log("TranscriptLink is visited", true);


        // Switch to the new window
        String currentWindowHandle = driver.getWindowHandle();
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(currentWindowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }

        // Login to the transcript page
        WebElement usernameInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("username")));
        usernameInput.sendKeys(username1);

        WebElement passwordInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("password")));
        passwordInput.sendKeys(password);

        WebElement loginButton = driver.findElement(By.name("_eventId_proceed"));
        loginButton.click();

        // Handle Duo 2FA
        handleDuo2FA();

        ScreenshotUtil.takeScreenshot(driver, "Scenario1", "My Transcript Page Before");
        ReportManager.test.info( "My Transcript Page Before");
        Reporter.log("My Transcript Page is Opened", true);

        // Step 11: Select options in the dropdowns
        WebElement transcriptLevelDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("levl_id")));
        Select transcriptLevelSelect = new Select(transcriptLevelDropdown);
        transcriptLevelSelect.selectByVisibleText("Graduate");

        WebElement transcriptTypeDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("type_id")));
        Select transcriptTypeSelect = new Select(transcriptTypeDropdown);
        transcriptTypeSelect.selectByVisibleText("Audit Transcript");

        Thread.sleep(2000);

        ScreenshotUtil.takeScreenshot(driver, "Scenario1", "10_SelectedTranscriptOptions");
        ReportManager.test.info( "Selected Transcript Options");
        Reporter.log("Selected Transcript Options", true);

        // Step 12: Click on Submit button to generate the transcript
        WebElement submitTranscriptButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='Submit']")));
        submitTranscriptButton.click();
        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, "Scenario1", "Submitted Transcript Request");
        ReportManager.test.info("Submit Transcript Request");
        Reporter.log("Submit Transcript Request", true);

        // Step 13: Right-click and open print dialog
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("paperWidth", 8.27);
            params.put("paperHeight", 11.69);
            params.put("printBackground", true);
            String pdfBase64 = (String) ((ChromeDriver) driver).executeCdpCommand("Page.printToPDF", params).get("data");

            // Check if PDF data was generated
            if (pdfBase64 == null || pdfBase64.isEmpty()) {
                throw new IOException("PDF data is null or empty. Failed to generate PDF.");


        }

        // Decode and save the PDF file
        try (FileOutputStream fileOutputStream = new FileOutputStream("transcript.pdf")) {
            fileOutputStream.write(Base64.getDecoder().decode(pdfBase64));
               }
        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, "Scenario1", "PDF Saved");
        ReportManager.test.pass("PDF Saved");
        Reporter.log("PDF Saved", true);


        } catch (Exception e) {

            System.out.println("Exception occured: " + e);
            ReportManager.test.fail("Exception occured: " + e);

        }

    }



        private void login(String username, String password) throws InterruptedException, IOException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Enter username
        WebElement usernameInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("i0116")));
        usernameInput.clear();
        usernameInput.sendKeys(username);

        // Click "Next" after entering username
        WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("idSIButton9")));
        nextButton.click();

        // Wait for password field and enter password
        try {
            WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("i0118")));
            passwordInput.clear();
            passwordInput.sendKeys(password);

            // Click "Sign in" after entering password
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("idSIButton9")));
            signInButton.click();
        } catch (TimeoutException e) {
            System.out.println("Password field not found. The page might have changed or loaded incorrectly.");
            ScreenshotUtil.takeScreenshot(driver, "Scenario1", "PasswordFieldNotFound");
            throw e;
        }

        // Handle Duo 2FA
        handleDuo2FA();

        // Handle "Stay signed in?" prompt if it appears
        try {
            WebElement noButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("idBtn_Back")));
            noButton.click();
        } catch (TimeoutException e) {
            System.out.println("'Stay signed in?' prompt did not appear. Continuing...");
        }
    }
    private void handleDuo2FA() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        System.out.println("Current URL: " + driver.getCurrentUrl());
        System.out.println("Page source: " + driver.getPageSource());

        // Check if we're on the "Is this your device?" page
        try {
            WebElement trustDeviceButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("trust-browser-button")));
            trustDeviceButton.click();
            System.out.println("Clicked 'Yes, this is my device' button");
            return; // Exit the method as we've handled the prompt
        } catch (TimeoutException e) {
            System.out.println("'Is this your device?' prompt not found. Proceeding with Duo authentication.");
        }

        try {
            // Wait for the Duo iframe to be present
            WebElement duoIframe = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("duo_iframe")));

            // Switch to the Duo iframe
            driver.switchTo().frame(duoIframe);
            System.out.println("Switched to Duo iframe");

            // Look for the "Send Me a Push" button
            WebElement sendPushButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Send Me a Push') or contains(@class, 'positive auth-button')]")));
            sendPushButton.click();
            System.out.println("Clicked 'Send Me a Push' button");

            // Wait for manual approval (you'll need to approve on your device)
            Thread.sleep(20000); // Adjust this time as needed

            // Switch back to the default content
            driver.switchTo().defaultContent();
            System.out.println("Switched back to default content");

        } catch (TimeoutException e) {
            System.out.println("Error during Duo authentication: " + e.getMessage());
            System.out.println("Page source: " + driver.getPageSource());
            throw e;
        }

        // Check for any additional prompts or elements after authentication
        try {
            WebElement additionalPrompt = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(text(), 'Continue') or contains(text(), 'Proceed')]")));
            additionalPrompt.click();
            System.out.println("Clicked additional prompt after authentication");
        } catch (TimeoutException e) {
            System.out.println("No additional prompts found after authentication");
        }
    }

}