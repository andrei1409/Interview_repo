package configs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;

import static constants.Constants.RunVariable.path;
import static constants.Constants.RunVariable.server;
import static io.restassured.RestAssured.*;

public class Config {

    protected ResponseSpecification responseSpecificationForGet = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .build();

    @BeforeClass
    public void setUp() {
        baseURI = server;
        basePath = path;
    }
}
