package com.xassure.framework.driver;

import static com.jayway.restassured.RestAssured.given;
import com.google.inject.Inject;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBodyExtractionOptions;
import com.xassure.reporting.logger.LogStatus;
import com.xassure.reporting.logger.Reporting;

public class ApiControlsLibrary implements RestControls {

	@Inject
	public ApiControlsLibrary() {

	}

	@Override
	public ResponseBodyExtractionOptions GetRequestWithOauth(String url) {
		ResponseBodyExtractionOptions response = null;
		try {
			long start = System.currentTimeMillis();
			response = given().header("Accept", "application/json").contentType(ContentType.JSON).expect()
					.statusCode(200).when().get(url).then().extract().body();
			long end = System.currentTimeMillis();
			if (response != null) {
				Reporting.getLogger().logApiResponseTime(LogStatus.PASS, "Successful hit to Users API Endpoint : " + url,(end - start) / 1000 + " secs");
//				Reporting.getLogger().logApiResponseTime(LogStatus.INFO, stepDescription, apiResponseTime);g(LogStatus.INFO,
//						"INFO: Users API Response time : " + (end - start) / 1000 + " secs");

			} else {
				Reporting.getLogger().log(LogStatus.FAIL, "Failed to access Users API Endpoint : " + url);

			}
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public ResponseBodyExtractionOptions GetRequestWithGraphQL(String url, String query) {
		return given().header("Accept", "application/json").body(query).contentType("application/json").expect()
				.statusCode(200).when().post(url).then().extract().body();
	}

	@Override
	public String makeGetRequestAndGetBody(String url) {
		System.out.println("URL is : " + url);
		return given().get(url).body().asString();
	}

	@Override
	public Response makeGetRequestAndGetResponse(String url) {
		return given().get(url);
	}

	@Override
	public int makeGetRequestAndGetStatusCode(String url) {
		return given().get(url).getStatusCode();
	}

	@Override
	public String makeGetRequestAndGetContentType(String url) {
		return given().get(url).getContentType();
	}

	@Override
	public String makeGetRequestAndGetHeaders(String url, String headerName) {
		return given().get(url).getHeader(headerName);
	}

}
