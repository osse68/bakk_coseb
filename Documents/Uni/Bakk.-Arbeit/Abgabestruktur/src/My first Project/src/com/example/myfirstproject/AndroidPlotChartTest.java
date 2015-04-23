package com.example.myfirstproject;

import android.content.Context;
import android.view.View;

public class AndroidPlotChartTest extends ChartTest {


	public AndroidPlotChartTest(double[][] data) {
		super(data);
	}

	@Override
	public View getView(int numberOfValues, Context context) {
		return new AndroidPlotLineChartView(context, getData(), getData()[0].length, numberOfValues);
	}

	@Override
	public String getLibraryName() {

		return "AndroidPlot";
	}

}
