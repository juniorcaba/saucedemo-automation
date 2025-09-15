package tests;

import basetest.BaseTest;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;

public class CartPageTest extends BaseTest {

    //@Test(description = "Navegacion y login automatico")
    public void testBasicHomePage() throws InterruptedException {

        HomePage homePage = new HomePage(getDriver());
        homePage.goTo(); // ← ¡Login automático incluido!
        homePage.addProductToCart("Sauce Labs Backpack");
        homePage.goToCart();
    }

    //@Test(description = "Consultar Checkout con productos agregados")
    public void testConsultCheckoutWithProduct() throws InterruptedException {
        HomePage homePage = new HomePage(getDriver());
        homePage.goTo();

        homePage.addProductToCart("Sauce Labs Backpack");
        homePage.goToCart();

        CartPage cartpage = new CartPage(getDriver());
        cartpage.goToCheckoutForm();
    }

    @Test(description = "Regresar a la pagina de inventario desde el cart")
    public void testReturnInventory() throws InterruptedException {
        HomePage homePage = new HomePage(getDriver());
        homePage.goTo();

        homePage.addProductToCart("Sauce Labs Backpack");
        homePage.goToCart();

        CartPage cartpage = new CartPage(getDriver());
        cartpage.continueShoppingButton();
    }



}
