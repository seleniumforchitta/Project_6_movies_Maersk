/*
	1.	Launch the Website for Film Locations in San Francisco | DataSF | City and County of San Francisco
	2.	Validate the page
	3.	Go to the Movies data that is present in the UI table
	4.	Validate the column names present in the table with that of the data provided in Test_data.xlsx
	5.	Fetch the data Present in the Page
	6.	Go to the next page & fetch all the data present in the UI table
	7.	Store all the table data in an Excel - i.e. - Movies_At_San_Francisco.xlsx in test-output folder in the Project.
	8.  Note – Created a Maven project using Hybrid design pattern and Use TestNG as a framework
 */


package com.movies.TestCases;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.movies.Pages.BaseClass;
import com.movies.Pages.Movies_Front_Page;

public class EndToEnd extends BaseClass {

	static int rowCount = 0;
	static int countIter = 0;

	@Test
	public void endToEndTest() throws InterruptedException, IOException {

		Movies_Front_Page mainPage = new Movies_Front_Page(driver);

		// Checking if the Page is opening correctly
		System.out.println("Opening DataSF - Movies Page !!! ");
		System.out.println(driver.getTitle());

		String page = "Film Locations in San Francisco | DataSF | City and County of San Francisco";
		Assert.assertEquals(driver.getTitle(), page);

		// Clicking on View Data
		mainPage.getviewData().click();

		// Waiting for the Page load to complete
		WebDriverWait pageWait = new WebDriverWait(driver, 60);
		pageWait.until(ExpectedConditions.visibilityOfElementLocated(mainPage.getcolDetails()));
		System.out.println("Page is loaded successfully");

		// Taking all the column names into the list
		List<WebElement> column_names = driver.findElements(mainPage.getcolDetails());

		// Checking if Total number of Columns is correct
		int totalCol = column_names.size();
		System.out.println("Total Number of Columns in the table are : " + totalCol);
		int expectedCol = mainPage.getcolCount();
		Assert.assertEquals(totalCol, expectedCol);
		System.out.println("Number of Columns is  as expected");

		// Printing all Column Names
		for (WebElement i : column_names) {
			System.out.print(i.getText() + "\t");
		}

		// Opening the Excel
		FileInputStream fis = new FileInputStream(
				new File(System.getProperty("user.dir") + "\\src\\test\\resources\\excel\\Test_Data.xlsx"));
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet sheet = wb.getSheetAt(0);

		// Reading the colunm name coming from UI with the data mentioned in
		// Test_Data.xlsx
		System.out.println();
		for (int i = 0; i < totalCol; i++) {
			System.out.print(sheet.getRow(0).getCell(i).toString() + "\t");
			Assert.assertEquals(column_names.get(i).getText().trim().toString(), sheet.getRow(0).getCell(i).toString());
		}
		System.out.println("\n" + "All the column Names are matching !!!");

		// Now Reading the data from the UI table and putting in Excel sheet
		do {

			int ix = 0;

			//if (countIter == 1) {break;}
			for (int i = 1; i <= 100; i++) {
				if (countIter != 0) {
					ix = countIter * 100;
				}
				Row row = sheet.createRow(ix + i);
				for (int j = 1; j <= totalCol; j++) {
					row.createCell(j - 1).setCellValue(driver
							.findElement(
									By.xpath("((//td[@data-cell-render-type='text']/..)[" + i + "]/td/div)[" + j + "]"))
							.getText().toString());
				}
				rowCount++;
			}
			countIter++;
			mainPage.getnextBtn().click();
			Thread.sleep(4000);
			System.out.println("*********************Row " + ix + " to " + (ix + 100)
					+ " is loaded successfully in to the Movies_At_San_Francisco.xlsx**********************");
		} while (mainPage.getnextBtn().isEnabled());

		// Writing into a new Test OutPut excel with the current time Stamp
		System.out.println("Total Row Printed - " + rowCount);
		String timeStamp = LocalDateTime.now().toString();
		timeStamp = timeStamp.replaceAll(":", "-");
		timeStamp = timeStamp.substring(0, 19);
		FileOutputStream fileOut = new FileOutputStream(
				System.getProperty("user.dir") + "\\test-output\\Movies_At_San_Francisco" + timeStamp + ".xlsx");
		wb.write(fileOut);
		fis.close();
		fileOut.close();

	}
}
