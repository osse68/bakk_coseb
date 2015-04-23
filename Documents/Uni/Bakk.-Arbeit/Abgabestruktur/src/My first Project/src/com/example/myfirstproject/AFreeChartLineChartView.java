package com.example.myfirstproject;

import org.afree.chart.AFreeChart;
import org.afree.chart.ChartRenderingInfo;
import org.afree.chart.plot.PlotOrientation;
import org.afree.data.xy.XYDataset;
import org.afree.data.xy.XYSeries;
import org.afree.data.xy.XYSeriesCollection;
import org.afree.graphics.geom.Dimension;
import org.afree.graphics.geom.RectShape;
import org.afree.ui.RectangleInsets;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
/**
 * Test-class for library afreechart
 * Creates a view with a line chart
 * @author christian osebitz
 *
 */
public class AFreeChartLineChartView extends View{

	private Dimension size;
	private double[][] data;
	private int seriesCount;
	private int numberOfValuesUsed;

	public AFreeChartLineChartView(Context context, double[][] data, int seriesCount, int numberOfValuesUsed){
		super(context);
		this.data = data;
		this.seriesCount = seriesCount;
		this.numberOfValuesUsed = numberOfValuesUsed;
	}
	
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectangleInsets insets = new RectangleInsets(0,0,0,0);
        RectShape available = new RectShape(insets.getLeft(), insets.getTop(),
                size.getWidth() - insets.getLeft() - insets.getRight(),
                size.getHeight() - insets.getTop() - insets.getBottom());
        
        // create the chart
        AFreeChart chart = org.afree.chart.ChartFactory.createXYLineChart(
                "AFreeChart", // Title
                "Jours", // x-axis Label
                "Repetitions", // y-axis Label
                createDataset(), // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
            );
        
        // draw the chart
        chart.draw(canvas, available, new ChartRenderingInfo());
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh){
    	this.size = new Dimension(w, h);
    }
    
    /**
     * Creates the dataset.
     * 
     * @return test dataset.
     */
    private XYDataset createDataset() {
        
    	final XYSeriesCollection dataset = new XYSeriesCollection();
    	
		for(int lines = 0; lines < numberOfValuesUsed; lines++){
			// create series
	    	XYSeries xSeries=new XYSeries("" + lines + 1);
		    for(int seriesNumber=0;seriesNumber<seriesCount;seriesNumber++)
		    {
		    	xSeries.add(seriesNumber, data[lines][seriesNumber]);
		   
		    }
		    // add series
		    dataset.addSeries(xSeries);
		}
                
        return dataset;
    }
}
