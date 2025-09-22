package tests;

import basetest.BaseTest;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutStepOnePage;
import pages.CheckoutStepTwoPage;
import pages.HomePage;

public class CheckoutStepTwoPageTest extends BaseTest {

    //@Test(description = "Validar funcionalidad boton Cancel page CheckoutOverview")
    public void testCancelButton() throws InterruptedException {
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
        checkoutStepTwoPage.clickBtnCancel();
    }

    @Test(description = "Completar el proceso de checkout")
    public void testCompleteCheckoutOverviewProcess() throws InterruptedException {
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
    }
}
