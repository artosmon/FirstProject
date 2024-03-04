# FirstProject
проект заточенный на использование Socket и многопоточности
схемы таблиц базы данных находится в ресурсах SocketServer
сервер запускается:
java -jar target/socket-server.jar --port=8081
клиент запускается(запуск клиентов не ограничен):
java -jar target/socket-client.jar --server-port=8081
Каждый клиент может:
1.Создать чат-комнату
2.Выбрать чат-комнату
3.Отправить сообщение в чат-комнату
4.Покинуть чат-комнату

пример:
Hello from Server!
1. signIn
2. SignUp
3. Exit
> 1
Enter username:
> Marsel
Enter password:
> qwerty007
1.	Create room
2.	Choose room
3.	Exit
> 2
Rooms:
1. First Room
2. SimpleRoom
3. JavaRoom
4. Exit
> 3
Java Room ---
JavaMan: Hello!
> Hello!
Marsel: Hello!
> Exit
You have left the chat.
