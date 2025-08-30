package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

public class HomePage extends BasePage{

    private final By appButton = By.id("app_logo");

    public HomePage(WebDriver driver){
        super(driver);
    }

    public void homePageValidation(){

    }

}
