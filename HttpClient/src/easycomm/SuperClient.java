package easycomm;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.regex.Pattern;

import  com.fortinet.devqa.*;
import  easycomm.data.*;
/**
 * 2018-10-27 Send payload 
 * 2018-02-25 Optimize the exception to overwrite the broken pipe: Split the
 * loopSend with SuperClientls 2018-02-24; a) adding loopsending inside thread;
 * b) add args support in cli c) add finite loop for create threads to workaroud
 * client socket (ip_v4_port_range) MAX_CONN
 *
 * 1) Test scalability by mutil-connections 2) Test Performance
 * 
 * @author ama
 *
 */
public class SuperClient implements Runnable {

	private static String IP = "172.18.13.106";
	private static int PORT = 8000;

	EditFromWireshark es = new EditFromWireshark();
	
	static int MAX_CONN = 1;
	static int INTERVAL = 100;  // in the span of the connection creation, control the speed (Milli-Sec)
	static int TTL = 1000 * 30; // The duration of one connection (Milli-Sec)

	

	SocketChannel socketChannel;

	private static int tid = 0; // thread_id

	private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	SuperClient(int threadId) {

		tid = threadId;

	}

	public static void main(String[] args) {

		if (args.length == 2) {
			IP = args[0];
			PORT = new Integer(args[1]);
	
		} else {
			Util.loger("Usage: {IP} {PORT} ");
			Util.loger("No IP and port defined, continue with: " + IP + " " + PORT);
		//	return;
		}

		Pattern pattern = Pattern.compile(IPADDRESS_PATTERN); // seems complex pattern should compile first;
		boolean validatedIP = pattern.matcher(IP).matches();
		// or another way
		validatedIP = Pattern.matches(IPADDRESS_PATTERN, IP);

		if (!validatedIP && !IP.equals("localhost")) {
			Util.loger("Invalidate IP format...");
			System.exit(-1);
		}

		for (int i = 0; i < MAX_CONN; i++) {
			SuperClient tc = new SuperClient(i);
			Thread t = new Thread(tc);
			t.setName("SuperClient-" + i);
			t.start();

			// Give interval for next threads in milli-seconds
			Util.setTimer(INTERVAL);
			Util.logd("Interval is:" + INTERVAL);
		}

	}

	void connect() throws UnknownHostException, IOException {

		if (null == IP || 0 == PORT) {
			Util.loger(" Need ip and/or port to connect");
			System.exit(-1);
			
		}

		socketChannel = SocketChannel.open();
		socketChannel.connect(new InetSocketAddress(IP, PORT));

		Util.loger("Connected to: \t" + IP);

	}

	@Override
	public void run() {

		String tName = Thread.currentThread().getName();
		Util.loger(tName + " started...");
	 
		
			try {
				this.connect();
				
//				 this.send(es.post_std_template);
				  
				this.send(es.POST_1());
						  
			
		
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		

	}

	void send(String msg) {

		try {
			int BUFFER_SIZE = msg.length();
			Util.loger("--->The packet to be sent is:\n\n" + msg);
			ByteBuffer writeBuffer = ByteBuffer.allocate(BUFFER_SIZE);
			ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER_SIZE);

			writeBuffer.put(msg.getBytes(), 0, BUFFER_SIZE);
			writeBuffer.flip();
	
			   long startPoint = System.currentTimeMillis();
				writeBuffer.rewind();
				socketChannel.write(writeBuffer);

				// check return
				readBuffer.clear();
				socketChannel.read(readBuffer);
				String response =  Util.byteBuffer2String(readBuffer);
				Util.logd("<----I got reply:\n\n" + response );
				
		
				
				if(response.indexOf("HTTP/1.1 100 Continue") != -1) {
					Util.logd("<----I got 100 Continue:\n\n" );								
					this.send(EditFromWireshark.REPLAY);
				   
				} else if(response.indexOf("HTTP/1.1 401 Unauthorized") != -1) {
					Util.logd("<----I got 401 Unauthorized \n" );
					this.send(es.POST_2());
					
				} else if(response.indexOf("HTTP/1.1 200 OK") != -1) {
					Util.logd("<----I got 200OK DONE! \n" );
				}
				
			
		} catch (IOException e) {
			Util.loger(e.getMessage());
		}
	}





}





