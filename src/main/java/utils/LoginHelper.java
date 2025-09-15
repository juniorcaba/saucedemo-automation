package utils;

import basetest.BaseTest;
import basetest.BaseTest.StepMode;
import org.openqa.selenium.WebDriver;
import pages.LoginPage;

/**
 * Clase utilitaria para manejar login automático en tests.
 * Proporciona métodos estáticos para iniciar sesión de manera transparente
 * antes de ejecutar tests en páginas que requieren autenticación.
 *
 * @author HECTOR CABA
 * @version 2.0 - Steps optimizados
 */
public class LoginHelper {

    // Credenciales por defecto para tests
    private static final String DEFAULT_USERNAME = "standard_user";
    private static final String DEFAULT_PASSWORD = "secret_sauce";

    /**
     * Realiza login completo con credenciales por defecto.
     * Navega a la página de login, completa las credenciales y valida el éxito.
     *
     * @param driver instancia de WebDriver activa
     * @throws InterruptedException si el proceso es interrumpido
     */
    public static void performLogin(WebDriver driver) throws InterruptedException {
        performLogin(driver, DEFAULT_USERNAME, DEFAULT_PASSWORD);
    }

    /**
     * Realiza login completo con credenciales específicas.
     * Navega a la página de login, completa las credenciales y valida el éxito.
     *
     * @param driver instancia de WebDriver activa
     * @param username nombre de usuario para el login
     * @param password contraseña para el login
     * @throws InterruptedException si el proceso es interrumpido
     */
    public static void performLogin(WebDriver driver, String username, String password) throws InterruptedException {
        BaseTest.createStep("Iniciando proceso de login", true, false, StepMode.IMMEDIATE);

        try {
            LoginPage loginPage = new LoginPage(driver);
            loginPage.goTo();
            loginPage.completeLogin(username, password);

            // Validar que el login fue exitoso
            if (loginPage.isLoginSuccessful()) {
                //BaseTest.createStep("Login completado exitosamente", true, true, StepMode.IMMEDIATE);
            } else {
                BaseTest.createStep("Login falló - No se pudo autenticar al usuario", false, true, StepMode.IMMEDIATE);
                throw new RuntimeException("Login automático falló");
            }

        } catch (Exception e) {
            BaseTest.createStep("Error en login: " + e.getMessage(), false, true, StepMode.IMMEDIATE);
            throw e;
        }
    }

    /**
     * Verifica si el usuario ya está logueado validando la URL actual.
     *
     * @param driver instancia de WebDriver activa
     * @return true si el usuario está en una página autenticada, false en caso contrario
     */
    public static boolean isUserLoggedIn(WebDriver driver) {
        try {
            String currentUrl = driver.getCurrentUrl();
            return currentUrl.contains("inventory.html") ||
                    currentUrl.contains("inventory-item.html") ||
                    currentUrl.contains("cart.html") ||
                    currentUrl.contains("checkout");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Realiza login solo si el usuario no está ya autenticado.
     * Optimiza el tiempo de ejecución evitando logins innecesarios.
     *
     * @param driver instancia de WebDriver activa
     * @throws InterruptedException si el proceso es interrumpido
     */
    public static void ensureUserLoggedIn(WebDriver driver) throws InterruptedException {
        if (!isUserLoggedIn(driver)) {
            performLogin(driver);
        }
    }
}