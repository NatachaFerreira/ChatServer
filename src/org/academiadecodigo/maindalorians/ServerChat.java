package org.academiadecodigo.maindalorians;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class ServerChat {

    private Socket clientSocket;
    private ServerSocket serverSocket;
    private Queue<ClientConnection> queue;

    public ServerChat(int port) {

        try {

            queue = new LinkedList<>();

            System.out.println("Binding to port " + port);
            serverSocket = new ServerSocket(port);
            System.out.println("Server started: " + serverSocket);

            listen();

        } catch (IOException ioe) {

            System.out.println(ioe.getMessage());

        }

    }

    public void removeClient(ClientConnection client) {

        for (ClientConnection comparer: queue) {

            if (client == comparer) {

                queue.poll();

            }

        }

    }

    public void broadcast(String message) {

        for (ClientConnection client: queue) {

            client.receiveMessage(message);

        }

    }
    public void listen() throws IOException {

        while (true) {

            System.out.println("Waiting for a client connection");
            clientSocket = serverSocket.accept();

            queue.offer(new ClientConnection(clientSocket, this));

        }
    }

    public String usernameList() {

        StringBuilder users = new StringBuilder();

        for (ClientConnection client: queue) {

            users.append(client.getUsername() + ", ");

        }

        return users.toString();
    }

    public static void main(String[] args) {

        if (args.length == 0) {

            System.out.println("Usage: java Server [port]");
            System.exit(1);

        }

        try {

            new ServerChat(Integer.parseInt(args[0]));


        } catch (NumberFormatException exception) {

            System.out.println("Invalid port number " + args[0]);

        }
    }

}
