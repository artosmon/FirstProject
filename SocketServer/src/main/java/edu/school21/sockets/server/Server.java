package edu.school21.sockets.server;

import com.beust.jcommander.Parameters;
import edu.school21.sockets.app.Main;
import edu.school21.sockets.models.Log;
import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.Room;
import edu.school21.sockets.repositories.*;
import edu.school21.sockets.services.LogService;
import edu.school21.sockets.services.RoomService;
import edu.school21.sockets.services.UsersService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;

@Component("server")
@Parameters(separators = "=")
public class Server extends Thread {
    private final  UsersRepository usersRepository;
    private final MessagesRepositoryImpl messageRepository;
    private final RoomsRepositoryImpl roomRepository;
    private final LogRepository logRepository;
    private final  UsersService usersService;
    private final  RoomService roomService;
    private final LogService logService;

    private Socket socket;
    private String userName;
    private BufferedReader in;
    private BufferedWriter out;

    private long isInRoom;

    public Server(@Qualifier("repo") UsersRepositoryImpl usersRepository,
                  @Qualifier("messageRepo") MessagesRepositoryImpl messageRepository,
                  @Qualifier("roomRepo") RoomsRepositoryImpl roomRepository,
                  @Qualifier("logRepo") LogRepository logRepository,
                  @Qualifier("service") UsersService usersService,
                  @Qualifier("roomService") RoomService roomService,
                  @Qualifier("logService") LogService logService) {

        this.usersRepository = usersRepository;
        this.messageRepository = messageRepository;
        this.roomRepository = roomRepository;
        this.logRepository = logRepository;
        this.usersService = usersService;
        this.roomService = roomService;
        this.logService = logService;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void run() {

                try {
                    isInRoom = 0;

                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    outputToClient("Hello from Server!");
                    System.out.println("User joined the chat with thread: " + Thread.currentThread());
                    printFirstMenu();

                    System.out.println("User logged out of the chat with thread: " + Thread.currentThread());
                    outputToClient("You have left the messenger.");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        socket.close();
                        out.close();
                        in.close();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

    }

    private void printFirstMenu() throws IOException {
        outputToClient("1. signIn");
        outputToClient("2. signUp");
        outputToClient("3. Exit");
        int command = inputCommand(3);
        if(command == 1 || command == 2)
            inputDataUserToMethod(command);
    }

    private void signUp(String name, String pass) throws IOException {

            try {
                usersService.signUp(name,pass);
            } catch (RuntimeException e) {
                outputToClient(e.getMessage());
                socket.close();
            }
        printRoomsMenu();
    }

    private void inputDataUserToMethod(int action) throws IOException {

        outputToClient("Enter username:");
        userName = inputFromClient();

        outputToClient("Enter password:");
        String pass = inputFromClient();

        if(action == 2)
            signUp(userName,pass);
        else
            signIn(userName,pass);
    }


    private void signIn(String name, String pass) throws IOException {

        if(!usersService.signIn(name,pass)) {
            outputToClient("Error,invalid username or password");
            socket.close();
        }
        else
            printRoomsMenu();
    }

    private void printRoomsMenu() throws IOException {
        outputToClient("1.\tCreate room");
        outputToClient("2.\tChoose room");
        outputToClient("3.\tExit");
        int command = inputCommand(3);
        switch (command) {
            case 1:
                createRoom();
                break;
            case 2:
                chooseRoom();
                break;
            case 3:
                printFirstMenu();

        }
    }

    private void createRoom() throws IOException {

        outputToClient("Enter room name");
        String roomName = inputFromClient();
        String bufferRoomName = roomName;
        int i = 1;
        while(roomService.checkExist(bufferRoomName)) {
            bufferRoomName = roomName.concat("("+i+")");
                i++;
        }
        roomName = bufferRoomName;

        Room room = new Room(roomName,
                usersRepository.findByName(userName).get(),
                null );

        roomRepository.save(room);

        messageInRoom(roomRepository.findByName(roomName).get());
    }

    private void chooseRoom() throws IOException {
        List<Room> rooms = roomRepository.findAll().get();
        outputToClient("Room:");
        int i = 1;
        if(roomRepository.findAll().isPresent()) {
            for (Room r : rooms) {
                outputToClient(i + ". " + r);
                i++;
            }
        }
        outputToClient(i + ". Exit");

        int command = inputCommand(i);

        if(command != i)
            messageInRoom(rooms.get(command-1));
        else
            printRoomsMenu();
    }

    private void messageInRoom(Room room) throws IOException {
        outputToClient(room + " ---");
        isInRoom = room.getId();

        getHistoryRoom(room);
        while(true) {
            String text = inputFromClient();

            if(text.equals("Exit")) {
                isInRoom = 0;
                outputToClient("You have left the chat.");
                chooseRoom();
                break;
            }


            messageRepository.save(new Message(
                    usersRepository.findByName(userName).get(),
                    room,
                    text,
                    LocalDateTime.now()));

            sendMessageInChat(text);

        }


    }



    private void sendMessageInChat(String message) {

        for(Server s : Main.getUsers()) {
            if(s.isInRoom == isInRoom) {
                s.outputToClient(userName + ": " + message);
            }

        }
    }




    private void getHistoryRoom(Room room) {


            List<Message> messages;

            if (messageRepository.findAll(room.getId()).isPresent()) {

                messages = messageRepository.findAll(room.getId()).get();
                Log log = logService.checkEnter(room.getId(), usersRepository.findByName(userName).get().getId());
                if (log != null) {

                    long i = messages.size() > 30 ? messages.size() - 30 : 0;
                    if(messages.size() - log.getMessageInd() < 30)
                        i=log.getMessageInd();

                    for (; i < messages.size(); i++) {
                        outputToClient(messages.get((int)i).toString());
                    }

                }
                else {
                    long index = messages.isEmpty() ? 0 : messages.size();
                    logRepository.save(new Log(usersRepository.findByName(userName).get().getId(),room.getId(), index));
                }

            }


    }

    private void outputToClient(String message) {
        try {
            JSONObject json = new JSONObject();
            json.put("message", message);
            out.write(json.toString() + "\n");
            out.flush();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String inputFromClient() {
        try {
            JSONObject json = new JSONObject(in.readLine());
            return json.getString("message");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private int inputCommand(int max) {
        int command = Integer.parseInt(inputFromClient());

        while (command > max || command <= 0) {
            outputToClient("try again");
            command = Integer.parseInt(inputFromClient());
        }
        return command;
    }
}
