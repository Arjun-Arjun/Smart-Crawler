package com.kwit.spider;

/**
 * PageInfo - Web Page Information object
 */

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

public class PageInfo
{
	private final static URL[] dummy = new URL[1];
	private final static String HTML = "text/html";
	private URL url;
	private URL parentUrl;
	private String title;
	private URL[] links;
	private URL[] images;
	private boolean valid;
	private int responseCode;
	private String contentType;
	private int contentLength;

	/** Constructor */
	public PageInfo(URL url, URL parentUrl, String contentType, int contentLength, int responseCode)
	{
		this.url = url;
		this.parentUrl = parentUrl;
		this.contentType = contentType;
		this.contentLength = contentLength;
		this.responseCode = responseCode;
		valid = false;
	}

	/** For debugging - dump page information */
	public void dump()
	{
		System.out.println("URL: " + url);
		System.out.println("Parent URL: " + parentUrl);
		System.out.println("Title: " + title);
		if (links != null)
		{
			System.out.print("Links: [");
			for (int i = 0; i < links.length; ++i)
			{
				System.out.print(links[i]);
				if (i < (links.length - 1))
					System.out.print(", ");
			}
			System.out.println("]");
		}
		if (images != null)
		{
			System.out.print("Images: [");
			for (int i = 0; i < images.length; ++i)
			{
				System.out.print(images[i]);
				if (i < (images.length - 1))
					System.out.print(", ");
			}
			System.out.println("]");
		}
		System.out.println("Valid: " + valid);
		System.out.println("Response Code: " + responseCode);
		System.out.println("Content Type: " + contentType);
		System.out.println("Content Length: " + contentLength);
	}

	/** Call WebPageXtractor and process WebPage */
	public void extract(Reader reader) throws IOException
	{
		// Note: contentLength of -1 means UNKNOWN
		if (reader == null || url == null || responseCode != HttpURLConnection.HTTP_OK || contentLength == 0
			|| contentType.equalsIgnoreCase(HTML) == false)
		{
			valid = false;
			return;
		}
		WebPageXtractor x = new WebPageXtractor();
		try
		{
			x.parse(reader);
		}
		catch (EOFException e)
		{
			valid = false;
			return;
		}
		catch (SocketTimeoutException e)
		{
			valid = false;
			throw (e);
		}
		catch (IOException e)
		{
			valid = false;
			return;
		}
		ArrayList rawlinks = x.getLinks();
		ArrayList rawimages = x.getImages();

		// Get web page title (1st title if more than one!)
		ArrayList rawtitle = x.getTitle();
		if (rawtitle.isEmpty())
			title = null;
		else
			title = new String((String) rawtitle.get(0));

		// Get links
		int numelem = rawlinks.size();
		if (numelem == 0)
			links = null;
		else
		{
			ArrayList t = new ArrayList();
			for (int i = 0; i < numelem; ++i)
			{
				String slink = (String) rawlinks.get(i);
				try
				{
					URL link = new URL(url, slink);
					t.add(link);
				}
				catch (MalformedURLException e)
				{ /* Ignore */
				}
			}
			if (t.isEmpty())
				links = null;
			else
				links = (URL[]) t.toArray(dummy);
		}

		// Get images
		numelem = rawimages.size();
		if (numelem == 0)
			images = null;
		else
		{
			ArrayList t = new ArrayList();
			for (int i = 0; i < numelem; ++i)
			{
				String simage = (String) rawimages.get(i);
				try
				{
					URL image = new URL(url, simage);
					t.add(image);
				}
				catch (MalformedURLException e)
				{
				}
			}
			if (t.isEmpty())
				images = null;
			else
				images = (URL[]) t.toArray(dummy);
		}

		// Set valid flag
		valid = true;
	}

	public String getContentType()
	{
		return (contentType);
	}

	public URL[] getImages()
	{
		return (images);
	}

	public URL[] getLinks()
	{
		return (links);
	}

	public URL getParentUrl()
	{
		return (parentUrl);
	}

	public int getResponseCode()
	{
		return responseCode;
	}

	public String getTitle()
	{
		return (title);
	}

	// Accessors
	public URL getUrl()
	{
		return (url);
	}

	public boolean isValid()
	{
		return (valid);
	}
}
