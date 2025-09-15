package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import basetest.BaseTest;
import basetest.BaseTest.StepMode;
import basetest.BaseTest.BufferAction;

import java.time.Duration;

public class LoginPage extends BasePage {

    private final WebDriverWait wait;

    // Localizadores
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.xpath("//input[@id='password']");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.xpath("//h3[@data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        this.pageUrl = "https://www.saucedemo.com/v1/";
        this.validationLocator = usernameField;
        this.pageName = "Sauce";
    }


    public void enterUsername(String username) throws InterruptedException {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
            driver.findElement(usernameField).clear();
            driver.findElement(usernameField).sendKeys(username);

            Thread.sleep(300);

            BaseTest.createStep("Usuario ingresado: " + username, true, true, StepMode.BUFFER);
        } catch (Exception e) {
            BaseTest.processBuffer(BufferAction.COMMIT_MERGED_FAILURE,
                    "Error al ingresar usuario: " + e.getMessage(), true);
            throw e;
        }
    }

    public void enterPassword(String password) throws InterruptedException {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
            driver.findElement(passwordField).clear();
            driver.findElement(passwordField).sendKeys(password);

            Thread.sleep(300);

            BaseTest.createStep("Contraseña ingresada", true, true, StepMode.BUFFER);
        } catch (Exception e) {
            BaseTest.processBuffer(BufferAction.COMMIT_MERGED_FAILURE,
                    "Error al ingresar contraseña: " + e.getMessage(), true);
            throw e;
        }
    }

    public void clickLoginButton() throws InterruptedException {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(loginButton));

            BaseTest.createStep("Click en botón de login", true, true, StepMode.BUFFER);

            driver.findElement(loginButton).click();
            Thread.sleep(500);

        } catch (Exception e) {
            BaseTest.processBuffer(BufferAction.COMMIT_MERGED_FAILURE,
                    "Error al hacer click en login: " + e.getMessage(), true);
            throw e;
        }
    }

    public boolean isErrorMessageDisplayed() {
        try {
            WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            return error.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        try {
            if (isErrorMessageDisplayed()) {
                return driver.findElement(errorMessage).getText();
            }
            return "No se encontró mensaje de error";
        } catch (Exception e) {
            return "Error al obtener mensaje: " + e.getMessage();
        }
    }

    public boolean isLoginSuccessful() {
        try {
            return getCurrentUrl().contains("inventory.html");
        } catch (Exception e) {
            return false;
        }
    }

    public void completeLogin(String username, String password) throws InterruptedException {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();

        Thread.sleep(1000);

        if (isErrorMessageDisplayed()) {
            String errorMsg = getErrorMessage();
            BaseTest.processBuffer(BufferAction.COMMIT_WITH_FAILURE, "Login falló: " + errorMsg, true);
        } else {
            if (isLoginSuccessful()) {
                BaseTest.processBuffer(BufferAction.COMMIT_SUCCESS, null, false);
                BaseTest.createStep("Login exitoso - Redirigido a inventario", true, true, StepMode.IMMEDIATE);
            } else {
                BaseTest.processBuffer(BufferAction.COMMIT_SUCCESS, null, false);
                BaseTest.createStep("Login procesado - Verificando resultado...", true, true, StepMode.IMMEDIATE);
            }
        }
    }

    /**
     * Obtiene la URL actual del navegador para validar el estado de navegación.
     * Este metodo es utilizado por isLoginSuccessful() para verificar si el login redirige correctamente.
     *
     * @return Cadena con la URL actual del navegador
     */

    private String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}



