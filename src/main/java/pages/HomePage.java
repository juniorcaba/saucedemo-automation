package pages;

import basetest.BaseTest;
import basetest.BaseTest.BufferAction;
import basetest.BaseTest.StepMode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 *
 * @author HECTOR CABA
 * @version 1.2
 */
public class HomePage extends BaseAuthenticatedPage {

    private final WebDriverWait wait;

    private final By productsContainer = By.className("inventory_list");
    private final By productItems = By.className("inventory_item");
    private final By shoppingCartIcon = By.className("shopping_cart_link");
    private final By burgerMenuButton = By.className("bm-burger-button");
    private final By sidebarMenu = By.className("bm-menu");
    private final By logoutButton = By.id("logout_sidebar_link");
    private final By checkoutButton = By.className("checkout_button");

    /**
     * Constructor de HomePage.
     * ¡Solo necesita el WebDriver! El login se maneja automáticamente.
     */
    public HomePage(WebDriver driver) {
        super(driver); // Llama al constructor de BaseAuthenticatedPage
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        this.pageUrl = "https://www.saucedemo.com/v1/inventory.html";
        this.validationLocator = productsContainer;
        this.pageName = "Página de Inventario";
    }

    /**
     * Obtiene la lista de todos los productos disponibles en el inventario.
     *
     * @return Lista de WebElements con los productos
     */
    public List<WebElement> getAllProducts() {
        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(productItems));
            List<WebElement> products = driver.findElements(productItems);

            BaseTest.createStep("Productos encontrados: " + products.size() + " items disponibles",
                    true, true, StepMode.BUFFER);

            return products;
        } catch (Exception e) {
            BaseTest.createStep("Error al obtener lista de productos: " + e.getMessage(),
                    false, true, StepMode.BUFFER);
            throw e;
        }
    }

    /**
     * Añade un producto específico al carrito por su nombre.
     *
     * @param productName nombre exacto del producto a añadir
     */
    public void addProductToCart(String productName) {
        try {
            // Localizar el producto específico y su botón de agregar
            By specificProductButton = By.xpath("//div[contains(@class,'inventory_item') and " +
                    ".//div[@class='inventory_item_name' and text()='" + productName + "']]" +
                    "//button[contains(text(),'ADD TO CART')]");

            WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(specificProductButton));
            addButton.click();

            BaseTest.createStep("Producto agregado al carrito: " + productName, true, true, StepMode.IMMEDIATE);

        } catch (Exception e) {
            BaseTest.processBuffer(BufferAction.COMMIT_MERGED_FAILURE,
                    "Error al agregar producto '" + productName + "' al carrito: " + e.getMessage(), true);
            throw e;
        }
    }

    /**
     * Verifica que la página de inventario esté completamente cargada.
     *
     * @return true si la página está cargada correctamente
     */
    public boolean isPageLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(productsContainer));
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(productItems));

            boolean isLoaded = driver.findElements(productItems).size() > 0;

            BaseTest.createStep("Validación de carga de página: " + (isLoaded ? "EXITOSA" : "FALLIDA"),
                    isLoaded, true, StepMode.BUFFER);

            return isLoaded;

        } catch (Exception e) {
            BaseTest.createStep("Error validando carga de página: " + e.getMessage(),
                    false, true, StepMode.BUFFER);
            return false;
        }
    }

    /**
     * Navega al carrito de compras.
     */
    public void goToCart() throws InterruptedException {
        try {
            WebElement cartIcon = wait.until(ExpectedConditions.elementToBeClickable(shoppingCartIcon));
            cartIcon.click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(checkoutButton));

            BaseTest.createStep("Navegación exitosa al carrito de compras", true, true, StepMode.IMMEDIATE);
            Thread.sleep(500);

        } catch (Exception e) {
            BaseTest.processBuffer(BufferAction.COMMIT_MERGED_FAILURE,
                    "Error al navegar al carrito: " + e.getMessage(), true);
            throw e;
        }
    }

    /**
     * Obtiene el número de items en el carrito.
     *
     * @return número de items en el carrito, 0 si está vacío
     */
    public int getCartItemCount() {
        try {
            By cartBadge = By.className("shopping_cart_badge");
            List<WebElement> badges = driver.findElements(cartBadge);

            if (!badges.isEmpty()) {
                String badgeText = badges.get(0).getText();
                int count = Integer.parseInt(badgeText);

                BaseTest.createStep("Items en carrito detectados: " + count, true, false, StepMode.IMMEDIATE);
                return count;
            } else {
                BaseTest.createStep("Carrito vacío - No hay items", true, false, StepMode.BUFFER);
                return 0;
            }

        } catch (Exception e) {
            BaseTest.createStep("Error al contar items del carrito: " + e.getMessage(),
                    false, false, StepMode.BUFFER);
            return 0;
        }
    }

    public void addMultipleProductsToCart(String... productNames) {
        try {
            for (String productName : productNames) {
                addProductToCart(productName);
            }

            BaseTest.processBuffer(BufferAction.COMMIT_SUCCESS, null, false);

            int finalCount = getCartItemCount();
            BaseTest.createStep("Adición múltiple completada - Total en carrito: " + finalCount + " items",
                    true, true, StepMode.IMMEDIATE);

        } catch (Exception e) {
            BaseTest.processBuffer(BufferAction.COMMIT_WITH_FAILURE,
                    "Error en adición múltiple de productos: " + e.getMessage(), true);
            throw e;
        }
    }

    /**
     * Verifica si el menú hamburguesa está visible en la página.
     */
    public boolean isBurgerMenuVisible() {
        try {
            List<WebElement> menuElements = driver.findElements(burgerMenuButton);
            return !menuElements.isEmpty() && menuElements.get(0).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si el menú lateral está abierto.
     */
    public boolean isSidebarMenuOpen() {
        try {
            List<WebElement> sidebarElements = driver.findElements(sidebarMenu);
            return !sidebarElements.isEmpty() && sidebarElements.get(0).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * Hace clic en el botón del menú hamburguesa y verifica que se despliegue completamente
     */
    public void openBurgerMenu() {
        try {
            // Hacer clic en el botón del menú
            WebElement menuButton = wait.until(ExpectedConditions.elementToBeClickable(By.className("bm-burger-button")));
            menuButton.click();

            // Esperar múltiples condiciones para asegurar que el menú esté completamente listo
            wait.until(ExpectedConditions.and(
                    ExpectedConditions.visibilityOfElementLocated(By.className("bm-menu")),
                    ExpectedConditions.elementToBeClickable(By.id("logout_sidebar_link"))
            ));

            BaseTest.createStep("Menú hamburguesa abierto exitosamente",
                    true, true, StepMode.IMMEDIATE);

        } catch (Exception e) {
            BaseTest.processBuffer(BufferAction.COMMIT_MERGED_FAILURE,
                    "Error al abrir el menú hamburguesa: " + e.getMessage(), true);
            throw new RuntimeException("No se encontró el botón del menú hamburguesa", e);
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
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("login-button")));

            BaseTest.createStep("Logout realizado exitosamente",
                    true, true, StepMode.IMMEDIATE);

        } catch (Exception e) {
            BaseTest.processBuffer(BufferAction.COMMIT_MERGED_FAILURE,
                    "Error durante el proceso de logout: " + e.getMessage(), true);
            throw new RuntimeException("Error en logout: " + e.getMessage(), e);
        }
    }
}