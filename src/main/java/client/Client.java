package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client {

//Note: This is intended for a stand alone client application



    public static void main(String args[])
            throws IOException, InterruptedException
    {

        // create DatagramSocket and get ip
        DatagramSocket dataSocket = new DatagramSocket(5544);
        InetAddress ip = InetAddress.getLocalHost();

        //Note: The server IP will be different on another machine. So, this will need to be handled.

        System.out.println("The Client is up...");

        Thread clientSender;
        clientSender = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try {
                    Scanner input = new Scanner(System.in);
                    while (true) {
                        synchronized (this)
                        {
                            byte[] sendingData = new byte[1000];

                            // pulling message from command line
                            sendingData = input.nextLine().getBytes();



                            DatagramPacket sendingPacket = new DatagramPacket(sendingData, sendingData.length, ip,1122);

                            // send it!
                            dataSocket.send(sendingPacket);

                            String msg = new String(sendingData);
                            System.out.println("Client says: " + msg);
                            // End the session
                            if (msg.equals("quit")) {
                                System.out.println("Exiting the client... ");
                                break;
                            }
                            System.out.println("Waiting for server response...");
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
                                CommandLineExecutor.executeProcess(commandList);
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