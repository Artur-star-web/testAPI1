package ru.netology.web;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private DataGenerator() {
    }

    @Value
    public static class User {
        String login;
        String password;
        String status;
    }

    public static String generateInvalidLogin(User validUser) {
        Faker faker = new Faker();
        String invalidLogin;
        do {
            invalidLogin = faker.name().username();
        } while (invalidLogin.equals(validUser.getLogin()));
        return invalidLogin;
    }

    public static String generateInvalidPassword(User validUser) {
        Faker faker = new Faker();
        String invalidPassword;
        do {
            invalidPassword = faker.internet().password();
        } while (invalidPassword.equals(validUser.getPassword()));
        return invalidPassword;
    }


    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(io.restassured.filter.log.LogDetail.ALL)
            .build();

    public static User generateUser(String status) {
        Faker faker = new Faker(new Locale("ru"));
        String login = faker.name().username();
        String password = faker.internet().password();

        User user = new User(login, password, status);

        given()
                .spec(requestSpec)
                .body(new Gson().toJson(user))
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);

        return user;
    }
}
