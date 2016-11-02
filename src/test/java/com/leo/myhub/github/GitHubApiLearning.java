package com.leo.myhub.github;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
		String result = client.target("https://api.github.com/")
		        .request(MediaType.APPLICATION_JSON)
		        .get(String.class);
		client.close();
		
		JSONObject json = new JSONObject(result);
		for (String key : json.keySet()) {
			System.out.println(key + " = " + json.getString(key));
		}
	}
	
	/**
	 * É pra acessar esse tipo de informação que precisaremos do OAuth
	 */
	@Test
	public void shouldBeNotAllowed() {
		Client client = ClientBuilder.newClient();
		Response response = client.target("https://api.github.com/").path("user").path("keys")
		        .request(MediaType.APPLICATION_JSON)
		        .get(Response.class);
		client.close();
		assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
	}
}
