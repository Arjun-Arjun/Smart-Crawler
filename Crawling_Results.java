package com.wsptt;

 
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import java.sql.*;
 
public class Crawling_Results {
    public static void main(String[] args) {
        
    
  int count=0;
  int count1=0;
  int count2=0;
  int count3=0;
  int count4=0;
  
  String sname1=null;
  String sname2=null;
  String sname3=null;
  String sname4=null;
  String sname5=null;
  
  double c1=0.0;
  double c2=0.0;
  double c3=0.0;
  double c4=0.0;
  double c5=0.0;
  double c6=0.0;
  double c7=0.0;
  double c8=0.0;
  
 
  
    	try
    	{
    		 Dbcon db = new Dbcon();
			 Connection con = db.getConnection();
             Statement stmt = con.createStatement();
             
    		ResultSet rs=stmt.executeQuery("select * from crawler");
    	
    		while(rs.next()==true)
    		{
    			count++;
    			
    			if(count==1)
    			{
    				sname1=rs.getString(2);
    				c1=Double.valueOf(rs.getString(4));
    			}
    			
    			if(count==2)
    			{
    				sname2=rs.getString(2);
    				c2=Double.valueOf(rs.getString(4));
    			}
    			if(count==3)
    			{
    				sname3=rs.getString(2);
    				c3=Double.valueOf(rs.getString(4));
    			}
    			if(count==4)
    			{
    				sname4=rs.getString(2);
    				c4=Double.valueOf(rs.getString(4));
    			}
    			if(count==5)
    			{
    				sname5=rs.getString(2);
    				c5=Double.valueOf(rs.getString(4));
    			}
    			
    		}
    		
    		
    		
    	
    		
    		
    		   	DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
    	        dataSet.setValue(c1, sname1, "Site1");
    	        dataSet.setValue(c2,  sname2,"Site2");
    	        dataSet.setValue(c3,  sname3,"Site3");
    	        dataSet.setValue(c4,  sname4,"Site4");
    	        dataSet.setValue(c5,  sname5,"Site5");
    	        
    	      
    	        JFreeChart chart = ChartFactory.createBarChart3D("SmartCrawler A Two stage Crawler for Efficiently Harvesting Deep Web Interfaces", "Total Site Rank", "Total Rank",
    	        dataSet, PlotOrientation.VERTICAL, true, true, true);
    	        ChartFrame chartFrame=new ChartFrame("SITE RANKING:: Smart Crawler Details",chart);
    	        chartFrame.setVisible(true);
    	        chartFrame.setSize(800,500);
    	}
    	catch(Exception ex)
    	{
    	System.out.println(ex);	
    		
    	}
    
}
}