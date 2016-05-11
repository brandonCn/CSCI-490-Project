

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ScraperEgg {
	
	static ArrayList<String> itemNames = new ArrayList<String>();
	static ArrayList<String> numEggs = new ArrayList<String>();
	static ArrayList<String> count = new ArrayList<String>();
	static ArrayList<String> amount = new ArrayList<String>();
	static ArrayList<String> ctg = new ArrayList<String>();
	static ArrayList<String> star1 = new ArrayList<String>();
	static ArrayList<String> star2 = new ArrayList<String>();
	static ArrayList<String> star3 = new ArrayList<String>();
	static ArrayList<String> star4 = new ArrayList<String>();
	static ArrayList<String> star = new ArrayList<String>();
	static Document doc = null;
	static Document itemDoc = null;
	static String itemLink = null;
	static String ua = "Mozilla/4.0 (iPad; U; CPU OS 3_2_1 like Mac OS X; en-us) AppleWebKit/530.19.10 (KHTML, like Gecko) Mobile/7B405";
	static int item = 1;
	static Boolean stars = false;
	
	public static void main(String[] args) {
		// Constants 
		// Begin page URL
		String URL = "http://www.newegg.com/Processors-Servers/SubCategory/ID-727";
		String output = "C:\\Users\\xxdun\\Desktop\\datasetEgg nstars.txt";
		int pages = 90;
		int begin = 1;
		// append or overrite
		Boolean empty = false;
		
		// Empty output
		if (empty) {
			try {
				PrintWriter writer = new PrintWriter(output);
				writer.print("");
				writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		if (begin == 1) {
			// initi page
			System.out.println("getting page 1");
			getItems(URL);
			System.out.println("Writing to file");
			writeFile(output);
		}
		else {
			// Get page
			System.out.println("going to page " + begin + "\ngetting page 1");
			System.out.println(URL);
			try {
				doc = Jsoup.connect(URL).timeout(30000).userAgent(ua).get();
			} catch (IOException e) {
				System.out.println("fetch er");
				e.printStackTrace();
			} 
			// inc item
			item++;
		}
		
		// Get other pages
		for(int i = 2; i <= pages; i++) {
			String nextURL = "Page-" + i;
			System.out.println("getting page " + i);
			System.out.println(URL + nextURL);
			if (i >= begin) {
				getItems((URL + nextURL));
				System.out.println("Writing to file");
				writeFile(output);
				item = 1;
			}
			else {
				// Get page
				try {
					doc = Jsoup.connect(URL + nextURL).timeout(30000).userAgent(ua).get();
				} catch (IOException e) {
					System.out.println("fetch er");
					e.printStackTrace();
				} 
				// inc item
				item++;
			}
		}
		
		System.out.println("Wrote");
	}

	public static void getItems(String URL) {
		// Get page
		try {
			doc = Jsoup.connect(URL).timeout(30000).userAgent(ua).get();
		} catch (IOException e) {
			System.out.println("fetch er");
			e.printStackTrace();
		}
		
		// get item info
		for(int i = 1; i <= 30; i++) {
			itemNames.add(doc.select("#titleDescriptionID" + i).text());
			Elements it = doc.select("#cellItem" + i + "> div.itemText > div > a");
			if (!doc.select("#cellItem" + item + " > div.itemGraphics > a.itemRating").attr("title").isEmpty())
				numEggs.add(Character.toString(doc.select("#cellItem" + item + " > div.itemGraphics > a.itemRating").attr("title").charAt(9)));
			else
				numEggs.add("0");
			if (!(doc.select("#cellItem" + item + "> div.itemAction > ul > li.price-current").text().length() > 14))
				amount.add(doc.select("#cellItem" + item + "> div.itemAction > ul > li.price-current").text().replace("–", "").replace("from", "").trim());
			else 
				amount.add("0");
			
			if (!(doc.select("#cellItem" + item + " > div.itemGraphics > a.itemRating").text().replace("(", "").replace(")", "").replace(" ", "").trim().isEmpty())) 
				count.add(doc.select("#cellItem" + item + " > div.itemGraphics > a.itemRating").text().replace("(", "").replace(")", "").replace(" ", "").trim());
			else 
				count.add("0");

			if (!doc.select("#baBreadcrumbTop > dl > dd:nth-child(5)").text().isEmpty())
				ctg.add(doc.select("#baBreadcrumbTop > dl > dd:nth-child(5)").text());
			else 
				ctg.add("0");
			
			if (stars) {
				// get % per star
				try {
					    itemDoc = Jsoup.connect(it.attr("href")).timeout(30000).userAgent(ua).get();
						System.out.println(it.attr("href"));
						if (!itemDoc.select("#reviewPercent1").text().isEmpty())
							star1.add(itemDoc.select("#reviewPercent1").text());
						else 
							star1.add("0%");
						
						if (!itemDoc.select("#reviewPercent2").text().isEmpty())
							star2.add(itemDoc.select("#reviewPercent2").text());
						else 
							star2.add("0%");
						
						if (!itemDoc.select("#reviewPercent3").text().isEmpty())
							star3.add(itemDoc.select("#reviewPercent3").text());
						else 
							star3.add("0%");
						
						if (!itemDoc.select("#reviewPercent4").text().isEmpty())
							star4.add(itemDoc.select("#reviewPercent4").text());
						else 
							star4.add("0%");
						if (!itemDoc.select("#reviewPercent5").text().isEmpty())
							star.add(itemDoc.select("#reviewPercent5").text());
						else 
							star.add("0%");
				} catch (IOException e) {
					System.out.println("fetch er");
					e.printStackTrace();
				} 
			}
			// get the item nextt
			item++;
		}
	}
	
	public static void writeFile(String output) {
		// Write to file
		try {
			FileWriter writer = new FileWriter(output, true);
			for(int i = 0; i <= itemNames.size() - 1; i++) {
				if (stars)
					writer.write(itemNames.get(i) + "~" + amount.get(i) + "~" + numEggs.get(i) + "~" + count.get(i) + "~" + star1.get(i) + "~" + star2.get(i) + "~" + star3.get(i) + "~" + star4.get(i) + "~" + star.get(i) + "~" + ctg.get(i) + "\r\n");
				else 
					writer.write(itemNames.get(i) + "~" + amount.get(i) + "~" + numEggs.get(i) + "~" + count.get(i) + "~" + ctg.get(i) + "\r\n");
			}	
			writer.close();
			itemNames.clear();
			numEggs.clear();
			amount.clear();
			count.clear();
			ctg.clear();
			star1.clear();
			star2.clear();
			star3.clear();
			star4.clear();
			star.clear();
		} 
		catch (IOException e) {
			e.printStackTrace();			
		}
	}
}
