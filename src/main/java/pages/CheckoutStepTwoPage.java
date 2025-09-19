package pages;

import basetest.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutStepTwoPage extends BasePage {

    private final By cancelButton = By.xpath("//*[contains(text(),'CANCEL')]");
    private final By buttonFinish = By.xpath("//*[contains(text(),'FINISH')]");
    private final By titleCheckoutOverview = By.xpath("//*[contains(text(),'Checkout: Overview')]");
    private final By titleFinish = By.xpath("//*[contains(text(),'Finish')]");
    private final By ponyExpressImg = By.xpath("//img[@class='pony_express']");



    /**
     * Constructor que inicializa la página con login automático incluido.
     *
     * @param driver instancia de WebDriver activa
     * @throws IllegalArgumentException si driver es null
     */
    public CheckoutStepTwoPage(WebDriver driver) {
        super(driver);
    }

    public void clickBtnCancel() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOfElementLocated(buttonFinish));
        try {
            // Esperar a que el botón Cancel sea clickeable
            WebElement cancelBtn = wait.until(ExpectedConditions.elementToBeClickable(cancelButton));
            cancelBtn.click();

            // Validar redirección exitosa al carrito
            wait.until(ExpectedConditions.urlContains("inventory.html"));

            BaseTest.createStep("Botón Cancel clickeado - Regreso exitoso al listado de productos",
                    true, true, BaseTest.StepMode.IMMEDIATE);

            Thread.sleep(500); // Pausa para estabilidad


        } catch (Exception e) {
            BaseTest.processBuffer(BaseTest.BufferAction.COMMIT_MERGED_FAILURE,
                    "Error al hacer clic en el botón Cancel: " + e.getMessage(), true);
            throw new RuntimeException("No se pudo hacer clic en Cancel", e);
        }
    }

    public void completeCheckoutOverview() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(titleCheckoutOverview));
            WebElement finishBtn = wait.until(ExpectedConditions.elementToBeClickable(buttonFinish));
            finishBtn.click();

            BaseTest.createStep("Botón Finish clickeado exitosamente en Checkout Overview.",
                    true, true, BaseTest.StepMode.IMMEDIATE);

            wait.until(ExpectedConditions.visibilityOfElementLocated(ponyExpressImg));
            wait.until(ExpectedConditions.visibilityOfElementLocated(titleFinish));

            Thread.sleep(500); // Pausa para estabilidad

        } catch (Exception e) {
            BaseTest.processBuffer(BaseTest.BufferAction.COMMIT_MERGED_FAILURE, "Error al completar Checkout Overview: " + e.getMessage(), true);
            throw new RuntimeException("No se pudo hacer clic en Finish", e);
        }
    }

}
