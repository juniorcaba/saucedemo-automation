package tests;

import basetest.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;

public class CheckoutCompleteTest extends BaseTest {


    @Test(description = "Checkout completado - Accediendo a inventario.")
    public void testReturnToInventory() throws InterruptedException {
        HomePage homePage = new HomePage(getDriver());
        homePage.goTo();

        homePage.addProductToCart("Sauce Labs Backpack");
        homePage.goToCart();
        CartPage cartpage = new CartPage(getDriver());
        cartpage.goToCheckoutForm();

        CheckoutStepOnePage checkoutStepOnePage = new CheckoutStepOnePage(getDriver());
        checkoutStepOnePage.completeCheckoutForm("Adam", "Martis", "40000");
        checkoutStepOnePage.goToCheckoutOverview();

        CheckoutStepTwoPage checkoutStepTwoPage = new CheckoutStepTwoPage(getDriver());
        checkoutStepTwoPage.completeCheckoutOverview();

        CheckoutCompletePage checkoutCompletePage = new CheckoutCompletePage(getDriver());
        checkoutCompletePage.returnToInventory();

    }

    @Test(description = "Checkout completado - Cerrando sesion.")
    public void testLogoutFromCheckout() throws InterruptedException {
        HomePage homePage = new HomePage(getDriver());
        homePage.goTo();

        homePage.addProductToCart("Sauce Labs Backpack");
        homePage.goToCart();
        CartPage cartpage = new CartPage(getDriver());
        cartpage.goToCheckoutForm();

        CheckoutStepOnePage checkoutStepOnePage = new CheckoutStepOnePage(getDriver());
        checkoutStepOnePage.completeCheckoutForm("Adam", "Martis", "40000");
        checkoutStepOnePage.goToCheckoutOverview();

        CheckoutStepTwoPage checkoutStepTwoPage = new CheckoutStepTwoPage(getDriver());
        checkoutStepTwoPage.completeCheckoutOverview();

        CheckoutCompletePage checkoutCompletePage = new CheckoutCompletePage(getDriver());
        checkoutCompletePage.performLogout();

    }

    @Test(description = "Checkout completado - Accediendo a Carro de compras.")
    public void testMoveToCart() throws InterruptedException {
        HomePage homePage = new HomePage(getDriver());
        homePage.goTo();

        homePage.addProductToCart("Sauce Labs Backpack");
        homePage.goToCart();
        CartPage cartpage = new CartPage(getDriver());
        cartpage.goToCheckoutForm();

        CheckoutStepOnePage checkoutStepOnePage = new CheckoutStepOnePage(getDriver());
        checkoutStepOnePage.completeCheckoutForm("Adam", "Martis", "40000");
        checkoutStepOnePage.goToCheckoutOverview();

        CheckoutStepTwoPage checkoutStepTwoPage = new CheckoutStepTwoPage(getDriver());
        checkoutStepTwoPage.completeCheckoutOverview();

        CheckoutCompletePage checkoutCompletePage = new CheckoutCompletePage(getDriver());
        checkoutCompletePage.goBackToCart();

    }
}
