package com.example.messenger_vintage.business.impl.file;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utility {

    public static final String COLUMN_SEPARATOR = "%";

    public static final String[] trim(String[] s) {
        for(int i = 0; i<s.length;i++){
            s[i] = s[i].trim();
        }
        return s;
    }

    public static FileData readAllRows(String filename) throws IOException {
        FileData result = new FileData();

        try(BufferedReader in = new BufferedReader(new FileReader(filename))){
            List<String[]> rows = new ArrayList<>();
            long counter = Long.parseLong(in.readLine());
            result.setCounter(counter);
            String line = null;
            while((line = in.readLine()) != null){
                rows.add(trim(line.split(COLUMN_SEPARATOR)));
            }
            result.setRows(rows);
        }
        return result;
    }
}

