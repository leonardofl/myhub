package com.leo.myhub.github;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;

public class GitHubClient {

	private static final String CLIENT_ID = "73f7ddeef4bc7e6c8447";
	private static final String AUTHORIZATION_BASE_URL = "https://github.com/login/oauth/authorize";
	private static final String REDIRECT_URI = "https://myhub.herokuapp.com/keys";
	private static final String KEYS_SCOPES = "read:public_key read:gpg_key";

	public String authorizationUrl() {
		try {
			URIBuilder uriBuilder = new URIBuilder(AUTHORIZATION_BASE_URL);
			uriBuilder.setParameter("client_id", CLIENT_ID);
			uriBuilder.setParameter("redirect_uri", REDIRECT_URI);
			uriBuilder.setParameter("scope", KEYS_SCOPES);
			uriBuilder.setParameter("state", randomString());
			URI url = uriBuilder.build();
			return url.toString();
		} catch (URISyntaxException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private String randomString() {
		return Long.toString(Math.round(Math.random()*1000));
	}

	public static void main(String[] args) {
		String url = new GitHubClient().authorizationUrl();
		System.out.println(url);
	}
	
}
