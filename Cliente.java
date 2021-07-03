import java.io.*;
import java.net.*;
import java.util.Scanner;


public class Cliente {
	
	public static void main(String args[]) throws Exception {
		
		String nomeArquivo;
		String modifiedSentence;
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Cliente\n");
		
		// Estabelece conex�o com o Servidor Principal (Endere�o IP, Localhost e PORTA 3334)
		Socket clientSocket = new Socket("localhost", 9876);
		
		
		// Pergunto ao cliente qual arquivo ele quer verificar a exist�ncia com o Servidor Principal
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		System.out.print("Informe o nome do arquivo com a sua extens�o: ");
		nomeArquivo = inFromUser.readLine();
		
		
		// Envio o Nome do Arquivo para o Servidor Principal
		outToServer.writeBytes(nomeArquivo + '\n');
		
		
		// Aguarda retorno do ServidorPrincipal 
		// com a Lista de ServidoresArquivos que cont�m o arquivo (modifiedSentence)
		modifiedSentence = inFromServer.readLine();
		
		
		// Como j� recebi a resposta do Servidor Principal, devo fechar a conex�o do cliente
		clientSocket.close();
		

		
		
// --------------------------------------------------------------------------------------------------------------
		// Caso exista algum Servidor Arquivo que possua o arquivo
		// Irei me conectar ao mesmo via TCP
		

		// Separa a resposta do Servidor Principal em suas respectivas vari�veis
		String[] textoSeparado = modifiedSentence.split("&");
		String nome = textoSeparado[0];
		String ipUDPServidorArquivo = textoSeparado[1];
		String porta = textoSeparado[2];
		String diretorioDestino = textoSeparado[3];
		int portaDestino = Integer.parseInt(porta);

		
		
		System.out.print("\nLista de Servidores que possuem o arquivo");
		System.out.println("\nNome: " + nome + " - IP: " + ipUDPServidorArquivo + " - " +  portaDestino  + "\n");	
		
		
		if (modifiedSentence != "NAO") {
			
			// Pergunta ao Cliente de qual TCPServidorArquivo ele quer baixar
			Scanner ler = new Scanner(System.in);
			System.out.print("\nInforme o IP do Servidor Arquivo: ");
			String ipDestino = ler.next();
			System.out.print("Informe o diret�rio onde deseja salvar o arquivo: ");
			String diretorio = ler.next();
			ler.close();
			
			try {
				
				// Estabelece conex�o com o servidor escolhido pelo Cliente: ENDERE�O - PORTA
				Socket clientAndServerSocket = new Socket(ipDestino, portaDestino);
				
				
				// Enviando o diret�rio de onde o arquivo est� para o Servidor
				DataOutputStream request = new DataOutputStream(clientAndServerSocket.getOutputStream());
				request.writeBytes(diretorioDestino);
				
				
				// Aguardando mensagem de retorno do servidor
				InputStream response = clientSocket.getInputStream();
				
				
				// Configurando arquivo recebido pelo TCPServidorArquivo
				byte[] rawArq = response.readAllBytes();
				FileOutputStream fos = new FileOutputStream(diretorio + "\\" + nomeArquivo);
				fos.write(rawArq);
				fos.close();
				
				// Arquivo recebido com sucesso!
				// Fechando conex�o TCP com TCPServidorArquivo
				clientAndServerSocket.close();
				
				
			} catch(OutOfMemoryError e) {
				System.err.println("Eita, o arquivo � muito grande!");
				
			} catch(Exception e) {
				e.printStackTrace();
			}
					
		} else {
			
			// Informa ao Cliente que nenhum Servidor de Arquivo possui o arquivo
			System.out.println("Nenhum Servidor Arquivo possui o arquivo requisitado!");
		}		
	}
	
}
