package pages;

import basetest.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * The type Cart page.
 */
public class CartPage extends BaseAuthenticatedPage{

    private final By shoppingCartIcon = By.className("shopping_cart_link");
    private final By checkoutButton = By.className("checkout_button");
    private final By cancelButton = By.className("cart_cancel_link");
    private final By continueShopping = By.xpath("//a[text()='Continue Shopping']");
    private final By burgerMenuButton = By.className("bm-burger-button");



    /**
     * Constructor que inicializa la página con login automático incluido.
     *
     * @param driver instancia de WebDriver activa
     * @throws IllegalArgumentException si driver es null
     */
    public CartPage(WebDriver driver) {
        super(driver); // Llama al constructor de BaseAuthenticatedPage
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        this.pageUrl = "https://www.saucedemo.com/v1/cart.html";
        this.validationLocator = shoppingCartIcon;
        this.pageName = "Página de Inventario";
    }


    /**
     * Go to checkout form.
     *
     * @throws InterruptedException the interrupted exception
     */
    public void goToCheckoutForm() throws InterruptedException {
        try {
            WebElement checkout = wait.until(ExpectedConditions.elementToBeClickable(checkoutButton));
            checkout.click();
            BaseTest.createStep("Se muestra la página de información del proceso de Checkout", true, true, BaseTest.StepMode.IMMEDIATE);
            wait.until(ExpectedConditions.visibilityOfElementLocated(cancelButton));
            wait.until(ExpectedConditions.urlContains("checkout-step-one"));
            Thread.sleep(500);

        }catch (Exception e){
            BaseTest.processBuffer(BaseTest.BufferAction.COMMIT_MERGED_FAILURE,"Error en mostrar opcion de Checkout", true);
            throw e;

        }

    }

    public void continueShoppingButton(){
        try{
        WebElement continueShoppingbtn = wait.until(ExpectedConditions.elementToBeClickable(continueShopping));
        continueShoppingbtn.click();
        BaseTest.createStep("Redirección exitosa a la página de inventario", true, true, BaseTest.StepMode.IMMEDIATE);
        wait.until(ExpectedConditions.visibilityOfElementLocated(burgerMenuButton));
        wait.until(ExpectedConditions.urlContains("inventory"));

        }catch (Exception e){
            BaseTest.processBuffer(BaseTest.BufferAction.COMMIT_MERGED_FAILURE, "Error al mostrar la pagina de Inventario", true);
        }

    }





}
