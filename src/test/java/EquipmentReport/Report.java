package EquipmentReport;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Browser.NewContextOptions;
import com.microsoft.playwright.Page.ScreenshotOptions;

public class Report {
    private Properties properties;

    public Report() {
        properties = new Properties();
        try (FileInputStream input = new FileInputStream("src/test/java/config.properties")) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void useConfigProperties() {
        // Retrieve and use properties  from config.properties
        String browser = properties.getProperty("browser");
        String appURL = properties.getProperty("appURL");
        String username = properties.getProperty("user");
        String password = properties.getProperty("password");
        String reportName = properties.getProperty("reportName");
        Playwright playwright = null;
        Browser browser_1 = null;
        Page page = null;

        try {
            playwright = Playwright.create();
            BrowserSelector browserSelector = new BrowserSelector();
            browser_1 = browserSelector.launchBrowser(browser);
            if (browser_1 != null) {
                BrowserContext context = browser_1.newContext(new NewContextOptions().setRecordVideoDir(Paths.get("./ReportCreation/Video/")));
                page = context.newPage();
                LocalDateTime currentDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm");
                String formattedDateTime = currentDateTime.format(formatter);

                LoginHandler loginHandler = new LoginHandler();
                loginHandler.login(page, appURL, username, password, formattedDateTime);  
                
                StatusReport  StatusReport= new StatusReport ();
                StatusReport.streport(page,formattedDateTime);              
                
               // Locate the SVG element using CSS selector
                Locator svgElement = page.locator("svg.fa-plus");
                // Verify that the creation of report option is visible and available
                assertThat(svgElement).isVisible();
                svgElement.click();
                // Create Report page is open
                assertThat(page).hasTitle("VideoManager EX | status | reports");   //Verifying the title name of page
                Locator h2Element = page.locator("//h2[contains(normalize-space(.), 'Create a New Report')]");
                assertThat(h2Element).hasText("Create a New Report");  //Verifying a text create a new report
                String rptname = reportName + formattedDateTime;
                Locator title = page.locator("//input[@id='inputName']");
                title.fill(rptname);
                Locator reportype = page.locator("//button[@data-id='inputType']//span[@class='caret']");
                reportype.click();
                Locator equipment = page.locator("//span[normalize-space()='Equipment']");
                equipment.click();
                //Selected default Schedule option "No"
                Locator create = page.locator("//button[normalize-space()='Create']");  
                create.click();      
                // Report Creation Verification
                Locator reptEquipment = page.locator("//tbody/tr[1]/td[5]");
                String Equipment = "Equipment";
                Equipment = Equipment + formattedDateTime;
                try {
                    // Perform assertion on Equipment 
                    assertThat(reptEquipment).hasText("Equipment");  //Verify that created report is of type Equipment
                } catch (AssertionError h) {
                    // Assertion failed, capture screenshot
                    System.out.println("Assertion failed for Equipment. Capturing screenshot...");
                    ScreenshotOptions screenshot = new ScreenshotOptions();
                    page.screenshot(screenshot.setFullPage(true).setPath(Paths.get("./ReportCreation/Snapshots/", Equipment + ".png")));
                    h.printStackTrace();
                    throw new RuntimeException("Assertion failed for Equipment. Stopping further execution.");
                }
                String xpath = String.format("//td[normalize-space()='%s']", rptname);
                Locator reptTitle = page.locator(xpath);
                assertThat(reptTitle).hasText(rptname);  //Verify that created report title matches to which is given in previous page
                System.out.println("The Case executed successfully and Equipment report creation completed");
                context.close();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (page != null) {
                page.close();
            }
            if (browser_1 != null) {
                browser_1.close();
            }
            if (playwright != null) {
                playwright.close();
            }
        }
    }
    // Main method
    public static void main(String[] args) {
        Report report = new Report();
        report.useConfigProperties();
        System.exit(0);
    }
}