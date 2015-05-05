import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class Naipe extends JPanel {
	private static final String[] valores={"As","2","3","4","5","6","7","8","9","10","J","Q","K"};
	private static final String[] figuras={"espadas","corazones","tréboles","diamantes"};
	private static final Image[] naipesImg={new ImageIcon("1e.png").getImage(),
											new ImageIcon("2e.png").getImage(),
											new ImageIcon("3e.png").getImage(),
											new ImageIcon("4e.png").getImage(),
											new ImageIcon("5e.png").getImage(),
											new ImageIcon("6e.png").getImage(),
											new ImageIcon("7e.png").getImage(),
											new ImageIcon("8e.png").getImage(),
											new ImageIcon("9e.png").getImage(),
											new ImageIcon("10e.png").getImage(),
											new ImageIcon("11e.png").getImage(),
											new ImageIcon("12e.png").getImage(),
											new ImageIcon("13e.png").getImage(),
											new ImageIcon("1c.png").getImage(),
											new ImageIcon("2c.png").getImage(),
											new ImageIcon("3c.png").getImage(),
											new ImageIcon("4c.png").getImage(),
											new ImageIcon("5c.png").getImage(),
											new ImageIcon("6c.png").getImage(),
											new ImageIcon("7c.png").getImage(),
											new ImageIcon("8c.png").getImage(),
											new ImageIcon("9c.png").getImage(),
											new ImageIcon("10c.png").getImage(),
											new ImageIcon("11c.png").getImage(),
											new ImageIcon("12c.png").getImage(),
											new ImageIcon("13c.png").getImage(),
											new ImageIcon("1t.png").getImage(),
											new ImageIcon("2t.png").getImage(),
											new ImageIcon("3t.png").getImage(),
											new ImageIcon("4t.png").getImage(),
											new ImageIcon("5t.png").getImage(),
											new ImageIcon("6t.png").getImage(),
											new ImageIcon("7t.png").getImage(),
											new ImageIcon("8t.png").getImage(),
											new ImageIcon("9t.png").getImage(),
											new ImageIcon("10t.png").getImage(),
											new ImageIcon("11t.png").getImage(),
											new ImageIcon("12t.png").getImage(),
											new ImageIcon("13t.png").getImage(),
											new ImageIcon("1d.png").getImage(),
											new ImageIcon("2d.png").getImage(),
											new ImageIcon("3d.png").getImage(),
											new ImageIcon("4d.png").getImage(),
											new ImageIcon("5d.png").getImage(),
											new ImageIcon("6d.png").getImage(),
											new ImageIcon("7d.png").getImage(),
											new ImageIcon("8d.png").getImage(),
											new ImageIcon("9d.png").getImage(),
											new ImageIcon("10d.png").getImage(),
											new ImageIcon("11d.png").getImage(),
											new ImageIcon("12d.png").getImage(),
											new ImageIcon("13d.png").getImage()};
	private static final Image dorsoImg=new ImageIcon("Fondo_carta.png").getImage();
	
	private int figura;
	private int valor;
	private boolean cambio;					//Para cambiar el valor del as en caso de que la suma pase 21
	
	public Naipe(int valor, int figura){
		this.valor=valor;
		this.figura=figura;
		this.cambio=false;
	}
	
	public String toString(){
		return this.valores[this.valor] +" de "+ this.figuras[this.figura];
	}
	
	public Image getImage(){
		int naipeImage;
		if(this.figura==0){
			naipeImage=this.valor;
		}
		else if(this.figura==1){
			naipeImage=this.valor+13;
		}
		else if(this.figura==2){
			naipeImage=this.valor+26;
			
		}
		else{
			naipeImage=this.valor+39;
		}
		return this.naipesImg[naipeImage];
	}
	
	public Image getDorsoImg(){
		return this.dorsoImg;
	}
	
	public int getValor(){
		if(this.valor>0 && this.valor<10){
			return this.valor+1;
		}
		else if(this.valor==0){
			if(this.cambio){
				return this.valor+1;
			}
			return this.valor+11;
		}
		else{
			return 10;
		}
	}
	
	public void setCambio(){
		this.cambio=true;
	}
	
	public int getValGuard(){				//Tomar el valor para el guardado
		return this.valor;
	}
	
	public int getFigura(){
		return this.figura;
	}
	
}
