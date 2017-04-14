package com.spbstu.configuration;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestDataProccessor {

    public static String readTestContentFromFile(String fileName) throws Exception {
        InputStream is = new FileInputStream(fileName);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));

        // read file to string
        String line = buf.readLine();
        StringBuilder sb = new StringBuilder();
        while(line != null) {
            sb.append(line);
            line = buf.readLine();
        }
        return sb.toString();
    }
}
