import java.io.*;
import java.net.*;
import java.util.Scanner;


class UDPServidorArquivo {
	
	public static void main(String args[]) throws Exception {
		
		int porta = 9876;
		InetAddress address = InetAddress.getLocalHost();
		String IPAddress = address.getHostAddress();
		
		
		// DatagramSocket representa um Socket UDP
		// Abre uma porta UDP - 9888
		try (DatagramSocket serverSocket = new DatagramSocket(porta)) {
			
			byte[] receiveData = new byte[1024];
			byte[] sendData = new byte[1024];
			
			System.out.println("UDPServidorArquivo");
			
			
			// Assim que o UDPServidor Arquivo inicia j� pe�o o Diret�rio padr�o dele
			Scanner ler = new Scanner(System.in);
			System.out.print("\nInforme o endere�o padr�o do diret�rio de arquivos: ");
			String diretorio = ler.next();
			
			
			
			// Esse Loop deixa a conex�o aberta para receber mensagens do Servidor Principal
			while (true) {
				
				
				// Nessa parte o UDPServidor fica esperando receber alguma mensagem do Servidor Principal
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				System.out.println("\nAguardando mensagem...");
				System.out.println("Esperando por datagrama UDP na porta " + porta);
				
				
				
				// Quando o Servidor enviar o Datagrama UDP o UDPServidorArquivo ir� receber atrav�s do serverSocket
				serverSocket.receive(receivePacket);
				
				
				
				// Armazena o pacote recebido na vari�vel sentence
				String sentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
				System.out.println("Nome do arquivo: " + sentence);
				

				
				// Com o diret�rio completo em m�os, vamos conferir a exist�ncia do arquivo
				// Cria uma inst�ncia da classe File:
				String diretorioCompleto = diretorio.concat("\\").concat(sentence);
				System.out.println("Diret�rio completo: " + diretorioCompleto);
				File arquivo = new File(diretorioCompleto);
				
				
				
				
				// Capturamos os dados do Servidor que enviou o datagrama, no caso IP e PORTA
				InetAddress IPAddressServerMain = receivePacket.getAddress();
				int port = receivePacket.getPort();
				
				
				
				// Usa a fun��o exists para verificar se o arquivo existe ou n�o...
				// Se o arquivo existir envia a resposta SIM para o Servidor Principal
				// Se o arquivo n�o existir envia a resposta NAO para o Servidor Principal
				if (arquivo.exists()) {
					
					
					// Criando resposta em String que ser� enviada em forma de bytes para o Servidor Principal
					// Essa resposta cont�m: Se existe arquivo ou n�o (SIM, NAO), PORTA do UDPServidorArquivo e IP do UDPServidorArquivo
					String resposta = "SIM";
					String respostaCompleta = resposta.concat("&").concat(IPAddress).concat("&").concat(Integer.toString(porta));
					
					sendData = respostaCompleta.getBytes();
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddressServerMain, port);
					System.out.println("Enviando " + respostaCompleta + " para o Servidor Principal...");
					serverSocket.send(sendPacket);
					
				} 
			}
		}
	}
}
