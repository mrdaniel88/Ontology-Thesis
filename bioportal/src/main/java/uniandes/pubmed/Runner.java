package uniandes.pubmed;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import uniandes.bioportal.SaveProcessor;

public class Runner {
	
	private Requester requester;
	private String base = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/";
	
	public Runner() {
		requester = new Requester();	
	}
	
	public void search(String db, String term) {
		String url = base.concat("esearch.fcgi?db=%db%&term=%term%&usehistory=y");
		String generatedUrl = url.replace("%term%", term).replace("%db%", db);
		try {
			String response = requester.request(generatedUrl);
			JSONObject search = XML.toJSONObject(response).getJSONObject("eSearchResult");
			System.out.println(search.toString(4));
			String webEnv = search.getString("WebEnv");
			Integer queryKey = search.getInt("QueryKey");
			Integer count = search.getInt("Count");
			fetchData(db, webEnv, queryKey, count, null);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} /*catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}
	
	private void fetchData(String db, String webEnv, Integer queryKey, Integer count, Integer limit) {
		Integer retmax = limit == null? 10 : limit;
		String efetch_url = base.concat("efetch.fcgi?db=%db%&WebEnv=%webEnv%");
        efetch_url += "&query_key=%queryKey%&retstart=$retstart";
        efetch_url += "&retmax=%retmax%&retmode=XML";
		//for (int retstart = 0; retstart < count; retstart += retmax) {
	        String request = efetch_url.replace("%webEnv%", webEnv)
	        						   .replace("%queryKey%", queryKey.toString())
	        						   .replace("%retmax%", retmax.toString())
	        						   .replace("%db%", db);
	        System.out.println("consultando " + request);
	        try {
				String response = requester.request(request);
				System.out.println("************************************************");
				System.out.println(response);
				System.out.println("************************************************");
				//JSONObject search = XML.toJSONObject(response);
				//System.out.println(search.toString(4));
				//SaveProcessor.save("pubmed.json", search.toString(4));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//}
		
		
	}

	public void fetchData(List<String> ids) {
		JSONArray data = new JSONArray();
		for (String id: ids) {
			JSONArray docs = requestArticle(id);
			for (int i = 0; i < docs.length(); i++) {
				data.put(docs.get(i));
			}
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 		}
		try {
			SaveProcessor.save("articles.json", data.toString(4));
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public JSONArray requestArticle(String id) {
		String url = "https://www.ncbi.nlm.nih.gov/CBBresearch/Lu/Demo/RESTful/tmTool.cgi/Disease/%id%/json/";
		String generatedUrl = url.replace("%id%", id);
		try {
			System.out.println("get data for "+ id);
			String response = requester.request(generatedUrl);
			System.out.println("*************************************************");
			System.out.println(response);
			System.out.println("*************************************************");
			return new JSONArray(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static void main(String[] args) {
		Runner runner = new Runner();
		runner.search("pubmed", "sleep");
	}

}
