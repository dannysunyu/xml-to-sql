/* CS145 Summer 2012 */
/* Parser skeleton for processing item-???.xml files. Must be compiled in
   JDK 1.4 or above. */

/* Instructions:

   This program processes all files passed on the command line (to parse
   an entire diectory, type "java MyParser myFiles/*.xml" at the shell).

   At the point noted below, an individual XML file has been parsed into a
   DOM Document node. You should fill in code to process the node. Java's
   interface for the Document Object Model (DOM) is in package
   org.w3c.dom. The documentation is available online at

http://www.w3.org/2003/01/dom2-javadoc/org/w3c/dom/package-summary.html

A tutorial of DOM can be found at:

http://www.w3schools.com/dom/default.asp

Some auxiliary methods have been written for you. You may find them
useful.

 */


import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;


class MyParser {

    // Any separator will do, but we recommend you do not use '|'
    static final String columnSeparator = "<>";

	static final String DIRECTORY_NAME = "sql_data";

    static DocumentBuilder builder;

    static final String[] typeName = {
        "none",
        "Element",
        "Attr",
        "Text",
        "CDATA",
        "EntityRef",
        "Entity",
        "ProcInstr",
        "Comment",
        "Document",
        "DocType",
        "DocFragment",
        "Notation",
    };

    static class MyErrorHandler implements ErrorHandler {

        public void warning(SAXParseException exception)
            throws SAXException {
                fatalError(exception);
            }

        public void error(SAXParseException exception)
            throws SAXException {
                fatalError(exception);
            }

        public void fatalError(SAXParseException exception)
            throws SAXException {
                exception.printStackTrace();
                System.out.println("There should be no errors " +
                        "in the supplied XML files.");
                System.exit(3);
            }

    }

    /* Non-recursive (NR) version of Node.getElementsByTagName(...) */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }

    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }

    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text. */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }

    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }

    /* Returns the amount (in XXXXX.xx format) denoted by a dollar-value
     * string like $3,453.23. Returns the input if the input is an empty
     * string. */
    static String formatDollar(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                        "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }

    /* Returns the time (in YYYY-MM-DD HH:MM:SS format) denoted by a
     * time string like Dec-31-01 23:59:59. */
    static String formatTime(String time) {
        DateFormat outputDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat inputDf = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        String result = "";
        try { result = outputDf.format(inputDf.parse(time)); }
        catch (ParseException e) {
            System.out.println("This method should work for all date/" +
                    "time strings you find in our data.");
            System.exit(20);
        }
        return result;
    }

    /* Process one items-???.xml file. */
    static void processFile(File xmlFile) {

        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }

        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);

        // Get the root of the tree
        Element root = doc.getDocumentElement();

		File dir = new File("./" + DIRECTORY_NAME);
		if (!dir.exists()) {
			dir.mkdir();
		}
		try {
			dir = new File("./" + DIRECTORY_NAME);
			if (!dir.exists()) {
				throw new IOException("Could not make dir: " + dir);
			}
		}
		catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
		}
		
		outputSellerData(root);
		outputBidderData(root);
		outputBidData(root);
    }
	
	public static void outputSellerData(Element root) {
		File sellerFile = new File("./" + DIRECTORY_NAME + "/seller.dat");
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(sellerFile));
		}
		catch (IOException e){
			e.printStackTrace();
		}
	
		NodeList sellerNodes = root.getElementsByTagName("Seller");
		for (int i = 0; i < sellerNodes.getLength(); i++) {
			/* why is the order of attributes switched ? */
			String sellerID = sellerNodes.item(i).getAttributes().item(1).getNodeValue();
			String rating = sellerNodes.item(i).getAttributes().item(0).getNodeValue();
			pw.println(sellerID + columnSeparator + rating);
		}
			
		pw.close();
	}
	
	public static void outputBidderData(Element root) {
		File bidderFile = new File("./" + DIRECTORY_NAME + "/bidder.dat");
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(bidderFile));
		}
		catch (IOException e){
			e.printStackTrace();
		}
	
		NodeList bidderNodes = root.getElementsByTagName("Bidder");
		for (int i = 0; i < bidderNodes.getLength(); i++) {
			/* why is the order of attributes switched ? */
			String bidderID = bidderNodes.item(i).getAttributes().item(1).getNodeValue();
			String rating = bidderNodes.item(i).getAttributes().item(0).getNodeValue();
			pw.println(bidderID + columnSeparator + rating);
		}
			
		pw.close();
	}
	
	public static void outputBidData(Element root) {
		File bidFile = new File("./" + DIRECTORY_NAME + "/bid.dat");
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(bidFile));
		}
		catch (IOException e){
			e.printStackTrace();
		}
	
		NodeList itemNodes = root.getElementsByTagName("Item");
		for (int i = 0; i < itemNodes.getLength(); i++) {
			Node itemNode = itemNodes.item(i);
			String itemID = itemNode.getAttributes().item(0).getNodeValue();
			/* why is the order of attributes switched ? */
			NodeList bidNodes = ((Element)itemNode).getElementsByTagName("Bid");
			for (int j = 0; j < bidNodes.getLength(); j++) {
				Node bidNode = bidNodes.item(j);
				NodeList bidElements = bidNode.getChildNodes();
				String bidderID = bidElements.item(0).getAttributes().item(1).getNodeValue();
				Element timeNode = (Element) bidElements.item(1);
				String time = bidElements.item(1).getFirstChild().getNodeValue();
				String amount = ((Element)bidElements.item(2)).getFirstChild().getNodeValue();
				pw.println(itemID + columnSeparator +
						bidderID + columnSeparator + 
							time + columnSeparator +
								amount);
			}
		}
			
		pw.close();
	}
	

    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }

        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }

        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }

        // Success!
        System.out.println("Success creating the SQL input files.");
    }
}
