package fmi.aws.create.song;

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

public class CreateSongHandler implements RequestHandler<Song, Response> {

	private DynamoDB dynamoDb;
	private String DYNAMO_DB_TABLE_NAME = "Songs";
	private Regions REGION = Regions.US_EAST_1;

	public Response handleRequest(Song songRequest, Context context) {

		this.initDynamoDbClient();
		persistData(songRequest);
		Response songResponse = new Response();
		songResponse.setMessage("Message Saved Successfully");
		return songResponse;

	}

	private void initDynamoDbClient() {
		AmazonDynamoDBClient client = new AmazonDynamoDBClient();
		client.setRegion(Region.getRegion(REGION));
		this.dynamoDb = new DynamoDB(client);
	}

	private PutItemOutcome persistData(Song song) {
		Table table = dynamoDb.getTable(DYNAMO_DB_TABLE_NAME);
		PutItemOutcome outcome = table.putItem(new PutItemSpec().withItem(new Item().withString("name", song.getName())
				.withString("album", song.getAlbum()).withString("artist", song.getArtist())));
		return outcome;
	}

}
