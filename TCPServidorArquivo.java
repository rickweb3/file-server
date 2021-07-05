import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

class Connection implements Runnable {
	
	private Socket connectionSocket;
	private String diretorio;
	
	public Connection(Socket newSocket, String diretorio) {
		connectionSocket = newSocket;
		this.diretorio = diretorio;
	}
	
	
	@Override
	public void run() {
		
		try {
			
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
			
			System.out.println("Lendo arquivo");
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


public class TCPServidorArquivo {
	
	public static void main(String args[]) throws Exception {
		
		
		// Porta do Servidor Principal
		int porta = 3334;
		
		
		System.out.println("TCP Servidor Arquivo\n");
		
		
		// Assim que o UDPServidor Arquivo inicia já peço o Diretório padrão dele
		Scanner ler = new Scanner(System.in);
		System.out.print("\nInforme o endereço padrão do diretório de arquivos: ");
		String diretorio = ler.next();
		ler.close();
		
		
		// ServerSocket representa um socket TCP
		// Abre uma porta TCP - 3334
		try (ServerSocket arqSocket = new ServerSocket(porta)) { 
			
			while (true) {
				
				Thread c = new Thread(new Connection(arqSocket.accept(), diretorio));
				c.start();
				
			}
			
		}
	}
}

