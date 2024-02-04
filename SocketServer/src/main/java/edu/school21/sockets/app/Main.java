package edu.school21.sockets.app;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import edu.school21.sockets.config.SocketsApplicationConfig;
import edu.school21.sockets.server.Server;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Main {
    private static Socket socket;
    private static int port;
    private static List<Server> users;
    private static ServerSocket server;
    public static List<Server> getUsers() {
        return users;
    }


    public static void main(String[] args) {

        if(args.length!=1 || !args[0].startsWith("--port=")) {
            throw new RuntimeException("Illegal argument");
        }
        port = Integer.parseInt(args[0].substring(7));

        users = new LinkedList<>();
        try {
            server = new ServerSocket(port);
            while(true) {
                ApplicationContext context = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);
                Server ser = context.getBean(Server.class);
                System.out.println("wait connection...");
                socket = server.accept();
                System.out.println("connection successful");
                ser.setSocket(socket);
                users.add(ser);
                ser.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                socket.close();
                server.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }
}
