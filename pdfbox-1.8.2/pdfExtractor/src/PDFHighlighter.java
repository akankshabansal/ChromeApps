import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDGamma;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationTextMarkup;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bansal
 * Date: 20/10/13
 * Time: 12:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class PDFHighlighter {

    public static void main(String[] args) throws Exception {
        try {
            highlightPDF("/Users/bansal/Desktop/cweb.pdf", 10, "/Users/bansal/Desktop/output.pdf");
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public static void highlightPDF(String fileName, int lineNo, String outputFileName) {
        String st = "";
        File inFile = new File(fileName);
        PDDocument document;
        try {
            document = PDDocument.load(inFile);

            PDFTextStripper stripper = null;
            stripper = new PDFTextStripper();
            stripper.setLineSeparator("\n");
            stripper.setSortByPosition(true);

            //stripper.setForceParsing( force );
            stripper.setSortByPosition(true);
            //stripper.setShouldSeparateByBeads( separateBeads );
            stripper.setStartPage(0);
            stripper.setEndPage(document.getNumberOfPages());
            st = stripper.getText(document);
            String[] stArr = st.split("\n");
            //System.out.print("\n"+st);

            System.out.print("\n" + stArr[lineNo]);

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
            float textWidth = (font.getStringWidth(stArr[lineNo]) / 1000) * 18;
            position = new PDRectangle();
            position.setLowerLeftX(inch);
            position.setLowerLeftY(ph - inch - 18 - lineNo * 18);
            position.setUpperRightX(72 + textWidth);
            position.setUpperRightY(ph - inch - lineNo * 18);
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
            document.save(outputFileName);
            document.close();

        } catch (Exception e) {
            System.out.print(e);
            e.getStackTrace();
        }


    }
}
