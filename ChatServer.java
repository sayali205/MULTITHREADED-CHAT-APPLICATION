import java.io.*;
import java.net.*;
import java.util.*;
public class ChatServer{
	private static final int PORT=1234;
	private static Set<ClientHandler>clients=new HashSet<>();
	public static void main(String arg[]){
		System.out.println("Server started:"+PORT);
		try(ServerSocket serverSocket=new ServerSocket(PORT)){
			while(true){
				Socket socket=serverSocket.accept();
				System.out.println("New Client is connected:"+socket);
				ClientHandler clienthandler=new ClientHandler(socket);
				clients.add(clienthandler);
				new Thread(clienthandler).start();
			}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		static void broadcast(String message,ClientHandler sender){
			for(ClientHandler client:clients){
				if(client!=sender){
					client.sendMessage(message);
				}
			}
		}
		static void removeClient(ClientHandler clienthandler){
			clients.remove(clienthandler);
		}
		static class ClientHandler implements Runnable{
			private Socket socket;
			private BufferedReader in;
			private PrintWriter out;
			public ClientHandler(Socket socket){
				this.socket=socket;
				try{
					in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
					out=new PrintWriter(socket.getOutputStream(),true);
					out.println("Welcome to chat!");
				}catch(IOException e){
					e.printStackTrace();
				}
				}
				public void sendMessage(String message){
					out.println(message);
				}
				public void run(){
					String message;
					try{
						while((message=in.readLine())!=null){
							System.out.println("Received:"+message);
							ChatServer.broadcast(message,this);
						}
					}catch(IOException e){
						System.out.println("Client is Disconnected:"+socket);
					}finally{
						try{
							socket.close();
						}catch(IOException e){}
						ChatServer.removeClient(this);
					}
				}
			}
		}
		
	
