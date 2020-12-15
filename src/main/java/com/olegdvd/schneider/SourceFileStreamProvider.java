package com.olegdvd.schneider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

class SourceFileStreamProvider {

    FileInputStream getInputSourceStream() {

//        File sourceXlsxFile = new File("Schneider_Electric.xlsx");
        File sourceXlsxFile = new File("HE_price2020_EU_08.xlsx");
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(sourceXlsxFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return fileInputStream;
    }

    FileOutputStream getOutputSourceStream() {

        File destXlsxFile = new File("HE_price2020_EU_08_out.xlsx");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(destXlsxFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return fileOutputStream;
    }
}
