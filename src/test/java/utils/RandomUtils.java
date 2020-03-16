package utils;

import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

public class RandomUtils {
    public static String generateRandomEmail() {
        return RandomStringUtils.randomAlphanumeric(8) + "@gmail.com";
    }
}
