package pages;

import basetest.BaseTest;
import basetest.BaseTest.StepMode;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Clase base simplificada para Page Object sin manejo de popups.
 * Enfoque limpio sin dependencias de PopupHandler.
 *
 * @author HECTOR CABA
 * @version 4.0 - Limpia sin PopupHandler
 */
public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    private static final Logger logger = LoggerFactory.getLogger(BasePage.class);

    /** URL de la página - debe ser definida por cada página hija */
    protected String pageUrl;
    /** Localizador para validar que la página cargó correctamente */
    protected By validationLocator;
    /** Nombre descriptivo de la página para reportes */
    protected String pageName;

    /**
     * Inicializa la página base con WebDriver y configura WebDriverWait.
     *
     * @param driver instancia de WebDriver activa
     * @throws IllegalArgumentException si driver es null
     */
    public BasePage(WebDriver driver){
        if (driver == null){
            throw new IllegalArgumentException("webdriver no puede ser null. ");
        }
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Pausa la ejecución por el tiempo especificado.
     *
     * @param seconds tiempo de pausa en segundos
     */
    public static void pause(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Navega a la página de forma simple y directa.
     *
     * @throws InterruptedException si el hilo es interrumpido durante la pausa
     * @throws RuntimeException si no se definieron las propiedades requeridas
     */
    public void goTo() throws InterruptedException {
        try {
            // Validar que la página haya definido los valores necesarios
            if (pageUrl == null || validationLocator == null || pageName == null) {
                throw new RuntimeException("La página debe definir pageUrl, validationLocator y pageName");
            }

            //BaseTest.createStep("Navegando a " + pageName, true, false, StepMode.IMMEDIATE);

            // Navegar a la página
            driver.get(pageUrl);

            // Validar que la página cargó
            wait.until(ExpectedConditions.presenceOfElementLocated(validationLocator));

            //BaseTest.createStep("Página " + pageName + " cargada correctamente", true, true, StepMode.IMMEDIATE);

        } catch (Exception e) {
            BaseTest.createStep("Error al navegar a " + pageName + ": " + e.getMessage(), false, true, StepMode.IMMEDIATE);
            throw e;
        }
    }

    /**
     * Click básico con espera y reporte.
     */
    public void click(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            element.click();
        } catch (Exception e) {
            throw new RuntimeException("Click falló: " + e.getMessage(), e);
        }
    }

//    /**
//     * Click con reporte detallado.
//     */
//    public void click(By locator, String description, StepMode mode) {
//        try {
//            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
//            element.click();
//            BaseTest.createStep(description, true, true, mode);
//        } catch (Exception e) {
//            BaseTest.createStep("Error: " + description + " - " + e.getMessage(), false, true, mode);
//            throw new RuntimeException("Click falló: " + e.getMessage(), e);
//        }
//    }

    /**
     * Envía texto a un campo con limpieza previa.
     */
    public void sendKeys(By locator, String text, String description, StepMode mode) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            element.clear();
            element.sendKeys(text);
            BaseTest.createStep(description, true, true, mode);
        } catch (Exception e) {
            BaseTest.createStep("Error: " + description + " - " + e.getMessage(), false, true, mode);
            throw new RuntimeException("SendKeys falló: " + e.getMessage(), e);
        }
    }

    /**
     * Valida una condición con reporte.
     */
    public void validate(boolean condition, String successMessage, String failureMessage, StepMode mode) {
        if (condition) {
            BaseTest.createStep(successMessage, true, true, mode);
        } else {
            BaseTest.createStep(failureMessage, false, true, mode);
            throw new AssertionError(failureMessage);
        }
    }

    /**
     * Ejecuta una acción con reporte de resultado.
     */
    protected void executeAction(Runnable action, String actionDescription) {
        try {
            action.run();
        } catch (Exception e) {
            BaseTest.createStep("Error en " + actionDescription + ": " + e.getMessage(), false, true, StepMode.IMMEDIATE);
            throw e;
        }
    }

    public void waitVisibility(By elementLocator){
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(elementLocator));
    }

    public void clickElement(By elementLocator){
        waitVisibility(elementLocator);
        driver.findElement(elementLocator).click();
    }

    public void clickOn(By element){
        try{
            wait.until(ExpectedConditions.elementToBeClickable(element));
            driver.findElement(element).click();
        }catch (NoSuchElementException e){
            logger.error("Error Class BasePage in method clickOn", e);
        }


    }
}