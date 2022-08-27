/**
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

public class CommandLineExecutor {




    public void executeProcess(String[] args) throws IOException {
        Process proc;

        switch (args[0].toLowerCase()){

//------------------NETWORK-----------------------------------------------

            case "ping":
                /**
                 * Ex: ping www.purple.com
                 * Can be used to check if a particular server is running by supplying the IP address
                 * Also good to see if the client computer has a working internet connection
                 * A ping to a website you know is running will fail if the computer performing the ping is offline
                 */
                proc = Runtime.getRuntime().exec(args[1]);
                //Show display the results that would normally show up in the prompt.
                showResults(proc);
                break;
            case "createnetdrive":
                //pass the path as a variable to map a network drive
                proc = Runtime.getRuntime().exec("net use " + args[1]);
                showResults(proc);
                break;
            case "deletenetdrive":
                //pass the path as a variable to delete a mapped network drive
                proc = Runtime.getRuntime().exec("net use " + args[1] + " /delete");
                showResults(proc);
                break;
            case "getmacaddress":
                proc = Runtime.getRuntime().exec("GETMAC /s localhost");
                showResults(proc);
                break;
//----------------SYSTEM INFO---------------------------------------------

            case "systeminfo":
                //If I use this one, I will parse it out and store the data for client config
                proc = Runtime.getRuntime().exec("systeminfo");
                showResults(proc);
                break;
//----------------WINDOWS STATE-------------------------------------------
            /**
             * Windows State functions are mostly self explanatory.
             * Any advanced functions will require the jar file have administrative privileges
             */

            case "hibernate":
                proc = Runtime.getRuntime().exec("shutdown.exe /h");
                showResults(proc);
                break;

            case "reboot":
                proc = Runtime.getRuntime().exec("shutdown.exe /r");
                showResults(proc);
                break;

            case "shutdown":
                proc = Runtime.getRuntime().exec("shutdown.exe /s");
                showResults(proc);
                break;
//-------------------Windows Management Interface Commands----------------

            /**
             * Windows Management Interface Commands (WMIC) is used to extract device details
             * about a given machine.
             */

            case "getserialnumber":
                //Get the serial number of this machine
                proc = Runtime.getRuntime().exec("wmic bios get serialnumber");
                showResults(proc);
                break;
            case "installedprograms":
                //Get a list of programs you can uninstall - Very handy if we want to run a decrapifier batch script later
                proc = Runtime.getRuntime().exec("wmic product get name");
                showResults(proc);
                break;
            case "removeprogram":
                //Argument is the name of the program you wish to uninstall
                proc = Runtime.getRuntime().exec("wmic product where name =\"" + args[1] + "\" call uninstall /nointeractive");
                showResults(proc);
                break;
            case "getcpudetails":
                //Get the processing power details of this machine
                proc = Runtime.getRuntime().exec("wmic cpu get caption, name, deviceid, numberofcores, maxclockspeed, status");
                showResults(proc);
                break;
            case "getcorecount":
                //Before sending a processing load to a given computer, it may be useful balance processes vs cores.
                proc = Runtime.getRuntime().exec("wmic cpu get numberofcores");
                showResults(proc);
                break;
            case "gettotalmemory":
                proc = Runtime.getRuntime().exec("wmic computersystem get totalphysicalmemory");
                //Divide result by  8 to convert bits to bytes
                showResults(proc);
                break;
            case "getpartitiondetails":
                proc = Runtime.getRuntime().exec("wmic partition get name,size,type");
                //command + options of what details to display regarding hard drive partitions
                showResults(proc);
                break;

            case "killprocess":
                //Pass a filename to kill the corresponding process
                proc = Runtime.getRuntime().exec("wmic process where name=\"" + args[1] + "\" call terminate");
                //command + options of what details to display regarding hard drive partitions
                showResults(proc);
                break;


//-------------------POWER SHELL------------------------------------------

            case "custom":
                /**
                 * Example parameters: {"powershell.exe", "-Command", "dir"};
                 * Can be any executable with any combination of parameters.
                **/

                String[] commandList = new String[4];
                for (int i =1; i < args.length -1; i++){
                    commandList[i -1] = args[i];
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
                 */
                proc = Runtime.getRuntime().exec(
                        args[1],
                        null,
                        new File(args[2]));
                showResults(proc);
                break;

        }



    }

    /**
     *
     * @param process
     * @throws IOException
     * This method allows you to see the text output of what was executed.
     */
    public static void showResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }

}
