//package pages;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import basetest.BaseTest;
//import basetest.BaseTest.StepMode;
//import basetest.BaseTest.BufferAction;
//
//import java.time.Duration;
//
//public class PopupHandler extends BasePage {
//
//    private final WebDriverWait wait;
//
//    // Localizadores comunes para popups de cambio de contraseña
//    private final By popupContainer = By.cssSelector("[role='dialog'], .modal, .popup");
//    private final By popupTitle = By.xpath("//h1[contains(text(),'Cambia la contraseña')] | //div[contains(text(),'Cambia la contraseña')]");
//    private final By acceptButton = By.xpath("//button[contains(text(),'Aceptar')] | //button[contains(text(),'OK')] | //button[contains(text(),'Continuar')]");
//    private final By dismissButton = By.xpath("//button[contains(text(),'Cancelar')] | //button[contains(text(),'Ahora no')] | //button[contains(text(),'Omitir')]");
//    private final By closeButton = By.xpath("//button[contains(@class,'close')] | //*[@data-dismiss='modal'] | //span[text()='×']");
//
//    // Localizadores alternativos por atributos comunes
//    private final By popupByRole = By.cssSelector("[role='alertdialog'], [role='dialog']");
//    private final By popupByClass = By.cssSelector(".modal, .popup, .dialog, .overlay");
//
//    public PopupHandler(WebDriver driver) {
//        super(driver);
//        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//    }
//
//    /**
//     * Verifica si hay algún popup visible en la página
//     */
//    public boolean isPopupPresent() {
//        try {
//            // Verificar múltiples posibles localizadores
//            return isElementPresent(popupContainer) ||
//                    isElementPresent(popupTitle) ||
//                    isElementPresent(popupByRole) ||
//                    isElementPresent(popupByClass);
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    /**
//     * Verifica si un elemento está presente sin lanzar excepción
//     */
//    private boolean isElementPresent(By locator) {
//        try {
//            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
//            return element.isDisplayed();
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    /**
//     * Cierra el popup haciendo click en "Aceptar"
//     */
//    public void acceptPopup() throws InterruptedException {
//        try {
//            if (isPopupPresent()) {
//                WebElement acceptBtn = wait.until(ExpectedConditions.elementToBeClickable(acceptButton));
//                acceptBtn.click();
//
//                // Esperar a que el popup desaparezca
//                wait.until(ExpectedConditions.invisibilityOfElementLocated(popupContainer));
//
//                BaseTest.createStep("Popup aceptado correctamente", true, true, StepMode.IMMEDIATE);
//                Thread.sleep(500);
//            }
//        } catch (Exception e) {
//            BaseTest.createStep("Error al aceptar popup: " + e.getMessage(), false, true, StepMode.IMMEDIATE);
//            throw e;
//        }
//    }
//
//    /**
//     * Cierra el popup haciendo click en "Cancelar" o "Ahora no"
//     */
//    public void dismissPopup() throws InterruptedException {
//        try {
//            if (isPopupPresent()) {
//                WebElement dismissBtn = wait.until(ExpectedConditions.elementToBeClickable(dismissButton));
//                dismissBtn.click();
//
//                wait.until(ExpectedConditions.invisibilityOfElementLocated(popupContainer));
//
//                BaseTest.createStep("Popup descartado correctamente", true, true, StepMode.IMMEDIATE);
//                Thread.sleep(500);
//            }
//        } catch (Exception e) {
//            BaseTest.createStep("Error al descartar popup: " + e.getMessage(), false, true, StepMode.IMMEDIATE);
//            throw e;
//        }
//    }
//
//    /**
//     * Cierra el popup usando el botón X o close
//     */
//    public void closePopup() throws InterruptedException {
//        try {
//            if (isPopupPresent()) {
//                WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(closeButton));
//                closeBtn.click();
//
//                wait.until(ExpectedConditions.invisibilityOfElementLocated(popupContainer));
//
//                BaseTest.createStep("Popup cerrado correctamente", true, true, StepMode.IMMEDIATE);
//                Thread.sleep(500);
//            }
//        } catch (Exception e) {
//            BaseTest.createStep("Error al cerrar popup: " + e.getMessage(), false, true, StepMode.IMMEDIATE);
//            throw e;
//        }
//    }
//
//    /**
//     * Maneja automáticamente cualquier popup que aparezca
//     * Intenta diferentes métodos para cerrarlo
//     */
//    public void handleAnyPopup() throws InterruptedException {
//        if (isPopupPresent()) {
//            BaseTest.createStep("Popup detectado - Intentando manejar", true, true, StepMode.IMMEDIATE);
//
//            try {
//                // Intentar aceptar primero
//                acceptPopup();
//            } catch (Exception e1) {
//                try {
//                    // Si no funciona aceptar, intentar cerrar
//                    closePopup();
//                } catch (Exception e2) {
//                    try {
//                        // Como último recurso, intentar descartar
//                        dismissPopup();
//                    } catch (Exception e3) {
//                        BaseTest.createStep("No se pudo manejar el popup automáticamente", false, true, StepMode.IMMEDIATE);
//                        throw new RuntimeException("No se pudo cerrar el popup: " + e3.getMessage());
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * Espera a que aparezca un popup y lo maneja automáticamente
//     */
//    public void waitAndHandlePopup(int timeoutSeconds) throws InterruptedException {
//        try {
//            // Esperar hasta que aparezca el popup o se agote el tiempo
//            WebDriverWait popupWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
//            popupWait.until(ExpectedConditions.or(
//                    ExpectedConditions.visibilityOfElementLocated(popupContainer),
//                    ExpectedConditions.visibilityOfElementLocated(popupTitle),
//                    ExpectedConditions.visibilityOfElementLocated(popupByRole)
//            ));
//
//            handleAnyPopup();
//
//        } catch (Exception e) {
//            BaseTest.createStep("No apareció ningún popup en " + timeoutSeconds + " segundos", true, true, StepMode.IMMEDIATE);
//            // No lanzamos excepción aquí porque no aparecer popup puede ser normal
//        }
//    }
//}

