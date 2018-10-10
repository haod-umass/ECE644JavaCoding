import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class Client {
	public void send(String message) {
		try {
			InetAddress address = InetAddress.getByName("127.0.0.1");
			Socket socket = new Socket(address, 9000);
			if(socket.isConnected()) {
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				byte[] data = message.getBytes(StandardCharsets.UTF_8);
				dos.writeInt(data.length);
				dos.write(data);
				socket.close();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Client client = new Client();
		client.send("Hello from client");
	}
}
