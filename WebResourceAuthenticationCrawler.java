package com.wsptt;

/**
 * GetGraphics - walk through a Web site, find all the graphics and save in a
 * local directory tree Sample program for WebSpider web spider framework
 */

import java.io.File;
import java.net.MalformedURLException;

public class WebResourceAuthenticationCrawler
{

	public static void main(String[] args)
	{
		if (args.length != 2)
		{
			System.out.println("GetGraphics Crawler <url> <output directory>");
			System.exit(-1);
		}
		File outdir = new File(args[1]);
		if (outdir.isDirectory() == false || outdir.canWrite() == false)
		{
			System.out.println("Cannot access directory " + args[1]);
			System.exit(-1);
		}
		ImageSpider spider = null;
		try
		{
			spider = new ImageSpider(args[0], outdir);
		}
		catch (MalformedURLException e)
		{
			System.out.println(e);
			System.out.println("Invalid URL: " + args[0]);
			System.exit(-1);
		}
		System.out.println("Get Graphics Crawler:");
		spider.traverse();
		System.out.println("Finished");
	}
}
