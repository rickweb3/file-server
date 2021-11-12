import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;   

public class BarraDeProgresso extends JFrame{

	JProgressBar barra = new JProgressBar();
	private static int bytes;

	public BarraDeProgresso() {
		this.bytes = bytes;
		configurarJanela();
		barra.setBounds(40, 40, 500, 50); 
		barra.setStringPainted(true);
		barra.setValue(50);
		barra.setMaximum(1000);
		barra.setForeground(new Color(50, 200, 50) );
		add(barra);
		new Temporizador().start();
	}

	public void configurarJanela() {
		setLayout(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(600,170);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new BarraDeProgresso();
	}
	
	public class Temporizador extends Thread{		
		public void run() {
			while(barra.getValue()<1000) {
				try {
					sleep(10);
					barra.setValue(barra.getValue()+10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			JOptionPane.showMessageDialog(null, "Baixado!");
		}
	}
}


