import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PublicKey;

public class Client {
	private Communicator comm;
	private KeyPair rsaKeys;
	private PublicKey serverPublicKey;
	
	public Client() {
		rsaKeys = Utils.generateRSAKeys();
		try {
			InetAddress address = InetAddress.getByName("127.0.0.1");
			comm = new Communicator(new Socket(address, 9000));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(String message) {
		byte[] data = message.getBytes(StandardCharsets.UTF_8);
		byte[] encrypted = Utils.encrypt(rsaKeys.getPrivate(), data);
		comm.write(encrypted);
		comm.close();
	}
	
	public void exchangePublicKeys() {
		comm.write(rsaKeys.getPublic().getEncoded());
		byte[] serverPublicKeyData = comm.read();
		serverPublicKey = Utils.convertArrayToPubKey(serverPublicKeyData, "RSA");
	}
	
	public static void main(String[] args) {
		for(int i=0;i<100;i++) {
			Client client = new Client();
			client.exchangePublicKeys();
			client.send("Hello from client x"+(i+1));
		}
	}
}
