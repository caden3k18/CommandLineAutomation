package client;

import java.util.Scanner;

public class CommandRouting {


    public static void main(String[] args) {
        Client client = new Client();


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                      client.loadClient();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Scanner input = new Scanner(System.in);
        String msg = "";

        while(true) {
            msg = input.nextLine();
            client.msgWaiting = msg;


        }


    }

}
