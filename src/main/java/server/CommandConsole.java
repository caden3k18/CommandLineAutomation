package server; /**
 * To send a msg to a specific client, you need the clients computer name.
 * Format example: send DESKTOP-TGH hello
 * To send a command to be executed on the client, use cmd instead of send
 * Format example: cmd DESKTOP-TGH ping www.google.com
 */


import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandConsole {



    public static void main(String[] args){

        Server svr = new Server();


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    svr.loadServer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


        Scanner input = new Scanner(System.in);
        String msg = "";

        while(true) {
            msg = input.nextLine();
            //clientRegistry

            final Matcher m = Pattern.compile("(get client list|send|cmd|broadcast)").matcher(msg);

            if (m.find())
                switch (m.group()) {
                       //Knowing which clients are connected will be necessary to send specific instructions
                    case "get client list":
                        for(String clientRegistry : svr.clientRegistry){
                            System.out.println(clientRegistry);
                        }
                        break;
                    case "send":
                        //This will require the specific client details
                        for(String clientRegistry : svr.clientRegistry){
                            String parcel[] = clientRegistry.split("~");
                            if(msg.contains(parcel[0])){

                                msg = msg.replace("send ", "").replace(parcel[0], "")
                                        + "~" + parcel[0] + "~" + parcel[1];
                                svr.msgWaiting = msg;
                            }
                        }
                    case "cmd":
                        //This will require the specific client details
                        for(String clientRegistry : svr.clientRegistry){
                            String parcel[] = clientRegistry.split("~");
                            if(msg.contains(parcel[0])){

                                msg = msg.replace(" " + parcel[0], "")
                                        + "~" + parcel[0] + "~" + parcel[1];
                                svr.msgWaiting = msg;
                            }
                        }
                    case "broadcast":
                        //This will require the specific client details
                        for(String clientRegistry : svr.clientRegistry){
                            String parcel[] = clientRegistry.split("~");
                                msg = msg.replace("send ", "").replace(" " + parcel[0], "")
                                        + "~" + parcel[0] + "~" + parcel[1];
                                svr.msgWaiting = msg;
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        //case broadcast:
                        break;

                    default:

                }

        }


    }




}
