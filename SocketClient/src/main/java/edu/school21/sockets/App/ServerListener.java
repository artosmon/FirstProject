package edu.school21.sockets.App;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerListener extends Thread {

    private Socket socket;
    private BufferedReader in;
    public ServerListener(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            JSONObject json;
            String line = null;

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while(true) {
                 line = in.readLine();
                 if(line == null)
                     break;
                 json = new JSONObject(line);
                String message = json.getString("message");
                System.out.println(message);
            }
            System.exit(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                in.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
