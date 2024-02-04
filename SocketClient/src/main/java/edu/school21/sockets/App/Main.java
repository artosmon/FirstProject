package edu.school21.sockets.App;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

@Parameters(separators = "=")
public class Main {
    @Parameter(names = "--server-port")
    private static int port;
    private static BufferedWriter writer;
    private static BufferedReader reader;
    private static Scanner sc;
    private static Socket socket;

    private static void  printInfo() {

        try {
            JSONObject json = new JSONObject();
            json.put("message", sc.nextLine());
            writer.write(json.toString() + "\n");
            writer.flush();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args){


        if (args.length != 1 || !args[0].startsWith("--server-port=")) {
            throw new RuntimeException("Illegal argument");
        }

        JCommander.newBuilder().addObject(new Main()).build().parse(args);

         sc = new Scanner(System.in);

         try {

             socket = new Socket("localhost", port);
             writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             ServerListener serverListener = new ServerListener(socket);
             serverListener.start();
             while(serverListener.isAlive()){
                 printInfo();
             }

         } catch (IOException e) {
             throw new RuntimeException(e);
         } finally {
             try {
                 socket.close();
                 writer.close();
             } catch (IOException e) {
                 throw new RuntimeException(e);
             }

         }



    }
}

