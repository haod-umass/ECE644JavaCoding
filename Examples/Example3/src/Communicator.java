import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Communicator {
	private Socket socket;
	private DataInputStream dis;
	private DataOutputStream dos;

	public Communicator(Socket socket) throws IOException {
		if (socket != null && socket.isConnected()) {
			this.socket = socket;
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
		}
	}

	public void write(byte[] data) {
		try {
			if (dos != null) {
				dos.writeInt(data.length);
				dos.write(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] read() {
		byte[] data = null;
		try {
			if (dis != null) {
				int length = dis.readInt();
				data = new byte[length];
				dis.readFully(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public void close() {
		try {
			dis.close();
			dos.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
