import java.awt.BorderLayout;

import javax.swing.JFrame;


public class Ventana extends JFrame{
	public Ventana(){
		super("BlackJack");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // cerrar proceso en consola cuando se cierra ventana
		
		BlackJack panelDibujo = new BlackJack();
		this.add(panelDibujo);
		
		this.pack();
		this.setVisible(true);
		
	}
	public static void main(String[] args) {
		boolean error;
		do{
			try {
				new Ventana();
				error =false;
			} catch (NumberFormatException  ex) {
				error = true;
				System.out.println(ex);
			}
		}
		while(error);
		
	}

}
