import java.io.*;
import java.net.*;


class UDPServidorArquivo {
	
	public static void main(String args[]) throws Exception {
		
		int porta = 9876;
		int numConn = 1;
		
		// DatagramSocket representa um Socket UDP
		// Abre uma porta UDP - 9888
		try (DatagramSocket serverSocket = new DatagramSocket(porta)) {
			
			byte[] receiveData = new byte[1024];
			byte[] sendData = new byte[1024];
			
			System.out.println("UDPServidorArquivo");
			
			// Esse Loop deixa a conexão aberta para receber mensagens do Servidor Principal
			while (true) {
				
				
				// Nessa parte o UDPServidor fica esperando receber alguma mensagem do Servidor Principal
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				System.out.println("\nAguardando mensagem...");
				System.out.println("Esperando por datagrama UDP na porta " + porta);
				
				
				
				// Quando o Servidor enviar o Datagrama UDP o UDPServidorArquivo irá receber através do serverSocket
				serverSocket.receive(receivePacket);
				
				
				// Armazena o pacote recebido na variável sentence
				String sentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
				System.out.println("Nome do arquivo: " + sentence);
				
				
				// Com o nome do arquivo em mãos temos que verificar se ele existe no diretório 
				
				
				
				// Capturamos os dados do Servidor que enviou o datagrama, no caso ip e porta
				InetAddress IPAddress = receivePacket.getAddress();
				int port = receivePacket.getPort();
				
		
				String capitalizedSentence = sentence.toUpperCase();
				sendData = capitalizedSentence.getBytes();
				System.out.println("Enviando resposta ao Servidor Principal!");
				
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
				serverSocket.send(sendPacket);
			}
			
		}
	}
}
