import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server {

	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(9000);
			System.out.println("Server socket is listening port "+9000);
			Socket socket = serverSocket.accept();
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			byte[] data = null;
			int length = dis.readInt();
			data = new byte[length];
			dis.readFully(data);
			dis.close();
			socket.close();
			serverSocket.close();
			
			String message = new String(data, StandardCharsets.UTF_8);
			System.out.println(message);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
