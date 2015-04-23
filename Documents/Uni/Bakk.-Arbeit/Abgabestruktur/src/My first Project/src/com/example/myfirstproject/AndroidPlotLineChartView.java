package com.example.myfirstproject;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

/**
 * Test-class for library androidplot
 * Creates a view with a line chart
 * @author christian osebitz
 *
 */
public class AndroidPlotLineChartView extends XYPlot{

	private double[][] data;
	private int seriesCount;
	private int numberOfValuesUsed;

	public AndroidPlotLineChartView(Context context, double[][] data, int seriesCount, int numberOfValuesUsed){
		super(context, "AndroidPlot Test");
		this.data = data;
		this.seriesCount = seriesCount;
		this.numberOfValuesUsed = numberOfValuesUsed;
		createPlot();
	}
	
	/**
	 * Creates the test-chart.
	 * 
	 * @return a sample dataset.
	 */
	private XYPlot createPlot() {
	    
		for(int lines = 0; lines < numberOfValuesUsed; lines++){
	    	if(lines == data.length) break;

			List<Integer> x = new ArrayList<Integer>();
			List<Double> y = new ArrayList<Double>();
			// create dataset
		    for(int seriesNumber=0;seriesNumber<seriesCount;seriesNumber++)
		    {
		    	x.add(seriesNumber);
		    	y.add(data[lines][seriesNumber]);
		    }
		    // create series with datset
		    SimpleXYSeries xSeries=new SimpleXYSeries(x, y, "" + lines + 1);
		    // set color for line and points
		    LineAndPointFormatter format = new LineAndPointFormatter(Color.BLUE, Color.GREEN, null, null);
		    // add series
		    this.addSeries(xSeries, format);
		}
		

	            
	    return this;
	}
}