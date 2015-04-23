package com.example.myfirstproject;

import android.content.Context;
import android.view.View;
/**
 * Text-view for afreechart
 * @author christian osebitz
 *
 */
public class AFreeChartTest extends ChartTest{

	public AFreeChartTest(double[][] data) {
		super(data);
	}

	@Override
	public View getView(int numberOfValues, Context context) {
		return  new AFreeChartLineChartView(context, getData(), getData()[0].length, numberOfValues);
	}

	@Override
	public String getLibraryName() {
		return "AFreeChart";
	}

}
