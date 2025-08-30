package basetest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;
import utils.ExtentManager;
import utils.ScreenshotUtils;
import com.aventstack.extentreports.Status;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase base para todos los tests de automatización con sistema avanzado de reportes.
 * Proporciona gestión de WebDriver thread-safe y un sistema de buffering para steps.
 *
 * @author HECTOR CABA
 * @version 2.0
 */
@Listeners(utils.ExtentTestListener.class)
public class BaseTest {
    private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<List<PendingStep>> pendingStepsThreadLocal = new ThreadLocal<>();

    /**
     * Define cómo se procesan los steps en el sistema de reportes.
     */
    public enum StepMode {
        /** Agrupa steps en buffer para procesamiento posterior */
        BUFFER,
        /** Procesa y escribe el step inmediatamente */
        IMMEDIATE,
        /** Comportamiento idéntico a BUFFER */
        STATIC
    }

    /**
     * Acciones disponibles para procesar el buffer de steps.
     */
    public enum BufferAction {
        /** Confirma todos los steps del buffer como exitosos */
        COMMIT_SUCCESS,
        /** Confirma steps del buffer + agrega un step de fallo */
        COMMIT_WITH_FAILURE,
        /** Combina el último step del buffer con mensaje de fallo */
        COMMIT_MERGED_FAILURE,
        /** Descarta buffer y solo reporta el fallo */
        DISCARD_AND_FAIL
    }

    /**
     * Representa un step pendiente en el buffer con su screenshot asociado.
     */
    public static class PendingStep {
        private String description;
        private boolean isPassed;
        private boolean takeScreenshot;
        private String screenshotBase64;

        public PendingStep(String description, boolean isPassed, boolean takeScreenshot) {
            this.description = description;
            this.isPassed = isPassed;
            this.takeScreenshot = takeScreenshot;

            if (takeScreenshot && getDriver() != null) {
                this.screenshotBase64 = captureScreenshotAsBase64();
            }
        }

        private String captureScreenshotAsBase64() {
            try {
                return ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BASE64);
            } catch (Exception e) {
                System.err.println("Error capturando screenshot: " + e.getMessage());
                return null;
            }
        }

