import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {


        CommandLineExecutor cle = new CommandLineExecutor();

        String[] commandList = new String[4]; //Accepts upto 3 command parameters

        //To activate any of these commands in CommandLineExecutor,
        //we just need the instance name and the command itself and applicable variables.
        //Examples:

        //PING
        commandList[0] = "ping";
        commandList[1] = "ping www.google.com";
        cle.executeProcess(commandList);

        //MAC ADDRESS
        commandList[0] = "getmacaddress";
        cle.executeProcess(commandList);


    }
}
