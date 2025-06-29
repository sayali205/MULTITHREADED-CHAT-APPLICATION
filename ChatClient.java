import java.io.*;
import java.net.*;
public class  ChatClient{
	private static final String SERVER_IP="127.0.0.1";
	private static final int SERVER_PORT=1234;
	public static void main(String ag[]){
		try(Socket socket=new Socket(SERVER_IP,SERVER_PORT);
			BufferedReader userIn=new BufferedReader(new InputStreamReader(System.in));
			BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out=new PrintWriter(socket.getOutputStream(),true)){
			new Thread(()->{
				String serverMessage;
				try {
					while((serverMessage=in.readLine())!=null){
						System.out.println("Server:"+serverMessage);
					}
				}catch(IOException e){
					System.out.println("Disconnected from Server");
				}
			}).start();
			String userMessage;
			while((userMessage=userIn.readLine())!=null){
				out.println(userMessage);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}