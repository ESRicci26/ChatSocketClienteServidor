package javaricci.com.br;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class ChatClient extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 3377;

    private static Socket socket;
    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;

    private JTextArea msgArea;
    private JTextField msgText;
    private JButton msgSend;

    public ChatClient() {
        initComponents();
        initializeSocketConnection();
        startMessageListener();
    }

    private void initComponents() {
        JScrollPane jScrollPane = new JScrollPane();
        msgArea = new JTextArea();
        msgText = new JTextField();
        msgSend = new JButton("Enviar Client");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cliente");

        msgArea.setColumns(20);
        msgArea.setRows(5);
        msgArea.setEditable(false);
        jScrollPane.setViewportView(msgArea);

        msgSend.addActionListener(evt -> sendMessage());

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane, GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                        .addComponent(msgText))
                    .addContainerGap())
                .addGroup(layout.createSequentialGroup()
                    .addGap(150, 150, 150)
                    .addComponent(msgSend)
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(msgText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(msgSend)
                    .addContainerGap(50, Short.MAX_VALUE))
        );

        pack();
    }

    private void initializeSocketConnection() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Erro ao conectar ao servidor", ex);
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao servidor.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void startMessageListener() {
        new Thread(() -> {
            try {
                String msgIn;
                while ((msgIn = dataInputStream.readUTF()) != null) {
                    if ("exit".equalsIgnoreCase(msgIn)) {
                        break;
                    }
                    msgArea.append("\nServidor: " + msgIn);
                }
            } catch (IOException ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Erro ao receber mensagem", ex);
            } finally {
                closeConnections();
            }
        }).start();
    }

    private void sendMessage() {
        try {
            String msgOut = msgText.getText().trim();
            if (!msgOut.isEmpty()) {
                dataOutputStream.writeUTF(msgOut);
                msgText.setText("");
            }
        } catch (IOException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Erro ao enviar mensagem", ex);
        }
    }

    private void closeConnections() {
        try {
            if (dataInputStream != null) dataInputStream.close();
            if (dataOutputStream != null) dataOutputStream.close();
            if (socket != null) socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Erro ao fechar conexões", ex);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChatClient().setVisible(true));
    }
}
