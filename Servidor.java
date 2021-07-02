import java.io.*;
import java.net.*;

class Servidor {
	
	public static void main(String args[]) throws Exception {
		
		String clientSentence;
		String capitalizedSentence;
		
		System.out.println("Servidor\n");
		
		// Abre uma porta TCP 9876
		try ( ServerSocket welcomeSocket = new ServerSocket(9876)) { 
			
			while (true) {
				
				System.out.println("Aguardando mensagem...");
				Socket connectionSocket = welcomeSocket.accept();
				
				// Recebe mensagem do Cliente
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
				clientSentence = inFromClient.readLine();
				System.out.println("\nCliente " + connectionSocket.getInetAddress() + " - " + connectionSocket.getPort());
				System.out.println("Solicita arquivo: " + clientSentence + "\n");
				
				// Servidor Responde ao Cliente
				capitalizedSentence = clientSentence.toUpperCase() + '\n';				
				outToClient.writeBytes(capitalizedSentence);
				
			}
		}
	}
}
