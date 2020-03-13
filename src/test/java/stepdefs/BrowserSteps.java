package stepdefs;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.google.inject.Inject;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.Network;
import utils.TestConfig;

import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

@ScenarioScoped
public class BrowserSteps {

    private static Logger logger = LoggerFactory.getLogger(BrowserSteps.class);

    @Inject
    ScenarioSetupSteps scenarioSetupSteps;

    private final String dockerImageName = "selenium/standalone-chrome-debug:3.141.59";
    private BrowserWebDriverContainer browserContainer;
    private Network network = Network.newNetwork();

    public void startBrowserIfNeeded() {
        if (browserContainer == null || !browserContainer.isRunning()) {
            browserContainer = startBrowserContainer();
            configureSelenide();
            setWebDriver(browserContainer);
            Selenide.open(Configuration.baseUrl);
            browserContainer.getWebDriver().close(); // close duplicated window (selenide opens new session)
            WebDriverRunner.getWebDriver().manage().window().setSize(new Dimension(1920, 1080));
            logger.info("Access browser with vnc viewer at {}\n", browserContainer.getVncAddress());
        }
    }

    private BrowserWebDriverContainer startBrowserContainer() {
        BrowserWebDriverContainer container = new BrowserWebDriverContainer(dockerImageName);
        container.addFileSystemBind("/dev/shm", "/dev/shm", BindMode.READ_WRITE); // to prevent OOM crashes when chrome eats memory
        container.addFileSystemBind(scenarioSetupSteps.scenarioDir.getAbsolutePath(), "/downloads", BindMode.READ_WRITE); // to prevent OOM crashes when chrome eats memory
        container.withEnv("SCREEN_WIDTH", "1920");
        container.withEnv("SCREEN_HEIGHT", "1080");
        container.withNetwork(network);
        container.start();
        logger.info("Browser container started. Remote webdriver address {}", container.getSeleniumAddress()
                .toString());
        return container;
    }

    private void setWebDriver(BrowserWebDriverContainer container) {
        if (container != null && container.isRunning()) {
            Configuration.browser = "chrome";
            Configuration.remote = container.getSeleniumAddress().toString();
        }
    }

    private void configureSelenide() {
        Configuration.baseUrl = TestConfig.DEV_URL;
        Configuration.savePageSource = false;
        Configuration.screenshots = false;
        Configuration.reportsFolder = scenarioSetupSteps.scenarioDir.getAbsolutePath();
        Configuration.timeout = TestConfig.DEFAULT_TIMEOUT_MS;
    }

    @After(order = 0)// very last hook
    public void stopBrowser(Scenario scenario) throws Throwable {
        takeScreenshotOnFailure(scenario);
        if (browserContainer != null) {
            logger.info("stopping browser");
            logBrowserErrors(scenario);
            browserContainer.stop();
        }
        scenarioSetupSteps.deleteScenarioFolder(scenario);
        network.close();
    }

    private void logBrowserErrors(Scenario scenario) {
        List<String> errors = getSevereLogErrors();
        if (!errors.isEmpty()) {
            logger.info("Found browser errors:");
            scenario.write("Found browser errors:");
            errors.forEach(error -> {
                scenario.write(error);
                logger.info(error);
            });
        }
    }

    private List<String> getSevereLogErrors() {
        return WebDriverRunner.getWebDriver().manage().logs()
                .get(LogType.BROWSER).getAll().stream().filter(l -> l.getLevel().equals(Level.SEVERE))
                .map(LogEntry::toString)
                .filter(entry -> !entry.contains("api.mixpanel.com"))
                .filter(entry -> !entry.contains("guide-images")) // some image are not available in dev
                .collect(Collectors.toList());
    }

    private void takeScreenshotOnFailure(Scenario scenario) {
        if (scenario.isFailed() && browserContainer != null && browserContainer.isRunning() && WebDriverRunner.hasWebDriverStarted()) {
            logger.info("taking screenshot");
            dismissAlertIfPresent();
            String scrName = Selenide.screenshot(String.valueOf(System.currentTimeMillis()));
            logger.info("Saved screenshot to {}", scrName);
            logger.info("failed at {}", WebDriverRunner.url());
            scenario.write(String.format("failed at %s", WebDriverRunner.url()));
        }
    }

    public void dismissAlertIfPresent() {
        try {
            logger.debug("trying to switch to alert");
            Alert alert = WebDriverRunner.getWebDriver().switchTo().alert();
            // alert present
            logger.warn("Closing alert '{}'", alert.getText());
            alert.dismiss();
        } catch (NoAlertPresentException e) {
            // do nothing
            logger.debug("no alerts found");
        }
    }

}