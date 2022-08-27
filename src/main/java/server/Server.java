package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedHashSet;
import java.util.Set;

public class Server {

    public Set<String> clientRegistry = new LinkedHashSet<>();

    String msgWaiting = "";

    public void loadServer() throws IOException, InterruptedException {

        // Create DatagramSocket and get the IP address
        DatagramSocket dataSocket = new DatagramSocket(1122);
        InetAddress ip = InetAddress.getLocalHost();

        System.out.println("Server is ready...");

        Thread ssend;
        ssend = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try {

                    while (true) {
                        synchronized (this) {
                            if (!msgWaiting.isBlank()) {
                                System.out.println("msgWaiting: " + msgWaiting);
                                byte[] sendData;

                                // scan new message to send


                                String parcel[] = msgWaiting.split("~");

                                sendData = parcel[0].getBytes();

                                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(parcel[1]), Integer.parseInt(parcel[2]));

                                // send the new packet
                                dataSocket.send(sendPacket);
                                msgWaiting = "";

                                String msg = new String(sendData);
                                System.out.println("Server says: " + msg);

                                // exit condition
                                if ((msg).equals("quit")) {
                                    System.out.println("Quitting... ");
                                    break;
                                }
                                System.out.println("Waiting for client response... ");
                            }
                        }
                    }
                }
                catch (Exception e) {
                    System.out.println("Something went wrong in the sender thread!");
                    System.out.println(e.getMessage());
                }
            }
        });

        Thread sreceive;
        sreceive = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try {
                    while (true) {
                        synchronized (this)
                        {

                            byte[] receiveData = new byte[1000];

                            // Receive new message
                            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                            dataSocket.receive(receivePacket);

                            // Convert byte data to string
                            String msg = (new String(receiveData)).trim();
                            System.out.println(receivePacket.getAddress().getHostName() + ":"  + " " + msg);
                            clientRegistry.add(receivePacket.getAddress().getHostName() + "~" + receivePacket.getPort());

                            // Exit condition
                            if (msg.equals("quit")) {
                                System.out.println("The client has left...");
                                break;
                            }
                        }
                    }
                }
                catch (Exception e) {
                    System.out.println("Something went wrong in the receiver thread!");
                    System.out.println(e.getMessage());
                }
            }
        });

        ssend.start();
        sreceive.start();

        ssend.join();
        sreceive.join();
    }
}