import configs.Config;
import org.testng.annotations.Test;
import model.Joke;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.not;


public class JokeApiTests extends Config {

    @Test
    public void testGeneralJokeById_Validation() {
        given().log().all()
                .when().get("378")
                .then().spec(responseSpecificationForGet).log().all()
                .body(matchesJsonSchemaInClasspath("jsonSchema.json"));
    }

    @Test
    public void testRandomJoke_Validation() {
        given().log().all()
                .when().get("/random")
                .then().spec(responseSpecificationForGet).log().all()
                .body(matchesJsonSchemaInClasspath("jsonSchema.json"));
    }

    @Test
    public void testKnockKnockJoke_Validation() {
        given().log().all()
                .when().get("/knock-knock/random")
                .then().spec(responseSpecificationForGet).log().all()
                .body(matchesJsonSchemaInClasspath("jsonSchemaKnockOrProgramming.json"));
    }

    @Test
    public void testProgrammingJoke_Validation() {
        given().log().all()
                .when().get("/programming/random")
                .then().spec(responseSpecificationForGet).log().all()
                .body(matchesJsonSchemaInClasspath("jsonSchemaKnockOrProgramming.json"));
    }

    @Test
    public void testJokeById_LinkedToJoke() {
        int expectedJokeId = 378;

        given().log().all()
                .pathParam("jokeId", expectedJokeId)
                .when()
                .get("/{jokeId}")
                .then().spec(responseSpecificationForGet).log().all()
                .body("id", equalTo(expectedJokeId));
    }

    @Test
    public void testRandomJoke_LinkedToJoke() {

        Joke jokeNeedToCheck = given().log().all()
                .when()
                .get("/random")
                .then().spec(responseSpecificationForGet).log().all()
                .extract()
                .as(Joke.class);

        Integer jokeNeedToCheckId = jokeNeedToCheck.getId();

        Joke referenceJoke = given().log().all()
                .pathParam("jokeId", jokeNeedToCheckId)
                .when()
                .get("/{jokeId}")
                .then().spec(responseSpecificationForGet).log().all()
                .extract()
                .as(Joke.class);

        assertThat(jokeNeedToCheck, samePropertyValuesAs(referenceJoke));
    }

    @Test
    public void testKnockKnockJoke_LinkedToJoke() {
        Joke[] jokesNeedToCheck = given().log().all()
                .when()
                .get("/knock-knock/random")
                .then().spec(responseSpecificationForGet).log().all()
                .extract().body().as(Joke[].class);

        for (Joke jokeNeedToCheck : jokesNeedToCheck) {
            Integer id = jokeNeedToCheck.getId();

            Joke referenceJoke = given().log().all()
                    .pathParam("jokeId", id)
                    .when()
                    .get("/{jokeId}")
                    .then().spec(responseSpecificationForGet).log().all()
                    .extract()
                    .as(Joke.class);

            assertThat(jokeNeedToCheck, samePropertyValuesAs(referenceJoke));
        }
    }

    @Test
    public void testProgrammingJoke_LinkedToJoke() {
        Joke[] jokesNeedToCheck = given().log().all()
                .when()
                .get("/programming/random")
                .then().spec(responseSpecificationForGet).log().all()
                .extract().body().as(Joke[].class);

        for (Joke jokeNeedToCheck : jokesNeedToCheck) {
            Integer id = jokeNeedToCheck.getId();
            Joke referenceJoke = given().log().all()
                    .pathParam("jokeId", id)
                    .when()
                    .get("/{jokeId}")
                    .then().spec(responseSpecificationForGet).log().all()
                    .extract()
                    .as(Joke.class);

            assertThat(jokeNeedToCheck, samePropertyValuesAs(referenceJoke));
        }
    }

    @Test
    public void testJokeById_AttemptToChangeId() {
        int originalJokeId = 378;
        int newJokeId = 123;

        Joke jokeResponse = given().log().all()
                .pathParam("jokeId", originalJokeId)
                .when()
                .get("/{jokeId}")
                .then().spec(responseSpecificationForGet).log().all()
                .extract()
                .as(Joke.class);

        jokeResponse.setId(newJokeId);

        given().log().all()
                .contentType("application/json")
                .body(jokeResponse)
                .pathParam("jokeId", originalJokeId)
                .when()
                .put("/{jokeId}")
                .then().log().all()
                .statusCode(equalTo(404))
                .body(containsString("Cannot PUT"));
    }

    @Test
    public void testErrorForNonExistentJoke() {
        Integer id = given().log().all()
                .when()
                .get("/random")
                .then().spec(responseSpecificationForGet).log().all()
                .extract().body().as(Joke.class).getId();

        int nonExistentJokeId = id + 1_000_000;

        given().log().all()
                .pathParam("jokeId", nonExistentJokeId)
                .when()
                .get("/{jokeId}")
                .then().log().all()
                .assertThat()
                .statusCode(404)
                .body("type", equalTo("error"))
                .body("message", equalTo("joke not found"));
    }

    @Test
    public void testRandomJoke_ReturnsDifferentJokes() {
        Joke peviousJoke = given().log().all()
                .when()
                .get("/random")
                .then().spec(responseSpecificationForGet).log().all()
                .extract()
                .as(Joke.class);

        for (int i = 0; i < 5; i++) {
            Joke nextJoke = given().log().all()
                    .when()
                    .get("/random")
                    .then().spec(responseSpecificationForGet).log().all()
                    .extract()
                    .as(Joke.class);

            assertThat(peviousJoke, not(samePropertyValuesAs(nextJoke)));

            peviousJoke = nextJoke;
        }
    }
}
