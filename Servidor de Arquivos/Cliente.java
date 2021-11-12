import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;


public class Cliente {
	
	
	
	// M�todo respons�vel por fazer o cliente se comunicar com o Servidor Principal 
	public ArrayList<String> requisitarArquivo (String nomeArquivo) throws Exception{
		
		
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Cliente\n");
		
		
		
		// Estabelece conex�o com o Servidor Principal (Endere�o IP, Localhost e PORTA 3334)
		Socket clientTCPSocket = new Socket("localhost", 4522);
		
		
		
		// Pergunto ao Usuario qual arquivo ele quer verificar a exist�ncia com o Servidor Principal
		DataOutputStream outToServer = new DataOutputStream(clientTCPSocket.getOutputStream());
//		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientTCPSocket.getInputStream()));
		//System.out.print("Informe o nome do arquivo com a sua extens�o: ");
		//nomeArquivo = inFromUser.readLine();
		
		
		
		// Envio o Nome do Arquivo para o Servidor Principal
		outToServer.writeBytes(nomeArquivo + '\n');
		
		
		
		
		// Recebe a resposta do Servidor Principal que � uma lista de String
		ObjectInputStream objectInput = new ObjectInputStream(clientTCPSocket.getInputStream());
		

		
		
		// Converte a resposta do Servidor Principal em um ArrayList (String)
		Object object = objectInput.readObject();
		ArrayList<String> listaRespostaUDPServidorArquivo = new ArrayList<String>();
		listaRespostaUDPServidorArquivo = (ArrayList<String>) object;

		
		// Como j� recebi a resposta do Servidor Principal, devo fechar a conex�o do cliente com o Server Principal
		clientTCPSocket.close();
		return listaRespostaUDPServidorArquivo;
	}
		
		
// --------------------------------------------------------------------------------------------------------------

	// M�todo respons�vel por fazer o cliente se conectar com o Servidor de arquivos  
	public String baixarArquivo(String ipDestino, String diretorio, String portaTCP, String nomeArquivo, ArrayList<String> listaRespostaUDPServidorArquivo) throws Exception {
	
		
		if (!listaRespostaUDPServidorArquivo.isEmpty()) {
				
				
				// Vari�veis que ser�o utilizadas durante a itera��o da lista que cont�m a resposta
				String[] textoSeparado;
				String nome;
				String ipUDPServidorArquivo;
				String porta;
				
				
				// Realizo a itera��o sobre a lista que no caso � todos os Servidor Arquivo que possue o arquivo
				System.out.println("\nLista de Servidores que possuem o arquivo:");
				for(String item : listaRespostaUDPServidorArquivo) {
					
					textoSeparado = item.split("&");
					nome = textoSeparado[0].replace("[", "");
					porta = textoSeparado[1];
					ipUDPServidorArquivo = textoSeparado[2];
					System.out.println(nome + " - " + porta + " - " + ipUDPServidorArquivo);
					
				}
				
				
				/*Pergunta ao Cliente de qual TCPServidorArquivo ele quer baixar
				Scanner ler = new Scanner(System.in);
				System.out.print("\nInforme o IP do TCPServidorArquivo: ");
				String ipDestino = ler.nextLine();
			
			
				System.out.print("Informe o diret�rio onde deseja salvar o arquivo: ");
				String diretorio = ler.nextLine();
				
				
				System.out.print("Informe a porta TCP do Servidor Arquivo: ");
				String port = ler.nextLine();
				int portaTCP = Integer.parseInt(port);
				ler.close();*/
				
				int numeroPortaTCP = Integer.parseInt(portaTCP);
				
				
				try {
					
					// Estabelece conex�o com o TCPServidorArquivo (Endere�o IP, Localhost e PORTA 3334)
					Socket clientSocket = new Socket(ipDestino, numeroPortaTCP);
					
					
					// Pergunto ao Usuario qual arquivo ele quer verificar a exist�ncia com o TCPServidorArquivo
					DataOutputStream toServer = new DataOutputStream(clientSocket.getOutputStream());
				
					
					// Envio o Nome do Arquivo para o TCPServidorArquivo
					toServer.writeBytes(nomeArquivo + '\n');
					
			
					// Aguardando mensagem de retorno do TCPServidorArquivo
					InputStream response = clientSocket.getInputStream();
	
					
					
					
					// Criando arquivo que sera recebido pelo servidor
					FileOutputStream fileOut = new FileOutputStream(diretorio + "\\" + nomeArquivo);
					
					// Criando canal de transferencia
					InputStream socketIn = clientSocket.getInputStream();
					
					// Configurando arquivo recebido pelo TCPServidorArquivo
					byte[] cbuffer = new byte[1024];
					int bytesRead;
					
					while ((bytesRead = response.read(cbuffer)) != -1) {
						fileOut.write(cbuffer, 0, bytesRead);
					}
					
					String mensagemFinal = "\nArquivo: " + nomeArquivo + " recebido com sucesso!\nSalvo em: " + diretorio +"\nEncerrando conex�o...";
					
					
					
					// Fechando conex�o TCP com TCPServidorArquivo
					clientSocket.close();
					
					return mensagemFinal;
					
				} catch(OutOfMemoryError e) {
					return "Eita, o arquivo � muito grande!";
					
				} catch(Exception e) {
					e.printStackTrace();
				}
						
			} else {
				
				// Informa ao Cliente que nenhum Servidor de Arquivo possui o arquivo
				return "Nenhum Servidor Arquivo possui o arquivo solicitado!";
				
			}	
		return "IP, diret�rio ou porta n�o encontrados.";
	}
	
}
