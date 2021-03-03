package uniandes.bioportal;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BioPortalRequester {

	private OkHttpClient client;
	private String apiKey;

	public BioPortalRequester(String apiKey) {
		client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
		        .writeTimeout(5, TimeUnit.MINUTES)
		        .readTimeout(5, TimeUnit.MINUTES)
		        .build();;
		this.apiKey = apiKey;
	}

	public String request(String url) throws IOException {
		Request request = new Request.Builder().url(url).get().addHeader("Authorization", "apikey token=" + apiKey)
				.build();
		try (Response response = client.newCall(request).execute()) {
			return response.body().string();
		}
	}
	
	public String request(String url, String query) throws IOException {
		HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
	    httpBuilder.addQueryParameter("query",query);
	    httpBuilder.addQueryParameter("apikey", apiKey);
	    
		Request request = new Request.Builder().url(httpBuilder.build()).get().addHeader("Authorization", "apikey token=" + apiKey)
				.addHeader("Accept", "text/tab-separated-values")
				.build();
		
		System.out.println(request.url().toString());
		try (Response response = client.newCall(request).execute()) {
			return response.body().string();
		}
	}
	
	public String request(String url, HashMap<String, String> queryMaps) throws IOException {
		HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
	    httpBuilder.addQueryParameter("apikey", apiKey);
	    for (Entry<String, String> entry: queryMaps.entrySet()) {
	    	httpBuilder.addQueryParameter(entry.getKey(), entry.getValue());
	    }
		Request request = new Request.Builder().url(httpBuilder.build()).get().addHeader("Authorization", "apikey token=" + apiKey)
				.addHeader("Accept", "text/tab-separated-values")
				.build();
		
		System.out.println(request.url().toString());
		try (Response response = client.newCall(request).execute()) {
			return response.body().string();
		}
	}


}
