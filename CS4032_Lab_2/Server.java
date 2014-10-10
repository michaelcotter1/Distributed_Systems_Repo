
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends Thread{

    private int serverPort;
    private ServerSocket echoServer = null;    
    private boolean isStopped = false;
    private ExecutorService threadPool = Executors.newFixedThreadPool(10);
    
    public Server(int port){
        this.serverPort = port;
    }

	public void run(){
        try {
        	this.echoServer = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port.", e);
        }        
        
        while(! isStopped()){
            Socket clientSocket = null;
            try {
                clientSocket = this.echoServer.accept();                         
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.") ;
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }     
            this.threadPool.execute(new ServerThread(clientSocket, "Thread Pooled Server", serverPort));
        }
        this.threadPool.shutdown();
        System.out.println("Server Stopped.") ;
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }
	
	public static void main(String[] args){
	
		//Server server = new Server(9000);
		Server server = new Server(Integer.parseInt(args[0])); //args[0] carries 
		new Thread(server).start();
		Scanner scan = new Scanner(System.in);
				
		System.out.println("To stop server, enter: \"stop\".");
		String order = scan.nextLine();
		if(order.toLowerCase().equals("stop")){
			System.out.println("Stopping Server");
			//server.stop();
			System.exit(0);
		}				
	}
}