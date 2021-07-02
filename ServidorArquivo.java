import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

class Connection implements Runnable {
	
	private Socket connectionSocket;
	private String caminho;
	private String caminho2;
	private String nomeArquivo;
	
	private void setCaminhoNomeArquivo(String request) {
		
		this.caminho = request.substring(0, request.indexOf("&"));
		this.nomeArquivo = request.substring(request.indexOf("&")+1);
	}
	
	public String getCaminho() {
		return caminho;
	}
	
	public String getNomeArquivo() {
		return nomeArquivo;
	}
	
	public Connection(Socket newSocket) {
		connectionSocket = newSocket;
	}
	
	@Override
	public void run() {
		try {
			
			System.out.println("Servidor de Arquivos\n");
			
			System.out.println("Nova conex�o recebida");
			System.out.println("Info do cliente:");
			System.out.println("IP: "+ connectionSocket.getInetAddress());
			System.out.println("Porta: "+ connectionSocket.getPort());
			
			BufferedReader request = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			setCaminhoNomeArquivo(request.readLine());
			
			
			// Exibe o diret�rio do arquivo solicitado pelo cliente
			System.out.println("Solicitando arquivo \"" + nomeArquivo + "\"");
			System.out.println("Arquivo est� em: \"" + caminho + "\"");
			
			
			// Come�a o processo de enviar o arquivo para o cliente
			File ptrArquivo = new File(caminho,nomeArquivo);
			FileInputStream fis = new FileInputStream(ptrArquivo);
			DataOutputStream response = new DataOutputStream(connectionSocket.getOutputStream());
			byte[] arqBytes = new byte [(int) ptrArquivo.length()];
			
			System.out.println("Lendo arquivo");
			fis.read(arqBytes);
			fis.close();
			
			System.out.println("Enviando...");
			response.write(arqBytes);
			
			System.out.println("Enviado!");
			connectionSocket.close();
			
			// Arquivo enviado com sucesso, com isso fecha a conex�o
			System.out.println("Conex�o Finalizada");
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
}


public class ServidorArquivo {

	public static void main(String[] args){
		
		// Tempo de delay de conex�o em milissegundos
		int timeout = 120000; 
		String caminho;
		
		System.out.println("Servidor de Arquivos\n");
		
	
		// Porta 3334
		try ( ServerSocket arqSocket = new ServerSocket(3334)) { 
			//arqSocket.setSoTimeout(timeout);
			
			// Pergunta qual o diret�rio do Servidor de Arquivos em quest�o
			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Informe o diret�rio padr�o: ");
			caminho = inFromUser.readLine();
			
			while (true) {
				System.out.println("Aguardando conex�o...");
				Thread c = new Thread(new Connection(arqSocket.accept()));
				c.start();
			}
		}
		catch(FileNotFoundException e) {
			System.err.println("N�o foi poss�vel encontrar o arquivo!");
		}
		catch(SocketTimeoutException e){
				System.err.println("Tempo m�ximo de espera atingido!");
				System.err.println("Servi�o encerrado");
		}
		catch (IOException e){
			e.printStackTrace();
		}
	
	}

}

