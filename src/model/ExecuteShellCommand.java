package model;

import java.io.*;

/**
 * This class executes shell command
 */
public class ExecuteShellCommand {
    public String executeCommand(String command) {

        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }
        }
	catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();

    }

    public boolean appendToEndOfFile(String file, String contents) {
	try{
 	FileWriter fstream = new FileWriter(file, true);
	BufferedWriter out = new BufferedWriter(fstream);
	out.write(contents);
	out.close();
	}
	catch(Exception e) {
	System.err.println(e.getMessage());
	}
	return true;
    }
}
