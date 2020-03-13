package stepdefs;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.TestConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@ScenarioScoped
public class ScenarioSetupSteps {
    Scenario scenario;
    public File reportsDir;
    public File scenarioDir;
    public String scenarioName;
    public File buildDir;

    private Logger logger = LoggerFactory.getLogger(ScenarioSetupSteps.class);

    @Before(order = 0)
    public void scenarioSetup(Scenario scenario) throws Throwable {
        this.scenario = scenario;
        System.setProperty("java.awt.headless", "true");
        scenario.write(String.format("scenario uri: %s:%s", scenario.getUri(), scenario.getLines().get(0)));
        setReportingFolder();
        setBuildFolder();
        prepareScenarioFolder();
    }

    public void deleteScenarioFolder(Scenario scenario) throws IOException {
        if (!scenario.isFailed() && TestConfig.DELETE_SCENARIO_DIR) {
            logger.info("Removing scenario directory");
            FileUtils.deleteDirectory(scenarioDir);
        }
    }

    public File setBuildFolder() {
        if (buildDir == null) {
            buildDir = reportsDir.toPath().getParent().getParent().toFile();
            buildDir.mkdirs();
            logger.info("Build directory is: {}", buildDir.getAbsolutePath());
        }
        return buildDir;
    }

    public void setReportingFolder() {
        if (reportsDir == null) {
            logger.info("Working directory is: {}", workingDirectory());
            reportsDir = Paths.get("build", "reports", "tests").toFile();
            reportsDir.mkdirs();
            logger.info("Reporting directory is: {}", reportsDir.getAbsolutePath());
        }
    }

    public void prepareScenarioFolder() {
        scenarioName = scenario.getName().replaceAll("[^A-Za-z0-9 ]", "");
        scenarioDir = new File(reportsDir.getAbsolutePath() + "/" + scenarioName);
        try {
            FileUtils.deleteDirectory(scenarioDir);
        } catch (IOException e) {
            logger.error("Could not delete scenario directory\n{}", e.getMessage());
        }
        scenarioDir.mkdirs();
        logger.info("Scenario folder: {}\n", scenarioDir.getAbsolutePath());
    }

    private String workingDirectory() {
        return System.getProperty("user.dir");
    }
}