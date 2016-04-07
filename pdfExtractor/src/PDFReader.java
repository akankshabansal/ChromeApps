import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.*;
import java.util.List;

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
            String fileName = "/Users/bansal/Desktop/inf/pdfExtractor/p89-ahmadi.pdf";
            String st = readPDF(fileName);
            System.out.print("\n" + st);
            st = readPDF2(fileName);
            System.out.print("\n" + st);

        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public static String readPDF(String fileName) throws Exception {
        String st = "";
        File inFile = new File(fileName);
        PDDocument document;
        document = PDDocument.load(inFile);
        try {
            //Extract data from pdf
            PDFTextStripper stripper = null;
            stripper = new PDFTextStripper();
            //Commenting this piece as that restricts the reading of double column files.
            //stripper.setLineSeparator("\n");
            //stripper.setStartPage(0);
            //stripper.setEndPage(document.getNumberOfPages());
            //stripper.setSortByPosition(true);
            st = stripper.getText(document);
            System.out.println(st);

        } catch (Exception e) {
            System.out.print("Error " + e + "\n" + e.getStackTrace());
            e.getStackTrace();
        } finally {
            document.close();
        }
        return st;
    }

    /**
     * can be used
     * @param fileName
     * @return
     * @throws Exception
     */
    public static String readPDF2(String fileName) throws Exception {
        String st = "";
        File inFile = new File(fileName);
        PDDocument document;
        document = PDDocument.load(inFile);
        try {
            PDFTextStripper printer = new PDFTextStripper();
            List allPages = document.getDocumentCatalog().getAllPages();
            for( int i=0; i<allPages.size(); i++ )
            {
                PDPage page = (PDPage)allPages.get( i );
                System.out.println( "Processing page: " + i );
                PDStream contents = page.getContents();
                if( contents != null )
                {
                    printer.processStream( page, page.findResources(), page.getContents().getStream() );
                }
                System.out.println(printer);
            }
        } catch (Exception e) {
            System.out.print("Error " + e + "\n" + e.getStackTrace());
            e.getStackTrace();
        } finally {
            document.close();
        }
        return st;
    }


}
