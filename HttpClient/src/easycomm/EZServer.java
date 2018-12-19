package easycomm;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.client.utils.DateUtils;

/**
 *2018-10-31: reproduced the issues of 2nd post blocked by FGT if av enabled
 *
 * Client: SuperClient + EditfromWireShark.java ( for mesgs)
 * 
 * src-location: https://github.com/aiyan2/EasyComm/tree/master/HttpClient/src/easycomm 
 * 
 */
public class EZServer {
	
	 static int port=80;
	 static String countinue_100 = 
			 "HTTP/1.1 100 Continue\r\n"+ "\r\n";
	 
	 static String eicar = "X5O!P%@AP[4\\PZX54(P^)7CC)7}$EICAR-STANDARD-ANTIVIRUS-TEST-FILE!$H+H*";
     static String ok_200 = 
    		 "HTTP/1.1 200 OK\r\n" + 
    		 "Content-Length: 0\r\n" +
//     		"Server: nginx/1.13.8\r\n" + 
//     		"Date: Tue, 30 Oct 2018 01:00:52 GMT\r\n" + 
//     		"Content-Type: text/html; charset=UTF-8\r\n" + 
//     		"Transfer-Encoding: chunked\r\n" + 
//     		"Connection: keep-alive\r\n" + 																																																																																																																							
//     		"Content-Encoding: gzip\r\n" + 
//     		"\r\n" + 
//     		"The script being executed is /usr/local/nginx/html/upload.php\r\n" + 
//     		"-------------taget_file is test-url.txt\r\n" + 
//     		"-------------taget_file is Array\r\n" + 
//     		"-------------taget_file is /var/www/html/uploads/test-url.txt\r\n" + 
//     		"File is not an image.Sorry, file already exists.The file test-url.txt has been uploaded. \r\n"+
     		"\r\n"
     		;
     static String ok_200_welcome =     		
     		"\r\n" + 
     		"<html><body><h1>It works!</h1>\r\n" + 
     		"<p>This is the default web page for this server.</p>\r\n" + 
     		"<p>The web server software is running but no content has been added, yet.</p>\r\n" + 
     		"<a href=/upload>Browsing the /upload directory in this file server</a><p>\r\n" + 
     		"</body></html>" +
     		 
     		"\r\n"
     		;
     static String auth_401 = 
    		"HTTP/1.1 401 Unauthorized\r\n" +    
     		"Server: nginx/1.13.8\r\n" + 
     		"Date: Tue, 30 Oct 2018 17:00:02 GMT\r\n" + 
     		"Content-Type: text/html\r\n" + 
     		"Content-Length: 0\r\n" +  // 597
     		"Connection: keep-alive\r\n" + 
     		"WWW-Authenticate: Basic realm=\"Restricted Content\"\r\n" 
//															
////     		+ 	// too long to send ..
////     		"<html>\r\n" + 
////     		"<head><title>401 Authorization Required</title></head>\r\n" + 
////     		"<body bgcolor=\"white\">\r\n" + 
////     		"<center><h1>401 Authorization Required</h1></center>\r\n" + 
////     		"<hr><center>nginx/1.13.8</center>\r\n" + 
////     		"</body>\r\n" + 
////     		"</html>\r\n" + 
////     		"<!-- a padding to disable MSIE and Chrome friendly error page -->\r\n" + 
////     		"<!-- a padding to disable MSIE and Chrome friendly error page -->\r\n" + 
////     		"<!-- a padding to disable MSIE and Chrome friendly error page -->\r\n" + 
////     		"<!-- a padding to disable MSIE and Chrome friendly error page -->\r\n" + 
////     		"<!-- a padding to disable MSIE and Chrome friendly error page -->\r\n" + 
////     		"<!-- a padding to disable MSIE and Chrome friendly error page -->"
	        
            +"\r\n" 
     		;		
    static OutputStream os;

	public static void main(String args[]) throws IOException {

		ServerSocket server = new ServerSocket(port);

		System.out.println("Listening for connection on port " + port + "..." + server.getInetAddress());

		while (true) {
			try (Socket socket = server.accept()) {

				String request;
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				os = socket.getOutputStream();

//				boolean step1 = false; // flag for 2 steps to handle auth-request ( pattern in the middle, not in end).
				try {
					while ((request = br.readLine()) != null) {

						System.out.println("Rvd:" + request);
						send200OK_EICAR();   // for ipv6 test ...
				        break;
						 
//						flowOfVerify(request);


					}
				} catch (java.net.SocketException e) {

					e.printStackTrace();

				}

//                Date today = new Date();
//                String httpResponse = "HTTP/1.1 200 OK\r\n" + today;
//                socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
			}
		} // end-while
	}
	
