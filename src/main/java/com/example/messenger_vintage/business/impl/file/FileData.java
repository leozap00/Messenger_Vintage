package com.example.messenger_vintage.business.impl.file;

import java.util.List;

public class FileData {

    public long getCounter() {
        return counter;
    }

    public void setCounter(long counter) {
        this.counter = counter;
    }

    public List<String[]> getRows() {
        return rows;
    }

    public void setRows(List<String[]> rows) {
        this.rows = rows;
    }

    private long counter;
    private List<String[]> rows;


}
