package com.github.newk5.vcmp.nodejs.plugin.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class FileResourceUtils {

    public FileResourceUtils() {
    }

    public InputStream getFileFromResourceAsStream(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }

    public String readResource(String file) {

        InputStream in = getFileFromResourceAsStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String code = reader.lines().collect(Collectors.joining("\n")).replaceFirst("var a =", "");
        return code.replaceFirst("var a =", "");
    }

}
