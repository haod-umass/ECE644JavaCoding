import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server {
	ServerSocket serverSocket;
	
	public void startListening(int port) {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Server socket is listening port "+port);
			while(true) {
				Socket socket = serverSocket.accept();
				new Thread() {
					@Override
					public void run() {
						readAndPrintFromSocket(socket);
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
	
	private void readAndPrintFromSocket(Socket socket) {
		try {
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			byte[] data = null;
			int length = dis.readInt();
			data = new byte[length];
			dis.readFully(data);
			dis.close();
			socket.close();
			
			Thread.sleep((int)(Math.random()*1000));
			
			String message = new String(data, StandardCharsets.UTF_8);
			System.out.println(message);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Server server = new Server();
		server.startListening(9000);
	}

}
