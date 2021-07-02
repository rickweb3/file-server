import java.io.*;
import java.net.*;


class Cliente {
	
	public static void main(String args[]) throws Exception {
		
		String nomeArquivo;
		String modifiedSentence;
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		
		
		// Estabelece conexão com o Servidor Principal (Endereço IP Localhost e PORTA 3334)
		Socket clientSocket = new Socket("localhost", 9876);
		
		
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		System.out.print("Informe o nome do arquivo: ");
		nomeArquivo = inFromUser.readLine();
		outToServer.writeBytes(nomeArquivo + '\n');
		
		
		// Aguarda retorno do ServidorPrincipal 
		// com a Lista de ServidoresArquivos que contém o arquivo (modifiedSentence)
		modifiedSentence = inFromServer.readLine();
		System.out.println("Servidor Principal Respondeu: " + modifiedSentence);
		
// A depender da resposta do servidor entro em uma estrutura de condição IF		
//		if(modifiedSentence.equals(modifiedSentence)) {
//			
//			try {
//			
//				Socket clientSocket = new Socket()
//			
//			} catch (OutOfMemoryError e) {
//				
//				System.err.println("Oooops, o arquivo é muito grande!");
//			
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			
//			
//		}
		
		
		// Fecha conexão do cliente
		clientSocket.close();
		
		
		
	}
	
}
