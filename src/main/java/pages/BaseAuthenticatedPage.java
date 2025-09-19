package pages;

import basetest.BaseTest;
import org.openqa.selenium.WebDriver;
import utils.LoginHelper;

/**
 * Clase base para páginas que requieren autenticación previa.
 * Extiende BasePage agregando funcionalidad de login automático.
 * Las páginas heredadas solo necesitan definir sus propiedades específicas.
 *
 * @author HECTOR CABA
 * @version 2.0 - Steps optimizados
 */
public abstract class BaseAuthenticatedPage extends BasePage {

    /**
     * Constructor que inicializa la página con login automático incluido.
     *
     * @param driver instancia de WebDriver activa
     * @throws IllegalArgumentException si driver es null
     */
    public BaseAuthenticatedPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navega a la página realizando login automático si es necesario.
     * Sobrescribe el metodo goTo() de BasePage para incluir autenticación.
     *
     * @throws InterruptedException si el hilo es interrumpido durante la navegación
     * @throws RuntimeException si no se definieron las propiedades requeridas o si falla el login
     */
    @Override
    public void goTo() throws InterruptedException {
        try {
            // Validar que la página haya definido los valores necesarios
            if (pageUrl == null || validationLocator == null || pageName == null) {
                throw new RuntimeException("La página debe definir pageUrl, validationLocator y pageName");
            }
            LoginHelper.ensureUserLoggedIn(driver);
            super.goTo();

        } catch (Exception e) {
            BaseTest.createStep("Error al cargar página autenticada " + pageName + ": " + e.getMessage(), false, true, BaseTest.StepMode.IMMEDIATE);
            throw e;
        }
    }

    /**
     * Versión alternativa de goTo() con credenciales específicas.
     * Útil cuando necesitas usar credenciales diferentes a las por defecto.
     *
     * @param username nombre de usuario específico
     * @param password contraseña específica
     * @throws InterruptedException si el hilo es interrumpido durante la navegación
     */
//    public void goTo(String username, String password) throws InterruptedException {
//        try {
//            // Validar propiedades requeridas
//            if (pageUrl == null || validationLocator == null || pageName == null) {
//                throw new RuntimeException("La página debe definir pageUrl, validationLocator y pageName");
//            }
//
//            // Forzar login con credenciales específicas
//            BaseTest.createStep("Login con credenciales específicas para " + pageName, true, false, BaseTest.StepMode.IMMEDIATE);
//
//            LoginHelper.performLogin(driver, username, password);
//
//            // Navegar a la página específica
//            super.goTo();
//
//        } catch (Exception e) {
//            BaseTest.createStep("Error al cargar " + pageName + " con credenciales específicas: " + e.getMessage(), false, true, BaseTest.StepMode.IMMEDIATE);
//            throw e;
//        }
//    }

    /**
     * Método de utilidad para verificar si el usuario sigue autenticado.
     * Útil para validaciones en medio de tests largos.
     * CAMBIADO A PUBLIC para permitir acceso desde tests.
     *
     * @return true si el usuario está autenticado, false en caso contrario
     */
    public boolean isStillLoggedIn() {
        return LoginHelper.isUserLoggedIn(driver);
    }

    /**
     * Re-autentica al usuario si la sesión ha expirado.
     * Útil para recuperación automática en tests largos.
     * CAMBIADO A PUBLIC para permitir acceso desde tests.
     *
     * @throws InterruptedException si el proceso es interrumpido
     */
    public void reAuthenticateIfNeeded() throws InterruptedException {
        if (!isStillLoggedIn()) {
            BaseTest.createStep("Re-autenticando usuario por sesión expirada", true, false, BaseTest.StepMode.IMMEDIATE);
            LoginHelper.performLogin(driver);
        }
    }
}