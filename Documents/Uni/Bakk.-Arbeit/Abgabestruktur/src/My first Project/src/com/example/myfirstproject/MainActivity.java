package com.example.myfirstproject;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import com.example.myfirstproject.util.DataReader;

/**
 * Draws a line chart and measures the rendering duration
 * 
 * Test for charting library androidplot, achartengine and a afreechart
 * Different numbers of lines a drawn and the rendering duration is measured. At
 * the end of the test a email will be created with the measurement results.
 * Enable (uncomment) the library to test. It's possible to test all 3 libraries
 * at the same time.
 * 
 * @author Christian Osebitz
 * 
 * 
 */
public class MainActivity extends Activity {
	// E-Mail Subject for Test-Result
	private static final String E_MAIL_SUBJECT = "Androidplot Test";
	// Default E-Mail Address for Test-Result
	private static final String E_MAIL_ADDRESS = "osse68@hotmail.com";

	// testdata
	double[][] data;
	// libaries used for test
	List<ChartTest> testCharts = new ArrayList<ChartTest>();
	// numbers of libraries used for test
	int chart = 0;
	// start value of used lines to draw
	int numberOfValuesUsed = 1000;
	// stepsize for tests
	int stepSize = 500;
	// number of series in data file
	int seriesCount = 3;
	// start time of test
	protected long startTime;
	// map to save result of test
	private Map<String, HashMap<Integer, Long>> results = new HashMap<String, HashMap<Integer, Long>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {

			// load data
			getData();
			// The tested libaries. Uncommt library for testing.
			// testCharts.add(new AChartEngineChartTest(data));
			testCharts.add(new AFreeChartTest(data));
			// testCharts.add(new AndroidPlotChartTest(data));

			// start test measurement
			startTime = System.currentTimeMillis();
			
			// run test
			run(testCharts.get(0).getView(numberOfValuesUsed, this));
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	// should stop test
	boolean isRunning = true;

	/**
	 * Render the view
	 * @param tv
	 */
	public void run(final View tv) {
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				try {
					
					// get chart
					final ChartTest chartTest = testCharts.get(chart);
					String library = chartTest.getLibraryName();
					if (results.get(library) == null) {
						results.put(library, new HashMap<Integer, Long>());
					}
					// end test measurement
					results.get(library).put(numberOfValuesUsed,
							System.currentTimeMillis() - startTime);

					System.out.println("Duration - " + numberOfValuesUsed
							+ ": " + (System.currentTimeMillis() - startTime));
					
					// pause between tests
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				if (isRunning) {
					//invalidate old view
					tv.invalidate();
					// start test measurement
					MainActivity.this.startTime = System.currentTimeMillis();
					// set new view
					setContentView(tv);
					// next chart/library
					chart++;
					
					// increase number of lines if all charts are render with same number of lines
					if (testCharts.size() == chart) {
						chart = 0;
						numberOfValuesUsed += stepSize;
						// end of data set
						if (numberOfValuesUsed > data.length) {
							isRunning = false;
							System.out.println("should stop");
							StringWriter str = new StringWriter();
							for (Entry<String, HashMap<Integer, Long>> entry : results
									.entrySet()) {
								String fileName = "test_" + entry.getKey()
										+ ".csv";

								str.append(fileName + "\n");
								for (Entry<Integer, Long> values : entry
										.getValue().entrySet()) {
									str.append(values.getKey() + ";"
											+ values.getValue() + "\n");
								}
							}
							
							// send measurement results
							sendData(str.toString());
							System.gc();
						}
					}

					final ChartTest chartTest = testCharts.get(chart);
					View view = chartTest.getView(numberOfValuesUsed,
							MainActivity.this);
					ViewTreeObserver viewTreeObserver = view
							.getViewTreeObserver();
					viewTreeObserver
							.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

								@Override
								public void onGlobalLayout() {
									String library = chartTest.getLibraryName();
									if (results.get(library) == null) {
										results.put(library,
												new HashMap<Integer, Long>());
									}
									results.get(library).put(
											numberOfValuesUsed,
											System.currentTimeMillis()
													- startTime);

									System.out.println("Duration - "
											+ numberOfValuesUsed
											+ ": "
											+ (System.currentTimeMillis() - startTime));
								}
							});
					
					// draw next chart
					MainActivity.this.run(view);
				}

			}

			/**
			 * Create email to send the measurement results
			 */
			private void sendData(String message) {
				Intent emailIntent = new Intent(Intent.ACTION_SEND);

				emailIntent.setType("text/plain");
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
						new String[] { E_MAIL_ADDRESS });
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						E_MAIL_SUBJECT);
				emailIntent
						.putExtra(android.content.Intent.EXTRA_TEXT, message);
				startActivity(emailIntent);
				finish();
			}
		}, 1000);
	}
}