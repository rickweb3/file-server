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

// -------------------------------------------------------------------------------------------------------------------------------				

			//  RECEBENDO MENSAGENS DE CLIENTE
			
			
			// Variável referente a mensagem do cliente
			String clientSentence;
			

		
			// Recebe mensagem do Cliente
			// Mensagem do Cliente está na variável clientSentence
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			clientSentence = inFromClient.readLine();
			System.out.println("\nCliente " + connectionSocket.getInetAddress() + " - " + connectionSocket.getPort());
			System.out.println("Solicita arquivo: " + clientSentence);
			
				
			
// -------------------------------------------------------------------------------------------------------------------------------				
			
			// ENVIANDO MENSAGEM VIA MULTICAST PARA TODOS OS UDPSERVIDORARQUIVO
			
			
			// A variável sendData é para enviar o nome do arquivo via UDP para todos os UDPServidorArquivo
			byte[] sendData = new byte[1024];
			
			// Armazeno o nome do arquivo enviado pelo Cliente em sendData
			sendData = clientSentence.getBytes();
			
			
			
			
			// Definindo o endereço de envio do pacote, neste caso o endereço do MULTICAST
			InetAddress addr = InetAddress.getByName("239.0.0.1");
			
			// Pacote UDP que será enviado: NomeArquivo, Tamanho Arquivo, IP MULTICAST, PORTA MULTICAST
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, addr, 6000);
			DatagramSocket clientSocket = new DatagramSocket();
			clientSocket.send(sendPacket);
			
			

// -------------------------------------------------------------------------------------------------------------------------------				

			// RECEBENDO MENSAGENS DO UDPSERVIDORARQUIVO
						
			
			// Lista que irá armazenar as respostas dos UDPServidorArquivo
			ArrayList<String> respostaUDPServidorArquivo = new ArrayList();
			
			// Crio um ObjectOutputStream para poder enviar a Lista via Socket para o Cliente
			ObjectOutputStream objectOutput = new ObjectOutputStream(connectionSocket.getOutputStream());
			
			
			
			
			
			// Crio um MulticastSocket para poder receber as respostas dos Servidores
			MulticastSocket s = new MulticastSocket(5000);
			String group = "239.0.0.1";
			s.joinGroup(InetAddress.getByName(group));
			
			
			// A variável receiveData é para receber todas as respostas via UDP de todos os UDPServidorArquivo
			byte[] receiveData = new byte[1024];
			DatagramPacket pack = new DatagramPacket(receiveData, receiveData.length);
			
			
			
			
			
			// Defino um TIMEOUT para ficar recebendo as respostas do UDPServidorArquivo
			int timeout = 10000;
			String mensagem;
			
			// Aciono o TIMEOUT
			s.setSoTimeout(timeout);
			
			// Fico recebendo respostas dos UDPServidor
			// Quanto o TIMEOUT estoura, envio a resposta pelo CATCH
			try {
				
				while(true) {
					s.receive(pack);
					mensagem = new String(pack.getData(), 0, pack.getLength());
					respostaUDPServidorArquivo.add(mensagem);
				}
				
			} catch(SocketTimeoutException e) {
				
				// Fecho conexão com o cliente, pois já recebi a resposta dos servidores
				clientSocket.close();
				
				if(!respostaUDPServidorArquivo.isEmpty()) {
					
					objectOutput.writeObject(respostaUDPServidorArquivo);
					
				} else {
					objectOutput.writeObject(respostaUDPServidorArquivo);
				}
			}			
			
			
			System.out.println("Resposta enviada para o Cliente!\n");
		
			
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

