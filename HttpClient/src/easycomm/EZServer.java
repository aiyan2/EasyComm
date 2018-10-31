package easycomm;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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
     

	public static void main(String args[]) throws IOException {

		ServerSocket server = new ServerSocket(port);

		System.out.println("Listening for connection on port " + port + "..." + server.getInetAddress());

		while (true) {
			try (Socket socket = server.accept()) {

				String request;
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				OutputStream os = socket.getOutputStream();

//				boolean step1 = false; // flag for 2 steps to handle auth-request ( pattern in the middle, not in end).
				try {
					while ((request = br.readLine()) != null) {

						System.out.println("Rvd:" + request);

						if (request.matches("Expect: 100-continue")) // get the first POST
							sendHttpResponse(os, auth_401, "401-auth");

						if (request.startsWith("Authorization: "))
//							step1 = true;
//						if (request.startsWith("Accept-Language:") // end of auth-mesg
//								&& step1)
							sendHttpResponse(os, countinue_100, "100-continue");

						if (request.matches("------WebKitFormBoundarydRcL8NeqnnjYDT3i--")) // ending of payload
							sendHttpResponse(os, ok_200, "200-OK");

//                	 else 
//                		 System.out.println("Unknown request-line");
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
    
    static void  sendHttpResponse (OutputStream os, String mesg, String mesgName) throws IOException {
    	
    	 os.write(mesg.getBytes());
	     os.flush();
	 	 System.out.println("\n===>Sent "+ mesgName+"\n");
    	
    }

}


