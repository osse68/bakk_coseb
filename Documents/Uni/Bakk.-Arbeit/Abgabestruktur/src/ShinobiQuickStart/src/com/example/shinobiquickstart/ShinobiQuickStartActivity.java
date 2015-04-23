package com.example.shinobiquickstart;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.shinobiquickstart.util.DataReader;
import com.shinobicontrols.charts.ChartFragment;
import com.shinobicontrols.charts.DataPoint;
import com.shinobicontrols.charts.LineSeries;
import com.shinobicontrols.charts.NumberAxis;
import com.shinobicontrols.charts.NumberRange;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.charts.SimpleDataAdapter;
/**
 * Draws a line chart and measures the rendering duration
 * 
 * Test for charting library shinobicharts in Version 1.7.1. 
 * A Trial license is used and has to be replaced after the 29.04.2015.
 * Different numbers of lines a drawn and the rendering duration is measured.
 * At the end of the test a email will be created with the measurement results.
 * 
 * 
 * @author Christian Osebitz
 * 
 *
 */
public class ShinobiQuickStartActivity extends ActionBarActivity {

	// E-Mail Subject for Test-Result
	private static final String E_MAIL_SUBJECT = "Shinobi Test";
	// Default E-Mail Address for Test-Result
	private static final String E_MAIL_ADDRESS = "osse68@hotmail.com";

	// Trial License. Valid until 29.04.2015
	private String license = "qIOH0rILQsIxMUFMjAxNTA0Mjlvc3NlNjhAaG90bWFpbC5jb20=HmF0AJuVFRlyb4iDw+8WfIIzc5O3NHJbS3ibpCg1N5qwadkj+"
			+ "KGvssZtzhv+RfIz6LUZavG2e67sKpXvlTEQmd8LAkJ8DDaTKiByofVYphHZdNE/lz8hFmWCrYlF8wcWraWdzcDlUYZqR28r0x5U0ctB7Ddg=BQxSUis"
			+ "l3BaWf/7myRmmlIjRnMU2cA7q+/03ZX9wdj30RzapYANf51ee3Pi8m2rVW6aD7t6Hi4Qy5vv9xpaQYXF5T7XzsafhzS3hbBokp36BoJZg8IrceBj742n"
			+ "QajYyV7trx5GIw9jy/V6r0bvctKYwTim7Kzq+YPWGMtqtQoU=PFJTQUtleVZhbHVlPjxNb2R1bHVzPnh6YlRrc2dYWWJvQUh5VGR6dkNzQXUrUVAxQnM"
			+ "5b2VrZUxxZVdacnRFbUx3OHZlWStBK3pteXg4NGpJbFkzT2hGdlNYbHZDSjlKVGZQTTF4S2ZweWZBVXBGeXgxRnVBMThOcDNETUxXR1JJbTJ6WXA3a1Y"
			+ "yMEdYZGU3RnJyTHZjdGhIbW1BZ21PTTdwMFBsNWlSKzNVMDg5M1N4b2hCZlJ5RHdEeE9vdDNlMD08L01vZHVsdXM+PEV4cG9uZW50PkFRQUI8L0V4cG9uZW50PjwvUlNBS2V5VmFsdWU+";

	// testdata
	private double[][] data;
	// start value of used lines to draw
	private int numberOfValuesUsed = 50;
	// stepsize for tests
	private int stepSize = 20;
	// number of series in data file
	private int seriesCount = 3;
	// start time of test
	private long startTime;
	// map to save result of test
	private Map<String, HashMap<Integer, Long>> results = new HashMap<String, HashMap<Integer, Long>>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shinobi_quick_start);

		if (savedInstanceState == null) {
			try {
				getData();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// Prepare the chart
			ChartFragment chartFragment = (ChartFragment) getFragmentManager()
					.findFragmentById(R.id.chart);

			ShinobiChart shinobiChart = chartFragment.getShinobiChart();
			// set the license key
			shinobiChart.setLicenseKey(license);

			shinobiChart.setTitle("Shinobi Test Chart");

			NumberAxis xAxis = new NumberAxis();
			shinobiChart.setXAxis(xAxis);

			NumberAxis yAxis = new NumberAxis();
			shinobiChart.setYAxis(yAxis);
			// set number range for y axis to -0.05 and 1.05 because test-values are normalized
			yAxis.setDefaultRange(new NumberRange(-0.05, 1.05));

			
			draw(shinobiChart);

		}
	}

	/**
	 * Draw the chart with different numbers of lines
	 * @param shinobiChart
	 */
	private void draw(ShinobiChart shinobiChart) {
		if (numberOfValuesUsed <= data.length + stepSize) {
			try {
				// pause between tests
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// start test measurement
			startTime = System.currentTimeMillis();
			for (int lines = 0; lines < numberOfValuesUsed; lines++) {
				// stop if last test-value is reached
				if (data.length - 1 == lines)
					break;
				
				// create Dataset for chart
				SimpleDataAdapter<Double, Double> dataAdapter = new SimpleDataAdapter<Double, Double>();
				for (int seriesNumber = 0; seriesNumber < seriesCount; seriesNumber++) {
					dataAdapter.add(new DataPoint<Double, Double>(
							(double) seriesNumber, data[lines][seriesNumber]));
				}

				// create line series and add dataset
				LineSeries series = new LineSeries();
				series.setDataAdapter(dataAdapter);
				
				// add Series to chart
				shinobiChart.addSeries(series);

			}

			// redraw chart
			shinobiChart.redrawChart();
			String library = "Shinobi";
			if (results.get(library) == null) {
				results.put(library, new HashMap<Integer, Long>());
			}
			// end test measurement
			long duration = System.currentTimeMillis() - startTime;
			results.get(library).put(numberOfValuesUsed, duration);
			System.out.println("Duration - " + numberOfValuesUsed + ": "
					+ duration);

			// break if duration > 15 Seconds
			if (duration > 15000) {
				numberOfValuesUsed = data.length + stepSize;
			}
			
			// increase number of lines
			numberOfValuesUsed += stepSize;
			
			// draw next chart
			draw(shinobiChart);
		} else {
			sendData();
			System.gc();
		}
	}

	/**
	 * Create email to send the measurement results
	 */
	private void sendData() {

		StringWriter str = new StringWriter();
		for (Entry<String, HashMap<Integer, Long>> entry : results.entrySet()) {
			String fileName = "test_" + entry.getKey() + ".csv";

			str.append(fileName + "\n");

			for (Entry<Integer, Long> values : entry.getValue().entrySet()) {
				str.append(values.getKey() + ";" + values.getValue() + "\n");
			}
		}

		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setType("text/plain");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
				new String[] { E_MAIL_ADDRESS });
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				E_MAIL_SUBJECT);
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, str.toString());
		startActivity(emailIntent);
		finish();

	}

	/**
	 * Load the test values from file
	 * @throws IOException
	 */
	private void getData() throws IOException {
		InputStream is = this.getResources().openRawResource(R.raw.norm_data);
		DataReader reader = new DataReader(is);
		data = reader.getData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds iems to the action bar if it is present.
		getMenuInflater().inflate(R.menu.shinobi_quick_start, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
