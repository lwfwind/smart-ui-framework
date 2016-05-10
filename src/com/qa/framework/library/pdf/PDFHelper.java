package com.qa.framework.library.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PRTokeniser;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * The type Pdf util.
 */
public class PDFHelper {

    private final static Logger logger = Logger
            .getLogger(PDFHelper.class);

    /**
     * Parses a PDF to a plain text file.
     *
     * @param pdf the original PDF
     * @param txt the resulting text
     */
    public static void parsePDF(String pdf, String txt) {
        try {
            PdfReader reader = new PdfReader(pdf);
            PdfReaderContentParser parser = new PdfReaderContentParser(reader);
            PrintWriter out = new PrintWriter(new FileOutputStream(txt));
            TextExtractionStrategy strategy;
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                strategy = parser.processContent(i,
                        new SimpleTextExtractionStrategy());
                out.println(strategy.getResultantText());
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    /**
     * Parses the PDF using PRTokeniser
     *
     * @param src  the path to the original PDF file
     * @param dest the path to the resulting text file
     * @throws IOException the io exception
     */
    public static void parsePdf(String src, String dest) throws IOException {
        PdfReader reader = new PdfReader(src);
        // we can inspect the syntax of the imported page
        byte[] streamBytes = reader.getPageContent(1);
        PRTokeniser tokenizer = new PRTokeniser(streamBytes);
        PrintWriter out = new PrintWriter(new FileOutputStream(dest));
        while (tokenizer.nextToken()) {
            if (tokenizer.getTokenType() == PRTokeniser.TokenType.STRING) {
                out.println(tokenizer.getStringValue());
            }
        }
        out.flush();
        out.close();
    }

    /**
     * Copy the specified pages from source PDF file to target file
     *
     * @param fromPage  the from page
     * @param toPage    the to page
     * @param strSource - Source PDF file
     * @param strDes    - target PDF file
     */
    public static void copyPDF(int fromPage, int toPage, String strSource,
                               String strDes) {
        try {
            FileInputStream strSourceFile = new FileInputStream(strSource);
            Document document = new Document();
            PdfCopy copy = new PdfCopy(document, new FileOutputStream(strDes));
            document.open();
            byte[] b = new byte[strSourceFile.available()];
            while (strSourceFile.read(b) > 0) {
                strSourceFile.read(b, 0, b.length);
                PdfReader pdfr = new PdfReader(new PdfReader(b));
                int num = pdfr.getNumberOfPages();
                logger.info("Total page is:" + num);
                if (num < toPage || fromPage < 1 || fromPage > toPage) {
                    logger.error("incorrect page setup!");
                } else {
                    for (int i = fromPage; i <= toPage; i++) {
                        document.newPage();
                        PdfImportedPage page = copy.getImportedPage(pdfr, i);
                        copy.addPage(page);
                    }
                }
            }
            document.close();
            strSourceFile.close();
            copy.close();
        } catch (IOException | DocumentException e) {
            logger.error(e.toString());
        }
    }

    /**
     * Copy all pages from source PDF file to target file
     *
     * @param strSource - Source PDF file
     * @param strDes    - target PDF file
     */
    public static void copyPDF(String strSource, String strDes) {
        try {
            FileInputStream strSourceFile = new FileInputStream(strSource);
            Document document = new Document();
            PdfCopy copy = new PdfCopy(document, new FileOutputStream(strDes));
            document.open();
            byte[] b = new byte[strSourceFile.available()];
            while (strSourceFile.read(b) > 0) {
                strSourceFile.read(b, 0, b.length);
                PdfReader pdfr = new PdfReader(new PdfReader(b));
                int num = pdfr.getNumberOfPages();
                logger.info("Total page is:" + num);
                for (int i = 1; i <= num; i++) {
                    document.newPage();
                    PdfImportedPage page = copy.getImportedPage(pdfr, i);
                    copy.addPage(page);
                }

            }
            document.close();
            strSourceFile.close();
            copy.close();
        } catch (IOException | DocumentException e) {
            logger.error(e.toString());
        }
    }

    /**
     * Combine multiPDF files to one target PDF file
     *
     * @param arr_strSource - Source PDF file
     * @param strDes        - target PDF file
     */
    public static void combinPDF(String[] arr_strSource, String strDes) {
        try {
            Document document = new Document();
            FileInputStream strSourceFile;
            byte[] b;
            int num;
            PdfCopy copy = new PdfCopy(document, new FileOutputStream(strDes));
            document.open();
            for (String anArr_strSource : arr_strSource) {
                strSourceFile = new FileInputStream(anArr_strSource);
                b = new byte[strSourceFile.available()];
                while (strSourceFile.read(b) > 0) {
                    strSourceFile.read(b, 0, b.length);
                    PdfReader pdfr = new PdfReader(new PdfReader(b));
                    num = pdfr.getNumberOfPages();
                    logger.info("Total page is:" + num);
                    for (int j = 1; j <= num; j++) {
                        document.newPage();
                        PdfImportedPage page = copy.getImportedPage(pdfr, j);
                        copy.addPage(page);
                    }
                }
                strSourceFile.close();
            }
            document.close();
            copy.close();
        } catch (IOException | DocumentException e) {
            logger.error(e.toString());
        }
    }

    /**
     * Combine multiPDF files to one target PDF file
     *
     * @param arrList_strSource - Source PDF file
     * @param strDes            - target PDF file
     */
    public static void combinPDF(ArrayList<String> arrList_strSource,
                                 String strDes) {
        try {
            Document document = new Document();
            FileInputStream strSourceFile;
            byte[] b;
            int num;
            PdfCopy copy = new PdfCopy(document, new FileOutputStream(strDes));
            document.open();
            for (String anArrList_strSource : arrList_strSource) {
                strSourceFile = new FileInputStream(anArrList_strSource);
                b = new byte[strSourceFile.available()];
                while (strSourceFile.read(b) > 0) {
                    strSourceFile.read(b, 0, b.length);
                    PdfReader pdfr = new PdfReader(new PdfReader(b));
                    num = pdfr.getNumberOfPages();
                    logger.info("Total page is:" + num);
                    for (int j = 1; j <= num; j++) {
                        document.newPage();
                        PdfImportedPage page = copy.getImportedPage(pdfr, j);
                        copy.addPage(page);
                    }
                }
                strSourceFile.close();
            }
            document.close();
            copy.close();
        } catch (IOException | DocumentException e) {
            logger.error(e.toString());
        }
    }

}
