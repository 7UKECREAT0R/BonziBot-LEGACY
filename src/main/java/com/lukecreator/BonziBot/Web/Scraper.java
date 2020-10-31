package com.lukecreator.BonziBot.Web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Scraper {
	
	public boolean did404 = false;
	public URL url;
	public String contentRaw = null;
	public String[] contentLines;
	
	public Scraper(String url) throws MalformedURLException {
		this.url = new URL(url);
	}
	
	public void downloadSiteData(String splitter) {
		WebClient client = null;
		try {
			client = new WebClient();
			client.getOptions().setCssEnabled(false);
			client.getOptions().setJavaScriptEnabled(false);
			client.getOptions().setUseInsecureSSL(true);
			HtmlPage page = client.getPage(url);
			contentRaw = page.getWebResponse().getContentAsString();
			//System.out.println(contentRaw);
			contentLines = contentRaw.split(splitter);
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (client != null) client.close();
		}
		return;
	}
	public boolean hasClass(String clazz) {
		return contentRaw.contains("class=\"" + clazz + "\"");
	}
	public boolean hasField(String field) {
		return contentRaw.contains(field);
	}
	public int lineOfField(String field) {
		for(int i = 0; i < contentLines.length; i++) {
			if(contentLines[i].contains(field)) {
				return i;
			}
		}
		return -1;
	}
	public String getLine(int line) {
		return contentLines[line];
	}
}
