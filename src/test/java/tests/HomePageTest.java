package tests;

import basetest.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.BasePage;
import pages.HomePage;

public class HomePageTest extends BaseTest {

    //@Test(description = "Navegacion y login automatico")
    public void testBasicHomePage() throws InterruptedException {

        HomePage homePage = new HomePage(getDriver());
        homePage.goTo(); // ← ¡Login automático incluido!

        // Ya estás en la página de inventario, listo para hacer tus validaciones
        assert homePage.isPageLoaded();
    }

    //@Test(description = "Agregar productos al carro de compras")
    public void testAddProductsToCart() throws InterruptedException {
        HomePage homePage = new HomePage(getDriver());
        homePage.goTo(); // ← Login automático

        // Ahora puedes hacer cualquier acción en la página autenticada
        homePage.addMultipleProductsToCart("Sauce Labs Backpack", "Sauce Labs Bike Light", "Sauce Labs Bolt T-Shirt");

        int cartCount = homePage.getCartItemCount();

        // Validación directa sin createStep
        assert cartCount == 3 : "Se esperaban 3 productos en el carrito, pero se encontraron " + cartCount;
    }

    //@Test(description = "Consultar carro de compras con productos agregados")
    public void testConsultProductsToCart() throws InterruptedException {
        HomePage homePage = new HomePage(getDriver());
        homePage.goTo();

        homePage.addProductToCart("Sauce Labs Backpack");
        homePage.goToCart();
    }

    //@Test(description = "Abrir menú hamburguesa y validar visibilidad")
    public void testOpenBurgerMenu() throws InterruptedException {
        HomePage homePage = new HomePage(getDriver());
        homePage.goTo();

        homePage.openBurgerMenu();

    }

    @Test(description = "Logout desde la pantalla de HomePage")
    public void testLogout() throws InterruptedException {
        HomePage homePage = new HomePage(getDriver());
        homePage.goTo();

        homePage.performLogout();
    }









}

