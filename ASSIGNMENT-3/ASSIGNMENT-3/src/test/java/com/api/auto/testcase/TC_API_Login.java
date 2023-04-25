package com.api.auto.testcase;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.api.auto.utils.PropertiesFileUtils;

import io.restassured.path.json.JsonPath;
import io.restassured.response.*;
import io.restassured.specification.RequestSpecification;
import io.restassured.RestAssured;
import  io.restassured.RestAssured.*;
import io.restassured.http.ContentType;


public class TC_API_Login {
	private String account;
	private String password;
	private Response response;
	private ResponseBody responseBody;
	private JsonPath jsonBody;

	@BeforeClass
	public void init() {
		String baseUrl = PropertiesFileUtils.getProperty("baseurl");
		String loginPath = PropertiesFileUtils.getProperty("loginPath");
		
		account = PropertiesFileUtils.getProperty("account");
		password = PropertiesFileUtils.getProperty("password");
		
		RestAssured.baseURI = baseUrl;
		
		Map<String, Object> body = new HashMap<String,Object>();
		body.put("account", account);
		body.put("password", password);
		

		RequestSpecification request = RestAssured.given().contentType(ContentType.JSON).body(body);
		response = request.post(loginPath);
		responseBody = response.body();
		jsonBody = responseBody.jsonPath();

		System.out.println("Response Body Pretty String:  " + responseBody.asPrettyString());
	}
	
	@Test(priority = 0)
	public void TC01_Validate200Ok() {
        // kiểm chứng status code
		assertEquals(response.getStatusCode(), 200,"Status Code sai");
	}

	@Test(priority = 1)
	public void TC02_ValidateMessage() {
		// kiểm chứng response body có chứa trường message hay không
		assertTrue(responseBody.asString().contains("message"), "response body không chứa trường message");
		// kiểm chứng trường message có = "Đăng nhập thành công
		assertEquals(jsonBody.getString("message"), "Đăng nhập thành công", "message không = \"Đăng nhập thành công\"");
	}

	@Test(priority = 2)
	public void TC03_ValidateToken() {
		// kiểm chứng response body có chứa trường token hay không
		assertTrue(responseBody.asString().contains("token"), "response body không chứa trường token");
		// lưu lại token
		String token = jsonBody.getString("token");
        PropertiesFileUtils.saveToken(token);
	}

	@Test(priority = 3)
	public void TC05_ValidateUserType() {
        // kiểm chứng response body có chứa thông tin user và trường type hay không
		assertTrue(responseBody.asString().contains("user"), "response body không chứa trường user");
		assertTrue(responseBody.asString().contains("type"), "response body không chứa trường type");
        // kiểm chứng trường type có phải là “UNGVIEN”
		assertEquals(jsonBody.getString("user.type"), "UNGVIEN", "trường type không phải là “UNGVIEN”");
	}

	@Test(priority = 4)
	public void TC06_ValidateAccount() {
		// kiểm chứng response chứa thông tin user (đã kiểm chứng ở Dòng 77)  và trường account hay không
		assertTrue(responseBody.asString().contains("account"), "response body không chứa trường account");	
          // Kiểm chứng trường account có khớp với account đăng nhập
		assertEquals(jsonBody.getString("user.account"), account,"trường account không khớp với account đăng nhập");
          // kiểm chứng response chứa thông tin user (đã kiểm chứng ở Dòng 77) và trường password hay không
		assertTrue(responseBody.asString().contains("password"), "response body không chứa trường password");
          // Kiểm chứng trường password có khớp với password đăng nhập
		assertEquals(jsonBody.getString("user.password"), password,"trường password không khớp với password đăng nhập");
	}
}
