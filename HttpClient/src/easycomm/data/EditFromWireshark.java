package easycomm.data;

public class EditFromWireshark {
	
	
	String IP = "172.18.13.106";
	String APP = "/upload.html";   //  
	
	
	// paste from wireshark	
	public String   POST_1() {
	
	  return "POST /upload.php HTTP/1.1\r\n" + 
			  "Host: 172.18.13.106\r\n" + 
			  "Connection: keep-alive\r\n" + 
			  "Content-Length: "+ REPLAY.length()+"\r\n" +    // modified 
			  "Cache-Control: max-age=0\r\n" + 
			  "Origin: http://172.18.13.106\r\n" + 
			  "Upgrade-Insecure-Requests: 1\r\n" + 
			  "Content-Type: multipart/form-data; boundary=----WebKitFormBoundarydRcL8NeqnnjYDT3i\r\n" + 
			  "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36\r\n" + 
			  "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\n" + 
			  "Referer: http://172.18.13.106/upload.html\r\n" + 
			  "Accept-Encoding: gzip, deflate\r\n" + 
			  "Accept-Language: zh-CN,zh;q=0.9,en;q=0.8,fr;q=0.7,zh-TW;q=0.6,ca;q=0.5\r\n" + 
			  "Expect: 100-continue\r\n\r\n"+                //added
			  "";
	}
	public static String REPLAY =  
	"------WebKitFormBoundarydRcL8NeqnnjYDT3i\r\n" + 
			  "Content-Disposition: form-data; name=\"fileToUpload\"; filename=\"test-url.txt\"\r\n" + 
			  "Content-Type: text/plain\r\n" + 
			  "\r\n" + 
			  "\r\n" + 
			  "*in*org\r\n" + 
			  "n*.org\r\n" + 
			  "*n.co*\r\n" + 
			  "*ibm\r\n" + 
			  "\r\n" + 
			  "\r\n" + 
			  "\r\n" + 
			  "------WebKitFormBoundarydRcL8NeqnnjYDT3i\r\n" + 
			  "Content-Disposition: form-data; name=\"submit\"\r\n" + 
			  "\r\n" + 
			  "Upload File\r\n" + 
			  "------WebKitFormBoundarydRcL8NeqnnjYDT3i--\r\n";
    
	public String POST_2() {
		 return  
			//	 REPLAY +  
			  "POST /upload.php HTTP/1.1\r\n" + 
			  "Host: 172.18.13.106\r\n" + 
			  "Connection: keep-alive\r\n" + 
			  "Content-Length: 0\r\n" +    // ==0
//			  "Authorization: Basic bmdpbng6MTIzNA==\r\n"+    //nginx:1234 
			  "Cache-Control: max-age=0\r\n" + 
			  "Origin: http://172.18.13.106\r\n" + 
			  "Upgrade-Insecure-Requests: 1\r\n" + 
			  "Content-Type: multipart/form-data; boundary=----WebKitFormBoundarydRcL8NeqnnjYDT3i\r\n" + 
			  "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36\r\n" + 
			  "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\n" + 
			  "Referer: http://172.18.13.106/upload.html\r\n" + 
			  "Accept-Encoding: gzip, deflate\r\n" + 
			  "Accept-Language: zh-CN,zh;q=0.9,en;q=0.8,fr;q=0.7,zh-TW;q=0.6,ca;q=0.5\r\n" + 
			  "Authorization: Basic bmdpbng6MTIzNA==\r\n"+    //nginx:1234 
			  "\r\n"
			  ;
	}

