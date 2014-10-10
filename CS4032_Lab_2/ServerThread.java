import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.Socket;

public class ServerThread extends Thread {

    protected Socket clientSocket = null;
    protected String serverText   = null;
	private int serverPort;
    protected DataInputStream is;
    protected PrintStream os;

    public ServerThread(Socket clientSocket, String serverText, int in_serverPort) {
        this.clientSocket = clientSocket;
        this.serverText = serverText;
		this.serverPort = in_serverPort;
    }

    public void run() {
        try {
        	
            InputStream input  = clientSocket.getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(input));
            
            OutputStream output = clientSocket.getOutputStream();
            
            is = new DataInputStream(clientSocket.getInputStream());
            os = new PrintStream(clientSocket.getOutputStream());
            
            String message = bf.readLine();
                    
			String response = respond(message);
			output.write(response.getBytes());
			output.close();
			input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	private String respond(String in_message){
		String message = in_message;
		String response = "";
			
		if(message != null){
			
			if( message.substring(0,4).equals("HELO")){
				
				String ip_address ="";
				try{
					InetAddress inetAddr = InetAddress.getLocalHost();          
					byte[] addr = inetAddr.getAddress();
					// Convert to dot representation
	
					for (int i = 0; i < addr.length; i++) {
						if (i > 0) {
							ip_address += ".";
						}
						ip_address += addr[i] & 0xFF;
					}
				}catch(UnknownHostException e) {
					System.out.println("Host could not be found: "+e.getMessage());
				}
	
				String port_number = Integer.toString(serverPort);
				String student_number = "10315411";
				response = message + "\nIP: "+ip_address+"\nPort: "+port_number+"\nStudentID: "+student_number;
			}
			else if(message.equals("KILL_SERVICE")){
				System.out.println("Service about to be killed!");
				System.exit(0);
			}
			else{
				System.out.println("Kill service ends up in the nowhere else.");
			}
		}
		else{
			response = "Sorry. I didn't understand you.";
		}
		return response;
	}
}