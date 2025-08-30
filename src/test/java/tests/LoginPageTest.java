package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import basetest.BaseTest;

public class LoginPageTest extends BaseTest {

    @Test(description = "Verificar que la página de login se carga correctamente", priority = 1)
    public void testLoginPageLoads() throws InterruptedException {

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.goTo();
        String currentUrl = getDriver().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("saucedemo.com"), "No se cargó correctamente la página de SauceDemo");

    }

    @Test(description = "Verificar login con credenciales válidas", priority = 2)
    public void testValidLogin() throws InterruptedException {
        LoginPage loginPage = new LoginPage(getDriver());

        loginPage.goTo();
        loginPage.login("standard_user", "secret_sauce");
        String currentUrl = getDriver().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("inventory.html"), "No se redirigió a la página de inventario");

    }

   @Test(description = "Verificar login con credenciales inválidas", priority = 3)
    public void testInvalidLogin() throws InterruptedException {
        LoginPage loginPage = new LoginPage(getDriver());

        loginPage.goTo();
        loginPage.login("user", "password");


        String currentUrl = getDriver().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("saucedemo.com") && !currentUrl.contains("inventory"),
                "Debería mantenerse en la página de login");
    }
}