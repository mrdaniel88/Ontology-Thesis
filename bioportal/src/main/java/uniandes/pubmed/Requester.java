package uniandes.pubmed;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Requester {
	
	private OkHttpClient client;

	public Requester() {
		client = new OkHttpClient().newBuilder().connectTimeout(5, TimeUnit.MINUTES)
		        .writeTimeout(5, TimeUnit.MINUTES)
		        .readTimeout(5, TimeUnit.MINUTES)
		        .build();
	}

	public String request(String url) throws IOException {
		Request request = new Request.Builder().url(url).get()
				.build();
		try (Response response = client.newCall(request).execute()) {
			return response.body().string();
		}
	}

}
