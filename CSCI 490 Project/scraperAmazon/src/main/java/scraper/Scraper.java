package scraper;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {
	
	static ArrayList<String> itemNames = new ArrayList<String>();
	static ArrayList<String> numStars = new ArrayList<String>();
	static ArrayList<String> count = new ArrayList<String>();
	static ArrayList<String> amount = new ArrayList<String>();
	static Document doc = null;
	static int item = 0;
	
	public static void main(String[] args) {
		// Constants 
		String URL = "http://www.amazon.com/s/ref=sr_nr_i_0_br?rh=k%3Acpu%2Ci%3Aelectronics&keywords=cpu&ie=UTF8&qid=1462378433";
		String output = "dataset.txt";
		int pages = 310;
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
		// initi page
		System.out.println("getting page 1");
		getItems(URL);
		System.out.println("Writing to file");
		writeFile(output);
		// Get other pages
		for(int i = 2; i <= pages; i++) {
			String nextURL = doc.select("#pagn > span:nth-child(3) > a").attr("href");
			System.out.println("getting page " + i);
			getItems("http://www.amazon.com/" + nextURL);
			System.out.println("Writing to file");
			writeFile(output);
		}
		
		System.out.println("Wrote");
	}

	public static void getItems(String URL) {
		// Get page
		try {
			doc = Jsoup.connect(URL).timeout(30000).userAgent("Mozilla/5.0 (iPad; U; CPU OS 3_2_1 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Mobile/7B405").get();
		} catch (IOException e) {
			System.out.println("fetch er");
			e.printStackTrace();
		} 
		
		//for(int i = 0; i < 24; i++) { 
	//		
		//	
		//	itemNames.add(doc.select("#result_" + i + " > div > div:nth-child(3) > div.a-row.a-spacing-none > a > h2").text());
		//}
		
		Elements items = doc.select(".a-size-base.a-color-null.s-inline.s-access-title.a-text-normal");
		//System.out.println(doc.select("*").text());
		
		for(Element it : items) {
			itemNames.add(it.text());
			if (!doc.select("#result_" + item + " > div > div.a-row.a-spacing-none > a").text().isEmpty())
				count.add(doc.select("#result_" + item + " > div > div.a-row.a-spacing-none > a").text());
			else 
				count.add("0");
			if (!doc.select("#result_" + item + " > div > div.a-row.a-spacing-none > span > span > a > i.a-icon.a-icon-star.a-star-4-5").text().isEmpty())
				numStars.add(doc.select("#result_" + item + " > div > div.a-row.a-spacing-none > span > span > a > i.a-icon.a-icon-star.a-star-4-5").text().substring(0,3));
			else 
				numStars.add("0");
			amount.add(doc.select("#result_" + item + " > div > div:nth-child(4) > div:nth-child(1) > a > span").text().split(" ", 1)[0]);
			item++;
		}
	}
	
	public static void writeFile(String output) {
		// Write to file
		try {
			FileWriter writer = new FileWriter(output, true);
			for(int i = 0; i <= itemNames.size() - 1; i++) {
				if (amount.get(i).isEmpty() || numStars.get(i).isEmpty() || count.get(i).isEmpty()) continue;
				writer.write(itemNames.get(i) + ":" + amount.get(i) + ":" + numStars.get(i) + ":" + count.get(i) + "\r\n");
			}
			writer.close();
			
		} 
		catch (IOException e) {
			e.printStackTrace();			
		}
	}
}
