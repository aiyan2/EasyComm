package com.fortinet.devqa;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TestHttpClient {

  final static boolean hasBody = true,  // true if has entry in POST, false otherwise;
		               noBody = false;  
  
  private static HttpClient client = HttpClientBuilder.create().build();
  
//  private HttpClient client = new HttpClient();
  private final String USER_AGENT = "Mozilla/5.0";

  public static void main(String[] args) throws Exception {

	String url = "http://172.18.13.106:8000/upload.php";

	TestHttpClient tc = new TestHttpClient();
	HttpPost post = new HttpPost(url);
	
	 tc.sendPost(prepPost(url, hasBody));
	 
	 tc.sendPost(prepPost(url, noBody, "lu1", "1234"));	 
	 
	 tc.sendPost(prepPost(url, hasBody, "lu1", "1234"));	 
	 
	 System.out.println("Done...");
  }
  private static HttpPost prepPost (String url, boolean hasEntry, String user, String passwd) throws Exception {
	  
		CredentialsProvider provider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials 
						= new UsernamePasswordCredentials(user, passwd);
		provider.setCredentials(AuthScope.ANY, credentials);
		
		
		 client = HttpClientBuilder.create()
				  .setDefaultCredentialsProvider(provider)
				  .build();

	//  HttpPost post = new HttpPost(url);	  
	//  post.addHeader("Authorization", "Basic bmdpbng6MTIzNA==");  // nginx:1234 
	//  post.addHeader("Authorization", "Basic bHUxOjEyMzQ=");  // lu1:1234 
   //  System.out.println("The auth header is:"+post.getHeaders("Authorization").length);

	 HttpPost post = prepPost(url, hasEntry);
	 

	 
	 return post;
	 

  }
  
  private static HttpPost  prepPost (String url, boolean hasEntry) {
	  
	  /**
		 POST /epower/rmtre.dll?10.RDReadAccess.GetServerVersion HTTP/1.1
		  Cache-Control: no-cache
		  Connection: Keep-Alive
		  Pragma: no-cache
		  User-Agent: Pivotal System Client (6.5.3.530; Offset -240; PID 6884; VA EA85680; P 0)
		  CRM_SYSTEM_NAME: UQBBAF8ATQBhAHMAdABlAHIA
		  Content-Length: 105
		  Host: pivot-pbs-q
		  ....x..a``.r	JMLqLNN-.....SK.S..R..R..3....b<.. .......	.......%.El@.sNfj^.. `...a..bF.&.d...Lp.#@...G^..		 
		 
	 **/
	  HttpPost post = new HttpPost(url);
				// add header
				post.setHeader("Host", "172.18.13.106"); 
				post.setHeader("Connection", "Keep-Alive");
				post.setHeader("Pragma", "no-cache");
		
		if(hasEntry) {
			String entry=" ....x..a``.r	JMLqLNN-.....SK.S..R..R..3....b<.. .......	.......%.El@.sNfj^.. `...a..bF.&.d...Lp.#@...G^..";
			System.out.println("The post has entry as:"+entry);	
				HttpEntity entity;
				try {
					entity = new ByteArrayEntity(entry.getBytes("UTF-8"));
					post.setEntity(entity);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
		}     
	  
		 return post;
	  
  }
  private void sendPost(HttpPost post)  throws Exception {
		
		HttpResponse response = client.execute(post);

		int responseCode = response.getStatusLine().getStatusCode();

		System.out.println("\nSending 'POST' request to URL : " + post.getURI());
		System.out.println("Response Code : " + responseCode);

		BufferedReader rd = new BufferedReader(
	                new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		 System.out.println(result.toString());
	  
  }   


}