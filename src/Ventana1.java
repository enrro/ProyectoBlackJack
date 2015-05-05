import java.awt.BorderLayout;

import javax.swing.JFrame;


public class Ventana1 extends JFrame{
	public Ventana1(){
		super("Mi primer ventana");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Naipe carta= new Naipe(12,2);
//		Panelprueba panelCarta=new Panelprueba(carta.getImage());
//		this.add(panelCarta);
		this.pack();
		this.setVisible(true);
		System.out.println(carta);
		System.out.println(carta.getValor());
	}
	
	public static void main(String[] args){
		Ventana1 ventana = new Ventana1();
	}
	
	
}
