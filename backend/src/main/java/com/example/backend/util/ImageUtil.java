package com.example.backend.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageUtil {
    public static byte[] readImageToByteArray(String imageUrl) throws IOException, MalformedURLException {
        URL url = null;
        try {
            url = new URL(imageUrl);
        } catch (MalformedURLException e) {
            throw new MalformedURLException("Invalid image URL: " + imageUrl);
        }
        try (InputStream inputStream = url.openStream()) {
            return IOUtils.toByteArray(inputStream);
        }
    }
}
