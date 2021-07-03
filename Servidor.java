import java.io.*;
import java.net.*;


class ThreadCliente implements Runnable {

	private Socket connectionSocket;
	
	
	public ThreadCliente(Socket newSocket) {
		connectionSocket = newSocket;
	}
	
	
	@Override
	public void run() {
		
		try {
			
			// Dados referente a mensagem do cliente
			String clientSentence;
			String capitalizedSentence;
			
			// IP do Servidor Principal para Broadcast
			String servidor = "localhost";
			InetAddress IPAddress = InetAddress.getByName(servidor);
			int porta = 9876;
			
			
			
			// Recebe mensagem do Cliente
			// Mensagem do Cliente está na variável clientSentence
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			clientSentence = inFromClient.readLine();
			System.out.println("\nCliente " + connectionSocket.getInetAddress() + " - " + connectionSocket.getPort());
			System.out.println("Solicita arquivo: " + clientSentence);
			
			
			
			// A variável sendData é para enviar o nome do arquivo via UDP para todos os UDPServidorArquivo
			byte[] sendData = new byte[1024];
			
			// A variável receiveData é para receber todas as respostas via UDP de todos os UDPServidorArquivo
			byte[] receiveData = new byte[1024];
			
			// Armazeno o nome do arquivo enviado pelo Cliente em sendData
			sendData = clientSentence.getBytes();
			
			
			
// -------------------------------------------------------------------------------------------------------------------------------				
			
			// Assim que o Servidor Principal recebe a solicitação do cliente
			// É enviada uma mensagem brodacast para todos os UDPServidorArquivo
			// Eles tem no máximo 10 segundos para retornar se possuem o arquivo
			

			// Crio o PACOTE UDP com as informações: 
			// NomeArquivo, Tamanho do arquivo, IP do Servidor Principal e PORTA do Servidor Principal
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, porta);
			
			
			// Envia o pacote acima para todos o UDPServidorArquivo
			DatagramSocket clientSocket = new DatagramSocket();
			clientSocket.send(sendPacket);
			
			
			// Recebe resposta do UDPServidorArquivo
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);
			String resposta = new String(receivePacket.getData());
			System.out.println("Resposta do UDPServidorArquivo: " + resposta);
			clientSocket.close();
			System.out.println("Socket cliente UDPServidorArquivo fechado!\n");
							
			
			// Servidor Principal responde ao cliente com a lista de todos os UDPServidorArquivo que possue o arquivo
			if (!resposta.isEmpty()) {
				capitalizedSentence = resposta + '\n';				
				outToClient.writeBytes(capitalizedSentence);
			} else {
				capitalizedSentence = "NAO";				
				outToClient.writeBytes(capitalizedSentence);
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}




public class Servidor {
	
	public static void main(String args[]) throws Exception {
		
		
		// Porta do Servidor Principal
		int porta = 9876;
		
		
		System.out.println("Servidor Principal\n");
		
		
		// ServerSocket representa um socket TCP
		// Abre uma porta TCP - 9876
		try (ServerSocket welcomeSocket = new ServerSocket(porta)) { 
			
			while (true) {
				System.out.print("Aguardando conexão com o Cliente...");
				Thread c = new Thread(new ThreadCliente(welcomeSocket.accept()));
				c.start();
			}
			
		}
	}
}

