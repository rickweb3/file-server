import java.io.*;
import java.net.*;
import java.util.Scanner;


class ThreadUDPServidorArquivo implements Runnable {
	
	private int porta;
	private String diretorio;
	private String mensagem;

	
	public ThreadUDPServidorArquivo(String diretorio, int porta) {
		this.porta = porta;
		this.diretorio = diretorio;
	}
	
	
	@Override
	public void run() {
		
		try {
			
			// Inicio uma conexão MULTICAST
			try(MulticastSocket multicast = new MulticastSocket(6000)) {
				
				
				// Endereço de um grupo MULTICAST
				InetAddress grupo = InetAddress.getByName("239.0.0.1");
				
				
				// Ingressando em um grupo para receber mensagens enviadas pelo SERVER PRINCIPAL
				multicast.joinGroup(grupo);
				
				
				// Crio as variáveis necessárias para receber a mensagem do Server Principal			
				byte rec[] = new byte[1024];
				DatagramPacket mensagemServer = new DatagramPacket(rec, rec.length);
				
				
					
				// Esse Loop deixa a conexão aberta para receber mensagens do Servidor Principal
				while (true) {
					
					// Recebo a mensagem do Servidor Principal
					multicast.receive(mensagemServer);
					mensagem = new String(mensagemServer.getData(), 0, mensagemServer.getLength());
					
					// Inicia thread responsável por verificar existência do arquivo e devolver a resposta
					Thread c = new Thread(new ThreadUDPVerificaArquivo(diretorio, porta, mensagem));
					c.start();
				}
				
			} 
						
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}



class ThreadUDPVerificaArquivo implements Runnable {
	
	private String diretorio;
	private int porta;
	private String mensagem;
	
	public ThreadUDPVerificaArquivo(String diretorio, int porta, String mensagem) {
		this.porta = porta;
		this.diretorio = diretorio;
		this.mensagem = mensagem;
	}
	

	@Override
	public void run() {
		try {
			
			// Concateno a string diretorio e o nome do arquivo
			String diretorioCompleto = diretorio.concat("\\").concat(mensagem);
			File arquivo = new File(diretorioCompleto);
			
			
			// Usa a função exists para verificar se o arquivo existe ou não...
			if (arquivo.exists()) {
				
				InetAddress address = InetAddress.getLocalHost();
				String ipAddress = address.getHostAddress();
				String nome = address.getHostName();
				
				
				// SendData - Enviar resposta para o Servidor Principal
				byte[] sendData = new byte[1024];
				String portaTCP = Integer.toString(porta);
				String respostaCompleta = nome.concat("&").concat(portaTCP).concat("&").concat(ipAddress);
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
