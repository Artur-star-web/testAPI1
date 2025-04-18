package ru.netology.web;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AuthTest {
    @BeforeEach
    void setUp() {
        Configuration.headless = false; // если нужно без UI — true
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("✅ Успешный вход активного пользователя")
    void shouldLoginSuccessfullyWithValidActiveUser() {
        var user = DataGenerator.generateUser("active");

        $("[data-test-id='login'] input").setValue(user.getLogin());
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $("[data-test-id='action-login']").click();

        $("h2").shouldHave(text("Личный кабинет"));
    }

    @Test
    @DisplayName("Вход с заблокированным пользователем")
    void shouldNotLoginWithBlockedUser() {
        var user = DataGenerator.generateUser("blocked");

        open("http://localhost:9999");
        $("[data-test-id='login'] input").setValue(user.getLogin());
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']")
                .shouldBe(visible)
                .shouldHave(text("Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Вход с несуществующим логином")
    void shouldNotLoginWithInvalidLogin() {
        var user = DataGenerator.generateUser("active");

        open("http://localhost:9999");
        $("[data-test-id='login'] input").setValue("wrongLogin");
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']")
                .shouldBe(visible)
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Вход с неверным паролем")
    void shouldNotLoginWithInvalidPassword() {
        var user = DataGenerator.generateUser("active");

        open("http://localhost:9999");
        $("[data-test-id='login'] input").setValue(user.getLogin());
        $("[data-test-id='password'] input").setValue("wrongPassword");
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']")
                .shouldBe(visible)
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

}
