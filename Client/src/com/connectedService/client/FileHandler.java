package com.connectedService.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandler {

	public static String readFile(String fileName) {
		StringBuilder sb = new StringBuilder();
		try {
			 
			FileReader fileReader = new FileReader(fileName);

			// Always wrap FileReader in BufferedReader.
			BufferedReader buffer = new BufferedReader(fileReader);

			String line;
			while ((line = buffer.readLine()) != null) {
				sb.append(line + "\r\n");
			}

			buffer.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");

		}
		return sb.toString();
	}

	public static boolean saveFile(String fileName, String fileData) {
		boolean isSaved = false;
		if(fileData==null || fileData.equals(""))
		{
			return isSaved;
		}
		BufferedWriter writer = null;
		File file = new File(fileName);
		try {
			
	        file.createNewFile();
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(fileData);
			System.out.println("File Saved at: " + file.getAbsolutePath());
			isSaved = true;
		} catch (IOException e) {
			System.out.println("Error writing file '" + file.getName() + "'");
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
				System.out.println("Error closing stream for '" + fileName + "'");
			}
		}
		return isSaved;
	}
}
