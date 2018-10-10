import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class Server {
	Map<Communicator, PublicKey> keyRing;
	ServerSocket serverSocket;
	KeyPair rsaKeys;
	
	public Server() {
		rsaKeys = Utils.generateRSAKeys();
		keyRing = new HashMap<>();
	}
	
	public void startListening(int port) {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Server socket is listening port "+port);
			while(true) {
				Socket socket = serverSocket.accept();
				Communicator comm = new Communicator(socket);
				new Thread() {
					@Override
					public void run() {
						exchangePublicKeys(comm);
						readAndPrint(comm);
						try {
							socket.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void exchangePublicKeys(Communicator comm) {
		comm.write(rsaKeys.getPublic().getEncoded());
		byte[] serverPublicKeyData = comm.read();
		keyRing.put(comm, Utils.convertArrayToPubKey(serverPublicKeyData, "RSA"));
	}
	
	private void readAndPrint(Communicator comm) {
		try {
			byte[] data = comm.read();
			
			Thread.sleep((int)(Math.random()*1000));
			
			byte[] decrypted = Utils.decrypt(keyRing.get(comm), data);
			String message = new String(decrypted, StandardCharsets.UTF_8);
			System.out.println(Base64.encode(data));
			System.out.println(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			comm.close();
		}
	}

	public static void main(String[] args) {
		Server server = new Server();
		server.startListening(9000);
	}

}
