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
	
	private String diretorio;
	private int portaTCP;
	
	public ThreadTCPServidorArquivo(String diretorio, int portaTCP) {
		this.diretorio = diretorio;
		this.portaTCP = portaTCP;
	}
	
	
	@Override
	public void run() {
		
		
		try {
			
			// Dados referente a mensagem do cliente
			String nomeArquivo;			
			
			ServerSocket welcomeSocket = new ServerSocket(portaTCP);	
			welcomeSocket.setReuseAddress(true);
						

			while (true) {
				
				// Aceita conexão com o cliente
				Socket connectionSocket = welcomeSocket.accept();

				// Variável para escutar o Cliente
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				
				// Recebe mensagem do Cliente
				nomeArquivo = inFromClient.readLine();
				
				// Inicia Thread responsável por enviar o arquivo para o Cliente
				Thread c = new Thread(new ThreadTCPEnviaArquivo(connectionSocket, diretorio, portaTCP, nomeArquivo));
				c.start();
			}
			
		}
		
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
}




class ThreadTCPEnviaArquivo implements Runnable {

	Socket connectionSocket;
	private int porta;
	private String diretorio;
	private String nomeArquivo;

	
	public ThreadTCPEnviaArquivo(Socket connectionSocket, String diretorio, int porta, String nomeArquivo) {
		this.connectionSocket = connectionSocket;
		this.diretorio = diretorio;
		this.porta = porta;
		this.nomeArquivo = nomeArquivo;
	}
	
	
	@Override
	public void run() {
		
		try {
			
			// Variável para enviar para o Cliente
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

			
			// Inicia o processo de enviar o arquivo para o cliente
			File ptrArquivo = new File(diretorio, nomeArquivo);
			FileInputStream fis = new FileInputStream(ptrArquivo);
			DataOutputStream response = new DataOutputStream(connectionSocket.getOutputStream());
			byte[] arqBytes = new byte [(int) ptrArquivo.length()];
			
			
			// Lê o arquivo na variável FIS
			System.out.println("\nLendo arquivo: " + nomeArquivo);

			fis.read(arqBytes);
			fis.close();
			
			
			// Envia o arquivo para o Cliente

			System.out.println("Enviando...");
			response.write(arqBytes);
			
			
			// Fecha a conexão com o cliente, pois já recebi o arquivo
			System.out.println("Enviado!");

			connectionSocket.close();
			
		} catch (Exception e) {
			
		}
		
	}
	
}
