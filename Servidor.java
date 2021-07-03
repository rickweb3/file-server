import java.io.*;
import java.net.*;

class Servidor {
	
	public static void main(String args[]) throws Exception {
		
		
		// Dados referente ao Servidor
		int porta = 9876;
		String servidor = "localhost";
		InetAddress IPAddress = InetAddress.getByName(servidor);
		
		
		// Dados referente a mensagem do cliente
		String clientSentence;
		String capitalizedSentence;
		
		
		System.out.println("Servidor Principal\n");
		
		
		// ServerSocket representa um socket TCP
		// Abre uma porta TCP - 9876
		try (ServerSocket welcomeSocket = new ServerSocket(porta)) { 
			
			while (true) {
				
				System.out.print("Aguardando mensagem...");
				Socket connectionSocket = welcomeSocket.accept();
				
				
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
				
				
				// Crio o pacote UDP com as informações: NomeArquivo, Tamanho do arquivo, IP do Servidor e PORTA do Servidor Principal
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, porta);
				
				
				
				
				// Cria um DatagramSocket e logo após envia o pacote via UDP
				// Servidor Principal pergunta se algum UDPServidorArquivo tem o arquivo
				DatagramSocket servidorSocket = new DatagramSocket();
				servidorSocket.send(sendPacket);
				
				
				// Essa parte é a resposta de todos os servidores UDPServidorArquivo
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				servidorSocket.receive(receivePacket);
				String respostaUDPServidorArquivo = new String(receivePacket.getData());
				System.out.println("Texto recebido do servidor: " + respostaUDPServidorArquivo + "\n");
				
				
				
				
				// Servidor Principal responde ao cliente com a lista de todos os UDPServidorArquivo que possue o arquivo
				outToClient.writeBytes(respostaUDPServidorArquivo);
				
			}
		}
	}
}
