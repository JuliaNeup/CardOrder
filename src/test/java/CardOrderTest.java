import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


public class CardOrderTest {
    @BeforeEach
    void setUp() {

        open("http://localhost:9999");
    }

    @Test
    void shouldSubmitRequest() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=name] input").setValue("Петров Петр Петрович");
        form.$("[data-test-id=phone] input").setValue("+79999999999");
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();
        $("[data-test-id=order-success]").shouldHave(exactText("Ваша заявка успешно" +
                " отправлена! Наш менеджер свяжется с вами в ближайшее время."));
    }

    @Test
    void shouldSubmitRequestIncorrect() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=name] input").setValue("Sмирнов Сергей Сергеевич");
        form.$("[data-test-id=phone] input").setValue("+79999999999");
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();
        $(".input_invalid[data-test-id='name']").shouldHave(text("Имя и Фамилия указаные неверно." +
                " Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldSubmitRequestIncorrectNomber() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=name] input").setValue("Петров Петр Петрович");
        form.$("[data-test-id=phone] input").setValue("79999999999");
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();
        $(".input_type_tel .input__sub").shouldHave(exactText("Телефон указан неверно. " +
                "Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void sholdThePhoneFieldIsEmpty() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=name] input").setValue("Петров Петр Петрович");
        form.$("[data-test-id=phone] input").setValue("");
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();
        $(".input_type_tel .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void sholdTheNameFieldIsEmpty() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=name] input").setValue("");
        form.$("[data-test-id=phone] input").setValue("+79999999999");
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();
        $(".input_type_text .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldSendFormWithoutCheckbox() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=name] input").setValue("Петров Петр Петрович");
        form.$("[data-test-id=phone] input").setValue("+79876543210");
        form.$(".button").click();
        $("[data-test-id='agreement'].input_invalid .checkbox__text")
                .shouldHave(exactText("Я соглашаюсь с условиями обработки и использования" +
                        " моих персональных данных и разрешаю сделать запрос в бюро кредитных историй"));
    }
}