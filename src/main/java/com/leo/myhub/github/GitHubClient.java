package com.leo.myhub.github;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import com.leo.myhub.config.ConfigVar;
import com.leo.myhub.config.Configuration;

public class GitHubClient {

	public static final String CLIENT_ID = "73f7ddeef4bc7e6c8447";
	public static final String AUTHORIZATION_BASE_URL = "https://github.com/login/oauth/authorize";
	public static final String ACCESS_TOKEN_BASE_URL = "https://github.com/login/oauth/access_token";
	public static final String KEYS_URL = "https://api.github.com/user/keys";
	public static final String REDIRECT_URI = "https://myhub.herokuapp.com/profile";
	public static final String KEYS_SCOPES = "read:public_key read:gpg_key";

	public static GitHubClient INSTANCE = new GitHubClient();

	public static GitHubClient getInstance() {
		return INSTANCE;
	}

	private String state;
	private String accessToken;

	public String authorizationUrl() {
		try {
			URIBuilder uriBuilder = new URIBuilder(AUTHORIZATION_BASE_URL);
			uriBuilder.setParameter("client_id", CLIENT_ID);
			uriBuilder.setParameter("redirect_uri", REDIRECT_URI);
			uriBuilder.setParameter("scope", KEYS_SCOPES);
			state = randomString();
			uriBuilder.setParameter("state", state);
			URI url = uriBuilder.build();
			return url.toString();
		} catch (URISyntaxException e) {
			throw new IllegalStateException(e);
		}
	}

	private String randomString() {
		return Long.toString(Math.round(Math.random() * 1000));
	}

	public static void main(String[] args) {
		String url = new GitHubClient().authorizationUrl();
		System.out.println(url);
	}

	public void askAccessToken(String code, String state) {
		if (!this.state.equals(state)) {
			throw new IllegalArgumentException("States doesn't match");
		}
		Client client = ClientBuilder.newClient();
		String response = client.target(ACCESS_TOKEN_BASE_URL).queryParam("client_id", CLIENT_ID)
		        .queryParam("client_secret", client_secret()).queryParam("code", code)
		        .queryParam("redirect_uri", REDIRECT_URI).queryParam("state", state).request(MediaType.APPLICATION_JSON)
		        .get(String.class);
		JSONObject json = new JSONObject(response);
		accessToken = json.getString("access_token");
		client.close();
	}

	private String client_secret() {
		Configuration conf = new Configuration();
		String clientSecret = conf.get(ConfigVar.GITHUB_OAUTH_CLIENT_SECRET);
		return clientSecret;
	}

	public List<SshKey> getSshKeys() {
		Client client = ClientBuilder.newClient();
		String response = client.target(KEYS_URL).request(MediaType.APPLICATION_JSON)
		        .header("Authorization", "token " + accessToken).get(String.class);
		client.close();
		JSONArray json = new JSONArray(response);
		List<SshKey> keys = new ArrayList<>();
		for (int i = 0; i < json.length(); i++) {
			String title = json.getJSONObject(i).getString("title");
			String key = json.getJSONObject(i).getString("key");
			SshKey sshKey = new SshKey(title, key);
			keys.add(sshKey);
		}
		return keys;
	}

}
