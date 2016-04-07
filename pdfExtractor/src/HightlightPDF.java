import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bansal
 * Date: 31/10/13
 * Time: 2:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class HightlightPDF {
    public static void main(String[] args) throws Exception {
        List<String> searchString = new ArrayList<String>();
        searchString.add("Barnes-Hut tree structure");
//        searchString.add("3.1 Distributed Barnes-Hut Tree");
//        searchString.add("Fig. 2 A distributed Barnes-Hut tree for 2D domain on four");
//        searchString.add("lower communication. The distributed Barnes-Hut");
//        searchString.add("tree structure is proper for distributed computing");
//        searchString.add("transport layer protocol in LiteOS running on MicaZ motes");
//        searchString.add("plemented as a transport protocol in LiteOS on a testbed of");
//        searchString.add("congestion control protocols in LiteOS and compare them");
//        searchString.add("We implemented our protocol in the LiteOS [14] operat-");
//        searchString.add("We implement a light monitoring application in Lit");
        callTextHighlight("/Users/bansal/Desktop/inf/Distributed-Barnes-Hut-2.pdf", "/Users/bansal/Desktop/output.pdf", searchString, 1);
    }

    public static void callTextHighlight(String inputFileName, String outputFileName, List<String> searchWords, int ignoreUptoStartingPages ) {
        try {
            PDDocument document = null;
            TextHighlight textHighlight = new TextHighlight();
            textHighlight.outputFileName = outputFileName;
            File inFile = new File(inputFileName);
            document = PDDocument.load(inFile);
            textHighlight.searchedWords = searchWords;
            textHighlight.ignoreStartingPages = ignoreUptoStartingPages;
            textHighlight.writeText(document, new OutputStreamWriter(System.out));
        } catch (Exception e) {
            System.out.println(e.toString());
            e.getStackTrace();
            e.printStackTrace();
        }

    }

}
