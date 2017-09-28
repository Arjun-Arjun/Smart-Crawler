package com.wsptt;

/**
 * Server Stress Test - this program emulates one or more users
 * walking through an entire Web site. It can be used to test load on a server,
 * or to test simulataneous accesses to a Web application. It should normally be
 * run on two machines on the same LAN, with one running this Application and
 * the other running the target server. NOTE: Under NO circumstances should it
 * be run against either servers or using networks that you do not have explicit
 * permissions to load/stress test.
 */

import java.net.MalformedURLException;
import java.net.URL;

import com.kwit.spider.PageInfo;
import com.kwit.spider.WebSpider;

public class SiteStressCrawler
{
	private static final String syntax = " StressCrawler <url> [ thread count ]";
	public static void main(String[] args)
	{
		if (args.length == 0 || args.length > 2)
		{
			System.out.println(syntax);
			System.exit(-1);
		}
		String baseUrl = args[0];
		if (baseUrl == null || baseUrl.length() == 0)
		{
			System.out.println("Missing URL");
			System.out.println(syntax);
			System.exit(-1);
		}
		int cnt = 1;
		int maxcnt = 100;
		if (args.length == 2)
		{
			try
			{
				cnt = Integer.parseInt(args[1]);
			}
			catch (NumberFormatException e)
			{
				cnt = -1;
			}
			if (cnt < 1 || cnt > maxcnt)
			{
				System.out.println("Invalid thread count, use 1 to " + maxcnt);
				System.exit(-1);
			}
		}
		System.out.println("Stress Crawler Started");

		final SiteStressCrawler test = new SiteStressCrawler(baseUrl);
		Thread threadList[] = new Thread[cnt];
		for (int i = 0; i < cnt; ++i)
		{
			Runnable r = new Runnable()
			{
				public void run()
				{
					test.spiderSite();
				}
			};
			threadList[i] = new Thread(r);
			System.out.println(threadList[i].toString() + " started");
			threadList[i].start();
		}

		try
		{
			for (int i = 0; i < cnt; ++i)
				threadList[i].join();
		}
		catch (InterruptedException e)
		{ // Ignore
		}

		System.out.println("Stress Crawler Finished");
		System.out.println("Traversed " + StressTestSpider.getPageCnt() + " pages");
		System.out.println("Found " + StressTestSpider.getBadPages() + " bad pages");
		System.out.println("I/O errors in " + StressTestSpider.getBadIO() + " pages");
	}

	private String base;

	public SiteStressCrawler(String base)
	{
		this.base = base;
	}

	private void spiderSite()
	{
		StressTestSpider spider = null;
		try
		{
			spider = new StressTestSpider(base);
		}
		catch (MalformedURLException e)
		{
			System.out.println(e);
			System.out.println("Invalid URL: " + base);
			System.exit(-1);
		}
		spider.traverse();
	}
}

class StressTestSpider extends WebSpider
{
	private static long pageCnt;
	private static long badPages;
	private static long badIO;

	/**
	 * Returns the badIO.
	 * 
	 * @return long
	 */
	public static long getBadIO()
	{
		return badIO;
	}

	/**
	 * Returns the badPages.
	 * 
	 * @return long
	 */
	public static long getBadPages()
	{
		return badPages;
	}

	/**
	 * Returns the pageCnt.
	 * 
	 * @return long
	 */
	public static synchronized long getPageCnt()
	{
		return pageCnt;
	}

	public StressTestSpider(String base) throws MalformedURLException
	{
		super(base);
		super.setDelay(2);
	}

	protected synchronized void handleBadIO(URL url, URL parentURL)
	{
		System.out.println("I/O error accessing page: " + url);
		badIO++;
	}

	protected synchronized void handleBadLink(URL url, URL parent, PageInfo p)
	{
		System.out.println("Invalid URL: " + url + ((parent == null) ? "" : " in " + parent));
		badPages++;
	}

	protected synchronized void handleExternalLink(URL url, URL parent)
	{
		System.out.println("External link: " + url + ((parent == null) ? "" : " in " + parent));
	}

	protected synchronized void handleLink(PageInfo p)
	{
		System.out.println(p.getUrl());
		pageCnt++;
	}

	protected synchronized void handleNonHTMLlink(URL url, URL parent, PageInfo p)
	{
		System.out.println("Non-HTML link: " + url + " type: " + p.getContentType());
	}
}
