package fmi.aws.song.upload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.MultipartStream;
import org.json.simple.JSONObject;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;

public class UploadSongHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {

		final String clientRegion = "us-east-1";
		final String bucketName = "fmi-aws-album-generator";

		String fileObjKeyName = "";
		String contentType = "";

		LambdaLogger logger = context.getLogger();
		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

		try {

			byte[] bI = Base64.decodeBase64(event.getBody().getBytes());

			Map<String, String> hps = event.getHeaders();

			logger.log("Current headers" + hps.toString());

			if (hps != null) {
				contentType = hps.get("content-type");
				logger.log(hps.get("content-type"));
				logger.log(hps.get("Content-Type"));
				logger.log(hps.get("accept"));
			}

			String[] boundaryArray = contentType.split("=");

			byte[] boundary = boundaryArray[1].getBytes();

			logger.log(new String(bI, "UTF-8") + "\n");

			ByteArrayInputStream content = new ByteArrayInputStream(bI);

			MultipartStream multipartStream = new MultipartStream(content, boundary, bI.length, null);

			ByteArrayOutputStream out = new ByteArrayOutputStream();

			boolean nextPart = multipartStream.skipPreamble();

			while (nextPart) {
				String header = multipartStream.readHeaders();

				logger.log("Headers:");
				logger.log(header);
				fileObjKeyName = header.substring(header.indexOf("filename=") + 10, header.indexOf("\n") - 2);

				if (!fileObjKeyName.endsWith(".mp3")) {
					response.setStatusCode(422);
					return response;
				}

				multipartStream.readBodyData(out);
				nextPart = multipartStream.readBoundary();
			}

			logger.log("Data written to ByteStream");

			InputStream fis = new ByteArrayInputStream(out.toByteArray());

			AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(clientRegion).build();

			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(out.toByteArray().length);
			metadata.setCacheControl("public, max-age=31536000");

			s3Client.putObject(bucketName, fileObjKeyName, fis, metadata);

			logger.log(metadata.toString());
			logger.log("Put object in S3");

			response.setStatusCode(200);
			Map<String, String> responseHeaders = new HashMap<String, String>();
			 responseHeaders.put("Access-Control-Allow-Origin", "*");
			 responseHeaders.put("Access-Control-Allow-Headers", "*");
			 responseHeaders.put("Access-Control-Allow-Methods", "OPTIONS,POST");
			Map<String, String> responseBody = new HashMap<String, String>();
			responseBody.put("Status", "File stored in S3");
			String responseBodyString = new JSONObject(responseBody).toJSONString();
			response.setHeaders(responseHeaders);
			response.setBody(responseBodyString);

		} catch (AmazonServiceException e) {
			logger.log(e.getMessage());
		} catch (SdkClientException e) {
			logger.log(e.getMessage());
		} catch (IOException e) {
			logger.log(e.getMessage());
		}

		logger.log(response.toString());
		return response;
	}

}
