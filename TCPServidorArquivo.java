import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.Scanner;

class ThreadTCPServidorArquivo implements Runnable {
	
	private Socket connectionSocket;
	private String diretorio;
	private int portaTCP;
	
	public ThreadTCPServidorArquivo(Socket newSocket, String diretorio, int portaTCP) {
		connectionSocket = newSocket;
		this.diretorio = diretorio;
		this.portaTCP = portaTCP;
	}
	
	
	@Override
	public void run() {
		
		try {
			
			
			System.out.println("TCP Servidor Arquivo\n");
						
			
			// Dados referente a mensagem do cliente
			String clientSentence;
			String capitalizedSentence;
			
			
			
			// Crio as variáveis necessárias para escutar e enviar dados para o cliente
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			
			
			
			// Recebe mensagem do Cliente
			clientSentence = inFromClient.readLine();
			

			
			// Inicia o processo de enviar o arquivo para o cliente
			File ptrArquivo = new File(diretorio, clientSentence);
			FileInputStream fis = new FileInputStream(ptrArquivo);
			DataOutputStream response = new DataOutputStream(connectionSocket.getOutputStream());
			byte[] arqBytes = new byte [(int) ptrArquivo.length()];
			
			System.out.println("\nLendo arquivo: " + clientSentence);
			fis.read(arqBytes);
			fis.close();
			
			System.out.println("Enviando...");
			response.write(arqBytes);
			
			System.out.println("Enviado!");
			connectionSocket.close();
			
			
			// Arquivo enviado com sucesso, com isso fecha a conexão
			System.out.println("Conexão Finalizada");
			
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
}
