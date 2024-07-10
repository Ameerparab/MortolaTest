package EquipmentReport;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.ScreenshotOptions;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginHandler {
    public void login(Page page, String appURL, String username, String password, String formattedDateTime) {
        try {
            // Navigate to appURL
            page.navigate(appURL);
            // Login page
            Locator logo = page.locator("//div[@id='loginModal']//img");
            String Logoimage = "Logoimage";
            Logoimage = Logoimage + formattedDateTime;
            try {
                assertThat(logo).isVisible();  // Verifying Motorola Solution Logo
            } catch (AssertionError f) {
                // Assertion failed, capture screenshot
                System.out.println("Assertion failed Logo. Capturing screenshot...");
                ScreenshotOptions screenshot = new ScreenshotOptions();
                page.screenshot(screenshot.setFullPage(true).setPath(Paths.get("./ReportCreation/Snapshots/", Logoimage + ".png")));
                f.printStackTrace();
            }
            // Fill username and password
            page.locator("//input[@id='username']").fill(username);
            page.locator("//input[@id='password' and @placeholder='Password']").fill(password);
            page.locator("//button[@id='sign-in']").click();
            // Next Home page displayed
            Locator logo2 = page.locator("//img[@src='/app-theme/navbar_logo']");
            assertThat(logo2).isVisible(); // Verifying Motorola Solution Logo2 on the next page
        } catch (Exception i) {
            i.printStackTrace();
        }
    }
}