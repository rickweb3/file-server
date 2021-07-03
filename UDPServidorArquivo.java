import java.io.*;
import java.net.*;
import java.util.Scanner;


class UDPServidorArquivo {
	
	public static void main(String args[]) throws Exception {
		
		int porta = 9876;
		
		// DatagramSocket representa um Socket UDP
		// Abre uma porta UDP - 9888
		try (DatagramSocket serverSocket = new DatagramSocket(porta)) {
			
			byte[] receiveData = new byte[1024];
			byte[] sendData = new byte[1024];
			
			System.out.println("UDPServidorArquivo");
			
			
			// Assim que o UDPServidor Arquivo inicia já peço o Diretório padrão dele
			Scanner ler = new Scanner(System.in);
			System.out.print("\nInforme o endereço padrão do diretório de arquivos: ");
			String diretorio = ler.next();
			
			
			
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
				

				
				// Com o diretório completo em mãos, vamos conferir a existência do arquivo
				// Cria uma instância da classe File:
				String diretorioCompleto = diretorio.concat("\\").concat(sentence);
				System.out.println("Diretório completo: " + diretorioCompleto);
				File arquivo = new File(diretorioCompleto);
				
				
				
				
				// Capturamos os dados do Servidor que enviou o datagrama, no caso ip e porta
				InetAddress IPAddressServerMain = receivePacket.getAddress();
				int port = receivePacket.getPort();
				
				
				
				// Usa a função exists para verificar se o arquivo existe ou não...
				// Se o arquivo existir envia a resposta SIM para o Servidor Principal
				// Se o arquivo não existir envia a resposta NAO para o Servidor Principal
				if (arquivo.exists()) {
					
					String resposta = "SIM";
					sendData = resposta.getBytes();
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddressServerMain, port);
					System.out.println("Enviando " + resposta + " para o Servidor Principal...");
					serverSocket.send(sendPacket);
					
				} else {
					
					String resposta = "NAO";
					sendData = resposta.getBytes();
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddressServerMain, port);
					System.out.println("Enviando " + resposta + " para o Servidor Principal...");
					serverSocket.send(sendPacket);					
				}
				
			}
			
		}
	}
}
