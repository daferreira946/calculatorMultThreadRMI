package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;

public class Client extends JFrame implements ActionListener, KeyListener {

    private final JTextArea text;
    private final JTextField textMessage;
    private final JButton buttonSend;
    private final JButton buttonQuit;
    private Socket socket;
    private OutputStream outputStream;
    private Writer outputStreamWriter;
    private BufferedWriter bufferedWriter;

    public Client() {

        JPanel panelContent = new JPanel();

        text = new JTextArea(10,20);
        text.setEditable(false);
        text.setBackground(new Color(240, 240,240));

        textMessage = new JTextField(20);

        JLabel labelResult = new JLabel("Resultado");
        JLabel labelExpression = new JLabel("Expressão");

        JTextArea options = new JTextArea();
        options.setEditable(false);
        options.setBackground(new Color(240, 240 ,240));
        options.setText("""
                Para operações básicas digite: N1 +|-|*|/ N2
                Para operações especiais digite: N1 %|^|# N2
                Sempre separado por espaços: N1 op N2
                Usar formato de decimal com . (Ex.: 2.58)
                Só uma operação por vez
                --------------------------------------------
                Operações disponíveis:
                + = Soma N1 com N2
                - = Subtrai N2 de N1
                * = Multiplica N1 e N2
                / = Divide N1 por N2
                % = N1 por cento de N2
                ^ = N1 elevado por N2
                # = N1 índice da raiz de N2""");

        buttonSend = new JButton("Enviar");
        buttonSend.setToolTipText("Enviar expressão");
        buttonQuit = new JButton("Sair");
        buttonQuit.setToolTipText("Sair da calculadora");

        buttonSend.addActionListener(this);
        buttonQuit.addActionListener(this);
        buttonSend.addKeyListener(this);
        textMessage.addKeyListener(this);

        JScrollPane scrollPane = new JScrollPane(text);
        text.setLineWrap(true);

        panelContent.add(labelResult);
        panelContent.add(scrollPane);
        panelContent.add(labelExpression);
        panelContent.add(textMessage);
        panelContent.add(buttonSend);
        panelContent.add(buttonQuit);
        panelContent.add(options);

        panelContent.setBackground(Color.LIGHT_GRAY);
        text.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
        textMessage.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));

        setContentPane(panelContent);
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(350,500);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) throws IOException{

        Client client = new Client();
        client.connect();
        client.listener();

    }

    public void connect() throws IOException{

        socket = new Socket("localhost", 2000);
        outputStream = socket.getOutputStream();
        outputStreamWriter = new OutputStreamWriter(outputStream);
        bufferedWriter = new BufferedWriter(outputStreamWriter);
        bufferedWriter.flush();

    }

    private void send(String expression) throws IOException {

        System.out.println("Enviando...");

        text.append(expression + "\r\n");

        bufferedWriter.write(expression + "\r\n");
        bufferedWriter.flush();

        textMessage.setText("");
    }

    public void listener() throws IOException {

        InputStream inputStream = socket.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String message = "";

        System.out.println("Ouvindo...");

        while (!"Sair".equalsIgnoreCase(message)){

            if (bufferedReader.ready()) {

                System.out.println("Pronto...");

                message = bufferedReader.readLine();

                System.out.println("Mensagem: " + message);

                if (message.equalsIgnoreCase("sair")) {

                    text.append("Servidor caiu... \r\n");

                } else {

                    text.append(message + "\r\n");

                }
            }

        }

    }

    public void quit() throws IOException {
        text.append("Desconectado");
        bufferedWriter.close();
        outputStreamWriter.close();
        outputStream.close();
        socket.close();
        System.exit(0);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            if (e.getActionCommand().equals(buttonSend.getActionCommand())) {
                send(textMessage.getText());
            } else {
                if (e.getActionCommand().equals(buttonQuit.getActionCommand())) {
                    quit();
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                send(textMessage.getText());
            }catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
