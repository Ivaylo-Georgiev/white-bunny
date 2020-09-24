package fmi.aws.fetch.songs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class FetchSongsHandler implements RequestHandler<Album, List<Song>> {

	private String DYNAMO_DB_TABLE_NAME = "Songs";
	private Regions REGION = Regions.US_EAST_1;

	public List<Song> handleRequest(Album input, Context context) {
		AmazonDynamoDBClient client = new AmazonDynamoDBClient();
		client.setRegion(Region.getRegion(REGION));

		ScanRequest scanRequest = new ScanRequest().withTableName(DYNAMO_DB_TABLE_NAME);
		ScanResult result = client.scan(scanRequest);

		String albumName = input.getTitle();
		List<Song> songs = new ArrayList<Song>();
		for (Map<String, AttributeValue> item : result.getItems()) {

			if (item.get("album").getS().equals(albumName)) {
				Song song = new Song();
				song.setName(item.get("name").getS());
				song.setAlbum(item.get("album").getS());
				song.setArtist(item.get("artist").getS());
				songs.add(song);
			}

		}

		return songs;
	}

}
