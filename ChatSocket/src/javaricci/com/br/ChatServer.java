package javaricci.com.br;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class ChatServer extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private static final int SERVER_PORT = 3377;
    private static final Logger LOGGER = Logger.getLogger(ChatServer.class.getName());

    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    // GUI Components
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea msgArea;
    private javax.swing.JButton msgSendButton;
    private javax.swing.JTextField msgTextField;

    public ChatServer() {
        initComponents();
        startServerInBackground();
    }

    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        msgArea = new javax.swing.JTextArea();
        msgTextField = new javax.swing.JTextField();
        msgSendButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Servidor");

        msgArea.setColumns(20);
        msgArea.setRows(5);
        msgArea.setEditable(false);
        jScrollPane1.setViewportView(msgArea);

        msgTextField.addActionListener(evt -> sendMessage());

        msgSendButton.setText("Enviar Server");
        msgSendButton.addActionListener(evt -> sendMessage());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(msgTextField))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(138, 138, 138)
                .addComponent(msgSendButton)
                .addContainerGap(180, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(msgTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(msgSendButton)
                .addContainerGap(114, Short.MAX_VALUE))
        );

        pack();
    }

    private void sendMessage() {
        try {
            String message = msgTextField.getText().trim();
            if (!message.isEmpty()) {
                dataOutputStream.writeUTF(message);
                msgArea.append("Server: " + message + "\n");
                msgTextField.setText("");
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error sending message", ex);
        }
    }

    private void startServerInBackground() {
        Thread serverThread = new Thread(this::startServer);
        serverThread.start();
    }

    private void startServer() {
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            LOGGER.info("Server started on port " + SERVER_PORT);
            socket = serverSocket.accept();
            LOGGER.info("Client connected");

            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            String incomingMessage;
            while (true) {
                incomingMessage = dataInputStream.readUTF();
                if (incomingMessage.equalsIgnoreCase("exit")) {
                    LOGGER.info("Client disconnected");
                    break;
                }
                msgArea.append("Client: " + incomingMessage + "\n");
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Server error", ex);
        } finally {
            closeConnections();
        }
    }

    private void closeConnections() {
        try {
            if (dataInputStream != null) {
                dataInputStream.close();
            }
            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error closing connections", ex);
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new ChatServer().setVisible(true));
    }
}

/*
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class ChatServer extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private static final int SERVER_PORT = 3377;
    private static final Logger LOGGER = Logger.getLogger(ChatServer.class.getName());

    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    // GUI Components
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea msgArea;
    private javax.swing.JButton msgSendButton;
    private javax.swing.JTextField msgTextField;

    public ChatServer() {
        initComponents();
        startServer();
    }

    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        msgArea = new javax.swing.JTextArea();
        msgTextField = new javax.swing.JTextField();
        msgSendButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Servidor");

        msgArea.setColumns(20);
        msgArea.setRows(5);
        msgArea.setEditable(false);
        jScrollPane1.setViewportView(msgArea);

        msgTextField.addActionListener(evt -> sendMessage());

        msgSendButton.setText("Enviar Server");
        msgSendButton.addActionListener(evt -> sendMessage());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(msgTextField))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(138, 138, 138)
                .addComponent(msgSendButton)
                .addContainerGap(180, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(msgTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(msgSendButton)
                .addContainerGap(114, Short.MAX_VALUE))
        );

        pack();
    }

    private void sendMessage() {
        try {
            String message = msgTextField.getText().trim();
            if (!message.isEmpty()) {
                dataOutputStream.writeUTF(message);
                msgArea.append("Server: " + message + "\n");
                msgTextField.setText("");
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error sending message", ex);
        }
    }

    private void startServer() {
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            LOGGER.info("Server started on port " + SERVER_PORT);
            socket = serverSocket.accept();
            LOGGER.info("Client connected");

            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            String incomingMessage;
            while (true) {
                incomingMessage = dataInputStream.readUTF();
                if (incomingMessage.equalsIgnoreCase("exit")) {
                    LOGGER.info("Client disconnected");
                    break;
                }
                msgArea.append("Client: " + incomingMessage + "\n");
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Server error", ex);
        } finally {
            closeConnections();
        }
    }

    private void closeConnections() {
        try {
            if (dataInputStream != null) {
                dataInputStream.close();
            }
            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error closing connections", ex);
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new ChatServer().setVisible(true));
    }
}
*/