package com.example.myfirstproject;

import android.content.Context;
import android.view.View;


/**
 * Abstract class for testind the libraries.
 * @author christian osebitz
 *
 */
public abstract class ChartTest {

	private double[][] data;

	public ChartTest(double[][] data) {
		this.data = data;
	}
	
	protected double[][] getData() {
		return data;
	}

	public abstract View getView(int numberOfValues, Context context);
	
	public abstract String getLibraryName();
}
