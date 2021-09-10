package org.academiadecodigo.maindalorians;

import java.io.*;
import java.net.Socket;

public class ClientConnection implements Runnable {

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private Thread threadInput;
    private ServerChat serverChat;
    private String username;

    public ClientConnection(Socket clientSocket, ServerChat serverChat) throws IOException {

        this.serverChat = serverChat;
        socket = clientSocket;
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);
        output.println("Hi!");
        output.println("What's your username?");
        username = input.readLine();
        threadInput = new Thread(this);
        System.out.println("Welcome " + username + ", you just connected.");
        output.println("/help - for commands.");
    }

    public void receiveMessage(String message) {

        output.println(message);
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void run() {

        while (true) {

            try {
                String line = input.readLine();

                if (line.equals("/quit")) {

                    serverChat.broadcast(username + " you just disconnected.");
                    serverChat.removeClient(this);

                    socket.close();
                    return;
                }

                if (line.equals("/users")) {

                    output.println(serverChat.usernameList());
                    continue;

                }

                if (line.equals("/username")) {

                    output.println("What's your new username?");
                    username = input.readLine();
                    output.println("Your new username is: " + username + ".");
                    continue;

                }
                if (line.equals("/help")) {

                    output.println("/username - Changes your username.");
                    output.println("/users - Shows users online.");
                    output.println("/quit - Disconnects you from the chat.");
                }

                serverChat.broadcast(username + ": " + line);

            } catch (IOException exception) {

                System.out.println("Receiving error: " + exception.getMessage());
            }
        }
    }
}
