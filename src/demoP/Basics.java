package demoP;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import files.Payload;
import files.ReusableMethods;

public class Basics {

	public static void main(String[] args) {
		// Validate if AddPlace API is working as expected
		//Given - All input details
		//When  - Submit the API (resource, http method)
		//Then  - Validate the response
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String response =  given().log().all().queryParam("key", "qaclick123").header("conetent-type","appliction/json")
		.body(Payload.Addplace()).when().post("maps/api/place/add/json")
		.then().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.header("Server", "Apache/2.4.18 (Ubuntu)").extract().response().asString();

		System.out.println(response);
		//JsonPath js = new JsonPath(response); //for parsing JSON
		JsonPath js = ReusableMethods.rawToJson(response);
		String PlaceID = js.getString("place_id");
		System.out.println(PlaceID);
		
		//Add place -> update place with new address ->Get place to validate if new address is present in response
		//Update Place
		String newAddress = "Rajpath delhi 229";
		given().log().all().queryParam("key", "qaclick123").header("conetent-type","appliction/json")
		.body("{\r\n"
				+ "\"place_id\":\""+PlaceID+"\",\r\n"
				+ "\"address\":\""+newAddress+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}")
		.when().put("maps/api/place/update/json")
		.then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		//Get Place
		String getPlaceResponse = given().log().all().queryParam("key", "qaclick123")
		.queryParam("place_id", PlaceID)
		.when().get("maps/api/place/get/json")
		//.then().assertThat().statusCode(200).body("address", equalTo("70 winter walk, USA"));
		.then().assertThat().statusCode(200).extract().response().asString();
		
		//JsonPath js1 = new JsonPath(getPlaceResponse);
		JsonPath js1 = ReusableMethods.rawToJson(getPlaceResponse);
		String actualAddress = js1.get("address");
		System.out.println(actualAddress);
		
		//Junit,TestNG
		Assert.assertEquals(actualAddress, newAddress);
	}

}
