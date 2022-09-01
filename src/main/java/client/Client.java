package client;

import Database.SQLiteCommandDB;

import java.io.*;
import java.net.*;
import java.sql.Connection;

public class Client {
    public static String msgWaiting = "";

//Note: Client and server should be compiled separately.



    public void loadClient() throws IOException, InterruptedException {

        SQLiteCommandDB scdb = new SQLiteCommandDB();
        Connection cnn = new SQLiteCommandDB().connect();


        // create DatagramSocket and get ip
        DatagramSocket dataSocket = new DatagramSocket(5544);
        InetAddress ip = InetAddress.getLocalHost();

        //Note: The server IP will be different on another machine. So, this will need to be handled.

        System.out.println("The Client is ready...");


        Thread clientSender;
        clientSender = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try {
                    //  Scanner input = new Scanner(System.in);
                    while (true) {
                        synchronized (this)
                        {
                            byte[] sendingData = new byte[1000];

                            // pulling message from command line
                            //  sendingData = input.nextLine().getBytes();

                            if (!msgWaiting.isBlank()){
                                sendingData = msgWaiting.getBytes();
                                msgWaiting = "";


                                DatagramPacket sendingPacket = new DatagramPacket(sendingData, sendingData.length, ip,1122);

                                // send it!
                                dataSocket.send(sendingPacket);

                                String msg = new String(sendingData);
                                System.out.println("Client says: " + msg);
                                // End the session
                                if (msg.equals("quit")) {
                                    scdb.disconnect(cnn);
                                    System.out.println("Exiting the client... ");
                                    break;
                                }
                                System.out.println("Waiting for server response...");

                            }
                        }
                    }
                }
                catch (IOException e) {
                    System.out.println("Something went wrong in the client sender thread!");
                    System.out.println(e.getMessage());
                }
            }
        });

        // create a receiver thread with a nested
        // runnable class definition
        Thread clientReceiver;
        clientReceiver = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try {

                    while (true) {
                        synchronized (this)
                        {

                            byte[] receivingData = new byte[1000];

                            // receive new message
                            DatagramPacket receivingPacket = new DatagramPacket(receivingData, receivingData.length);
                            dataSocket.receive(receivingPacket);

                            // convert byte data to string
                            String msg = (new String(receivingData)).trim();
                            System.out.println("Server: " + msg);
                            if (msg.substring(0, 3).contains("cmd")){

                                String[] commandList = msg.split(" ");
                                CommandLineExecutor.executeProcess(cnn, commandList);
                            }

                            // exit condition
                            if (msg.equals("quit")) {
                                System.out.println("The server is offline....");
                                break;
                            }
                        }
                    }
                }
                catch (IOException e) {
                    System.out.println("Something went wrong in the client receiver thread!");
                    System.out.println(e.getMessage());
                }
            }
        });

        clientSender.start();
        clientReceiver.start();

        clientSender.join();
        clientReceiver.join();
    }
}