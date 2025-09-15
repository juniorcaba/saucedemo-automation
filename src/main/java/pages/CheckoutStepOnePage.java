package pages;

import basetest.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutStepOnePage extends BaseAuthenticatedPage{

    private final By inputFirstName = By.xpath("//input[@id='first-name']");
    private final By inputLastName = By.xpath("//input[@id='last-name']");
    private final By inputPortalCode = By.xpath("//input[@id='postal-code']");
    private final By continueButton = By.xpath("//input[@value='CONTINUE']");

    /**
     * Constructor que inicializa la página con login automático incluido.
     *
     * @param driver instancia de WebDriver activa
     * @throws IllegalArgumentException si driver es null
     */
    public CheckoutStepOnePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Complete checkout form.
     *
     * @param firstName  the first name
     * @param lastName   the last name
     * @param postalCode the postal code
     */
    public void completeCheckoutForm(String firstName, String lastName, String postalCode){
        // Completar campo First Name
        WebElement firstNameElement = wait.until(ExpectedConditions.elementToBeClickable(inputFirstName));
        firstNameElement.clear();
        firstNameElement.sendKeys(firstName);
        BaseTest.createStep("Completando el campo First Name", true, true, BaseTest.StepMode.IMMEDIATE);

        // Completar campo Last Name
        WebElement lastNameElement = wait.until(ExpectedConditions.elementToBeClickable(inputLastName));
        lastNameElement.clear();
        lastNameElement.sendKeys(lastName);
        BaseTest.createStep("Completando el campo Last Name", true, true, BaseTest.StepMode.IMMEDIATE);

        // Completar campo Postal Code
        WebElement postalCodeElement = wait.until(ExpectedConditions.elementToBeClickable(inputPortalCode));
        postalCodeElement.clear();
        postalCodeElement.sendKeys(postalCode);
        BaseTest.createStep("Completando el campo Postal Code", true, true, BaseTest.StepMode.IMMEDIATE);


    }

    /**
     * Go to checkout overview.
     */
    public void goToCheckoutOverview(){
        try{
            WebElement buttonContinue = wait.until(ExpectedConditions.elementToBeClickable(continueButton));
            buttonContinue.click();
            BaseTest.createStep("Navegando a la pagina de Checkout: Overview", true, true, BaseTest.StepMode.IMMEDIATE);
        }catch (Exception e){
            BaseTest.processBuffer(BaseTest.BufferAction.COMMIT_MERGED_FAILURE,"Error al ingresar a la sesion de Checkout: Overview" + e.getMessage(), true);
        }

    }

}
