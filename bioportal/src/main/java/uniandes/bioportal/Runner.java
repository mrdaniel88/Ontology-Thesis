package uniandes.bioportal;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Runner {

	private BioPortalRequester client;

	public Runner(String apikey) {
		client = new BioPortalRequester(apikey);
	}

	public void run() {
		String sparqlService1 = "http://data.bioontology.org/ontologies/SNOMEDCT/classes/:clase";
		String clase2 = "http%3A%2F%2Fpurl.bioontology.org%2Fontology%2FSNOMEDCT%2F39898005";
		String url = sparqlService1.replace(":clase", clase2);
		HashMap<String, String> queryParameters = new HashMap<>();
		queryParameters.put("display_links", "true");
		queryParameters.put("display_context", "false");
		try {
			String result = client.request(url, queryParameters);
			System.out.println(result);
			JSONObject json = new JSONObject(result);
			mapping(json, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(String clase: Entities.CLASES) {
			try {
				String claseUrl = sparqlService1.replace(":clase", URLEncoder.encode(clase, "UTF-8"));
				String result = client.request(claseUrl, queryParameters);
				System.out.println(result);
				JSONObject json = new JSONObject(result);
				mapping(json, false);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {
		// apikey token=fccf9fa3-12d0-4648-8687-373e1c570066
		String apikey = "fccf9fa3-12d0-4648-8687-373e1c570066";
		Runner runner = new Runner(apikey);
		runner.run();
		// runner.getProperties("http://purl.bioontology.org/ontology/SNOMEDCT/363314000");
	}

	public void mapping(JSONObject json, boolean goUnder) {
		try {
			processJson(json, goUnder, true);
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public JSONObject processJson(JSONObject json, boolean goUnder, boolean  root) throws JSONException, IOException {
		System.out.println("procesando id :" + json.get("@id"));
		JSONObject nuevo = new JSONObject();
		JSONObject links = json.getJSONObject("links");
		String children = links.getString("children");
		nuevo.put("prefLabel", json.get("prefLabel"));
		nuevo.put("synonym", json.get("synonym"));
		nuevo.put("@id", json.get("@id"));
		if (root || goUnder) {
			nuevo.put("childrens", getChildren(children, goUnder));
		}
		String prefLabel = json.getString("prefLabel").replace(" ", "_").replace("\\", "_").replace("/", "_");
		System.out.println("********" + prefLabel);
		getRelationships(json.getString("@id"), prefLabel);
		getProperties(json.getString("@id"), prefLabel);
		return nuevo;
	}

	public String selfprefLabel(String self) {

		JSONObject json_self = null;
		try {
			String self_result = client.request(self);
			System.out.println(self_result);
			json_self = new JSONObject(self_result);
			return json_self.getString("prefLabel");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (JSONException e) {
			if (json_self != null) {
				return "Error no se pudo mapear: " + json_self.getJSONArray("errors").join(",");
			} else {
				return "error mapping";
			}
		}
	}

	public JSONArray getChildren(String childrenUrl, boolean goUnder) throws IOException {
		String childrenResult = client.request(childrenUrl);
		JSONArray childrens = new JSONArray();
		JSONObject result = new JSONObject(childrenResult);
		JSONArray collection = result.getJSONArray("collection");
		System.out.println("procesando childrens: " + collection.length());
		for (int i = 0; i < collection.length(); i++) {
			JSONObject nuevo = processJson(collection.getJSONObject(i), goUnder, false);
			childrens.put(nuevo);
		}
		return childrens;
	}

	public void getRelationships(String id, String prefLabel) {
		
		String query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
				+ "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> \n" + "SELECT DISTINCT ?o ?p2 ?s WHERE { \n"
				+ "	GRAPH <http://bioportal.bioontology.org/ontologies/SNOMEDCT> { \n" + "		?s ?p ?o . \n"
				+ "		FILTER(?s = <:id:>) \n"
				+ "		FILTER((?p = <http://purl.bioontology.org/ontology/SNOMEDCT/has_causative_agent>) || \n"
				+ "			           (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/cause_of >) || \n"
				+ "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/causative_agent_of>) || \n"
				+ "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/definitional_manifestation_of>) || \n"
				+ "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/due_to >) || \n"
				+ "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/during >) || \n"
				+ "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/has_active_ingredient>) || \n"
				+ "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/has_associated_finding >) || \n"
				+ "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/associated_finding_of>) || \n"
				+ "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/has_causative_agent>) || \n"
				+ "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/has_definitional_manifestation>) || \n"
				+ "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/has_dependent>) || \n"
				+ "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/finding_context_of>) || \n"
				+ "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/has_finding_context >) || \n"
				+ "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/has_finding_site>) || \n"
				+ "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/finding_site_of>) || \n"
				+ "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/has_occurrence>) || \n"
				+ "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/has_pathological_process>) || \n"
				+ "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/occurs_after>) || \n"
				+ "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/occurs_before>) || \n"
				+ "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/occurs_in>) || \n"
				+ "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/pathological_process_of>) || \n"
				+ "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/substance_used_by>) || \n"
				+ "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/uses_substance>)) \n"
				+ "			     OPTIONAL{?o ?p2 ?s.\n"
				+ "				    }\n" + "	} \n" + "}";
		
		
		/*
		 * String query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
		 * "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> \n" +
		 * "SELECT DISTINCT * WHERE { \n" +
		 * "	GRAPH <http://bioportal.bioontology.org/ontologies/SNOMEDCT> { \n" +
		 * "		?s ?p ?o . \n" + "		FILTER(?s = <:id:>) \n" +
		 * "		FILTER((?p = <http://purl.bioontology.org/ontology/SNOMEDCT/has_causative_agent>) || \n"
		 * +
		 * "			           (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/cause_of >) || \n"
		 * +
		 * "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/causative_agent_of>) || \n"
		 * +
		 * "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/definitional_manifestation_of>) || \n"
		 * +
		 * "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/due_to >) || \n"
		 * +
		 * "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/during >) || \n"
		 * +
		 * "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/has_active_ingredient>) || \n"
		 * +
		 * "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/has_associated_finding >) || \n"
		 * +
		 * "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/associated_finding_of>) || \n"
		 * +
		 * "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/has_causative_agent>) || \n"
		 * +
		 * "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/has_definitional_manifestation>) || \n"
		 * +
		 * "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/has_dependent>) || \n"
		 * +
		 * "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/finding_context_of>) || \n"
		 * +
		 * "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/has_finding_context >) || \n"
		 * +
		 * "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/has_finding_site>) || \n"
		 * +
		 * "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/finding_site_of>) || \n"
		 * +
		 * "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/has_occurrence>) || \n"
		 * +
		 * "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/has_pathological_process>) || \n"
		 * +
		 * "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/occurs_after>) || \n"
		 * +
		 * "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/occurs_before>) || \n"
		 * +
		 * "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/occurs_in>) || \n"
		 * +
		 * "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/pathological_process_of>) || \n"
		 * +
		 * "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/substance_used_by>) || \n"
		 * +
		 * "                       (?p = <http://purl.bioontology.org/ontology/SNOMEDCT/uses_substance>) || \n"
		 * +
		 * "                       (?p = <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>) || \n"
		 * +
		 * "			           (?p = <http://www.w3.org/2004/02/skos/core#prefLabel>) || \n"
		 * +
		 * "					   (?p = <http://www.w3.org/2004/02/skos/core#altLabel>)|| \n"
		 * +
		 * "                    (?p = <http://www.w3.org/2000/01/rdf-schema#subClassOf>)) \n"
		 * + "			     OPTIONAL{?o ?a ?b.\n" +
		 * "				    FILTER((?a = <http://www.w3.org/2004/02/skos/core#prefLabel>) ||\n"
		 * +
		 * "				                 (?a = <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>) &&\n"
		 * + "				                (?b !=\"\"))}\n" + "	} \n" + "}";
		 */

		String replacedQuery = query.replace(":id:", id);
		String url = "http://sparql.bioontology.org/sparql";
		try {
			String data = client.request(url, replacedQuery);
			if (!StringUtils.isBlank(data)) {
				String rdf = processCsv(data);
				SaveProcessor.save("data\\sparql_data_2.rdf", rdf, true);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: falló " + prefLabel + " causa: " + e.getMessage());
		}
	}
	
				
	private String processCsv(String csv) {
		String[] data = StringUtils.split(csv, "\r\n");
		String rdf = "";
		for(int i = 1; i < data.length; i++) {
			String[] line = StringUtils.split(data[i], "\t");
			if (line.length == 3) {
				rdf += data[i] + ". \r\n";
			} else if (line.length == 5) {
				String first = line[0] + "\t" + line[1] + "\t" + line[2] + ". \r\n";
				String second = line[2] + "\t" + line[3] + "\t" + line[4] + ". \r\n";
				rdf += first + second ;
			}
		}
		return rdf;
	}

	public void getProperties(String id, String prefLabel) {
		try {
			String sparqlService1 = "http://data.bioontology.org/ontologies/SNOMEDCT/classes/:clase";
			String clase = URLEncoder.encode(id, "UTF-8");
			String url = sparqlService1.replace(":clase", clase);
			HashMap<String, String> queryParameters = new HashMap<>();
			queryParameters.put("include", "prefLabel,properties");
			queryParameters.put("display_links", "true");
			queryParameters.put("display_context", "false");

			System.out.println("buscando properties");
			String result = client.request(url, queryParameters);
			System.out.println(result);
			JSONObject json = new JSONObject(result);
			JSONObject properties = json.getJSONObject("properties");
			String triplets = "";
			for (String prop : properties.keySet()) {
				if (Entities.PROPERTIES.contains(prop)) {
					JSONArray data = properties.getJSONArray(prop);
					for (int i = 0; i < data.length(); i++) {
						String value = data.getString(i);
						if (!value.matches("^(https://|http://|ftp://|ftps://)(?!-.)[^\\\\s/\\$.?#].[^\\\\s]*$")) {
							value = "\"" + value + "\"";
						}else {
							value = "<" + value + ">";
						}
						triplets += "<"+id + ">\t<" + prop + ">\t" + value + " . \n";
					}
				}
			}
			if(!StringUtils.isBlank(triplets)) {
				SaveProcessor.save("data\\properties_rest_2.rdf", triplets, true);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
