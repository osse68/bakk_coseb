package com.example.myfirstproject.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataReader {
	double[][] data = new double[5114][3];
	
	public double[][] getData() {
		return data;
	}

	public DataReader(InputStream is) throws IOException {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		
		String line;
		int i = 0;
		while ((line = reader.readLine()) != null) {
			String[] split = line.split(";");
			if(!split[0].equals("date")) {
				data[i][0] = new Double(split[1]);
				data[i][1] = new Double(split[2]);
				data[i][2] = new Double(split[3]);
				i++;
			}
		}
		reader.close();
	}
	
}
