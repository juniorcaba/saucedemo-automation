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
import java.util.List;

/**
 *
 * @author HECTOR CABA
 * @version 1.0
 */
public class HomePage extends BaseAuthenticatedPage {

    private final WebDriverWait wait;

    private final By productsContainer = By.className("inventory_list");
    private final By productItems = By.className("inventory_item");
    private final By shoppingCartIcon = By.className("shopping_cart_link");
    private final By menuButton = By.id("react-burger-menu-btn");
    private final By inventoryItemName = By.className("inventory_item_name");
    private final By inventoryItemPrice = By.className("inventory_item_price");
    private final By addToCartButtons = By.xpath("//button[contains(text(),'ADD TO CART')]");
    private final By sortDropdown = By.className("product_sort_container");

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

            BaseTest.createStep("Producto agregado al carrito: " + productName, true, true, StepMode.BUFFER);

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
    public void goToCart() {
        try {
            WebElement cartIcon = wait.until(ExpectedConditions.elementToBeClickable(shoppingCartIcon));
            cartIcon.click();

            BaseTest.createStep("Navegación exitosa al carrito de compras", true, true, StepMode.BUFFER);

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

//            if (driver.findElements(cartBadge).size() > 0) {
//                String badgeText = driver.findElement(cartBadge).getText();
//                int count = Integer.parseInt(badgeText);
            List<WebElement> badges = driver.findElements(cartBadge);

                if (!badges.isEmpty()) {
                    String badgeText = badges.get(0).getText();
                    int count = Integer.parseInt(badgeText);


                    BaseTest.createStep("Items en carrito detectados: " + count, true, false, StepMode.BUFFER);
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
                addProductToCart(productName); // Cada producto se añade al buffer
            }

            // Procesar todos los steps del buffer como exitosos
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
}