import java.io.*;
import java.net.*;
import java.util.ArrayList;
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
		
		
		
		
		// Recebe a resposta do Servidor Principal que é uma lista de String
		ObjectInputStream objectInput = new ObjectInputStream(clientTCPSocket.getInputStream());
		

		
		
		// Converte a resposta do Servidor Principal em um ArrayList (String)
		Object object = objectInput.readObject();
		ArrayList<String> listaRespostaUDPServidorArquivo = new ArrayList<String>();
		listaRespostaUDPServidorArquivo = (ArrayList<String>) object;

		
		// Como já recebi a resposta do Servidor Principal, devo fechar a conexão do cliente com o Server Principal
		clientTCPSocket.close();
		
		
		
// --------------------------------------------------------------------------------------------------------------


		int portaTCPArquivo = 3334;

				
		if (!listaRespostaUDPServidorArquivo.isEmpty()) {
			
			
			// Variáveis que serão utilizadas durante a iteração da lista que contém a resposta
			String[] textoSeparado;
			String nome;
			String ipUDPServidorArquivo;
			String porta;
			String diretorioDestino;
			
			
			// Realizo a iteração sobre a lista que no caso é todos os Servidor Arquivo que possue o arquivo
			System.out.println("\nLista de Servidores que possuem o arquivo:");
			for(String item : listaRespostaUDPServidorArquivo) {
				
				textoSeparado = item.split("&");
				nome = textoSeparado[0].replace("[", "");
				porta = textoSeparado[1];
				ipUDPServidorArquivo = textoSeparado[2];
				System.out.println(nome + " - " + porta + " - " + ipUDPServidorArquivo);
				
			}
			
			
			// Pergunta ao Cliente de qual TCPServidorArquivo ele quer baixar
			Scanner ler = new Scanner(System.in);
			System.out.print("\nInforme o IP do TCPServidorArquivo: ");
			String ipDestino = ler.nextLine();
		
		
			System.out.print("Informe o diretório onde deseja salvar o arquivo: ");
			String diretorio = ler.nextLine();
			ler.close();
			
			
			try {
				
				// Estabelece conexão com o TCPServidorArquivo (Endereço IP, Localhost e PORTA 3334)
				Socket clientSocket = new Socket(ipDestino, portaTCPArquivo);
				
				
				// Pergunto ao Usuario qual arquivo ele quer verificar a existência com o TCPServidorArquivo
				DataOutputStream toServer = new DataOutputStream(clientSocket.getOutputStream());
				BufferedReader inServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
				
				// Envio o Nome do Arquivo para o TCPServidorArquivo
				toServer.writeBytes(nomeArquivo + '\n');
				
		
				
				// Aguardando mensagem de retorno do TCPServidorArquivo
				InputStream response = clientSocket.getInputStream();

				
				
				// Configurando arquivo recebido pelo TCPServidorArquivo
				byte[] rawArq = response.readAllBytes();
				FileOutputStream fos = new FileOutputStream(diretorio + "\\" + nomeArquivo);
				
				
				// Arquivo recebido com sucesso!
				fos.write(rawArq);
				fos.close();
				
				
				System.out.println("\n\nArquivo: " + nomeArquivo + " recebido com sucesso!");
				System.out.println("Encerrando conexão...");
				
				// Fechando conexão TCP com TCPServidorArquivo
				clientSocket.close();
				
				
			} catch(OutOfMemoryError e) {
				System.err.println("Eita, o arquivo é muito grande!");
				
			} catch(Exception e) {
				e.printStackTrace();
			}
					
		} else {
			// Informa ao Cliente que nenhum Servidor de Arquivo possui o arquivo
			System.out.println("Nenhum Servidor Arquivo possui o arquivo solicitado!");
		}		
	}
	
}
