package com.example.myfirstproject;

import org.achartengine.ChartFactory;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.view.View;

/**
 * Test-class for library achartengine Creates a view with a line chart
 * 
 * @author christian osebitz
 * 
 */
public class AChartEngineChartTest extends ChartTest {

	public AChartEngineChartTest(double[][] data) {
		super(data);
	}

	@Override
	public View getView(int numberOfValues, Context context) {
		// add rendering information
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

		mRenderer.setChartTitle("X Vs Y Chart");
		mRenderer.setXTitle("X Values");
		mRenderer.setYTitle("Y Values");
		
		// zoom enabled
		mRenderer.setZoomButtonsVisible(true);
		mRenderer.setXLabels(0);
		// pan disabled
		mRenderer.setPanEnabled(false);

		mRenderer.setShowGrid(true);

		mRenderer.setClickEnabled(true);

		// Adding data to the X Series.
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		for (int lines = 0; lines < numberOfValues; lines++) {
			
			// break if end of data reached
			if (lines == getData().length)
				break;
			XYSeries xSeries = new XYSeries("" + lines + 1);
			for (int seriesNumber = 0; seriesNumber < getData()[lines].length; seriesNumber++) {
				xSeries.add(seriesNumber, getData()[lines][seriesNumber]);

			}
			
			// add series
			dataset.addSeries(xSeries);
			
			// Create XYSeriesRenderer to customize XSeries

			XYSeriesRenderer Xrenderer = new XYSeriesRenderer();
			mRenderer.addSeriesRenderer(Xrenderer);
		}

		// get the Chart in a view
		return ChartFactory.getLineChartView(context, dataset, mRenderer);
	}

	@Override
	public String getLibraryName() {
		return "AChartEngine";
	}

}
