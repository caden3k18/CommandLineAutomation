package client; /**
 * This class exposes a means to automate certain windows functions and tasks.
 * I will keep adding to this for a bit and eventually set up
 * TCP socket server and clients apps to make use of these for remote administration.
 *
 * Note: It is likely that most/all of the things done with this project can also be
 * managed directly from powershell. I am using Java as a middleman mostly for demo/fun and
 * because I can add this to one of my main projects as an auxiliary app.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.sql.Connection;
import java.util.Scanner;


import Database.SQLiteCommandDB;
import Notifications.Emailer;

public class CommandLineExecutor {


    static String[] recipients = new String[1];

    static String results = "";

    public static void executeProcess(Connection cnn, String[] args) throws IOException {

        SQLiteCommandDB sdb = new SQLiteCommandDB(); //TODO: Implement commands through db search...


        //This is for sending admin alerts to a phone via email to SMS.
        recipients[0] = "10DigitPhoneNumber@YourCellularGateway.com";


        Process proc;

        results = "";

        /**
         * DEBUGGING COMMANDS
         */
        for (String s: args) {
            System.out.println(s);
        }

        /**
         * SQLite database implementation to house all the simple/dedicated command functions and add some
         * new options. If only 2 arguments are given, it's a simple command. Any command could be entered
         * into the DB as a simple command and called by it's tagName if the arguments are preset.
         */

        // System.out.println("cmd length : " + args.length);

        if(args.length == 2){ //It's a dedicated or 'simple' command. It should be in the DB
            //TODO: Add interactive prompt sequence to insert new entries + search/help functionality later...

            //For now, just pull the command directly and execute it.
            String cmd = sdb.translateCommand(cnn, args[1]);
            proc = Runtime.getRuntime().exec(cmd);
            //Show display the results that would normally show up in the prompt.
            showResults(proc);

        } else { //The command is dynamic, user came up with it in the moment.

            /**
             * Leaving the switch statement intact for dynamic commands, but it's time to start
             * moving static commands to a database. This will add flexibility
             * without the need to code and recompile.
             */
            switch (args[1].toLowerCase()) {

//------------------NETWORK-----------------------------------------------

                case "listcommands": //Return a list of all available commands in the DB
                    results = sdb.listCommands(cnn);
                    Client.msgWaiting = results;
                case "commandslike": //Return a list of all commands containing the provided parameter.
                    results = sdb.searchCommand(cnn, args[2]);
                    Client.msgWaiting = results;
                case "commandtranslation": //Return a list of all commands containing the provided parameter.
                    results = sdb.translateCommand(cnn, args[2]);
                    Client.msgWaiting = results;
                case "ping": //Specified ping - There is a default version in the DB that can also be set.
                    //Usage: ping www.google.com
                    proc = Runtime.getRuntime().exec(args[1] + " " + args[2]);
                    //Show display the results that would normally show up in the prompt.
                    showResults(proc);
                    break;
                case "createnetdrive": //Can set a default in the DB to make this easy.
                    //pass the path as a variable to map a network drive
                    proc = Runtime.getRuntime().exec("net use " + args[2]);
                    showResults(proc);
                    break;
                case "deletenetdrive":
                    //pass the path as a variable to delete a mapped network drive
                    proc = Runtime.getRuntime().exec("net use " + args[2] + " /delete");
                    showResults(proc);
                    break;

//----------------Health Check Trigger Concept--------------------------------------------
                case "healthcheck": //Example trigger, presently overridden by the DB
                    proc = Runtime.getRuntime().exec("DISM /Online /Cleanup-Image /ScanHealth");
                    showResults(proc);
                    if (!results.contains("No component store corruption detected")) {
                        //The scan shows a problem, so the repair process will be attempted automatically.
                        proc = Runtime.getRuntime().exec("DISM /Online /Cleanup-Image /RestoreHealth");
                        showResults(proc);
                        //Since a repair operation was triggered, we will add an alert to be sent to the admin's phone

                        Emailer.sendMail(recipients, "Alert!", "Health Check encountered an issue on " + InetAddress.getLocalHost().getHostName() +
                                ". There may be damage that cannot be repaired with /RestoreHealth");
                    }
                    break;


//-------------------Windows Management Interface Commands----------------

                /**
                 * Windows Management Interface Commands (WMIC) is used to extract device details
                 * about a given machine.
                 */

                case "removeprogram":
                    //Argument is the name of the program you wish to uninstall
                    proc = Runtime.getRuntime().exec("wmic product where name =" + args[2] + " call uninstall /nointeractive");
                    showResults(proc);
                    break;

                case "killprocess":
                    //Pass a filename to kill the corresponding process
                    proc = Runtime.getRuntime().exec("wmic process where name=\"" + args[2] + "\" call terminate");
                    //command + options of what details to display regarding hard drive partitions
                    showResults(proc);
                    break;


//-------------------POWER SHELL------------------------------------------

                case "custom":
                    /**
                     * Example parameters: {"powershell.exe", "-Command", "dir"};
                     * Can be any executable with any combination of parameters.
                     * Note: Not ready with this one yet!
                     **/

                    String[] commandList = new String[args.length - 1];
                    for (int i = 1; i < args.length - 1; i++) {
                        commandList[i - 1] = args[i];
                    }

                    ProcessBuilder pb = new ProcessBuilder(commandList);
                    proc = pb.start();
                    showResults(proc);

//---------------------SCRIPT EXECUTOR------------------------------------
                case "script":
                    /**
                     * Here, a call to execute a script can be made. In Windows, this will be a batch file (.bat) or powershell (.ps1).
                     * Linux typically uses a bash script.
                     * Example:
                     * "cmd /c start script1.bat"
                     * new File("C:\\scriptfolder\\"));
                     *  * Note: Not ready with this one yet!
                     */
                    proc = Runtime.getRuntime().exec(
                            args[2],
                            null,
                            new File(args[3]));
                    showResults(proc);
                    break;

            }

        }



    }

    /**
     *
     * @param process
     * @throws IOException
     * This method allows you to see the text output of what was executed.
     */
    public static void showResults(Process process)  {

        Scanner keyboard = new Scanner(System.in);

        try{

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            String temp = "";
            while ((line = reader.readLine()) != null) {
                temp = temp + (line) + "\n";
            }

            results = temp;
            Client.msgWaiting = temp;

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
