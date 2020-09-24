package fmi.aws.fetch_albums_cors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;

public class FetchAlbumsCorsHandler implements RequestHandler<Map<String, Object>, APIGatewayProxyResponseEvent> {
	private String DYNAMO_DB_TABLE_NAME = "Albums";
	private Regions REGION = Regions.US_EAST_1;

	public APIGatewayProxyResponseEvent handleRequest(Map<String, Object> input, Context context) {
		AmazonDynamoDBClient client = new AmazonDynamoDBClient();
		client.setRegion(Region.getRegion(REGION));

		ScanRequest scanRequest = new ScanRequest().withTableName(DYNAMO_DB_TABLE_NAME);
		ScanResult result = client.scan(scanRequest);

		List<String> albumNames = new ArrayList<String>();
		for (Map<String, AttributeValue> item : result.getItems()) {
			for (AttributeValue albumName : item.values()) {
				albumNames.add(albumName.getS());
			}

		}

		String albumNamesJson = new Gson().toJson(albumNames);
		Map<String, String> responseHeaders = new HashMap<String, String>();
		responseHeaders.put("Access-Control-Allow-Origin", "*");
		responseHeaders.put("Access-Control-Allow-Headers", "*");
		responseHeaders.put("Access-Control-Allow-Methods", "OPTIONS,GET");
		Map<String, String> responseBody = new HashMap<String, String>();
		responseBody.put("albums", albumNamesJson);
		String responseBodyString = new JSONObject(responseBody).toJSONString();

		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
		response.setHeaders(responseHeaders);
		response.setStatusCode(200);
		response.setBody(responseBodyString);
		return response;
	}
}
