package com.kwit.spider;

/**
 * WebSpider - Abstract Web spider class To use, derive class from WebSpider,
 * Add handleLink(), handleBadLink(), handleNonHTMLlink(), handleExternalLink(),
 * and handleBadIO() methods Instantiate and call traverse()
 */

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;

public abstract class WebSpider
{
	private static final String HTML = "text/html";
	private String base;
	private URL baseUrl;
	private HashSet visited;
	private int delay;

	/** Constructor */
	public WebSpider(String base) throws MalformedURLException
	{
		this.base = base;
		baseUrl = new URL(base);
		visited = new HashSet();
		delay = 2;
	}

	/** Return base URL (starting point for Web traversal) */
	public URL getBaseUrl()
	{
		return (baseUrl);
	}

	/** Get contents of a URL */
	public byte[] getContent(URL url)
	{
		byte[] buf = null;
		try
		{
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			int responseCode = connection.getResponseCode();
			int contentLength = connection.getContentLength();
			// System.out.println("Content length: "+contentLength);
			if (responseCode != HttpURLConnection.HTTP_OK || contentLength <= 0)
				return (null);
			InputStream in = connection.getInputStream();
			BufferedInputStream bufIn = new BufferedInputStream(in);
			buf = new byte[contentLength];
			// Added code to handle blocked reads
			int bytesToRead = contentLength;
			int flag = 10;
			while (bytesToRead != 0 && flag != 0)
			{
				int bytesRead = bufIn.read(buf, (contentLength - bytesToRead), bytesToRead);
				bytesToRead = bytesToRead - bytesRead;
				flag--;
				if (flag <= 5)
					sleep(1);
			}
			in.close();
			connection.disconnect();
			if (flag == 0)
				return (null);
		}
		catch (Exception e)
		{
			// System.out.println(e);
			// e.printStackTrace();
			return (null);
		}

		return (buf);
	}

	/**
	 * Returns delay (N second pause after processing EACH web page)
	 * 
	 * @return int
	 */
	public int getDelay()
	{
		return delay;
	}

	// Populate a PageInfo object from a URL
	private PageInfo getWebPage(URL url, URL parentUrl) throws IOException
	{
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		int responseCode = connection.getResponseCode();
		String contentType = connection.getContentType();
		// Note: contentLength == -1 if NOT KNOWN (i.e. not returned from
		// server)
		int contentLength = connection.getContentLength();
		PageInfo p = new PageInfo(url, parentUrl, contentType, contentLength, responseCode);
		InputStreamReader rdr = new InputStreamReader(connection.getInputStream());
		p.extract(rdr);
		rdr.close();
		connection.disconnect();
		return (p);
	}

	/** (Abstract) Handle an I/O Exception (server problem) */
	protected abstract void handleBadIO(URL url, URL parent);

	/** (Abstract) Handle bad URL */
	protected abstract void handleBadLink(URL url, URL parent, PageInfo p);

	/** (Abstract) Handle an external (outside of Web site) link */
	protected abstract void handleExternalLink(URL url, URL parent);

	/** (Abstract) Handle a link; a Web page in the site */
	protected abstract void handleLink(PageInfo p);

	/** (Abstract) Handle a non-HTML link */
	protected abstract void handleNonHTMLlink(URL url, URL parent, PageInfo p);

	private boolean isExternalSite(URL link)
	{
		// Return true if link host is different from base or
		// if path of link is not a superset of base URL
		if (link.getAuthority() != baseUrl.getAuthority() || (!UrlPathDir(link).startsWith(UrlPathDir(baseUrl))))
			return (true);
		else
			return (false);
	}

	/**
	 * Return true if it's OK to visit the link, false if it's not
	 */
	private boolean isOKtoVisit(URL link)
	{
		// Return false if it's not HTTP protocol
		if (!link.getProtocol().equals("http"))
			return (false);
		// Return false if it's an external site
		else if (isExternalSite(link))
			return (false);
		else if (visited.contains(link))
			return (false);
		else
			return (true);
	}

	/**
	 * Sets delay (N second pause after processing EACH web page)
	 * 
	 * @param delay
	 *            The delay to set
	 */
	public void setDelay(int delay)
	{
		this.delay = delay;
	}

	// Sleep N seconds
	private void sleep(int n)
	{
		if (n <= 0)
			return;
		Thread mythread = Thread.currentThread();
		try
		{
			mythread.sleep(n * 1000);
		}
		catch (InterruptedException e)
		{ // Ignore
		}
	}

	/** Traverse Web site */
	public void traverse()
	{
		traverse(baseUrl, null);
	}

	private void traverse(URL url, URL parent)
	{
		boolean isHTMLfile = true;
		PageInfo p = null;
		try
		{
			p = getWebPage(url, parent);
		}
		catch (IOException e)
		{
			handleBadIO(url, parent);
			sleep(delay);
			return;
		}
		if (p == null)
		{
			handleBadLink(url, parent, null);
			sleep(delay);
			return;
		}
		if (p.isValid() == false)
		{
			if (p.getContentType().equalsIgnoreCase(HTML) == false)
				handleNonHTMLlink(url, parent, p);
			else
				handleBadLink(url, parent, p);
			sleep(delay);
			return;
		}
		else
			handleLink(p);

		// Navigate through links on page
		URL[] links = p.getLinks();
		if (links == null)
		{
			sleep(delay);
			return;
		}
		int n = links.length;
		for (int i = 0; i < n; ++i)
		{
			if (isOKtoVisit(links[i]))
			{
				visited.add(links[i]);
				traverse(links[i], url);
			}
			else if (isExternalSite(links[i]))
				handleExternalLink(links[i], url);
		}
		sleep(delay);
		return;
	}

	private String UrlPathDir(URL u)
	{
		String p = u.getPath();
		if (p == null || p.equals(""))
			return ("/");
		int i = p.lastIndexOf("/");
		if (i == -1)
			return ("/");
		else
			p = p.substring(0, i + 1);
		return (p);
	}

}