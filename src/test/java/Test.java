import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

    public class Test {
        private WebDriver driver;

        @BeforeAll
        static void setUpAll() {
            WebDriverManager.chromedriver().setup();
        }

        @BeforeEach
        void setUp() {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");
            options.addArguments("--headless");
            driver = new ChromeDriver(options);
            driver.get("http://0.0.0.0:9999");
        }

        @AfterEach
        void TearDown() {
            driver.quit();
            driver = null;
        }

        @org.junit.jupiter.api.Test
        void shouldAcceptValidfields() { //тест для первой задачи

            List<WebElement> inputField = driver.findElements(By.className("input__control"));
            inputField.get(0).sendKeys("Василий Петров");
            inputField.get(1).sendKeys("+79119418503");
            driver.findElement(By.className("checkbox__box")).click();
            driver.findElement(By.tagName("button")).click();
            String actualMessage = driver.findElement(By.className("paragraph_theme_alfa-on-white")).getText();
            String expectedMessage = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
            Assertions.assertEquals(expectedMessage, actualMessage.strip());
        }

        @org.junit.jupiter.api.Test
        void shouldFailWithUnproperName() { //тест для второй задачи (сделала не до конца 2 часть)
            List<WebElement> inputField = driver.findElements(By.className("input__control"));
            inputField.get(0).sendKeys("Vasiliy Petrov");
            inputField.get(1).sendKeys("+79119418503");
            driver.findElement(By.className("checkbox__box")).click();
            driver.findElement(By.tagName("button")).click();
            String actualMessage = driver.findElement(By.className("input__sub")).getText();
            String expectedMessage = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
            Assertions.assertEquals(expectedMessage, actualMessage.strip());
        }
    }

