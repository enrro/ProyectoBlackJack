
public class Baraja {

	private Naipe[] baraja;				
	private int posicion;
	
	public Baraja(){
		this.posicion=-1;
		this.baraja=new Naipe[52];
		int num=0;
		for(int j=0;j<4;j++){
			for(int i=0;i<13;i++){
				this.baraja[num]=new Naipe(i,j);		//Inicializa cada naipe en orden desde el lugar 0 hasta el 51
				num++;
			}
		}
		num=0;
	}
	
	public void mezclar(){
		int rnd;
		Naipe temp;
		for(int i=0;i<52;i++){
			rnd=(int)(Math.random()*52);		//Genera número random entre 0 y 51
			temp=this.baraja[i];				//Guarda el valor actual del naipe en un temporal
			this.baraja[i]=this.baraja[rnd];	//Guarda el valor de un lugar random de naipe en el lugar actual
			this.baraja[rnd]=temp;				//Guarda el temporal en el lugar random
		}
	}
	
	public Naipe next(){
		this.posicion++;
		return this.baraja[this.posicion];
	}
	
	public Naipe getBaraja(int naipe){
		return this.baraja[naipe];
	}
	
	public int getPos(){
		return this.posicion;			//Para archivo de guardado
	}
	
	public void setBaraja(int pos, int valor, int figura){			//Para guardado de baraja anterior
		this.baraja[pos]=new Naipe(valor,figura);
	}
	
	public void setPos(int pos){				//Para guardado de baraja anterior
		this.posicion=pos;
	}
	
	
}
