import java.io.*;
import java.net.*;
import java.util.Scanner;


class ThreadUDPServidorArquivo implements Runnable {

	private DatagramSocket serverSocket;
	private DatagramPacket receivePacket;
	private int porta;
	private String IPAddress;
	private String nome;
	private String diretorio;

	
	public ThreadUDPServidorArquivo(DatagramSocket serverSocket, DatagramPacket receivePacket, int porta, InetAddress address, String IPAddress, String nome, String diretorio) {
		this.serverSocket = serverSocket;
		this.receivePacket = receivePacket;
		this.porta = porta;
		this.IPAddress = IPAddress;
		this.nome = nome;
		this.diretorio = diretorio;
	}
	
	
	@Override
	public void run() {
		
		try {
			
			byte[] sendData = new byte[1024];
			
			String portaString = Integer.toString(porta);
			
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
						
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}




public class UDPServidorArquivo {
	
	public static void main(String args[]) throws Exception {
		
		
		// Esses dados são referentes ao UDPServidorArquivo
		int porta = 4522;
		InetAddress address = InetAddress.getLocalHost();
		String IPAddress = address.getHostAddress();
		String nome = address.getHostName();
		
		
		// DatagramSocket representa um Socket UDP
		// Abre uma porta UDP - 4522
		try (DatagramSocket serverSocket = new DatagramSocket(porta)) {
			
			byte[] receiveData = new byte[1024];

			
			System.out.println("UDPServidorArquivo");
			
			// Assim que o UDPServidor Arquivo inicia já peço o Diretório padrão dele
			Scanner ler = new Scanner(System.in);
			System.out.print("\nInforme o endereço padrão do diretório de arquivos: ");
			String diretorio = ler.next();
			ler.close();
		
			
			// Esse Loop deixa a conexão aberta para receber mensagens do Servidor Principal
			while (true) {
				
				
				// Nessa parte o UDPServidor fica esperando receber alguma mensagem do Servidor Principal
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				System.out.println("\nAguardando mensagem...");
				System.out.println("Esperando por datagrama UDP na porta " + porta);
				
				
				// O Servidor de Arquivos recebeu a resposta do Servidor Principal - Datagrama UDP
				serverSocket.receive(receivePacket);
				
				
				// Como eu já tenho uma mensagem do servidor, inicio uma Thread para tratar cada solicitação
				Thread c = new Thread(new ThreadUDPServidorArquivo(serverSocket, receivePacket, porta, address, IPAddress, nome, diretorio));
				c.start();
				
			}
		}
	}
}
