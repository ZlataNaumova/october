package test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.CreateUserRequestDTO;
import models.settings;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class RegistrationTest {

    @Before
    public void setup() {
        RestAssured.baseURI = "https://api.october.eu/";
    }


    @Test
    public void whenRequestedPostWithExistingUserDataErrorAppears() {
        settings settings = models.settings.builder().agreements("lendix-fr").build();
        CreateUserRequestDTO user = CreateUserRequestDTO.builder().name("test + test")
                                    .email("sdf@sdfq.sd")
                                    .password("1qaz!QAZ")
                                    .settings(settings).build();


        Response response  = given().contentType(ContentType.JSON)
        .when().body(user).post("/users");
        response.body().prettyPrint();
        response.then().assertThat().statusCode(400);
//                .and().body("errors.type", equalTo("Veuillez sélectionner un type de compte"));

    }
}
