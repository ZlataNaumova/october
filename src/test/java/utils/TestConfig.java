package utils;


import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class TestConfig {

    public static final String DEV_URL = "https://fr.october.eu/";
    public static final long LONG_TIMEOUT_MS = 30000;
    public static final long DEFAULT_TIMEOUT_MS = 15000;
    public static final boolean DELETE_SCENARIO_DIR = true;


}
