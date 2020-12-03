package com.olegdvd.schneider;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Objects;


public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private final ExcelFileUpdater fileUpdater;
    private final SourceFileStreamProvider fileProvider;

    private Main(ExcelFileUpdater fileUpdater, SourceFileStreamProvider fileProvider1) {
        this.fileUpdater = fileUpdater;
        this.fileProvider = fileProvider1;
    }

    public static void main(String[] args) throws Exception {

        Main main = new Main(new ExcelFileUpdater(new UrlContentGrabber()), new SourceFileStreamProvider());

        FileInputStream fIP = main.getFileProvider().getInputSourceStream();
        FileOutputStream fileOutputStream = main.getFileProvider().getOutputSourceStream();

        XSSFWorkbook wb = null;
        if (Objects.nonNull(fIP)) {
            wb = new XSSFWorkbook(fIP);
            main.getFileUpdater().updateExcelFile(wb, fileOutputStream);

        } else {
            LOG.info(" either not exist or cannot open stream {}", fIP);
        }
    }

    private SourceFileStreamProvider getFileProvider() {
        return fileProvider;
    }

    private ExcelFileUpdater getFileUpdater() {
        return fileUpdater;
    }
}

