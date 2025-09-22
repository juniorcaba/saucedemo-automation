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
        //assert homePage.isPageLoaded();
        Assert.assertTrue(homePage.isPageLoaded(), "La página de inventario no cargó correctamente");
    }

    //@Test(description = "Agregar productos al carro de compras")
    public void testAddProductsToCart() throws InterruptedException {
        HomePage homePage = new HomePage(getDriver());
        homePage.goTo(); // ← Login automático

        // Ahora puedes hacer cualquier acción en la página autenticada
        homePage.addMultipleProductsToCart("Sauce Labs Backpack", "Sauce Labs Bike Light", "Sauce Labs Bolt T-Shirt");

        // Validar cantidad en carrito
        int cartCount = homePage.getCartItemCount();
        Assert.assertEquals(cartCount, 3, "Cantidad incorrecta de productos en el carrito");
    }

    //@Test(description = "Consultar carro de compras con productos agregados")
    public void testConsultProductsToCart() throws InterruptedException {
        HomePage homePage = new HomePage(getDriver());
        homePage.goTo();

        homePage.addProductToCart("Sauce Labs Backpack");
        homePage.goToCart();

        //Assert.assertTrue(cartPage.isProductInCart("Sauce Labs Backpack"));
    }

    //@Test(description = "Abrir menú hamburguesa y validar visibilidad")
    public void testOpenBurgerMenu() throws InterruptedException {
        HomePage homePage = new HomePage(getDriver());
        homePage.goTo();

        homePage.openBurgerMenu();

        Assert.assertTrue(homePage.isSidebarMenuOpen(), "El menú hamburguesa no se abrió correctamente");

    }

    @Test(description = "Logout desde la pantalla de HomePage")
    public void testLogout() throws InterruptedException {
        HomePage homePage = new HomePage(getDriver());
        homePage.goTo();

        homePage.performLogout();
    }

}

