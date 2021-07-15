

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class TelaInicialCliente {

	private JFrame frame;
	private JTextField txtNome;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaInicialCliente window = new TelaInicialCliente();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TelaInicialCliente() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Cliente");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel.setBounds(176, 55, 78, 28);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Nome do arquivo(ex: arquivo.txt):");
		lblNewLabel_1.setBounds(121, 94, 196, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		txtNome = new JTextField();
		txtNome.setBounds(134, 119, 165, 20);
		frame.getContentPane().add(txtNome);
		txtNome.setColumns(10);
		
		JButton btnNome = new JButton("Procurar");
		btnNome.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				Cliente cliente = new Cliente();
				
				try {
					// guarda o arraylist com os servidores de arquivos e passa o nome do arqivo pesquisado
					ArrayList<String> listaRespostaUDPServidorArquivo = cliente.requisitarArquivo(txtNome.getText());
					
					// chama a tela de baixar arquivos e passa o nome do arquivo e a lista de servidores de arquivos  
					TelaClienteBaixar janelaBaixar = new TelaClienteBaixar(txtNome.getText(),listaRespostaUDPServidorArquivo);
					janelaBaixar.show();
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnNome.setBounds(165, 161, 89, 23);
		frame.getContentPane().add(btnNome);
		
		
	}
}
