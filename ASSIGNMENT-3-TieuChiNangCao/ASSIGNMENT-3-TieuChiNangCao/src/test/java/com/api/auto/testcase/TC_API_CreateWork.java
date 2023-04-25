package com.api.auto.testcase;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.api.auto.utils.ExcelReadAndWrite;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

public class TC_API_CreateWork {
	private Response response;
	private ResponseBody responseBody;
	private JsonPath jsonBody;
	
	private String myWork = "Giáo viên";
	private String myExperience = "5 năm";
	private String myEducation = "Tốt nghiệp đại học";
	
	
	@BeforeClass
	public void init() throws IOException {
		String token = ExcelReadAndWrite.getToken();
		
		String baseUrl = ExcelReadAndWrite.getValuefromKey("./data/propertiesFile.xlsx","Sheet1","baseurl");
		String createWorkPath = ExcelReadAndWrite.getValuefromKey("./data/propertiesFile.xlsx","Sheet1","createWorkPath");
		
		RestAssured.baseURI = baseUrl;
		
		Map<String, Object> body = new HashMap<String, Object>();
			body.put("nameWork", myWork);
			body.put("experience", myExperience);
			body.put("education", myEducation);
		
		RequestSpecification request = RestAssured
				.given()
					.header("token",token)
					.contentType(ContentType.JSON)
					.body(body);
		response = request.post(createWorkPath);
		responseBody = response.body();
		jsonBody = responseBody.jsonPath();	
		
		System.out.println("Response Body Pretty String:  " + responseBody.asPrettyString());
	}

	@Test(priority = 0)
	public void TC01_Validate201Created() {
        assertEquals(response.getStatusCode(), 201, "Status Code sai");
	}



	@Test(priority = 1)
	public void TC02_ValidateWorkId() {
		assertTrue(responseBody.asString().contains("id"), "response body không chứa trường id");
		assertNotNull(responseBody.asString(),"id Null");
		assertNotEquals(responseBody, "id Empty");
	}

	@Test(priority = 2)
	public void TC03_ValidateNameOfWorkMatched() {
		// kiểm chứng tên công việc nhận được có giống lúc tạo
		assertTrue(responseBody.asString().contains("nameWork"), "response body không chứa trường nameWork");
		assertEquals(jsonBody.getString("nameWork"), myWork, "tên công việc nhận được không giống lúc tạo");

	}

	@Test(priority = 3)
	public void TC03_ValidateExperienceMatched() {
		// kiểm chứng kinh nghiệm nhận được có giống lúc tạo
		assertTrue(responseBody.asString().contains("experience"), "response body không chứa trường experience");
		assertEquals(jsonBody.getString("experience"), myExperience, "kinh nghiệm nhận được không giống lúc tạo");
	}

	@Test(priority = 4)
	public void TC03_ValidateEducationMatched() {
		// kiểm chứng học vấn nhận được có giống lúc tạo
		assertTrue(responseBody.asString().contains("education"), "response body không chứa trường education");
		assertEquals(jsonBody.getString("education"), myEducation, "học vấn nhận được không giống lúc tạo");
	}


}
