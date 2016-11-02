package com.leo.myhub.github;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;
import org.junit.Test;

/**
 * Teste de aprendizado
 *
 */
public class GitHubApiLearning {

	@Test
	public void shouldConsumirApiGithub() {
		Client client = ClientBuilder.newClient();
		String returned = client.target("https://api.github.com/")
		        .request(MediaType.APPLICATION_JSON)
		        .get(String.class);
		client.close();
		
		JSONObject json = new JSONObject(returned);
		for (String key : json.keySet()) {
			System.out.println(key + " = " + json.getString(key));
		}
	}
}
