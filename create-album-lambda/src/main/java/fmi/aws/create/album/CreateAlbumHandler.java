package fmi.aws.create.album;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class CreateAlbumHandler implements RequestHandler<Album, Response> {

	private DynamoDB dynamoDb;
	private String DYNAMO_DB_TABLE_NAME = "Albums";
	private Regions REGION = Regions.US_EAST_1;

	public Response handleRequest(Album albumRequest, Context context) {

		this.initDynamoDbClient();
		persistData(albumRequest);
		Response albumResponse = new Response();
		albumResponse.setMessage("Message Saved Successfully");
		return albumResponse;

	}

	private void initDynamoDbClient() {
		AmazonDynamoDBClient client = new AmazonDynamoDBClient();
		client.setRegion(Region.getRegion(REGION));
		this.dynamoDb = new DynamoDB(client);
	}

	private PutItemOutcome persistData(Album album) {
		Table table = dynamoDb.getTable(DYNAMO_DB_TABLE_NAME);
		PutItemOutcome outcome = table
				.putItem(new PutItemSpec().withItem(new Item().withString("title", album.getTitle())));
		return outcome;
	}
}
