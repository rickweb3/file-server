import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class TelaClienteBaixar extends JFrame {

	private JPanel contentPane;
	private JTextField txtIP;
	private JTextField txtDiretorio;
	private JTextField txtPorta;
	private static String nomeArquivo;
	private static ArrayList<String> listaRespostaUDPServidorArquivo;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try { 
					TelaClienteBaixar frame = new TelaClienteBaixar(nomeArquivo,listaRespostaUDPServidorArquivo);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TelaClienteBaixar(String nomeArquivo, ArrayList<String> listaRespostaUDPServidorArquivo) {
		
		this.nomeArquivo = nomeArquivo;
		this.listaRespostaUDPServidorArquivo = listaRespostaUDPServidorArquivo;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Lista de Servidores de Arquivos:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel.setBounds(106, 11, 208, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Diretório onde deseja salvar...: ");
		lblNewLabel_1.setBounds(26, 161, 180, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Porta do Servidor de Arquivos:");
		lblNewLabel_2.setBounds(26, 186, 180, 14);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("IP do Servidor de Arquivos......:");
		lblNewLabel_3.setBounds(26, 136, 180, 14);
		contentPane.add(lblNewLabel_3);
		
		JTextArea txtServerList = new JTextArea();
		txtServerList.setBounds(26, 36, 384, 87);
		contentPane.add(txtServerList);
		
		txtIP = new JTextField();
		txtIP.setBounds(216, 133, 194, 20);
		contentPane.add(txtIP);
		txtIP.setColumns(10);
		
		txtDiretorio = new JTextField();
		txtDiretorio.setBounds(216, 158, 194, 20);
		contentPane.add(txtDiretorio);
		txtDiretorio.setColumns(10);
		
		txtPorta = new JTextField();
		txtPorta.setBounds(216, 183, 194, 20);
		contentPane.add(txtPorta);
		txtPorta.setColumns(10);
		
		// Variáveis que serão utilizadas durante a iteração da lista que contém a resposta
		String[] textoSeparado;
		String nome;
		String ipUDPServidorArquivo;
		String porta;
		
		// Realizo a iteração sobre a lista de todos os Servidores de Arquivo que possui o arquivo
		for(String item : listaRespostaUDPServidorArquivo) {
			
			textoSeparado = item.split("&");
			nome = textoSeparado[0].replace("[", "");
			porta = textoSeparado[1];
			ipUDPServidorArquivo = textoSeparado[2];
			txtServerList.append(nome + " - " + porta + " - " + ipUDPServidorArquivo + "\n");	
		}
		
		//String listString = String.join("\n ", listaRespostaUDPServidorArquivo);
		
		//Verifica se o arraylist não possui lista de servidores
		if(listaRespostaUDPServidorArquivo.isEmpty()){
			
			txtServerList.append("Nenhum Servidor Arquivo possui o arquivo solicitado!");
		}
		
		txtServerList.setEditable(false);
		
		JButton btnBaixar = new JButton("Baixar ");
		btnBaixar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Cliente cliente = new Cliente();
				
				try {
					// Passando todos os parâmetros para método baixarArquivo de Cliente e pegando retorno com mensagens 
					String mensagens = cliente.baixarArquivo(
							txtIP.getText(),
							txtDiretorio.getText(),
							txtPorta.getText(),
							nomeArquivo,
							listaRespostaUDPServidorArquivo);
					
					//Atualiza textArea 
					txtServerList.setEditable(true);
					txtServerList.setText("");
					txtServerList.append(mensagens);
					txtServerList.setEditable(false);
				
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnBaixar.setBounds(180, 211, 89, 23);
		contentPane.add(btnBaixar);
		
		
	}

}
