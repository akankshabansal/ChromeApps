package org.apache.pdfbox.examples.pdmodel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDGamma;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationTextMarkup;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: bansal
 * Date: 18/10/13
 * Time: 11:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class SampleCode {

    private static final Log log = LogFactory.getLog(SampleCode.class);

    private static final String encoding = "UTF-16LE";

    /**
     * If the current index is whitespace then skip any subsequent whitespace.
     */
    private int skipWhitespace(char[] array, int index) {
        //if we are at a space character then skip all space
        //characters, but when all done rollback 1 because stringsEqual
        //will roll forward 1
        if (array[index] == ' ' || array[index] > 256) {
            while (index < array.length && (array[index] == ' ' || array[index] > 256)) {
                index++;
            }
            index--;
        }
        return index;
    }

    /**
     * Determine whether two strings are equal, where two null strings are
     * considered equal.
     *
     * @param expected Expected string
     * @param actual   Actual String
     * @return <code>true</code> is the strings are both null,
     *         or if their contents are the same, otherwise <code>false</code>.
     */
    private boolean stringsEqual(String expected, String actual) {
        boolean equals = true;
        if ((expected == null) && (actual == null)) {
            return true;
        } else if (expected != null && actual != null) {
            expected = expected.trim();
            actual = actual.trim();
            char[] expectedArray = expected.toCharArray();
            char[] actualArray = actual.toCharArray();
            int expectedIndex = 0;
            int actualIndex = 0;
            while (expectedIndex < expectedArray.length && actualIndex < actualArray.length) {
                if (expectedArray[expectedIndex] != actualArray[actualIndex]) {
                    equals = false;
                    log.warn("Lines differ at index"
                            + " expected:" + expectedIndex + "-" + (int) expectedArray[expectedIndex]
                            + " actual:" + actualIndex + "-" + (int) actualArray[actualIndex]);
                    break;
                }
                expectedIndex = skipWhitespace(expectedArray, expectedIndex);
                actualIndex = skipWhitespace(actualArray, actualIndex);
                expectedIndex++;
                actualIndex++;
            }
            if (equals) {
                if (expectedIndex != expectedArray.length) {
                    equals = false;
                    log.warn("Expected line is longer at:" + expectedIndex);
                }
                if (actualIndex != actualArray.length) {
                    equals = false;
                    log.warn("Actual line is longer at:" + actualIndex);
                }
            }
        } else if ((expected == null && actual != null && actual.trim().equals("")) ||
                (actual == null && expected != null && expected.trim().equals(""))) {
            //basically there are some cases where pdfbox will put an extra line
            //at the end of the file, who cares, this is not enough to report
            // a failure
            equals = true;
        } else {
            equals = false;
        }
        return equals;
    }


    public static void doTestFile(File inFile, File outDir, boolean bLogResult, boolean bSort)
            throws Exception {

        if (!outDir.exists()) {
            if (!outDir.mkdirs()) {
                throw (new Exception("Error creating " + outDir.getAbsolutePath() + " directory"));
            }
        }
        PDDocument document;
        File outFile;
        OutputStream os;
        Writer writer;

        document = PDDocument.load(inFile);
        outFile = new File(outDir, inFile.getName() + "output.pdf");

        os = new FileOutputStream(outFile);
        writer = new OutputStreamWriter(os, encoding);


        try {
            //Extract data from pdf
            PDFTextStripper stripper = null;
            stripper = new PDFTextStripper();
            //stripper.setLineSeparator("\n");
            stripper.setStartPage(1);
            stripper.setEndPage(1);
            //stripper.setSortByPosition( true );
            String st = stripper.getText(document);
            System.out.println(st);

            //To extract images if needed
            List pages = document.getDocumentCatalog().getAllPages();
            Iterator iter = pages.iterator();
            int i =1;
            while (iter.hasNext()) {
                PDPage page = (PDPage) iter.next();
                PDResources resources = page.getResources();
                Map pageImages = resources.getImages();
                if (pageImages != null) {
                    Iterator imageIter = pageImages.keySet().iterator();
                    while (imageIter.hasNext()) {
                        String key = (String) imageIter.next();
                        PDXObjectImage image = (PDXObjectImage) pageImages.get(key);
                        image.write2file("/Users/bansal/Desktop/image" + i);
                        i ++;
                    }
                }
            }

            os.write(0xFF);
            os.write(0xFE);
            stripper.writeText(document,writer);

            LineNumberReader actualReader =
                    new LineNumberReader(new InputStreamReader(new FileInputStream(inFile), encoding));

            while (true) {
                String actualLine = actualReader.readLine();
                log.info("Data read "+actualLine + "\n");
                if (actualLine == null) {
                    break;
                }
            }
        } catch (Exception e) {
            log.info(e + "\n");
            e.getStackTrace();
        } finally {
            document.close();
            writer.close();
            os.close();
        }


        document = PDDocument.load(inFile);
        PDFont font = PDType1Font.HELVETICA_BOLD;

        PDPage page = (PDPage) document.getDocumentCatalog().getAllPages().get(0);
        List annots = page.getAnnotations();
        PDAnnotationTextMarkup markup = new PDAnnotationTextMarkup(PDAnnotationTextMarkup.SUB_TYPE_HIGHLIGHT);
        markup.setRectangle(new PDRectangle());
        markup.setColour(new PDGamma());
        PDGamma colourBlue = new PDGamma();
        colourBlue.setB(1);
        markup.setColour(colourBlue);
        //markup.setQuads();
        PDRectangle position = new PDRectangle();
        float inch = 72;
        float ph = page.getMediaBox().getUpperRightY();
        float textWidth = (font.getStringWidth( "   Hello" )/1000) * 18;
        position = new PDRectangle();
        position.setLowerLeftX(inch);
        position.setLowerLeftY( ph-inch-18 );
        position.setUpperRightX(72 + textWidth);
        position.setUpperRightY(ph-inch);
        markup.setRectangle(position);

        // work out the points forming the four corners of the annotations
        // set out in anti clockwise form (Completely wraps the text)
        // OK, the below doesn't match that description.
        // It's what acrobat 7 does and displays properly!
        float[] quads = new float[8];
quads[0] = position.getLowerLeftX();  // x1
        quads[1] = position.getUpperRightY() - 2; // y1
        quads[2] = position.getUpperRightX(); // x2
        quads[3] = quads[1]; // y2
        quads[4] = quads[0];  // x3
        quads[5] = position.getLowerLeftY() - 2; // y3
        quads[6] = quads[2]; // x4
        quads[7] = quads[5]; // y5

        markup.setQuadPoints(quads);
        //markup.setContents("Highlighted since it's important");

        annots.add(markup);

        annots.add(markup);
        document.save("/Users/bansal/Desktop/t" + inFile.getName());
        document.close();

    }


    public static void main(String[] args) throws Exception {
        try {
            File inDir = new File("/Users/bansal/Desktop/");
            File outDir = new File("/Users/bansal/Desktop/");


            File[] testFiles = inDir.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return (name.contains("hello3"));
                }
            });
            doTestFile(testFiles[0], outDir, false, false);

        } catch (Exception e) {
            log.info(e + "\n");
            e.getStackTrace();
        }
    }
}