	public String post_std_template =
					  "POST /upload.php HTTP/1.1\r\n" + 
					  "Host: 172.18.13.106\r\n" + 
					  "Connection: keep-alive\r\n" + 
					  "Content-Length: 335\r\n" + 
					  "Cache-Control: max-age=0\r\n" + 
					  "Origin: http://172.18.13.106\r\n" + 
					  "Upgrade-Insecure-Requests: 1\r\n" + 
					  "Content-Type: multipart/form-data; boundary=----WebKitFormBoundarydRcL8NeqnnjYDT3i\r\n" + 
					  "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36\r\n" + 
					  "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\n" + 
					  "Referer: http://172.18.13.106/upload.html\r\n" + 
					  "Accept-Encoding: gzip, deflate\r\n" + 
					  "Accept-Language: zh-CN,zh;q=0.9,en;q=0.8,fr;q=0.7,zh-TW;q=0.6,ca;q=0.5\r\n" + 
					  "\r\n" + 
					  "------WebKitFormBoundarydRcL8NeqnnjYDT3i\r\n" + 
					  "Content-Disposition: form-data; name=\"fileToUpload\"; filename=\"test-url.txt\"\r\n" + 
					  "Content-Type: text/plain\r\n" + 
					  "\r\n" + 
					  "\r\n" + 
					  "*in*org\r\n" + 
					  "n*.org\r\n" + 
					  "*n.co*\r\n" + 
					  "*ibm\r\n" + 
					  "\r\n" + 
					  "\r\n" + 
					  "\r\n" + 
					  "------WebKitFormBoundarydRcL8NeqnnjYDT3i\r\n" + 
					  "Content-Disposition: form-data; name=\"submit\"\r\n" + 
					  "\r\n" + 
					  "Upload File\r\n" + 
					  "------WebKitFormBoundarydRcL8NeqnnjYDT3i--\r\n"
					  ;

  public String back_forth =
		  "POST /upload.php HTTP/1.1\r\n" + 
		  "Host: 172.18.13.106\r\n" + 
		  "Connection: keep-alive\r\n" + 
		  "Content-Length: 335\r\n" + 
		  "Cache-Control: max-age=0\r\n" + 
		  "Origin: http://172.18.13.106\r\n" + 
		  "Upgrade-Insecure-Requests: 1\r\n" + 
		  "Content-Type: multipart/form-data; boundary=----WebKitFormBoundarydRcL8NeqnnjYDT3i\r\n" + 
		  "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36\r\n" + 
		  "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\n" + 
		  "Referer: http://172.18.13.106/upload.html\r\n" + 
		  "Accept-Encoding: gzip, deflate\r\n" + 
		  "Accept-Language: zh-CN,zh;q=0.9,en;q=0.8,fr;q=0.7,zh-TW;q=0.6,ca;q=0.5\r\n" + 
		  "\r\n" + 
		  "------WebKitFormBoundarydRcL8NeqnnjYDT3i\r\n" + 
		  "Content-Disposition: form-data; name=\"fileToUpload\"; filename=\"test-url.txt\"\r\n" + 
		  "Content-Type: text/plain\r\n" + 
		  "\r\n" + 
		  "\r\n" + 
		  "*in*org\r\n" + 
		  "n*.org\r\n" + 
		  "*n.co*\r\n" + 
		  "*ibm\r\n" + 
		  "\r\n" + 
		  "\r\n" + 
		  "\r\n" + 
		  "------WebKitFormBoundarydRcL8NeqnnjYDT3i\r\n" + 
		  "Content-Disposition: form-data; name=\"submit\"\r\n" + 
		  "\r\n" + 
		  "Upload File\r\n" + 
		  "------WebKitFormBoundarydRcL8NeqnnjYDT3i--\r\n" + 
//		  "HTTP/1.1 200 OK\r\n" + 
//		  "Server: nginx/1.13.8\r\n" + 
//		  "Date: Mon, 29 Oct 2018 23:14:42 GMT\r\n" + 
//		  "Content-Type: text/html; charset=UTF-8\r\n" + 
//		  "Transfer-Encoding: chunked\r\n" + 
//		  "Connection: keep-alive\r\n" + 
//		  "Content-Encoding: gzip\r\n" + 
//		  "\r\n" + 
//		  "The script being executed is /usr/local/nginx/html/upload.php\r\n" + 
//		  "-------------taget_file is test-url.txt\r\n" + 
//		  "-------------taget_file is Array\r\n" + 
//		  "-------------taget_file is /var/www/html/uploads/test-url.txt\r\n" + 
//		  "File is not an image.The file test-url.txt has been uploaded."
		  "\r\n"
		  ;
}
