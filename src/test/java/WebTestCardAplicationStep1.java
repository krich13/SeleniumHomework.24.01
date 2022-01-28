import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WebTestCardAplicationStep1 {
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
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void TearDown() {
        driver.quit();
        driver = null;
    }

    //Позитивные проверки (Поле "Имя и фамилия" содержат дефис и пробелы)

    @Test
    void shouldAcceptValidfields() { //все данные валидные
        driver.findElement(By.cssSelector("[name='name']")).sendKeys("Василий Петров-Иванов");
        driver.findElement(By.cssSelector("[name= 'phone']")).sendKeys("+79119418601");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();
        // String actualMessage = driver.findElement(By.className("paragraph_theme_alfa-on-white")).getText(); поиск элемента через класс
        String actualMessage = driver.findElement(By.cssSelector("[data-test-id='order-success'].paragraph_theme_alfa-on-white")).getText(); //поиск элемента через селектор
        String expectedMessage = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        Assertions.assertEquals(expectedMessage, actualMessage.strip());
    }

    //Негативные проверки
    @Test
    void fieldNameSurnameContainsLatin() { //Имя и Фамилия содержат латинские символы
        driver.findElement(By.cssSelector("[name='name']")).sendKeys("Vasiliy");
        driver.findElement(By.cssSelector("[name= 'phone']")).sendKeys("+79119418601");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();
        String actualMessage = driver.findElement(By.className("input__sub")).getText(); //поиск элемента по классу
        //String actualMessage = driver.findElement(By.cssSelector("[data-test-id='name'].input_theme_alfa-on-white.input__sub")).getText(); //по элементу не могу найти
        String expectedMessage = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        Assertions.assertEquals(expectedMessage, actualMessage.strip());
    }

    @Test
    void fieldPhoneContainsMoreSymbolsThenShould() { //Телефон содержит 12 символов
        driver.findElement(By.cssSelector("[name='name']")).sendKeys("Василий Петров-Иванов");
        driver.findElement(By.cssSelector("[name= 'phone']")).sendKeys("+791194186011");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();
        String actualMessage = driver.findElement(By.xpath("/html/body/div[1]/div/form/div[2]/span/span/span[3]")).getText();
        String expectedMessage = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        assertEquals(expectedMessage, actualMessage.strip());
    }


    @Test
    void fieldPhoneContainsLessSymbolsThenShould() { //Телефон содержит меньше 11 символов
        driver.findElement(By.cssSelector("[name='name']")).sendKeys("Василий Петров-Иванов");
        driver.findElement(By.cssSelector("[name= 'phone']")).sendKeys("+7");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();
        String actualMessage = driver.findElement(By.xpath("/html/body/div[1]/div/form/div[2]/span/span/span[3]")).getText();
        String expectedMessage = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        Assertions.assertEquals(expectedMessage, actualMessage.strip());
    }

    @Test
    void fieldPhoneDoesNotContainPlus() { //Телефон не содержит +
        driver.findElement(By.cssSelector("[name='name']")).sendKeys("Василий Петров-Иванов");
        driver.findElement(By.cssSelector("[name= 'phone']")).sendKeys("8908209093");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();
        String actualMessage = driver.findElement(By.xpath("/html/body/div[1]/div/form/div[2]/span/span/span[3]")).getText();
        String expectedMessage = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        Assertions.assertEquals(expectedMessage, actualMessage.strip());
    }

    @Test
    void CheckboxINotSelected() { //Когда не проставлена галочка, то в классе появляется еще поле input_invalid.(Нужно проверить, что оно появилось)
        driver.findElement(By.cssSelector("[name='name']")).sendKeys("Василий Петров-Иванов");
        driver.findElement(By.cssSelector("[name= 'phone']")).sendKeys("+79012345678");
        String actualElement = driver.findElement(By.cssSelector("[data-test-id='agreement']")).getAttribute("class");
        String expectedElement = "checkbox checkbox_size_m checkbox_theme_alfa-on-white";
        Assertions.assertEquals(expectedElement, actualElement.strip());
        driver.findElement(By.tagName("button")).click();
        String actualResultAfterClick = driver.findElement(By.cssSelector("[data-test-id='agreement']")).getAttribute("class");
        String expectedResultAfterClick = "checkbox checkbox_size_m checkbox_theme_alfa-on-white input_invalid";
        Assertions.assertEquals(actualResultAfterClick, expectedResultAfterClick.strip());
    }

    @Test
    void EmptyFields() { //Пустые поля ввода
        driver.findElement(By.cssSelector("[name='name']")).sendKeys(" ");
        driver.findElement(By.cssSelector("[name= 'phone']")).sendKeys(" ");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();
        String actualResult = driver.findElement(By.xpath("/html/body/div[1]/div/form/div[1]/span/span/span[3]")).getText();
        String expectedResult = "Поле обязательно для заполнения";
        Assertions.assertEquals(expectedResult, actualResult.strip());
    }
}
