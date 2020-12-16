package com.olegdvd.schneider;

import com.olegdvd.schneider.domain.DanfossGatheredData;
import com.olegdvd.schneider.domain.GatheredData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;
import java.util.stream.IntStream;

import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK;

public class ExcelFileUpdater {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelFileUpdater.class);
    //private final UrlContentGrabber grabber;
    private final Danfos grabber;

    ExcelFileUpdater(Danfos grabber) {
        this.grabber = grabber;
    }

    boolean updateExcelFile(XSSFWorkbook wb, OutputStream fOP) {
        Sheet sheet = wb.getSheetAt(0);
        int lastUsedRow = sheet.getLastRowNum();
       //й!!! lastUsedRow = 20;
        int pos = 1;
        int dataColumn = 2;
        int step = 12;
        long start = 0;
        long finish = 0;
        LOG.info("Available cores number: {}", Runtime.getRuntime().availableProcessors());
        while (pos <= lastUsedRow) {
            start = Instant.now().toEpochMilli();
            IntStream.range(pos, pos + step).parallel()
                    .boxed()
                    .forEach(rowNumber -> getArticleAndProcess(sheet, rowNumber, dataColumn));
            pos += step;
            finish = Instant.now().toEpochMilli();
            LOG.info("{} steps takes {}sec", step, (float) (finish - start) / 1000);
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

    private void getArticleAndProcess(Sheet sheet, int idx, int dataColumn) {
        Row row = sheet.getRow(idx);
        String article = null;
        try {
            article = row.getCell(dataColumn, CREATE_NULL_AS_BLANK).getStringCellValue();
            String grabbedValue = grabber.request(article);
            cellsMapper(row, grabbedValue);
        } catch (Exception e) {
            LOG.warn("Empty article with row index: {}",idx);
        }

    }

    private void cellsMapper(Row row, String grabbedValue) {
        GatheredData gatheredData = new DanfossGatheredData();
        fullfillCells(gatheredData, row);

    }

    private void fullfillCells(GatheredData gatheredData, Row row) {
        gatheredData.keys().forEach(key -> row.getCell(3, CREATE_NULL_AS_BLANK).setCellValue(key));
    }
}