        public String getDescription() { return description; }
        public boolean isPassed() { return isPassed; }
        public boolean shouldTakeScreenshot() { return takeScreenshot; }
        public String getScreenshotBase64() { return screenshotBase64; }
    }

    @BeforeSuite
    public void setUpSuite() {
        ExtentManager.createInstance();
        File reportsDir = new File(System.getProperty("user.dir") + "/reports");
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }
    }

    /**
     * Configura un nuevo WebDriver para cada test method.
     * Inicializa ChromeDriver con configuración estándar.
     */
    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driverThreadLocal.set(driver);
        pendingStepsThreadLocal.set(new ArrayList<>());
    }

    /**
     * Limpia recursos después de cada test method.
     * Cierra el WebDriver y limpia ThreadLocal variables.
     */
    @AfterMethod
    public void tearDown() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
        }
        pendingStepsThreadLocal.remove();
        ExtentManager.removeTest();
    }

    @AfterSuite
    public void tearDownSuite() {
        ExtentManager.flushReport();
    }

    @AfterTest
    public void afterTestMethod() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
        }
    }

    /**
     * Obtiene la instancia de WebDriver del hilo actual.
     *
     * @return WebDriver instance para el test actual
     * @throws RuntimeException si WebDriver no ha sido inicializado
     */
    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            throw new RuntimeException("WebDriver no inicializado. Asegúrate de que @BeforeMethod se haya ejecutado.");
        }
        return driver;
    }

    private static List<PendingStep> getPendingSteps() {
        List<PendingStep> steps = pendingStepsThreadLocal.get();
        if (steps == null) {
            steps = new ArrayList<>();
            pendingStepsThreadLocal.set(steps);
        }
        return steps;
    }

    /**
     * Crea un step de test con diferentes modos de procesamiento.
     * Permite agrupar steps relacionados usando el buffer o procesarlos inmediatamente.
     *
     * @param description descripción detallada del step ejecutado
     * @param isPassed true si el step fue exitoso, false si falló
     * @param takeScreenshot true para capturar screenshot del estado actual
     * @param mode modo de procesamiento (BUFFER, IMMEDIATE, STATIC)
     */
    public static void createStep(String description, boolean isPassed, boolean takeScreenshot, StepMode mode) {
        switch (mode) {
            case BUFFER:
            case STATIC:
                List<PendingStep> steps = getPendingSteps();
                steps.add(new PendingStep(description, isPassed, takeScreenshot));
                break;

            case IMMEDIATE:
                writeStepDirectly(description, isPassed, takeScreenshot, getDriver());
                break;
        }
    }

    /**
     * Procesa todos los steps almacenados en el buffer según la acción especificada.
     * Permite diferentes estrategias para manejar éxitos y fallos agrupados.
     *
     * @param action tipo de procesamiento a aplicar al buffer
     * @param failureDescription mensaje de error (requerido para acciones de fallo)
     * @param takeScreenshot true para capturar screenshot en fallos
     */
    public static void processBuffer(BufferAction action, String failureDescription, boolean takeScreenshot) {
        List<PendingStep> steps = getPendingSteps();
        WebDriver driver = getDriver();

        switch (action) {
            case COMMIT_SUCCESS:
                for (PendingStep step : steps) {
                    writeStepDirectlyWithStoredScreenshot(step);
                }
                break;

            case COMMIT_WITH_FAILURE:
                for (PendingStep step : steps) {
                    writeStepDirectlyWithStoredScreenshot(step);
                }
                writeStepDirectly(failureDescription, false, takeScreenshot, driver);
                break;

            case COMMIT_MERGED_FAILURE:
                if (steps.isEmpty()) {
                    writeStepDirectly(failureDescription, false, takeScreenshot, driver);
                    break;
                }

                for (int i = 0; i < steps.size() - 1; i++) {
                    writeStepDirectlyWithStoredScreenshot(steps.get(i));
                }

                PendingStep lastStep = steps.get(steps.size() - 1);
                String mergedMessage = lastStep.getDescription() + "<br>" + failureDescription;

                String failureScreenshot = null;
                if (takeScreenshot) {
                    try {
                        failureScreenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
                    } catch (Exception e) {
                        System.err.println("Error capturando screenshot de fallo: " + e.getMessage());
                    }
                }
                writeStepWithCustomScreenshot(mergedMessage, false, failureScreenshot);
                break;

            case DISCARD_AND_FAIL:
                writeStepDirectly(failureDescription, false, takeScreenshot, driver);
                break;
        }

        steps.clear();
    }

    private static void writeStepDirectlyWithStoredScreenshot(PendingStep step) {
        if (ExtentManager.getTest() == null) {
            System.err.println("No hay test activo para crear step: " + step.getDescription());
            return;
        }

        Status status = step.isPassed() ? Status.PASS : Status.FAIL;

        if (step.shouldTakeScreenshot() && step.getScreenshotBase64() != null) {
            try {
                String styleName = step.isPassed() ? "success" : "error";
                String imageHtml = ScreenshotUtils.generateScreenshotHtml(
                        step.getScreenshotBase64(), styleName, step.getDescription());
                ExtentManager.getTest().log(status, step.getDescription() + "<br>" + imageHtml);
            } catch (Exception e) {
                ExtentManager.getTest().log(status, step.getDescription());
                ExtentManager.getTest().log(Status.WARNING, "Error mostrando screenshot: " + e.getMessage());
            }
        } else {
            ExtentManager.getTest().log(status, step.getDescription());
        }
    }

    private static void writeStepWithCustomScreenshot(String stepDescription, boolean isPassed, String screenshotBase64) {
        if (ExtentManager.getTest() == null) {
            System.err.println("No hay test activo para crear step: " + stepDescription);
            return;
        }

        Status status = isPassed ? Status.PASS : Status.FAIL;

        if (screenshotBase64 != null) {
            try {
                String styleName = isPassed ? "success" : "error";
                String imageHtml = ScreenshotUtils.generateScreenshotHtml(screenshotBase64, styleName, stepDescription);
                ExtentManager.getTest().log(status, stepDescription + "<br>" + imageHtml);
            } catch (Exception e) {
                ExtentManager.getTest().log(status, stepDescription);
                ExtentManager.getTest().log(Status.WARNING, "Error mostrando screenshot: " + e.getMessage());
            }
        } else {
            ExtentManager.getTest().log(status, stepDescription);
        }
    }

    private static void writeStepDirectly(String stepDescription, boolean isPassed, boolean takeScreenshot, WebDriver driver) {
        if (ExtentManager.getTest() == null) {
            System.err.println("No hay test activo para crear step: " + stepDescription);
            return;
        }

        Status status = isPassed ? Status.PASS : Status.FAIL;

        if (takeScreenshot && driver != null) {
            try {
                String base64Screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
                String styleName = isPassed ? "success" : "error";
                String imageHtml = ScreenshotUtils.generateScreenshotHtml(base64Screenshot, styleName, stepDescription);
                ExtentManager.getTest().log(status, stepDescription + "<br>" + imageHtml);
            } catch (Exception e) {
                ExtentManager.getTest().log(status, stepDescription);
                ExtentManager.getTest().log(Status.WARNING, ScreenshotUtils.getErrorMessage() + ": " + e.getMessage());
            }
        } else {
            ExtentManager.getTest().log(status, stepDescription);
        }
    }
}