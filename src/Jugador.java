import javax.swing.JOptionPane;


public class Jugador {

	private int saldo;
	private String nombre;
	private boolean perdio, ganoPartida, empate;    	//ganoPartida para saber si gana la partida actual o empate
	private Naipe[] juego;								
	
	public Jugador(int saldo, String nombre){
		this.saldo=saldo;
		this.nombre=nombre;
		this.perdio=false;
		this.juego=new Naipe[10];
		this.ganoPartida=false;	
		this.empate=false;
	}
	
	public int getSaldo(){
		return this.saldo;
	}
	
	public String getNombre(){
		return this.nombre;
	}
	
	public boolean getPerdio(){
		return this.perdio;
	}
	
	public int getTotal(){
		int sum=0;		
										
		for(int j=0;j<juego.length;j++) {		//Se recorre todo el arreglo
			if(this.juego[j]!=null){			//Si hay carta en la posicion actual, suma
				sum+=this.juego[j].getValor();
			}
		}					
		return sum;
	}
	
	public void ganoPartida(int apuesta){
		this.saldo+=2*apuesta;
	}
	
	public void perdioPartida(int apuesta){
		this.saldo-=apuesta;
	}
	
	public void empatoPartida(int apuesta){
		this.saldo+=apuesta;
	}
	
	public void perdioDinero(){
		this.perdio=true;
	}
	
	public boolean otraCarta(boolean decision){
		if(decision){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isBlackjack(){
		int sum=0;
		if(this.juego[1]!=null && this.juego[2]==null){
			if((this.juego[0].getValor()+this.juego[1].getValor())==21){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
	}
	
	public void setJuego(Naipe carta){
		int i=0;
		et: while(true){					//Revisa desde la posicion 0
			if(this.juego[i]==null){		//Si no hay carta en la posicion actual, guarda la carta que se pasa
				this.juego[i]=carta;
				break et;					//Sale del ciclo para solo guardar una vez
			}
			i++;
		}
	}
	
	public int numNaipes(){
		int isCarta = 0;
		for (int i = 0; i < juego.length; i++) {
			if (juego[i]!=null) {
				isCarta++;
			}
		}
		return isCarta;
	}
	
	public Naipe regresaNaipe(int num){				//Para acceder al valor de cada naipe
		return this.juego[num];
	}
	
	public void borraNaipe(){
		this.juego=new Naipe[10];
	}
	
	public void setGanoPartida(boolean a){			//Para hacer saber si gana la partida actual
		if(a){
			this.ganoPartida=true;
		}
		else{
			this.ganoPartida=false;
		}
	}
	
	public boolean getGanoPartida(){
		return this.ganoPartida;
	}
	
	public void setEmpatePartida(boolean a){
		if(a){
			this.empate=true;
		}
		else{
			this.empate=false;
		}
	}
	
	public boolean getEmpatePartida(){
		return this.empate;
	}
	
	public void setPerdio(boolean valor){			//Para el guardado
		this.perdio=valor;
	}
	
	
	
}
