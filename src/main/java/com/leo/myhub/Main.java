package com.leo.myhub;

import static spark.Spark.get;
import static spark.SparkBase.port;
import static spark.SparkBase.staticFileLocation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
			List<SshKey> sshKeys = gitHubClient.getSshKeys();
			Map<String, Object> attributes = new HashMap<>();
			attributes.put("ssh_keys", sshKeys);
			return new ModelAndView(attributes, "profile.ftl");
		}, new FreeMarkerEngine());

		get("/hardcoded_profile", (req, res) -> {
			Map<String, Object> attributes = new HashMap<>();
			SshKey sshKey1 = new SshKey("work", "8DD3");
			SshKey sshKey2 = new SshKey("home", "7FF7");
			List<SshKey> sshKeys = Arrays.asList(new SshKey[] {sshKey1, sshKey2});  
			attributes.put("ssh_keys", sshKeys);
			return new ModelAndView(attributes, "profile.ftl");
		}, new FreeMarkerEngine());

	}

}
