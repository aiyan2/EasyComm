package com.fortinet.devqa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;

public class NTLMAuth {
	
	
	
	public static void main(String[] args) throws Exception {

 		String url = "http://172.18.13.106/upload.php";
//		String url = "http://172.18.43.124";
		sendGet(url);
		helper1(url, "adu1", "12345678","win-124", "devqa");
		
		
	}
    
	static void sendGet(String url) throws Exception, IOException {
		
			 

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);

		// add request header
		 
		HttpResponse response = client.execute(request);

		System.out.println("Response Code : " 
	                + response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(
			new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		
	}
	static void helper1(String url, String user1, String passwd, String hostname, String Domain) throws Exception, IOException {

		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(AuthScope.ANY, new NTCredentials("username", "passwd", hostname, "domain.at"));

		HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(credsProvider).build();

		HttpPost post = new HttpPost(url);

		StringEntity input = new StringEntity("bodyAsString", HTTP.UTF_8);
		input.setContentType("application/json");
		input.setContentEncoding("UTF-8");
		post.setEntity(input);

		post.setHeader("Accept", "application/json");
		post.setHeader("Content-type", "application/json");

		HttpResponse response = client.execute(post);

		int responseCode = response.getStatusLine().getStatusCode();

		System.out.println("\nSending 'POST' request to URL : " + post.getURI());
//	System.out.println("Post parameters : " + postParams);
		System.out.println("Response Code : " + responseCode);

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		System.out.println(result.toString());

	}
private void fromApacheHC(String url) throws Exception, IOException {
 

	CredentialsProvider credsProvider = new BasicCredentialsProvider();
	credsProvider.setCredentials(AuthScope.ANY,
	        new NTCredentials("user", "pwd", "myworkstation", "microsoft.com"));

	HttpHost target = new HttpHost("www.microsoft.com", 80, "http");
	HttpClient httpclient = HttpClientBuilder.create().setDefaultCredentialsProvider(credsProvider).build();
	// Make sure the same context is used to execute logically related requests
	HttpClientContext context = HttpClientContext.create();
	context.setCredentialsProvider(credsProvider);

	// Execute a cheap method first. This will trigger NTLM authentication
	HttpGet httpget = new HttpGet("/ntlm-protected/info");
	
	HttpResponse response1 = httpclient.execute(target, httpget, context);
	
 
	HttpEntity entity1 = response1.getEntity();
 System.out.println(entity1);
	// Execute an expensive method next reusing the same context (and connection)
	HttpPost httppost = new HttpPost("/ntlm-protected/form");
	httppost.setEntity(new StringEntity("lots and lots of data"));
	HttpResponse response2 = httpclient.execute(target, httppost, context);
	 
	    HttpEntity entity2 = response2.getEntity();
   System.out.println(entity1);	 
}
	 
}
