package files;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

public class DynamicJson {

	@Test
	public void addBook()
	{
		RestAssured.baseURI = "http://216.10.245.166";
		String response = given().header("conetent-type","appliction/json")
		.body(Payload.AddBook("adsfs","6464"))
		.when()
		.post("Library/Addbook.php")
		.then().assertThat().statusCode(200)
		.extract().response().asString();
		
		JsonPath js =  ReusableMethods.rawToJson(response);
		String id = js.get("ID");
		System.out.println(id);
	}
	
	@DataProvider
	public object[][] getData()
	{
		//array = collections of elements
		//multidimensional array = collection of arrays
		return new object[][] {{"ojwfty","9363"}, {"sssds","4253"}, {"okmfet","533"} };
	}
}
