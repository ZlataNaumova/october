import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Arrays;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import cucumber.api.StepDefinitionReporter;
import cucumber.api.event.TestRunFinished;
import cucumber.runtime.ClassFinder;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import cucumber.runtime.model.CucumberFeature;
import gherkin.pickles.Pickle;
import gherkin.pickles.PickleTag;


public class CucumberRunner {
    public static int ATTEMPTS = getRetryCount();
    public static String STEPS = "UITestDirectory";
    public static String[] NO_RETRY_TAGS = {"@known-bug", "@no-retry", "@regression"}; // edit this per your preference
    private static Runtime runtime;
    private static int scenarioLineNr;

    public static void run(String reportDir, String id, String feature) throws Throwable {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ResourceLoader resourceLoader = new MultiLoader(classLoader);
        ClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader, classLoader);
        Optional<Throwable> lastError = Optional.empty();
        getScenarioLineNr(feature);

        for (int attemptsLeft = ATTEMPTS; attemptsLeft >= 0; attemptsLeft--) {
            File jsonReportFile = new File(reportDir, id + ".json");
            jsonReportFile.delete();

            RuntimeOptions runtimeOptions = new RuntimeOptions(Arrays.asList(
                    "-s",
                    "-g", STEPS,
                    "--monochrome",
                    "-p", "json:" + jsonReportFile.getAbsolutePath(),
                    "-p", "pretty",
                    "classpath:" + feature
            ));

            runtime = new Runtime(resourceLoader, classFinder, classLoader, runtimeOptions);

            List<CucumberFeature> features = runtimeOptions.cucumberFeatures(resourceLoader, runtime.getEventBus());
            StepDefinitionReporter stepDefinitionReporter = runtimeOptions.stepDefinitionReporter(classLoader);

            runtime.reportStepDefinitions(stepDefinitionReporter);

            runtime.getGlue().reportStepDefinitions(runtimeOptions.stepDefinitionReporter(classLoader));


            for (CucumberFeature cucumberFeature : features) {
                if (containsTags(cucumberFeature, NO_RETRY_TAGS)) {
                    attemptsLeft = 0; // do not retry on failure
                }
                runtime.runFeature(cucumberFeature);
                runtime.getEventBus().send(new TestRunFinished(runtime.getEventBus().getTime()));
                runtime.printSummary();
            }

            Throwable previousErrorValue = lastError.orElse(null);
            lastError = runtime.getErrors().stream().findFirst();
            if (!lastError.isPresent()) { // if passed
                if (previousErrorValue != null) { // and there is a previous failure (passed on retry)
                    File flakesFile = Paths.get(reportDir, "flakes", id)
                            .toFile(); // then we'll keep flakes file to detect flaky test
                    flakesFile.getParentFile().mkdirs();
                    Files.touch(flakesFile);
                    try (
                            StringWriter writer = new StringWriter();
                            PrintWriter printWriter = new PrintWriter(writer)
                    ) {
                        previousErrorValue.printStackTrace(printWriter);
                        Files.asCharSink(flakesFile, Charset.defaultCharset(), FileWriteMode.APPEND)
                                .write(writer.toString());
                    }
                }
                break;
            }

            if (attemptsLeft > 1) {
                lastError.ifPresent(Throwable::printStackTrace);
                System.out.println("Retrying " + feature + " after failed attempt #" + (ATTEMPTS - attemptsLeft + 1));
            }
        }

        if (runtime != null) {
            runtime.printSummary();
        }

        if (lastError.isPresent()) {
            // failed
            throw lastError.get();
        }
    }

    private static void getScenarioLineNr(String feature) {
        if (feature.contains(":")) {
            scenarioLineNr = Integer.parseInt(feature.substring(feature.indexOf(":") + 1));
        } else {
            scenarioLineNr = 0;
        }
    }

    private static boolean containsTags(CucumberFeature cucumberFeature, String... tags) {
        List<String> tagsToLookFor = Arrays.asList(tags);
        return getScenarioTags(cucumberFeature).stream().anyMatch(tagsToLookFor::contains);
    }

    public static List<String> getScenarioTags(CucumberFeature cucumberFeature) {
        Pickle pickle = getPickleByLocation(cucumberFeature, scenarioLineNr);
        if (pickle == null) {
            return Collections.emptyList();
        }
        return pickle.getTags().stream().map(PickleTag::getName).collect(Collectors.toList());
    }

    private static Pickle getPickleByLocation(CucumberFeature cucumberFeature, int lineNr) {
        return runtime.compileFeature(cucumberFeature).stream()
                .map(pickleEvent -> pickleEvent.pickle)
                .filter(pickle -> pickle.getLocations().stream().anyMatch(l -> l.getLine() == lineNr))
                .findFirst().orElseGet(null);
    }

    private static int getRetryCount() {
        return Integer.parseInt(Optional.ofNullable(System.getenv("RETRY")).orElse("1"));
    }
}

