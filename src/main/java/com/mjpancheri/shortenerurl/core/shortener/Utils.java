package com.mjpancheri.shortenerurl.core.shortener;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;

public class Utils {

    private static final int ID_LENGTH = 12;

    private static final String DOMAIN = "http://localhost:8080/api/shorteners/";

    private Utils(){}

    public static String generateUniqueID() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder id = new StringBuilder();

        long currentTimeMillis = System.currentTimeMillis();
        id.append(currentTimeMillis);

        while (id.length() < ID_LENGTH) {
            int randomIndex = random.nextInt(characters.length());
            id.append(characters.charAt(randomIndex));
        }

        return id.substring(0, ID_LENGTH);
    }

    public static String generateNanoId() {
        return NanoIdUtils.randomNanoId(
                NanoIdUtils.DEFAULT_NUMBER_GENERATOR,
                NanoIdUtils.DEFAULT_ALPHABET,
                ID_LENGTH
        );
    }

    public static String getShortUrl(String id) {
        return DOMAIN + id + "/short";
    }

    public static URI getUriResource(String id) {
        try {
            return new URI(DOMAIN + id);
        } catch (Exception e) {
            return null;
        }
    }

    public static String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