	static void flowOfReproduce(String request) throws IOException {

		if (request.matches("Expect: 100-continue")) // get the first POST
			sendHttpResponse(os, auth_401, "401-auth");

		if (request.startsWith("Authorization: "))
//			step1 = true;
//		if (request.startsWith("Accept-Language:") // end of auth-mesg
//				&& step1)
			sendHttpResponse(os, countinue_100, "100-continue");

		if (request.matches("------WebKitFormBoundarydRcL8NeqnnjYDT3i--")) // ending of payload
			sendHttpResponse(os, ok_200, "200-OK");

//	 else 
//		 System.out.println("Unknown request-line");
	}
    
	// FGT dis-connect the first round, start a new one:
	static void flowOfVerify(String request) throws IOException {		

		if (request.startsWith("Authorization: ")) 			
			sendHttpResponse(os, countinue_100, "100-continue");
		
		if (request.matches("------WebKitFormBoundarydRcL8NeqnnjYDT3i--")) // ending of payload
			sendHttpResponse(os, ok_200, "200-OK");
	}
	
	static void send200OK_EICAR() throws IOException {
 
	       
	     
				
		String myOK_NOK = "\r\n" + 
	     		"<html><body><h1> !</h1>\r\n" + 
	     		"<p>This is "+
	     		eicar+
	     		" </p>\r\n" + 
	     		"<p>The web server software is running but no content has been added, yet.</p>\r\n" + 
	     		"<a href=/upload>Browsing the /upload directory in this file server</a><p>\r\n" + 
	     		"</body></html>" +	
				"\r\n" ;
		
		 String myOK_1 = 
	    		 "HTTP/1.1 200 OK\r\n" + 
	    		 "Content-Type: text/html; charset=UTF-8\r\n" +
	    		 "Content-Length: "+eicar.length()+"\r\n" +
	    				 "\r\n"            // MUST BE for content 
	    		         +eicar  ;
//	    				 +"\r\n";	 // MUST NOT 
	    	 
		String myOK_OK = 
				"HTTP/1.1 200 OK\r\n" + 
//				"Date: Mon, 10 Dec 2018 23:42:18 GMT\r\n" + 
//				"Server: Apache/2.2.17 (Ubuntu)\r\n" + 
//				"Last-Modified: Wed, 01 Apr 2015 20:54:50 GMT\r\n" + 
//				"ETag: \"31c01f4-44-512afed197a80\"\r\n" + 
//				"Accept-Ranges: bytes\r\n" + 
//				"Vary: Accept-Encoding\r\n" + 
//				"Content-Encoding: gzip\r\n" + 
				"Content-Length: 68\r\n" + 
//				"Keep-Alive: timeout=15, max=100\r\n" + 
//				"Connection: Keep-Alive\r\n" + 
				"Content-Type: text/plain\r\n" + 
				"\r\n" + 
				eicar
//				"X5O!P%@AP[4\\PZX54(P^)7CC)7}$EICAR-STANDARD-ANTIVIRUS-TEST-FILE!$H+H*"
				;
		
		 Calendar calendar = Calendar.getInstance();
	     String dateGMT = calendar.getTime().toGMTString();  
	 
	        // Add 3 minutes to the calendar time
	        calendar.add(Calendar.MINUTE, 3);	 
	      String targetDate =  calendar.getTime().toGMTString();  
	      
		String content = "<html><head></head>\r\n" + 
				"<body><p align=\"left\">Expire\r\n" + 
				"</body></html> ";
	
		
		String cache_OK = 
				"HTTP/1.1 200 OK\r\n" + 
				"Date: "+dateGMT+" \r\n" + 
				"Server: Apache/2.2.17 (Ubuntu)\r\n" + 
//				"Last-Modified: Wed, 12 Dec 2018 19:23:00 GMT\r\n" + 
//				"ETag: \"5f80033-51-57cd820529d45\"\r\n" + 
				"Accept-Ranges: bytes\r\n" + 
				"Content-Length: "+content.length()+"\r\n" + 
				"Keep-Alive: timeout=15, max=100\r\n" + 
				"Connection: Keep-Alive\r\n" + 
				"Expires: "+targetDate+" \r\n"+     // added from wad-debug-mantis
//				"Content-Type: text/plain\r\n" + 
				"\r\n" + 
				content
				;
		String cache_OK_bak =  // from mantis_wad_debug
				"HTTP/1.1 200 OK\r\n" + 
				"Date: "+dateGMT+" \r\n" + 
				"Server: Apache/2.2.22 (Ubuntu)\r\n" + 
				"Accept-Ranges: bytes\r\n" + 
				"Content-Length: 67\r\n" + 
				"Expires: Fri, 19 Oct 2018 01:25:15 GMT";
		
		String cache_206_cust =  // composed based on  mantis_wad_debug
				"HTTP/1.1 206 Partial Content\r\n" + 
				"Date: "+dateGMT+" \r\n" + 
				"Server: Apache/2.2.22 (Ubuntu)\r\n" + 
				"Accept-Ranges: bytes\r\n" + 
//				"Content-Length: "+content.length()+"\r\n" + 
				"Content-Length: "+67+"\r\n" + 
				"Expires: "+targetDate+" \r\n"+     // added from wad-debug-mantis
				"Content-Range: bytes 10-66/67"+
				"\r\n" + 
				content.substring(9,content.length());
		String cache_206 = "HTTP/1.1 206 Partial Content\r\n" + 
				"Content-Length: 57\r\n" + 
				"Content-Range: bytes 10-66/67\r\n" + 
				"Date: Fri, 19 Oct 2018 01:25:18 GMT\r\n" + 
				"Server: Apache/2.2.22 (Ubuntu)\r\n" + 
				"Accept-Ranges: bytes\r\n" + 				
				"Expires: Fri, 19 Oct 2018 01:28:18 GMT\r\n" + 
				"Age: 39\r\n" + 
				""+"\r\n" + 
				content.substring(10,content.length())+
				"\r\n";
		
		
		String cache_206_OK_on_reproeduce = "HTTP/1.1 206 Partial Content\r\n" + 
				"Content-Length: 57\r\n" + 
				"Content-Range: bytes 10-66/67\r\n" + 
				"Date: Fri, 19 Oct 2018 01:22:15 GMT\r\n" + 
				"Server: Apache/2.2.22 (Ubuntu)\r\n" + 
				"Accept-Ranges: bytes\r\n" + 
				"Expires: Fri, 19 Oct 2018 01:25:15 GMT\r\n" + 
				"Age: 39\r\n" + 
				" \r\n" + 
				"d></head>\r\n" + 
				"<body><p align=\"left\">Expire\r\n" + 
				"</body></html> "
				+ 
				"\r\n";   // added 
		
		
		
		String external_webfilter = "HTTP/1.1 200 OK\r\n" + 
				"Date: Fri, 14 Dec 2018 19:59:10 GMT\r\n" + 
				"Server: Apache/2.2.17 (Ubuntu)\r\n" + 
				"Last-Modified: Wed, 10 Oct 2018 22:37:32 GMT\r\n" + 
				"ETag: \"5f8000c-25-577e780068b8f\"\r\n" + 
				"Accept-Ranges: bytes\r\n" + 
				"Vary: Accept-Encoding\r\n" + 
				"Content-Encoding: gzip\r\n" + 
//				"Content-Length: 49\r\n" + 
//				"Keep-Alive: timeout=15, max=100\r\n" + 
//				"Connection: Keep-Alive\r\n" + 
//				"Content-Type: text/plain\r\n" + 
//				"\r\n" + 
//				"\r\n" + 
//				"*in*org\r\n" + 
//				"n*.org\r\n" + 
//				"*n.co*\r\n" + 
//				"*ibm\r\n" + 
//				"\r\n" + 
				"\r\n" + 
				"";
		
		String PC_206_ReProduce = "[0x7f5f90fff198] Received response from server:\r\n" + 
				"\r\n" + 
				"HTTP/1.1 206 Partial Content\r\n" + 
				"Date: Mon, 17 Dec 2018 21:41:01 GMT\r\n" + 
				"Server: Apache/2.4.18 (Ubuntu)\r\n" + 
				"Accept-Ranges: bytes\r\n" + 
				"Content-Length: 12\r\n" + 
				"Expires: Mon, 17 Dec 2018 21:42:06 GMT\r\n" + 
				"Age: 114\r\n" + 
				"Content-Range: bytes 10-76/77\r\n" + 
				"Content-Type: application/xml\r\n" + 
				"\r\n" + 
				content.substring(1,12)+
				"\r\n";
				
		sendHttpResponse(os, PC_206_ReProduce, "PC_206");
		
				


	}
    static void  sendHttpResponse (OutputStream os, String mesg, String mesgName) throws IOException {
    	
    	System.out.println("\r\nTo be sent:\n"+mesg); 
    	os.write(mesg.getBytes());
	     os.flush();
	 	 System.out.println("\n===>Sent "+ mesgName+"\n");
    	
    }
    

}


