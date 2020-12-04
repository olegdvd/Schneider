package com.olegdvd.schneider;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;
import java.util.stream.IntStream;

public class ExcelFileUpdater {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelFileUpdater.class);
    //private final UrlContentGrabber grabber;
    private final Danfos grabber;

    ExcelFileUpdater(Danfos grabber) {
        this.grabber = grabber;
    }

    boolean updateExcelFile(XSSFWorkbook wb, OutputStream fOP) {
        Sheet sheet = wb.getSheetAt(1);
        int lastUsedRow = sheet.getLastRowNum();
        lastUsedRow = 100;
        int pos = 1;
        int step = 10;
        long start = 0;
        long finish = 0;
        LOG.info("Available cores number: {}", Runtime.getRuntime().availableProcessors());
        while (pos <= lastUsedRow) {
            start = Instant.now().toEpochMilli();
            IntStream.range(pos, pos + step).parallel()
                    .boxed()
                    .forEach(rowNumber -> getArticleAndProcess(sheet, rowNumber));
            pos += step;
            finish = Instant.now().toEpochMilli();
            LOG.info("{} steps takes {}sec", step, (float)(finish - start)/1000);
        }
            try {
                wb.write(fOP);


                LOG.info("File was updated successfully. {} cells processed. {}", pos, fOP.toString());
            } catch (IOException e) {
                LOG.warn("Failed to open/write to file {}", fOP);
                return false;
            }

        try {
            fOP.close();
        } catch (IOException e) {
            LOG.warn("Failed to close the file {}", fOP);
        }
        return true;
    }

    private void getArticleAndProcess(Sheet sheet, int idx) {
        Row row = sheet.getRow(idx);
        String article = row.getCell(0).getStringCellValue();
        row.getCell(1).setCellValue(grabber.request(article));
    }
}
