import java.io.*;
import java.net.*;


class UDPServidorArquivo {
	
	public static void main(String args[]) throws Exception {
		
		// Abre uma porta UDP 9888
		try (DatagramSocket serverSocket = new DatagramSocket(9888)) {
			
			byte[] receiveData = new byte[1024];
			byte[] sendData = new byte[1024];
			
			System.out.println("UDPServidorArquivo");
			
			// Esse Loop deixa a conexão aberta para receber mensagens do ServidorPrincipal
			while (true) {
				
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				System.out.println("Aguardando mensagem");
				
				serverSocket.receive(receivePacket);
				System.out.println("pacote recebido!");
				
				String sentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
				InetAddress IPAddress = receivePacket.getAddress();
				int port = receivePacket.getPort();
				String capitalizedSentence = sentence.toUpperCase();
				sendData = capitalizedSentence.getBytes();
				System.out.println("Reenviando!");
				
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
				serverSocket.send(sendPacket);
			}
			
		}
	}
}
