import javax.swing.JFrame;

public class ClientStart {
	public static void main(String[] args) {
		Client start = new Client("127.0.0.1");
		start.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		start.startRunning();
		start.setResizable(false);
	}
}
