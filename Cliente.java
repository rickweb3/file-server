import java.io.*;
import java.net.*;
import java.util.Scanner;


public class Cliente {
	
	public static void main(String args[]) throws Exception {
		
		String nomeArquivo;
		String modifiedSentence;
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Cliente\n");
		
		// Estabelece conexão com o Servidor Principal (Endereço IP, Localhost e PORTA 3334)
		Socket clientTCPSocket = new Socket("localhost", 4522);
		
		
		// Pergunto ao Usuario qual arquivo ele quer verificar a existência com o Servidor Principal
		DataOutputStream outToServer = new DataOutputStream(clientTCPSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientTCPSocket.getInputStream()));
		System.out.print("Informe o nome do arquivo com a sua extensão: ");
		nomeArquivo = inFromUser.readLine();
		
		
		// Envio o Nome do Arquivo para o Servidor Principal
		outToServer.writeBytes(nomeArquivo + '\n');
		
		
		// Aguarda retorno do ServidorPrincipal 
		// com a Lista de ServidoresArquivos que contém o arquivo (modifiedSentence)
		modifiedSentence = inFromServer.readLine();
		
		
		// Como já recebi a resposta do Servidor Principal, devo fechar a conexão do cliente
		clientTCPSocket.close();
		

		
		
// --------------------------------------------------------------------------------------------------------------
		// Caso exista algum Servidor Arquivo que possua o arquivo
		// Irei me conectar ao mesmo via TCP
		

		// Separa a resposta do Servidor Principal em suas respectivas variáveis
		String[] textoSeparado = modifiedSentence.split("&");
		String nome = textoSeparado[0];
		String ipUDPServidorArquivo = textoSeparado[1];
		String porta = textoSeparado[2];
		String diretorioDestino = textoSeparado[3];
		int portaDestino = Integer.parseInt(porta);
		int portaTCPArquivo = 3334;

		
		
		System.out.print("\nLista de Servidores que possuem o arquivo");
		System.out.println("\nNome: " + nome);	
		
		
		if (modifiedSentence != "NAO") {
			
			// Pergunta ao Cliente de qual TCPServidorArquivo ele quer baixar
			Scanner ler = new Scanner(System.in);
			System.out.print("\nInforme o IP do TCPServidorArquivo: ");
			String ipDestino = ler.next();
			System.out.print("Informe o diretório onde deseja salvar o arquivo: ");
			String diretorio = ler.next();
			ler.close();
			
			try {
				
				
				// Estabelece conexão com o Servidor Principal (Endereço IP, Localhost e PORTA 3334)
				Socket clientSocket = new Socket(ipDestino, 3334);
				
				
				// Pergunto ao Usuario qual arquivo ele quer verificar a existência com o Servidor Principal
				DataOutputStream toServer = new DataOutputStream(clientSocket.getOutputStream());
				BufferedReader inServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
				
				// Envio o Nome do Arquivo para o TCP Servidor Arquivo
				toServer.writeBytes(nomeArquivo + '\n');
				
		
				// Aguardando mensagem de retorno do servidor
				InputStream response = clientSocket.getInputStream();

				
				// Configurando arquivo recebido pelo TCPServidorArquivo
				byte[] rawArq = response.readAllBytes();
				FileOutputStream fos = new FileOutputStream(diretorio + "\\" + nomeArquivo);
				
				// Arquivo recebido com sucesso!
				fos.write(rawArq);
				fos.close();
				
				
				// Fechando conexão TCP com TCPServidorArquivo
				clientSocket.close();
				
				
			} catch(OutOfMemoryError e) {
				System.err.println("Eita, o arquivo é muito grande!");
				
			} catch(Exception e) {
				e.printStackTrace();
			}
					
		} else {
			
			// Informa ao Cliente que nenhum Servidor de Arquivo possui o arquivo
			System.out.println("Nenhum Servidor Arquivo possui o arquivo requisitado!");
		}		
	}
	
}
