package com.olegdvd.schneider;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

public class ExcelFileUpdater {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelFileUpdater.class);
    private final UrlContentGrabber grabber;

    ExcelFileUpdater(UrlContentGrabber grabber) {
        this.grabber = grabber;
    }

    boolean updateExcelFile(XSSFWorkbook wb, OutputStream fOP) {
        Sheet sheet = wb.getSheetAt(1);
        for (int i = 1; i < 10; i++) {
            Row row = sheet.getRow(i);
            String article = row.getCell(0).getStringCellValue();
            row.getCell(1).setCellValue(grabber.request(article));
        }

        try {
            wb.write(fOP);
            fOP.close();
            LOG.info("File was updated successfully {}", fOP);
        } catch (IOException e) {
            LOG.warn("Failed to open/write to file {}", fOP);
            return false;
        }
        return true;
    }
}
