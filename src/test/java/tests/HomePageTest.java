package tests;

import basetest.BaseTest;
import org.testng.annotations.Test;
import pages.BasePage;
import pages.HomePage;

public class HomePageTest extends BaseTest {

    @Test(description = "Navegacion y login automatico")
    public void testBasicHomePage() throws InterruptedException {
        // ¡Eso es todo! Una línea para crear la página y otra para ir allí con login automático
        HomePage homePage = new HomePage(getDriver());
        homePage.goTo(); // ← ¡Login automático incluido!

        // Ya estás en la página de inventario, listo para hacer tus validaciones
        assert homePage.isPageLoaded();
    }

    @Test(description = "Agregar productos al carro de compras")
    public void testAddProductsToCart() throws InterruptedException {
        HomePage homePage = new HomePage(getDriver());
        homePage.goTo(); // ← Login automático

        // Ahora puedes hacer cualquier acción en la página autenticada
        homePage.addMultipleProductsToCart("Sauce Labs Backpack", "Sauce Labs Bike Light", "Sauce Labs Bolt T-Shirt");

        int cartCount = homePage.getCartItemCount();

        // Validación directa sin createStep
        assert cartCount == 3 : "Se esperaban 3 productos en el carrito, pero se encontraron " + cartCount;
    }



}