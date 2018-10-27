
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.regex.Pattern;

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
public class SuperClientls implements Runnable {

	private static String IP = "localhost";
	private static int PORT = 8000;

	static int MAX_CONN = 1;
	static int INTERVAL = 100; // in the span of the connection creation, control the speed (Milli-Sec)
	static int TTL = 1000 * 30; // The duration of one connection (Milli-Sec)

	

	SocketChannel socketChannel;

	private static int tid = 0; // thread_id

	private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	SuperClientls(int threadId) {

		tid = threadId;

	}

	public static void main(String[] args) {

		if (args.length == 2) {
			IP = args[0];
			PORT = new Integer(args[1]);
	
		} else {
			Util.loger(" Usage: {IP} {PORT} ");
			Util.loger("Current ip and port :" + IP + " " + PORT);

			return;
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
			SuperClientls tc = new SuperClientls(i);
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
				
				this.send(post_1());
				this.send(post_2());				

				Util.setTimer(TTL); // keep it here , not such important if loop Sending
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			


	}

	void send(String msg) {

		try {
			int BUFFER_SIZE = msg.length();
			Util.loger("The packet length to be sent is:" + msg.length());
			ByteBuffer writeBuffer = ByteBuffer.allocate(BUFFER_SIZE);
			ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER_SIZE);

			writeBuffer.put(msg.getBytes(), 0, BUFFER_SIZE);
			writeBuffer.flip();

			// while (true)
			{
				long startPoint = System.currentTimeMillis();
				writeBuffer.rewind();
				socketChannel.write(writeBuffer);

				// check return
				readBuffer.clear();
				socketChannel.read(readBuffer);
				Util.loger(startPoint, "Intervial_of_echo time");
				Util.logd(" I got reply:" + Util.byteBuffer2String(readBuffer));

			}
		} catch (IOException e) {
			Util.loger(e.getMessage());
		}
	}

String post_1 () {
	
	// from https://www.freeformatter.com/java-dotnet-escape.html#ad-output 
return "POST /upload.php HTTP/1.1\\r\\n "+ 
		"Host: 172.18.13.106:8000\\r\\n" + 
	//	"Authorization: Basic bmdpbng6MTIzNA==\\r\\n"+
		"User-Agent: curl/7.53.1\\r\\nAccept: */*\\r\\n"+
		"Content-Length: "+ payload.length() +"\\r\\n"
		+ "Content-Type: application/x-www-form-urlencoded\\r\\n"+
		"Expect: 100-continue\\r\\n\\r\\n"+
		"Content-Disposition: form-data; name=\\\"GetKeyCode\\\"\\r\\n\\r\\n"
		
		;
	
}

String post_2() {
	
 
	
	return  payload
			+"POST /upload.php HTTP/1.1\r\n"
			+ "Host: 172.18.13.106:8000\r\n"
			+ "Authorization: Basic bmdpbng6MTIzNA==\r\n"
			+ "User-Agent: curl/7.53.1\r\nAccept: */*\r\n"
			+ "Content-Length: 0\r\n"        // content length =0 as designed 
			+ "Content-Type: application/x-www-form-urlencoded\r\n"
			+ "Expect: 100-continue\r\n\r\n"
			+ "Content-Disposition: form-data; name=GetKeyCode"
			;
  }

static String payload = "13B22F4EC941C4D9148286B1F5213B3005E806D7A688FCE3585B62BCD013A9B87213B1"
		+ "E89EFA0EE115AF2B9F651A52FAB6470FEB45F5E4D0D8BD8777603179CF3BAF784A67"
		+ "70A4490E892DE2ECCD504B4013AB038DAE6600CDA0584E4029E4C13E591FA72F0849"
		+ "4D8D391029146A379B51C778E39525E41AC3AC6D56D79DAC4DD87AE150AB55BBC3B2"
		+ "1580AE6412CE639CEA39DFD3EA6AA92FCECA31599B1FDA2E1EFCF19305EB5868B356"
		+ "B5DEE337AC50291C16BC1758DF9C4EE4A5E6DE9CA2388EA12CD8BC3E2F595BE78E8D"
		+ "BCB75A54FB190AFBD81DE192C57A0A4B5F874DEC85CF743C217EB606BB21F7DE1FFC"
		+ "B93ED51CB791E13926A0F1B3E6AB45741D58A8EDF249EEE978FEB406EA1AA5ECF519"
		+ "5F2276D2C12D119BEE2351D69F43E1180816B90B45614C209691BBF9EFD96CBFAABD"
		+ "08B1D2E799F7E5658DB334830D95796523BFB1A694BDCA9336ACB105CB22AC048433"
		+ "EB053F4C06C854D4AC8552CD1CCA1ED448AB607C233BCE863A772B32DC463C05FC5C"
		+ "249AAB9AA2C1182C1F316492A08A93073A1305B96E3358CB8F5C1BA584611A48C8D59"
		+ "74E3F445936BC69BD89BAF25E06FDAD3B9566E7FF053FE1EF749D2C6DA8A800E4CD16"
		+ "FA6829DCF9AC6CF0D55DEE774AFBB616556F8E641E818AE83955657D5655FED5608BC"
		+ "4204B9A66E66C5000BE888C74745B9EEB5C099B0ACA22AF170F55D0E698B13F0C14"
		;
}
