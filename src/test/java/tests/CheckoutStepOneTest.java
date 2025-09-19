package tests;

import basetest.BaseTest;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutStepOnePage;
import pages.HomePage;

public class CheckoutStepOneTest extends BaseTest {

    @Test(description = "Completar formulario Checkout")
    public void testCompleteCheckoutForm() throws InterruptedException {
        HomePage homePage = new HomePage(getDriver());
        homePage.goTo();

        homePage.addProductToCart("Sauce Labs Backpack");
        homePage.goToCart();
        CartPage cartpage = new CartPage(getDriver());
        cartpage.goToCheckoutForm();
        CheckoutStepOnePage checkoutstepone = new CheckoutStepOnePage(getDriver());
        checkoutstepone.completeCheckoutForm("Juan", "De los Santos","40000");
        checkoutstepone.goToCheckoutOverview();

    }

    @Test(description = "Validar funcionalidad boton Cancel")
    public void testCancelButton() throws InterruptedException {
        HomePage homePage = new HomePage(getDriver());
        homePage.goTo();

        homePage.addProductToCart("Sauce Labs Backpack");
        homePage.goToCart();
        CartPage cartpage = new CartPage(getDriver());
        cartpage.goToCheckoutForm();

        CheckoutStepOnePage checkoutstepone = new CheckoutStepOnePage(getDriver());
        checkoutstepone.clickCancel();
    }
}
