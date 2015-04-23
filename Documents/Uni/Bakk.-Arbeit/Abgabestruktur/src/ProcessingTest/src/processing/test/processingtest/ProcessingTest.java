package processing.test.processingtest;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import processing.core.PApplet;
import processing.test.processingtest.util.DataReader;
import android.content.Intent;

/**
 * Draws a line chart and measures the rendering duration
 * 
 * Test for library processing
 * Different numbers of lines a drawn and the rendering duration is measured.
 * At the end of the test a email will be created with the measurement results.
 * 
 * 
 * @author Christian Osebitz
 * 
 *
 */
public class ProcessingTest extends PApplet {
	// E-Mail Subject for Test-Result
	private static final String E_MAIL_SUBJECT = "Processing Test";
	// Default E-Mail Address for Test-Result
	private static final String E_MAIL_ADDRESS = "osse68@hotmail.com";
	// testdata
	private double[][] data;
	// start value of used lines to draw
	private int numberOfValuesUsed = 1000;
	// stepsize for tests
	private int stepSize = 500;
	// number of series in data file
	private int seriesCount = 3;
	// start time of test
	private long startTime;
	// map to save result of test
	private Map<String, HashMap<Integer, Long>> results = new HashMap<String, HashMap<Integer, Long>>();

	/**
	 * setup the the Activity
	 */
	public void setup() {
		size(400, 200);
		smooth();
		try {
			// read the test values
			getData();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Draw the chart
	 */
	public void draw() {

		if (numberOfValuesUsed <= data.length + stepSize) {
			try {
				// pause between tests
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// draw the next chart
			drawChart();
			// increase number of lines
			numberOfValuesUsed += 500;
		} else {
			sendData();
			System.gc();
		}
	}

	/**
	 * Create email to send the measurament results
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

	private void drawChart() {
		// clear screen
		background(255);

		// calculate width between points on x series
		float elementWidth = width / (seriesCount - 1);

		// start test measurement
		startTime = System.currentTimeMillis();

		// create Dataset for chart
		for (int lines = 0; lines < numberOfValuesUsed; lines++) {
			if (data.length - 1 == lines)
				break;
			for (int seriesNumber = 0; seriesNumber < seriesCount - 1; seriesNumber++) {
				stroke(0);
				strokeWeight(2);
				// create line
				line(elementWidth * seriesNumber,  //Position in x direction for startpoint
						height * (float) data[lines][seriesNumber], // calculate position in y direction for startpoint
						elementWidth * (seriesNumber + 1), //Position in x direction for endpoint
						height 	* (float) data[lines][seriesNumber + 1]); //calculate position in y direction for endpoint

			}
		}

		String library = "Processing";
		if (results.get(library) == null) {
			results.put(library, new HashMap<Integer, Long>());
		}
		
		// end test measurement
		results.get(library).put(numberOfValuesUsed,
				System.currentTimeMillis() - startTime);
		System.out.println("Duration - " + numberOfValuesUsed + ": "
				+ (System.currentTimeMillis() - startTime));
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
}
