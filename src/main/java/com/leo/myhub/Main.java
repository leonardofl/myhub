package com.leo.myhub;

import static spark.Spark.get;
import static spark.SparkBase.port;
import static spark.SparkBase.staticFileLocation;

import java.util.HashMap;
import java.util.Map;

import com.leo.myhub.github.GitHubClient;
import com.leo.myhub.github.SshKey;

import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

public class Main {

	public static void main(String[] args) {

		GitHubClient gitHubClient = GitHubClient.getInstance();
		
		port(Integer.valueOf(System.getenv("PORT")));
		staticFileLocation("/public");

		get("/", (req, res) -> {
			Map<String, Object> attributes = new HashMap<>();
			attributes.put("github_authorization_url", gitHubClient.authorizationUrl());
			return new ModelAndView(attributes, "index.ftl");
		}, new FreeMarkerEngine());

		get("/profile", (req, res) -> {
			String code = req.queryParams("code");
			String state = req.queryParams("state");
			gitHubClient.askAccessToken(code, state);
			SshKey sshKey = gitHubClient.getSshKey();
			Map<String, Object> attributes = new HashMap<>();
			attributes.put("ssh_key.title", sshKey.title);
			attributes.put("ssh_key.key", sshKey.key);
			return new ModelAndView(attributes, "profile.ftl");
		}, new FreeMarkerEngine());

	}

}
