package com.wsptt;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;

import com.kwit.spider.PageInfo;
import com.kwit.spider.WebSpider;

public class ImageSpider extends WebSpider
{
	private HashSet images;
	private File outdir;

	public ImageSpider(String base, File outdir) throws MalformedURLException
	{
		super(base);
		super.setDelay(5);
		images = new HashSet();
		this.outdir = outdir;
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
		URL[] list = p.getImages();
		if (list != null)
		{
			for (int i = 0; i < list.length; ++i)
			{
				if (images.contains(list[i]) == false)
				{
					images.add(list[i]);
					if (saveImage(list[i], outdir))
						System.out.println("Saved image from: " + list[i].toString());
					else
						System.out.println("Could not save image: " + list[i].toString());
				}
			}
		}
	}

	protected void handleNonHTMLlink(URL url, URL parent, PageInfo p)
	{
	}

	private boolean saveImage(URL url, File dir)
	{
		String outdir = dir.toString();
		String file = url.getFile();
		File outfile;
		if (outdir == null || file == null || outdir.length() == 0 || file.length() == 0)
			return (false);
		if (File.separatorChar == '\\')
		{
			StringBuffer b = new StringBuffer(file);
			for (int i = 0; i < b.length(); ++i)
				if (b.charAt(i) == '/')
					b.setCharAt(i, '\\');
			file = b.toString();
		}
		if (outdir.charAt(outdir.length() - 1) == File.separatorChar || file.charAt(0) == File.separatorChar)
			outfile = new File(outdir + file);
		else
			outfile = new File(outdir + File.separatorChar + file);

		// Create any needed intermediate directories
		if (outfile.getParent() != null)
		{
			File parentDir = outfile.getParentFile();
			if (parentDir.exists() == false)
				parentDir.mkdirs();
		}

		byte[] theImage = this.getContent(url);
		if (theImage != null)
		{
			int size = theImage.length;
			try
			{
				BufferedOutputStream fstream = new BufferedOutputStream(new FileOutputStream(outfile));
				fstream.write(theImage, 0, size);
				fstream.flush();
				fstream.close();
			}
			catch (IOException e)
			{
				return (false);
			}
			return (true);
		}
		else
			return (false);
	}
}