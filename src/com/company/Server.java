package com.company;

import com.company.interfaces.BasicCalculator;
import com.company.interfaces.SpecialCalculator;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;

public class Server extends Thread {

    private final Socket socket;
    private BufferedReader bufferedReader;
    private BasicCalculator basicCalculator;
    private SpecialCalculator specialCalculator;

    public Server(Socket socket) {
        this.socket = socket;
        try {
            this.basicCalculator = (BasicCalculator) Naming.lookup("rmi://localhost:3000/Basic");
            this.specialCalculator = (SpecialCalculator) Naming.lookup("rmi://localhost:4000/Special");
            InputStream inputStream = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
        } catch (IOException | NotBoundException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {

            int port = 2000;
            ServerSocket serverSocket = new ServerSocket(port);

            JOptionPane.showMessageDialog(null, "Server pronto");

            while(true){
                System.out.println("Aguardando conexão...");
                Socket connection = serverSocket.accept();
                System.out.println("Cliente conectado...");
                Thread thread = new Server(connection);
                thread.start();
            }

        } catch (IOException exception) {
            JOptionPane.showMessageDialog(null, "Server falhou: " + exception.getMessage());
        }

    }

    public void run() {

        try{
            OutputStream outputStream =  this.socket.getOutputStream();
            Writer outputStreamWriter = new OutputStreamWriter(outputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            String expression;
            String result;

            System.out.println("Rodando...");

            while(true)
            {
                expression = bufferedReader.readLine();
                System.out.println("Expressão: " + expression);
                result = distributor(expression);
                System.out.println("Retornando -> " + result);
                bufferedWriter.write(result + "\r\n");
                bufferedWriter.flush();
            }

        }catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    private String distributor(String expression) throws RemoteException {
        if (expression.contains("%") | expression.contains("^") | expression.contains("#")) {
            System.out.println("Calculadora especial...");
            return calcSpecial(expression);
        } else if (expression.contains("+") | expression.contains("-") | expression.contains("/") | expression.contains("*")){
            System.out.println("Calculadora básica...");
            return calBasic(expression);
        }

        return "Operador inválido";
    }

    private String calBasic(String expression) throws RemoteException {

        if (!expression.contains(" ")) {
            return "Erro de sintáxe";
        }

        expression = expression.trim();

        String[] elements = expression.split(" ");

        if (Arrays.stream(elements).count() < 3 | Arrays.stream(elements).count() > 3) {
            return "Elementos de mais ou de menos";
        }

        if (!elements[0].matches("^\\d+(\\.\\d+)?")) {
            return "Primeiro operando não é double";
        }

        double numberOne = Double.parseDouble(elements[0]);

        if (!elements[2].matches("^\\d+(\\.\\d+)?")) {
            return "Segundo operando não é double";
        }

        double numberTwo = Double.parseDouble(elements[2]);

        if (elements[1].equals("+")) {
            return "Resultado: " + this.basicCalculator.plus(numberOne, numberTwo);
        }

        if (elements[1].equals("-")) {
            return "Resultado: " + this.basicCalculator.minus(numberOne, numberTwo);
        }

        if (elements[1].equals("/")) {
            return "Resultado: " + this.basicCalculator.div(numberOne, numberTwo);
        }

        if (elements[1].equals("*")) {
            return "Resultado: " + this.basicCalculator.multi(numberOne, numberTwo);
        }

        return "Erro desconhecido";
    }

    private String calcSpecial(String expression) throws RemoteException {

        if (!expression.contains(" ")) {
            return "Erro de sintáxe";
        }

        expression = expression.trim();

        String[] elements = expression.split(" ");

        if (Arrays.stream(elements).count() < 3 | Arrays.stream(elements).count() > 3) {
            return "Elementos de mais ou de menos";
        }

        if (!elements[0].matches("^\\d+(\\.\\d+)?")) {
            return "Primeiro operando não é double";
        }

        double numberOne = Double.parseDouble(elements[0]);

        if (!elements[2].matches("^\\d+(\\.\\d+)?")) {
            return "Segundo operando não é double";
        }

        double numberTwo = Double.parseDouble(elements[2]);

        if (elements[1].equals("%")) {
            return "Resultado: " + this.specialCalculator.perCent(numberOne, numberTwo);
        }

        if (elements[1].equals("^")) {
            return "Resultado: " + this.specialCalculator.elevation(numberOne, numberTwo);
        }

        if (elements[1].equals("#")) {
            return "Resultado: " + this.specialCalculator.root(numberOne, numberTwo);
        }

        return "Erro desconhecido";
    }
}
