package com.kwit.spider;

import java.io.PrintStream;

public class SimpleHTMLToken
{
	public static final int TAG = 0;
	public static final int ENDTAG = 1;
	public static final int CONTENT = 2;
	private static final int UNDEFINED = -1;

	private int type;
	private String content;

	/**
	 * Constructor for SimpleHTMLToken.
	 */
	public SimpleHTMLToken()
	{
		type = UNDEFINED;
		content = null;
	}

	/**
	 * Constructor for SimpleHTMLToken.
	 */
	public SimpleHTMLToken(int type, String content)
	{
		this.type = type;
		this.content = content;
	}

	/**
	 * dump - used for debugging
	 */
	public void dump(PrintStream out)
	{
		switch (type)
		{
			case UNDEFINED:
				out.println("Error!");
				break;
			case TAG:
				out.println("<" + content + ">");
				break;
			case ENDTAG:
				out.println("</" + content + ">");
				break;
			case CONTENT:
				out.println("\"" + content + "\"");
				break;
			default:
				out.println("Error!");
				break;
		}

	}

	/**
	 * Returns the content.
	 * 
	 * @return String
	 */
	public String getContent()
	{
		return content;
	}

	/**
	 * Returns the type.
	 * 
	 * @return int
	 */
	public int getType()
	{
		return type;
	}

	/**
	 * Sets the content.
	 * 
	 * @param content
	 *            The content to set
	 */
	public void setContent(String content)
	{
		this.content = content;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            The type to set
	 */
	public void setType(int type)
	{
		this.type = type;
	}
}
