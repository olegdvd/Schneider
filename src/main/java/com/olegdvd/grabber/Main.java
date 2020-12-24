package com.olegdvd.grabber;

import com.olegdvd.grabber.harvester.DanfossHarvester;
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

    private static String inputFileName;
    private static String outputFileName;

    static {
        //  String fileName "Schneider_Electric.xlsx";
        String fileName = "Danfoss_Price_Ukraine_03.02.2020_v01";
        String out = "_out";
        String fileSeparator = ".";
        String extension = "xlsx";
        inputFileName = fileName + fileSeparator + extension;
        outputFileName = fileName + out + fileSeparator + extension;
    }



    private Main(ExcelFileUpdater fileUpdater, SourceFileStreamProvider fileProvider1) {
        this.fileUpdater = fileUpdater;
        this.fileProvider = fileProvider1;
    }

    public static void main(String[] args) throws Exception {

        Main main = new Main(new ExcelFileUpdater(new DanfossHarvester()), new SourceFileStreamProvider());

        FileInputStream fIP = main.getFileProvider().getInputSourceStream(inputFileName);
        FileOutputStream fileOutputStream = main.getFileProvider().getOutputSourceStream(outputFileName);

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
