import java.io.*;
import java.net.*;
import java.util.Scanner;


class ThreadUDPServidorArquivo implements Runnable {
	
	private String mensagem;
	private int portaUDP;
	private String diretorio;

	
	public ThreadUDPServidorArquivo(String mensagem, int portaUDP, String diretorio) {
		this.mensagem = mensagem;
		this.portaUDP = portaUDP;
		this.diretorio = diretorio;
	}
	
	
	@Override
	public void run() {
		
		try {
					
			// Concateno a string diretorio e o nome do arquivo
			String diretorioCompleto = diretorio.concat("\\").concat(mensagem);
			File arquivo = new File(diretorioCompleto);
			
			
			// Usa a fun��o exists para verificar se o arquivo existe ou n�o...
			if (arquivo.exists()) {
				
				InetAddress address = InetAddress.getLocalHost();
				String ipAddress = address.getHostAddress();
				String nome = address.getHostName();
				
				
				// SendData - Enviar resposta para o Servidor Principal
				byte[] sendData = new byte[1024];
				String respostaCompleta = nome.concat("&").concat(ipAddress);
				sendData = respostaCompleta.getBytes();
				
				
				// Envia a resposta para o Servidor Principal - Utilizo a porta 5000
				MulticastSocket s = new MulticastSocket();
				DatagramPacket pack = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("239.0.0.1"), 5000);
				s.send(pack);
				
				s.close();
				
			} 
			
						
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}




public class UDPServidorArquivo {
	
	public static void main(String args[]) throws Exception {
		
		System.out.println("UDPServidorArquivo");
		
		
		// Assim que o UDPServidorArquivo inicia, j� pe�o o Diret�rio padr�o dele
		Scanner ler = new Scanner(System.in);
		System.out.print("\nInforme o diret�rio padr�o de arquivos: ");
		String diretorio = ler.nextLine();
		ler.close();
		
		
		int portaUDP = 4522;

		
		// Inicio uma conex�o MULTICAST
		try(MulticastSocket multicast = new MulticastSocket(6000)) {
			
			
			// Endere�o de um grupo MULTICAST
			InetAddress grupo = InetAddress.getByName("239.0.0.1");
			
			
			// Ingressando em um grupo para receber mensagens enviadas pelo SERVER PRINCIPAL
			multicast.joinGroup(grupo);
			
			
			// Crio as vari�veis necess�rias para receber a mensagem do Server Principal			
			byte rec[] = new byte[1024];
			DatagramPacket mensagemServer = new DatagramPacket(rec, rec.length);
			
			
				
			// Esse Loop deixa a conex�o aberta para receber mensagens do Servidor Principal
			while (true) {
				
				// Recebo a mensagem do Servidor Principal
				multicast.receive(mensagemServer);
				String mensagem = new String(mensagemServer.getData(), 0, mensagemServer.getLength());
				
				// Executo a Thread respons�vel por verificar a exist�ncia do arquivo e responder o servidor
				Thread c = new Thread(new ThreadUDPServidorArquivo(mensagem, portaUDP, diretorio));
				c.start();
				
			}
		} 
	}
}
