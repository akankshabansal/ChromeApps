import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: bansal
 * Date: 20/10/13
 * Time: 12:54 AM
 * To change this template use File | Settings | File Templates.
 */

public class PDFReader {

    private static final String encoding = "UTF-16LE";

    public static void main(String[] args) throws Exception {
        try {
            String st = readPDF("/Users/bansal/Desktop/hello.pdf");
            System.out.print("\n"+st);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public static String readPDF(String fileName) throws Exception {
        String st = "";
        File inFile = new File (fileName);
        PDDocument document;
        document = PDDocument.load(inFile);
        try {
            //Extract data from pdf
            PDFTextStripper stripper = null;
            stripper = new PDFTextStripper();
            //stripper.setLineSeparator("\n");
            stripper.setStartPage(0);
            stripper.setEndPage(document.getNumberOfPages());
            stripper.setSortByPosition( true );
            st = stripper.getText(document);
            System.out.println(st);

        } catch (Exception e) {
            System.out.print(e);
            e.getStackTrace();
        } finally {
            document.close();
        }
        return st;
    }
}
