import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.Utilities;

public class Client extends JFrame {
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Socket connection;
	private String serverIP;
	private String message = "";
	private int coordenadas=12;

	public Client(String host) {
		super("MSN Pro v0.1  CLIENTE");
		serverIP = host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				sendMessage(event.getActionCommand());
				userText.setText("");
			}
		});
		add(userText, BorderLayout.SOUTH);
		chatWindow = new JTextArea();
		chatWindow.setEditable(false);
		add(new JScrollPane(chatWindow));
		setSize(300, 150);
		setVisible(true);
	}

	public void startRunning() {
		try {
			connectToServer();
			setupStreams();
			whileChatting();
		} catch (EOFException eofException) {
			showMessage("\nEl cliente finalizo la conexion");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			closeCrap();
		}
	}

	private void connectToServer() throws IOException {
		showMessage("Buscando conectarse al servidor..");
		connection = new Socket(InetAddress.getByName(serverIP), 6789);
		showMessage("\nConectado correctamente");
	}

	private void setupStreams() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\nLos streams estan establecidos");
	}

	private void whileChatting() throws IOException {
		ableToType(true);
		do {
			try {
				message = (String) input.readObject();
				showMessage("\n" + message);
			} catch (ClassNotFoundException classNotFoundException) {
				showMessage("\nNo se pudo recibir mensaje");
			}
		} while (!message.equals("Server: SALIR"));
	}

	private void closeCrap() {
		showMessage("\nFinalizando conexion");
		ableToType(false);
		try {
			input.close();
			output.close();
			connection.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	private void sendMessage(String message) {
		try {
			output.writeObject("Cliente: " + message);
			output.flush();
			showMessage("\nCliente: " + message);

		} catch (IOException ioException) {
			chatWindow.append("\nError al enviar mensaje");
		}
	}

	private void showMessage(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				chatWindow.append(text);
			}
		});
	}

	private void ableToType(final boolean tof) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				userText.setEditable(tof);
			}
		});
	}
}
