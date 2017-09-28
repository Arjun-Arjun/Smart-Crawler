package com.wsptt;

import java.net.MalformedURLException;
import java.net.URL;

import com.kwit.spider.PageInfo;
import com.kwit.spider.WebSpider;

class MySpider extends WebSpider
{
	public MySpider(String base) throws MalformedURLException
	{
		super(base);
	}

	protected void handleBadIO(URL url, URL parent)
	{
	}

	protected void handleBadLink(URL url, URL parent, PageInfo p)
	{
	}

	protected void handleExternalLink(URL url, URL parent)
	{
	}

	protected void handleLink(PageInfo p)
	{
		String link = p.getUrl().toString();
		String title = p.getTitle();
		if (link == null || title == null || link.length() == 0 || title.length() == 0)
			return;
		else
			System.out.println("<li><a href=\"" + link + "\">" + title + "</a></li>");
	}

	protected void handleNonHTMLlink(URL url, URL parent, PageInfo p)
	{
	}
}

public class WebMapGenCrawler
{
	private final static String header = "<html><head><title>Site Map</title></head><body><ul>";
	private final static String trailer = "</ul></body></html>";
	public static void main(String[] args)
	{
		if (args.length != 1)
		{
			System.err.println(" SiteMapGen Crawler <url>");
			System.exit(-1);
		}
		WebMapGenCrawler s = new WebMapGenCrawler(args[0]);
		s.generate();
	}

	private String site;

	public WebMapGenCrawler(String site)
	{
		this.site = site;
	}

	public void generate()
	{
		MySpider spider = null;
		try
		{
			spider = new MySpider(site);
		}
		catch (MalformedURLException e)
		{
			System.err.println(e);
			System.err.println("Invalid URL: " + site);
			return;
		}
		System.out.println(header);
		spider.traverse();
		System.out.println(trailer);
	}
}