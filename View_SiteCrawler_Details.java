package com.wsptt;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;


import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import java.awt.*;

import java.net.*;

public class View_SiteCrawler_Details extends JFrame implements ActionListener
{
 JButton property,Reset;
 JPanel panel;
 JLabel label1;
  JTextField  text1;
  Vector data;
Vector heading;

JButton view;
JScrollPane pane;
JTable table;
int v,h;
String s,d,call,dt;
Container c;

JLabel imglabel;
View_SiteCrawler_Details() 
 {
	
setTitle("View_SiteCrawler_Details::SmartCrawler A Two stage Crawler for Efficiently Harvesting Deep Web Interfaces");
c= getContentPane();
c.setLayout (new FlowLayout());
c.setBackground(Color.ORANGE);
setSize(800,100);
setVisible(true);

try {
	 Vector heading = new Vector();
	 
	 heading.addElement("Site ID");
	 heading.addElement("Site or URL Name");
	 heading.addElement("Search Keyword");
	 heading.addElement("Site Ranks");
	 heading.addElement("MAX Urls Searched");
	 heading.addElement("Date&Time");
	 
	 
	 Vector data=new Vector();
	 
	 Dbcon db = new Dbcon();
	 Connection con = db.getConnection();
     Statement stmt = con.createStatement();
     
     
     String query = "SELECT * FROM crawler";
     ResultSet rs = stmt.executeQuery(query);

	ResultSetMetaData rsm=rs.getMetaData();
	int col=rsm.getColumnCount();


	            while(rs.next())
	             {
	Vector row = new Vector();
	  for(int i = 1; i <=col; i++){
	                   row.addElement(rs.getObject(i));

	             }

	data.addElement(row);
	             }
			  
	            JTable table = new JTable(data,heading);
				  
				  pane = new JScrollPane(table);
				 
				  pane.setBounds(10,230,1100,500);
				  c.add(pane);
	 } 
	 catch(Exception ex) {
		 ex.printStackTrace();
		 } 

  }

public void actionPerformed(ActionEvent ae)
{

Object o=ae.getSource();

if(o==property)
{
		
}

}

}