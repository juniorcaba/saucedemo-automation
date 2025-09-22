package pages;

import basetest.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static basetest.BaseTest.StepMode.*;
import static basetest.BaseTest.BufferAction.*;

public class CheckoutCompletePage extends BasePage{

    private final By shoppingCartIcon = By.className("shopping_cart_link");
    private final By burgerMenuButton = By.className("bm-burger-button");
    private final By inventoryButton = By.id("inventory_sidebar_link");
    private final By productLabel = By.className("product_label");
    private final By logoutButton = By.id("logout_sidebar_link");
    private final By sideMenu = By.className("bm-menu");
    private final By loginButton = By.id("login-button");
    private final By checkoutButton = By.className("checkout_button");

    /**
     * Inicializa la página base con WebDriver y configura WebDriverWait.
     *
     * @param driver instancia de WebDriver activa
     * @throws IllegalArgumentException si driver es null
     */
    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }


    /**
     * Hace clic en el botón del menú hamburguesa y verifica que se despliegue completamente
     */
    public void openBurgerMenu() {
        try {
            // Hacer clic en el botón del menú
            WebElement menuButton = wait.until(ExpectedConditions.elementToBeClickable(burgerMenuButton));
            menuButton.click();

            // Esperar múltiples condiciones para asegurar que el menú esté completamente listo
            wait.until(ExpectedConditions.and(
                    ExpectedConditions.visibilityOfElementLocated(sideMenu),
                    ExpectedConditions.visibilityOfElementLocated(inventoryButton),
                    ExpectedConditions.elementToBeClickable(inventoryButton),
                    ExpectedConditions.elementToBeClickable(logoutButton),
                    ExpectedConditions.visibilityOfElementLocated(logoutButton)
            ));

            BaseTest.createStep("Menú hamburguesa abierto exitosamente",
                    true, true, IMMEDIATE);

        } catch (Exception e) {
            BaseTest.processBuffer(COMMIT_MERGED_FAILURE,
                    "Error al abrir el menú hamburguesa: " + e.getMessage(), true);
            throw new IllegalStateException("No se encontró el botón del menú hamburguesa", e);
        }
    }


    public void returnToInventory() throws NoSuchElementException {
        try {
            openBurgerMenu();

            WebElement allItemButton = wait.until(ExpectedConditions.elementToBeClickable(inventoryButton));
            allItemButton.click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(shoppingCartIcon));
            wait.until(ExpectedConditions.presenceOfElementLocated(productLabel));

            BaseTest.createStep("Transición completada: ahora estás en el módulo de inventario.",
                    true, true, IMMEDIATE);

        } catch (Exception e) {
            BaseTest.processBuffer(COMMIT_MERGED_FAILURE,
                    "Error durante el proceso de logout: " + e.getMessage(), true);
            throw new NoSuchElementException("Error en logout: " + e.getMessage(), e);
        }
    }

    /**
     * Realiza logout usando variable de clase, desde el menu Burger
     */
    public void performLogout() {
        try {
            // 1. Abrir menú hamburguesa (incluye todas las esperas necesarias)
            openBurgerMenu();

            // 2. Hacer clic en logout usando la variable de clase
            WebElement logoutBtn = wait.until(ExpectedConditions.elementToBeClickable(logoutButton));
            logoutBtn.click();

            // 3. Verificar redirección exitosa a página de login
            wait.until(ExpectedConditions.presenceOfElementLocated(loginButton));

            BaseTest.createStep("Logout realizado exitosamente",
                    true, true, IMMEDIATE);

        } catch (Exception e) {
            BaseTest.processBuffer(COMMIT_MERGED_FAILURE,
                    "Error durante el proceso de logout: " + e.getMessage(), true);
            throw new IllegalStateException("Error en logout: " + e.getMessage(), e);
        }
    }

    /**
     * Navega al carrito de compras.
     */
    public void goBackToCart() throws InterruptedException {
        try {
            WebElement cartIcon = wait.until(ExpectedConditions.elementToBeClickable(shoppingCartIcon));
            cartIcon.click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(checkoutButton));

            BaseTest.createStep("Navegación exitosa al carrito de compras", true, true, IMMEDIATE);
            Thread.sleep(500);

        } catch (Exception e) {
            BaseTest.processBuffer(COMMIT_MERGED_FAILURE,
                    "Error al navegar al carrito: " + e.getMessage(), true);
            throw e;
        }
    }

    public void shoppingCartVisible(){
        if (!driver.findElements(shoppingCartIcon).isEmpty()) {
            WebElement button = driver.findElement(shoppingCartIcon);
            button.click();
        }

    }
}
