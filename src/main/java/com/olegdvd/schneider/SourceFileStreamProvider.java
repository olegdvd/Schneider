package com.olegdvd.schneider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

class SourceFileStreamProvider {

    FileInputStream getInputSourceStream() {

//        File sourceXlsxFile = new File("Schneider_Electric.xlsx");
        File sourceXlsxFile = new File("Danfoss_Price_Ukraine_03.02.2020_v01.xlsx");
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(sourceXlsxFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return fileInputStream;
    }

    FileOutputStream getOutputSourceStream() {

        File destXlsxFile = new File("Danfoss_Price_Ukraine_03.02.2020_v01_out.xlsx");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(destXlsxFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return fileOutputStream;
    }
}
