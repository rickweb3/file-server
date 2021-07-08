import java.net.BindException;
import java.net.ServerSocket;
import java.util.Scanner;

public class ServidorArquivo {

	public static void main(String args[]) throws Exception {
		
		
		// O range da porta TCP que será utilizada começará a partir da PORTA 10000
		int portaTCP = 10000;
				

		// Verifica qual a próxima porta disponível para a comunicação TCP
		while(true) {
			
			try {
					
				var ignored = new ServerSocket(portaTCP);
				ignored.close();
				break;
				
			} catch(BindException e) {
				portaTCP++;	
			}
		}
		
		
		System.out.println("Servidor Arquivo");

		
		// Assim que o TCPServidorArquivo inicia, já peço o Diretório PADRÃO dele
		Scanner ler = new Scanner(System.in);
		System.out.print("\nInforme o diretório padrão de arquivos: ");
		String diretorio = ler.nextLine();
		ler.close();
		
		
		// Iniciando Thread UDP
		Thread threadUDP = new Thread(new ThreadUDPServidorArquivo(diretorio, portaTCP));
		threadUDP.start();
		
		
		// Iniciando Thread TCP
		Thread threadTCP = new Thread(new ThreadTCPServidorArquivo(diretorio, portaTCP));
		threadTCP.start();
		
	}
	
}
