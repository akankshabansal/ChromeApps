import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.OutputStreamWriter;

/**
 * Created with IntelliJ IDEA.
 * User: bansal
 * Date: 31/10/13
 * Time: 2:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class HightlightPDF {
    public static void main(String[] args) throws Exception {
        try {
            String fileName = "/Users/bansal/Desktop/inf/pdfExtractor/p89-ahmadi.pdf";

            PDDocument document=null;
            File inFile = new File(fileName);
            document = PDDocument.load(inFile);

            TextHighlight textHighlight = new TextHighlight();
            //textHighlight.searchedWords ="This paper presents a novel congestion control scheme for";
            textHighlight.searchedWords ="ing system using C++. LiteOS offers a Unix-like thread-";
            textHighlight.outputFileName="/Users/bansal/Desktop/output.pdf";
            textHighlight.writeText(document,new OutputStreamWriter(System.out));

        } catch (Exception e) {
            e.getStackTrace();
        }
    }

}
