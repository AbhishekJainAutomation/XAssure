package com.xassure.framework.driver;

import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBodyExtractionOptions;

public interface RestControls {
	
	public ResponseBodyExtractionOptions GetRequestWithOauth(String url);

	public ResponseBodyExtractionOptions GetRequestWithGraphQL(String url, String query);

	public String makeGetRequestAndGetBody(String url);

	public Response makeGetRequestAndGetResponse(String url);

	public int makeGetRequestAndGetStatusCode(String url);

	public String makeGetRequestAndGetContentType(String url);

	public String makeGetRequestAndGetHeaders(String url,String headerName);

}
