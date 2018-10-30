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

public class TestClient2 {

  final static boolean hasBody = true,  // true if has entry in POST, false otherwise;
		               noBody = false;  
  
  private static HttpClient client = HttpClientBuilder.create().build();
  
//  private HttpClient client = new HttpClient();
  private final String USER_AGENT = "Mozilla/5.0";

  public static void main(String[] args) throws Exception {

	String url = "http://172.18.13.106:80/upload.php";

	TestClient2 tc = new TestClient2();
	
	HttpPost post1, post2;
	
	post1= prepPost_1(url);
	post2= prepPost_2(url,"lu1", "1234");
	
	
	 tc.sendPost(post1);
	 
	 tc.sendPost(post2);	 
	 
	
	 
	 System.out.println("Done...");
	 
	 
	 
  }
  
  
 private static HttpPost  prepPost_1 (String url) {
	  
	  /**
POST /epower/rmtre.dll?10.RDReadAccess.GetConfigurationVersion HTTP/1.1
User-Agent: Pivotal System Proxy (6.5.3.530; Offset -240; PID 6884)
CRM_SYSTEM_NAME: UQBBAF8ATQBhAHMAdABlAHIA
Host: pivot-pbs-q
Cache-Control: no-store,no-cache
Pragma: no-cache
Content-Length: 119
Expect: 100-continue
Connection: Keep-Alive	
		 
	 **/
	  HttpPost post = new HttpPost(url);
				// add header
				post.setHeader("Host", "172.18.13.106"); 
				post.setHeader("Connection", "Keep-Alive");
				post.setHeader("Pragma", "no-cache");
	//			post.setHeader("Content-Length", "119");
				post.setHeader("Expect","100-continue");
				post.setHeader("Connection","Keep-Alive");		
			    post.expectContinue();
				
				// Add entry 	
				String entry="....X	.a``.r	JMLqLNN-.....SK....2.K..K2...R....#PJP...!..`.......b .......}..KR.@R.9..y%...Q............3.3......d.....";
				System.out.println("The post has entry as:"+entry);	
					HttpEntity entity;
					try {
						entity = new ByteArrayEntity(entry.getBytes("UTF-8"));
						post.setEntity(entity);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
								
				
	  
	 return post;
	  
  }
  
  private static HttpPost prepPost_2 (String url, String user, String passwd) throws Exception {

		CredentialsProvider provider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials 
						= new UsernamePasswordCredentials(user, passwd);
		provider.setCredentials(AuthScope.ANY, credentials);
		
		
		 client = HttpClientBuilder.create()
				  .setDefaultCredentialsProvider(provider)
				  .build();
			HttpPost post2 = new HttpPost(url); 
			post2.setHeader("Host", "172.18.13.106"); 
			post2.setHeader("Connection", "Keep-Alive");
			post2.setHeader("Pragma", "no-cache");
		//	post2.setHeader("Content-Length", "0");  // should be no need as no content added.
		     
						 
			 return post2;
						// Add other POST with Auth 
					
						  /**
						
					 ....X	.a``.r	JMLqLNN-.....SK....2.K..K2...R....#PJP...!..`.......b .......}..KR.@R.9..y%...Q............3.3......d.....POST /epower/rmtre.dll?10.RDReadAccess.GetConfigurationVersion HTTP/1.1
					User-Agent: Pivotal System Proxy (6.5.3.530; Offset -240; PID 6884)
					CRM_SYSTEM_NAME: UQBBAF8ATQBhAHMAdABlAHIA
					Authorization: Negotiate TlRMTVNTUAABAAAAl4II4gAAAAAAAAAAAAAAAAAAAAAKAKs/AAAADw==
					Host: pivot-pbs-q
					Cache-Control: no-store,no-cache
					Pragma: no-cache
					Content-Length: 0
						  
				   */					  

	 

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