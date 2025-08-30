package pages;

import basetest.BaseTest;
import basetest.BaseTest.StepMode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Clase base para implementar el patrón Page Object con sistema integrado de reportes.
 * Las páginas heredadas deben definir pageUrl, validationLocator y pageName.
 *
 * @author HECTOR CABA
 * @version 2.0
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
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void click(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            element.click();
        } catch (Exception e) {
            System.out.println("Fallo al hacer clic en: " + locator);
            throw e;
        }
    }

    /**
     * Pausa la ejecución por el tiempo especificado.
     * Maneja InterruptedException adecuadamente.
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
     * Navega a la página usando las propiedades definidas por la página hija.
     * Valida que la página cargó correctamente mediante validationLocator.
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

            driver.get(pageUrl);
            wait.until(ExpectedConditions.presenceOfElementLocated(validationLocator));

            // Pequeña pausa para asegurar que la página esté completamente cargada
            Thread.sleep(500);

            BaseTest.createStep("Navegando a " + pageName, true, true, StepMode.IMMEDIATE);

        } catch (Exception e) {
            BaseTest.createStep("Error al navegar a " + pageName + ": " + e.getMessage(), false, true, StepMode.IMMEDIATE);
            throw e;
        }
    }

    /**
     * Ejecuta click en elemento con reporte automático de resultado.
     * Espera que el elemento sea clickeable antes de interactuar.
     *
     * @param locator selector del elemento objetivo
     * @param description descripción de la acción para el reporte
     * @param mode modo de procesamiento del step (BUFFER, IMMEDIATE, STATIC)
     */
    public void clickWithReport(By locator, String description, StepMode mode) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            element.click();
            BaseTest.createStep("Click exitoso: " + description, true, true, mode);
        } catch (Exception e) {
            BaseTest.createStep("Error al hacer click: " + description + " - " + e.getMessage(), false, true, mode);
            throw e;
        }
    }

    /**
     * Ingresa texto en campo con limpieza previa y reporte automático.
     * Espera que el elemento sea visible antes de escribir.
     *
     * @param locator selector del campo de texto
     * @param text contenido a ingresar
     * @param description descripción de la acción para el reporte
     * @param mode modo de procesamiento del step
     */
    public void sendKeysWithReport(By locator, String text, String description, StepMode mode) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            element.clear();
            element.sendKeys(text);
            BaseTest.createStep("Texto ingresado: " + description, true, true, mode);
        } catch (Exception e) {
            BaseTest.createStep("Error al ingresar texto: " + description + " - " + e.getMessage(), false, true, mode);
            throw e;
        }
    }

    /**
     * Ejecuta validación con reporte automático del resultado.
     * Lanza AssertionError si la condición falla.
     *
     * @param condition expresión booleana a evaluar
     * @param successMessage mensaje para mostrar si la validación pasa
     * @param failureMessage mensaje para mostrar si la validación falla
     * @param mode modo de procesamiento del step
     * @throws AssertionError cuando la condición es false
     */
    public void validateWithReport(boolean condition, String successMessage, String failureMessage, StepMode mode) {
        if (condition) {
            BaseTest.createStep("Validación exitosa: " + successMessage, true, true, mode);
        } else {
            BaseTest.createStep("Validación fallida: " + failureMessage, false, true, mode);
            throw new AssertionError(failureMessage);
        }
    }
}