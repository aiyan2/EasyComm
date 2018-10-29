package easycomm;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * Java program to create a simple HTTP Server to demonstrate how to use
 * ServerSocket and Socket class.
 * 
 * @author Javin Paul
 */
public class SimpleHTTPServer {

    public static void main(String args[]) throws IOException {
        int port=8000;
        ServerSocket server = new ServerSocket(port);
        
        System.out.println("Listening for connection on port "+port+"..."+server.getInetAddress());
        while (true) {
            try (Socket socket = server.accept()) {
            	
            	String request;
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while ((request = br.readLine()) != null) {
                	Util.loger("I got the request:"+ request);
                }
            	
            	
                Date today = new Date();
                String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + today;
                socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
            }
        }
    }
    
    

}

