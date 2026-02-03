package se.omegapoint.productdirectory.testutil;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class JsonTestUtils {

    private JsonTestUtils() {
    }

    //Metoden hämtar json filerna och läser av de i bytes och gör till en sträng. Om det inte funkar kastar den exception.
    public static String readJson(String classpathResource) {
        try (InputStream is = JsonTestUtils.class.getClassLoader().getResourceAsStream(classpathResource)) {
            if (is == null) {
                throw new IllegalArgumentException("Missing test resource: " + classpathResource);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read test resource: " + classpathResource, e);
        }
    }
}
