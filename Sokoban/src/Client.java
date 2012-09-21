import java.io.*;
import java.net.Socket;
//import java.net.*;
//import java.util.Date;
import java.util.ArrayList;

import model.Agent;
import model.Map;

/**
 * Remote connection
 *
 */
public class Client {
	
        static BufferedReader br;
	
	public static void main(String[] pArgs) 
	{
		if(pArgs.length<3)
		{
			System.out.println("usage: java Client host port boardnum");
			return;
		}
	
		try
		{
			Socket lSocket=new Socket(pArgs[0],Integer.parseInt(pArgs[1]));
			PrintWriter lOut=new PrintWriter(lSocket.getOutputStream());
			BufferedReader lIn=new BufferedReader(new InputStreamReader(lSocket.getInputStream()));
	
            lOut.println(pArgs[2]);
            lOut.flush();

            String lLine=lIn.readLine();
            
            //read number of rows
            int lNumRows=Integer.parseInt(lLine);

            //read each row
            ArrayList<String> bufferToString = new ArrayList<String>();
            
            for(int i=0;i<lNumRows;i++)
            {
                lLine=lIn.readLine();
                bufferToString.add(lLine);
            }
            
                       // We store the Map in a DataStructure
            Agent agent = new Agent(bufferToString);

            //we've found our solution
            //Map map = new Map(bufferToString);
            //System.out.println(map);
            
            
            br = new BufferedReader(new FileReader("maps/path/map1.txt"));
            Map map = new Map(br);
            System.out.println(map);

            System.out.println("Sol: "+agent.solve(map));
            

            /*String lMySol = agent.solve(map);

            // send the solution to the server
            lOut.println(lMySol);
            lOut.flush();
    
            //read answer from the server
            lLine = lIn.readLine();
    
            System.out.println(lLine);*/
    
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
	}
}
