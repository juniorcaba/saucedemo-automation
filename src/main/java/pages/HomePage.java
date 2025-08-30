package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage{

    private final By appButton = By.id("app_logo");

    public HomePage(WebDriver driver){
        super(driver);
    }

    public void homePageValidation(){

    }

}
