package com.example.backend.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImageUtil {
    public static byte[] readImageToByteArray(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        try (InputStream inputStream = url.openStream()) {
            return IOUtils.toByteArray(inputStream);
        }
    }
}