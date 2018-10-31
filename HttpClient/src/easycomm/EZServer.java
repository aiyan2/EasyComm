package easycomm;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Date;

import com.fortinet.devqa.Util;

/**
 * Java program to create a simple HTTP Server to demonstrate how to use
 * ServerSocket and Socket class.
 *  
 */
public class EZServer {
	
	 static int port=80;
	 static String countinue_100 = 
			 "HTTP/1.1 100 Continue\r\n"+ "\r\n";
	 
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
     static String auth_401 = 
    		"HTTP/1.1 401 Unauthorized\r\n" +    
     		"Server: nginx/1.13.8\r\n" + 
     		"Date: Tue, 30 Oct 2018 17:00:02 GMT\r\n" + 
     		"Content-Type: text/html\r\n" + 
     		"Content-Length: 0\r\n" +  // 597
     		"Connection: keep-alive\r\n" + 
     		"WWW-Authenticate: Basic realm=\"Restricted Content\"\r\n" 
     		+"\r\n" 																// too long to send ..
//     		+ 
//     		"<html>\r\n" + 
//     		"<head><title>401 Authorization Required</title></head>\r\n" + 
//     		"<body bgcolor=\"white\">\r\n" + 
//     		"<center><h1>401 Authorization Required</h1></center>\r\n" + 
//     		"<hr><center>nginx/1.13.8</center>\r\n" + 
//     		"</body>\r\n" + 
//     		"</html>\r\n" + 
//     		"<!-- a padding to disable MSIE and Chrome friendly error page -->\r\n" + 
//     		"<!-- a padding to disable MSIE and Chrome friendly error page -->\r\n" + 
//     		"<!-- a padding to disable MSIE and Chrome friendly error page -->\r\n" + 
//     		"<!-- a padding to disable MSIE and Chrome friendly error page -->\r\n" + 
//     		"<!-- a padding to disable MSIE and Chrome friendly error page -->\r\n" + 
//     		"<!-- a padding to disable MSIE and Chrome friendly error page -->"
     		;		
     static String auth_401_from_customer_internaL = "HTTP/1.1 401 Unauthorized\r\n" + 
     		"Content-Type: text/html\r\n" + 
     		"Server: Microsoft-IIS/7.5\r\n" + 
     		"WWW-Authenticate: Negotiate\r\n" + 
     		"WWW-Authenticate: NTLM\r\n" + 
     		"X-Powered-By: ASP.NET\r\n" + 
     		"Date: Mon, 15 Oct 2018 14:42:38 GMT\r\n" + 
     		"Content-Length: 1293\r\n" + 
     		"\r\n" + 
     		"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\r\n" + 
     		"<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n" + 
     		"<head>\r\n" + 
     		"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\"/>\r\n" + 
     		"<title>401 - Unauthorized: Access is denied due to invalid credentials.</title>\r\n" + 
     		"<style type=\"text/css\">\r\n" + 
     		"<!--\r\n" + 
     		"body{margin:0;font-size:.7em;font-family:Verdana, Arial, Helvetica, sans-serif;background:#EEEEEE;}\r\n" + 
     		"fieldset{padding:0 15px 10px 15px;} \r\n" + 
     		"h1{font-size:2.4em;margin:0;color:#FFF;}\r\n" + 
     		"h2{font-size:1.7em;margin:0;color:#CC0000;} \r\n" + 
     		"h3{font-size:1.2em;margin:10px 0 0 0;color:#000000;} \r\n" + 
     		"#header{width:96%;margin:0 0 0 0;padding:6px 2% 6px 2%;font-family:\"trebuchet MS\", Verdana, sans-serif;color:#FFF;\r\n" + 
     		"background-color:#555555;}\r\n" + 
     		"#content{margin:0 0 0 2%;position:relative;}\r\n" + 
     		".content-container{background:#FFF;width:96%;margin-top:8px;padding:10px;position:relative;}\r\n" + 
     		"-->\r\n" + 
     		"</style>\r\n" + 
     		"</head>\r\n" + 
     		"<body>\r\n" + 
     		"<div id=\"header\"><h1>Server Error</h1></div>\r\n" + 
     		"<div id=\"content\">\r\n" + 
     		" <div class=\"content-container\"><fieldset>\r\n" + 
     		"  <h2>401 - Unauthorized: Access is denied due to invalid credentials.</h2>\r\n" + 
     		"  <h3>You do not have permission to view this directory or page using the credentials that you supplied.</h3>\r\n" + 
     		" </fieldset></div>\r\n" + 
     		"</div>\r\n" + 
     		"</body>\r\n" + 
     		"</html>";

    public static void main(String args[]) throws IOException {
       
        ServerSocket server = new ServerSocket(port);
        
        
        System.out.println("Listening for connection on port "+port+"..."+server.getInetAddress());
                
        while (true) {
            try (Socket socket = server.accept()) {
          	
            	String request;
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                OutputStream os = socket.getOutputStream();
                
//                boolean sent_100 = false, sent_200;
            try {
                while ((request = br.readLine()) != null ) {
//                if ((request = br.readLine()) != null) {
                	 System.out.println("Rvd:"+ request);
                	 
                	 if (request.matches("Expect: 100-continue"))
                	 {	 os.write(countinue_100.getBytes());
                	     os.flush();
                	     System.out.println("\n===>Sent 100_Continue\n");
                	 }
                	
                	 
                	 else if(request.matches("------WebKitFormBoundarydRcL8NeqnnjYDT3i--") )  // ending of payload    
                	 { 
                		 os.write(auth_401.getBytes());
                	     os.flush();
                	     System.out.println("\n===>Sent Auth_401\n");
                	 }
                	 else if(request.matches("Authorization: Basic bmdpbng6MTIzNA=="))
                	 {	 os.write(ok_200.getBytes());
                	     os.flush();
                	 	 System.out.println("\n===>Sent 200_OK\n");
                	 }
//                	 else 
//                		 System.out.println("Unknown request-line");
                }
            }
              catch (java.net.SocketException e) {
            	  
            	  e.printStackTrace();
            	  
              }
            
     
//                Date today = new Date();
//                String httpResponse = "HTTP/1.1 200 OK\r\n" + today;
//                socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
            }
        } // end-while
    }
    

}


