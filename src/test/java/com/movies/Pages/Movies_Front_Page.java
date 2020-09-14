package com.movies.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Movies_Front_Page {

	WebDriver driver;

	public Movies_Front_Page(WebDriver driver) {
		this.driver = driver;
	}

	private By viewData = By.xpath("//a[@class='btn btn-primary btn-sm btn-grid view-data']");
	private By colDetails = By.xpath("//span[@class='column-header-content-column-name']");
	private int colCount = 11;
	private By nextBtn = By.xpath("//button[@class='pager-button-next']");
	
	public WebElement getviewData() {
		return driver.findElement(viewData);
	}
	
	public By getcolDetails() {
		return colDetails;
	}
	
	public int getcolCount() {
		return colCount;
	}
	
	public WebElement getnextBtn() {
		return driver.findElement(nextBtn);
	}

}
