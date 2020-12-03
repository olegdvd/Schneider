package com.olegdvd.schneider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class SourceFileStreamProvider {




    public FileInputStream getInputSourceStream() {

        String result;
        File sourceXlsxFile = new File("Schneider_Electric.xlsx");
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(sourceXlsxFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return fileInputStream;
    }

    public FileOutputStream getOutputSourceStream() {


        String result;
        File destXlsxFile = new File("Schneider_Electric_out.xlsx");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(destXlsxFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return fileOutputStream;
    }
}
