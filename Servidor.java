import java.io.*;
import java.net.*;
import java.util.ArrayList;


class ThreadCliente implements Runnable {

	private Socket connectionSocket;
	
	
	public ThreadCliente(Socket newSocket) {
		this.connectionSocket = newSocket;
	}
	
	
	@Override
	public void run() {
		
		try {
			
			// Variável referente a mensagem do cliente
			String clientSentence;
			
			// IP do Servidor Principal para Broadcast
			String servidor = "localhost";
		
			int porta = 4522;
			
			
			// Recebe mensagem do Cliente
			// Mensagem do Cliente está na variável clientSentence
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
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
			
			// Crio o PACOTE UDP com as informações: 
			// NomeArquivo, Tamanho do arquivo, IP do Servidor Principal e PORTA do Servidor Principal
			InetAddress IPAddress = InetAddress.getByName(servidor);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, porta);
						
			
			
			// Envia o pacote acima para todos o UDPServidorArquivo
			DatagramSocket clientSocket = new DatagramSocket();
			clientSocket.send(sendPacket);
			
			
			
			// Recurso necessário para poder receber resposta do UDPServidorArquivo
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			
			
			
			
			// Crio a lista que irá armazenar as respostas dos UDPServidorArquivo
			ArrayList<String> respostaUDPServidorArquivo = new ArrayList();
			
			
			
			// Crio um ObjectOutputStream para poder enviar a Lista via Socket
			ObjectOutputStream objectOutput = new ObjectOutputStream(connectionSocket.getOutputStream());

			
			
			
// -----------------------------------------------------------------------------------------------------------------------------			

			try {
				
				String mensagem;
				String mensagem2 = "[DESKTOP-VIO6I3U&192.168.0.103&4522&C:\\Users\\Henrique\\Documents\\Diretorio\\teste.mp4";
				
//				int timeout = 10000;
//				connectionSocket.setSoTimeout(timeout);
				
				clientSocket.receive(receivePacket);
//				respostaUDPServidorArquivo.add(mensagem = new String(receivePacket.getData()));
				respostaUDPServidorArquivo.add(mensagem2);
				respostaUDPServidorArquivo.add(mensagem2);
				respostaUDPServidorArquivo.add(mensagem2);

				

			} catch (SocketTimeoutException e) {
				
			} 
			
			
// -----------------------------------------------------------------------------------------------------------------------------
			

			
			if(!respostaUDPServidorArquivo.isEmpty()) {
				objectOutput.writeObject(respostaUDPServidorArquivo);
				
			} else {
				objectOutput.writeBytes("NAO");
			}
			
			
			System.out.println("Resposta enviada para o Cliente!\n");
			
			// Fecho conexão com o cliente, pois já recebi a resposta dos servidores
			clientSocket.close();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}




public class Servidor {
	
	public static void main(String args[]) throws Exception {
		
		
		// Porta do Servidor Principal
		int porta = 4522;
		
		
		System.out.println("Servidor Principal\n");
		
		
		// ServerSocket representa um socket TCP
		// Abre uma porta TCP - 4522
		try (ServerSocket welcomeSocket = new ServerSocket(porta)) { 
			
			while (true) {
				
				Thread c = new Thread(new ThreadCliente(welcomeSocket.accept()));
				c.start();
			}
			
		}
	}
}

