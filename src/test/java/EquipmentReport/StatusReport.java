package EquipmentReport;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import java.nio.file.Paths;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.ScreenshotOptions;

public class StatusReport {

	public void streport(Page page,String formattedDateTime)
	{
        // Verify and click on Status tab
        Locator status = page.locator("//a[normalize-space()='Status']");
        String Status = "Status";
        Status = Status + formattedDateTime;
        
        try {
            assertThat(status).hasText("Status");  // Verifying Status text tab availability Status
            status.click();
        } catch (AssertionError g) {
            // Assertion failed, capture screenshot
            System.out.println("Assertion failed for Status. Capturing screenshot...");
            ScreenshotOptions screenshot = new ScreenshotOptions();
            page.screenshot(screenshot.setFullPage(true).setPath(Paths.get("./ReportCreation/Snapshots/", Status + ".png")));
            g.printStackTrace();
            throw new RuntimeException("Assertion failed for Status. Stopping further execution.");
        }
        // Report Session
        Locator rept = page.locator("//body/div[@class='wrapper']/div[@id='childNavRegion']/div[@class='child-nav-bar']/ul[@role='tablist']/li[2]/a[1]");
        rept.click();
	}
}