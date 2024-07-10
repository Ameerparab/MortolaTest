package EquipmentReport;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.BrowserType.LaunchOptions;

public class BrowserSelector {
    public Browser launchBrowser(String browserType) {
        Playwright playwright = null;
        Browser browser = null;
        try {
            playwright = Playwright.create();
            // Choosing of Browser
            switch (browserType.toLowerCase()) {
                case "chromium":
                    browser = playwright.chromium().launch(new LaunchOptions().setHeadless(false));
                    break;
                case "chrome":
                    browser = playwright.chromium().launch(new LaunchOptions().setChannel("chrome").setHeadless(false));
                    break;
                case "msedge":
                    browser = playwright.chromium().launch(new LaunchOptions().setChannel("msedge").setHeadless(false));
                    break;
                case "firefox":
                    browser = playwright.firefox().launch(new LaunchOptions().setHeadless(false));
                    break;
                case "webkit":
                    browser = playwright.webkit().launch(new LaunchOptions().setHeadless(false));
                    break;
                default:
                    System.out.println("Unsupported browser:  " + browserType);
                    break;
            }
        } catch (Exception j) {
            j.printStackTrace();
        }
        return browser;
    }
}