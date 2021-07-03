import java.io.*;
import java.net.*;
import java.util.Scanner;


class ThreadUDPServidorArquivo implements Runnable {

	private Socket threadUDPServidorArquivo;
	
	public ThreadUDPServidorArquivo(Socket serverSocket) {
		threadUDPServidorArquivo = serverSocket;
	}
	
	
	@Override
	public void run() {
		
		// Nessa parte o UDPServidor fica esperando receber alguma mensagem do Servidor Principal
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		// Quando o Servidor enviar o Datagrama UDP o UDPServidorArquivo irá receber através do serverSocket
		serverSocket.receive(receivePacket);
		
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		
		
		// Armazena o pacote recebido na variável sentence
		String sentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
		System.out.println("Nome do arquivo: " + sentence);
		

		
		// Com o diretório completo em mãos, vamos conferir a existência do arquivo
		// Cria uma instância da classe File:
		String diretorioCompleto = diretorio.concat("\\").concat(sentence);
		System.out.println("Diretório completo: " + diretorioCompleto);
		File arquivo = new File(diretorioCompleto);
		
		
		
		
		// Capturamos os dados do Servidor que enviou o datagrama, no caso IP e PORTA
		InetAddress IPAddressServerMain = receivePacket.getAddress();
		int port = receivePacket.getPort();
		
		
		
		// Usa a função exists para verificar se o arquivo existe ou não...
		// Se o arquivo existir envia a resposta SIM para o Servidor Principal
		// Se o arquivo não existir envia a resposta NAO para o Servidor Principal
		if (arquivo.exists()) {
			
			
			// Criando resposta em String que será enviada em forma de bytes para o Servidor Principal
			// Essa resposta contém: Se existe arquivo ou não (SIM, NAO), PORTA do UDPServidorArquivo e IP do UDPServidorArquivo
			String respostaCompleta = nome.concat("&").concat(IPAddress).concat("&").concat(portaString).concat("&").concat(diretorioCompleto);
			
			sendData = respostaCompleta.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddressServerMain, port);
			System.out.println("Enviando " + nome + "&" + IPAddress + " para o Servidor Principal...");
			serverSocket.send(sendPacket);
			
		}
		
	}
	
}




public class UDPServidorArquivo {
	
	public static void main(String args[]) throws Exception {
		
		
		// Esses dados são referentes ao UDPServidorArquivo
		int porta = 9876;
		String portaString = Integer.toString(porta);
		InetAddress address = InetAddress.getLocalHost();
		String IPAddress = address.getHostAddress();
		String nome = address.getHostName();
		
		
		// DatagramSocket representa um Socket UDP
		// Abre uma porta UDP - 9876
		try (DatagramSocket serverSocket = new DatagramSocket(porta)) {
			
			
			System.out.println("UDPServidorArquivo");
			
			// Assim que o UDPServidorArquivo inicia, já peço o DIRETÓRIO padrão dele
			Scanner ler = new Scanner(System.in);
			System.out.print("\nInforme o endereço padrão do diretório de arquivos: ");
			String diretorio = ler.next();
			
			
			
			// Esse Loop deixa a conexão aberta para receber mensagens do Servidor Principal
			while (true) {
								
				System.out.println("\nAguardando mensagem do Servidor Principal...");
				System.out.println("Esperando por datagrama UDP na porta " + porta);
				
				Thread c = new Thread(new ThreadUDPServidorArquivo(serverSocket.receive(receivePacket)));				
				
			}
		}
	}
}
