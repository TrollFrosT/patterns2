package ru.netology.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.data.DataGenerator.Registration.getUser;
import static ru.netology.data.DataGenerator.getRandomLogin;
import static ru.netology.data.DataGenerator.getRandomPassword;

public class AuthTest {

    @BeforeEach
    void setup() {open("http://localhost:9999");}

    @Test
    void registeredValidUser() {

        var registeredUser = getRegisteredUser("active");
        $("span[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("span[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("button[data-test-id=action-login]").click();
        $("h2").shouldHave(Condition.exactText("Личный кабинет")).shouldBe((visible));
    }

    @Test
    void notRegisteredValidUser() {
        var notRegisteredUser = getUser("active");
        $("span[data-test-id=login] input").setValue(notRegisteredUser.getLogin());
        $("span[data-test-id=password] input").setValue(notRegisteredUser.getPassword());
        $("button[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe((visible));
    }

    @Test
    void blockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("span[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("span[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("button[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(Condition.text("Ошибка! Пользователь заблокирован"))
                .shouldBe((visible));
    }

    @Test
    void registeredUserWithNotValidLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("span[data-test-id=login] input").setValue(wrongLogin);
        $("span[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("button[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe((visible));
    }

    @Test
    void registeredUserWithNotValidPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("span[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("span[data-test-id=password] input").setValue(wrongPassword);
        $("button[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe((visible));
    }
}
