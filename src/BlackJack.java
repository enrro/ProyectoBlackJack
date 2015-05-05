import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class BlackJack extends JPanel implements Runnable, ActionListener {
	private int x,
	y;
	private boolean start;
	private Color mesaVerde = new Color(0,153,76);
	private Color mesaCafe = new Color(102,51,0);
	private int noPlayers;
	private Jugador[] jugadores;
	private int apuesta;
	private int montoMeta;
	private Dealer dealer;
	private boolean imprimirCartaGrafica;
	//private boolean ganador=false;
	private JButton jbUnJugador,
					jbDosJugadores,
					jbTresJugadores,
					jbCuatroJugadores,
					jbGuardar,
					jbCargar,
					jbJugar,
					jbPedirCarta,
					jbMantenerCarta;

	private Baraja bar;
	private boolean pintarJugadores;
	private boolean pedir, mantener;
	private int nomJugador;
	private int ganadorFinal;
	private boolean oculto;	


	public BlackJack(){
		super();
		this.x = 1200;
		this.y = 800;
		this.imprimirCartaGrafica = false;
		this.setPreferredSize(new Dimension(x,y));
		this.dealer=new Dealer();
		//cambia el tablero para que los botones funcionen de manera relativa
		this.setLayout(null);
		this.pintarJugadores=false;
		this.start=false;
		this.pedir=this.mantener=false;
		this.nomJugador=5;					//Para no poner el turno todavia hasta que se reparten cartas
		this.ganadorFinal=5;				//En caso de que nadie gane
		this.oculto=true;					//Ocultar la segunda carta del Dealer
		Thread hilo = new Thread(this);
		hilo.start();
		this.bar=new Baraja();
		bar.mezclar();

		//arreglar los botones de carga, guardar y jugar
		this.jbJugar = new JButton("Jugar");
		this.add(this.jbJugar);
		this.jbJugar.setBounds(x/2-50,y/2-50,100,30);
		this.jbJugar.setVisible(false);
		this.jbCargar = new JButton("Cargar partida");
		this.add(this.jbCargar);
		this.jbCargar.setBounds(x-180, 0, 180, 30);
		this.jbCargar.setVisible(true);
		this.jbGuardar = new JButton("Guardar partida");
		this.add(this.jbGuardar);
		this.jbGuardar.setVisible(false);
		this.jbGuardar.setBounds(0, 0, 180, 30);

		//arreglar los botones de los jugadores
		this.jbUnJugador = new JButton("Un jugador");
		this.add(this.jbUnJugador);
		this.jbUnJugador.setBounds(x/2-90, y/2, 180, 30);
		this.jbDosJugadores = new JButton("Dos jugadores");
		this.add(this.jbDosJugadores);
		this.jbDosJugadores.setBounds(x/2-90, y/2+30, 180, 30);
		this.jbTresJugadores = new JButton("Tres jugadores");
		this.add(this.jbTresJugadores);
		this.jbTresJugadores.setBounds(x/2-90, y/2+60, 180, 30);
		this.jbCuatroJugadores = new JButton("Cuatro jugadores");
		this.add(this.jbCuatroJugadores);
		//arreglar los botones del pedir y mantener
		this.jbCuatroJugadores.setBounds(x/2-90, y/2+90, 180, 30);
		this.jbPedirCarta = new JButton("Pedir");
		this.add(this.jbPedirCarta);
		this.jbPedirCarta.setVisible(false);
		this.jbMantenerCarta = new JButton("Mantener");
		this.add(this.jbMantenerCarta);
		this.jbMantenerCarta.setVisible(false);
		//agregar los actionListener de los botones
		this.jbUnJugador.addActionListener(this);
		this.jbDosJugadores.addActionListener(this);
		this.jbTresJugadores.addActionListener(this);
		this.jbCuatroJugadores.addActionListener(this);
		this.jbMantenerCarta.addActionListener(this);
		this.jbPedirCarta.addActionListener(this);
		this.jbCargar.addActionListener(this);
		this.jbGuardar.addActionListener(this);
		this.jbJugar.addActionListener(this);

	}

	
	public Jugador nombreJugadores(int jugador){
		String nombre;
		String saldo;
		int saldo1;
		nombre = JOptionPane.showInputDialog("Introduce el nombre del jugador " + jugador);
		if (nombre == null) {
			System.exit(0);
		}
		do {
			saldo = JOptionPane.showInputDialog("Introduce el saldo del jugador " + jugador);
			if (saldo == null) {
				System.exit(0);
			}
		} while (!isInteger(saldo));

		saldo1=Integer.parseInt(saldo);

		return new Jugador(saldo1,nombre);
	}

	public void dormirJuego(){
		this.repaint();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void pintarNaipes(Graphics g, int jugadorNumero, int x, int y){
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.WHITE);
		RoundRectangle2D rect1 = new RoundRectangle2D.Float(x, y, 80, 100, 5, 5);
		g2d.draw(rect1);
		jugadorNumero = jugadorNumero- 1;

		//Dibuja los naipes uno junto al otro
		if (imprimirCartaGrafica) {
			for (int naipe = 0; naipe < this.jugadores[jugadorNumero].numNaipes(); naipe++) {
				g.drawImage(
						this.jugadores[jugadorNumero].regresaNaipe(naipe).getImage(),
						x + 40 * naipe, y, 85, 108, this);
			}
		}

		//Dibuja el ganar o perder
		g.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		if(this.jugadores[jugadorNumero].getGanoPartida()){
			g.setColor(Color.YELLOW);
			g.drawString("GANADOR", x, y-75);
		}
		if(this.jugadores[jugadorNumero].getEmpatePartida()){
			g.setColor(Color.BLUE);
			g.drawString("EMPATE", x, y-75);
		}

		//Dibuja la informacion del usuario
		g.setColor(Color.WHITE);
		g.drawString(this.jugadores[jugadorNumero].getNombre(), x, y-50);
		g.drawString('$'+String.valueOf(this.jugadores[jugadorNumero].getSaldo()), x, y+130);
		g.drawString('$'+String.valueOf(this.apuesta), x+40, y-20);

		//Coloca los botones del jugador
		this.jbMantenerCarta.setBounds(x,y-160,100,20);
		this.jbPedirCarta.setBounds(x,y-140,100,20);

	}
	private void pintarNaipesDealer(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		x= (int)(this.getWidth()/2-40);
		y= (int)(this.getHeight()*.2);
		g2d.setColor(Color.WHITE);
		RoundRectangle2D rect1 = new RoundRectangle2D.Float(x, y, 80, 100, 5, 5);
		g2d.draw(rect1);
		if (imprimirCartaGrafica) {
			for (int naipe = 0; naipe < this.dealer.numNaipes(); naipe++) {
				if(this.oculto){
					if(naipe==1){
						g.drawImage(this.dealer.regresaNaipe(naipe).getDorsoImg(),x + 40 * naipe, y, 85, 108, this);
					}
					else{
						g.drawImage(this.dealer.regresaNaipe(naipe).getImage(),x + 40 * naipe, y, 85, 108, this);
					}
				}
				else{
					g.drawImage(this.dealer.regresaNaipe(naipe).getImage(),x + 40 * naipe, y, 85, 108, this);
				}
			}
		}
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		if(this.ganadorFinal==5){
			g.setColor(this.mesaCafe);
			g.fillOval(-((int)(this.getWidth()*1.25)-this.getWidth())/2,-300,(int)(this.getWidth()*1.25),(int)(this.getHeight()*1.38+100));
			g.setColor(this.mesaVerde);
			g.fillOval(-((int)(this.getWidth()*1.25)-this.getWidth())/2,-300,(int)(this.getWidth()*1.25),(int)(this.getHeight()*1.38));
			g.setColor(Color.WHITE);
			g.fillOval(-((int)(this.getWidth()*1.25)-this.getWidth())/2,-300,(int)(this.getWidth()*1.25),(int)(this.getHeight()*.78));
			g.setColor(this.mesaVerde);
			g.fillOval(-((int)(this.getWidth()*1.25)-this.getWidth())/2,-300,(int)(this.getWidth()*1.25),(int)(this.getHeight()*.74));
			g.setColor(Color.WHITE);
			g.setFont(new Font("Times New Roman", Font.PLAIN, 50));
			g.drawString("Gran Casino ISC", this.getWidth()/2-170,this.getHeight()/5-50);

			//Dibujar Naipes
			this.pintarNaipesDealer(g);

			//Dibujar monto meta
			if (montoMeta!=0) {
				g.setFont(new Font("Times New Roman", Font.PLAIN, 40));
				g.drawString("Se gana con $" + montoMeta, this.getWidth()/2-115,(int)(this.getHeight()*.54));
			}

			//Coloca el nombre del jugador en turno
			if(this.nomJugador!=5){
				g.setFont(new Font("Times New Roman", Font.PLAIN, 35));
				g.drawString("Turno de "+this.jugadores[this.nomJugador].getNombre(), (int)(this.getWidth()*0.1), (int)(this.getHeight()*0.2));
			}


			//Dibujar los jugadores
			if(this.pintarJugadores){		//Solo cuando se dieron sus datos se dibujan
				switch (noPlayers) {
				case 1:
					int[] posicion = {this.getWidth()/2-40,(int)(this.getHeight()*.8)};
					this.pintarNaipes(g,1,posicion[0],posicion[1]);

					break;
				case 2:
					int[] posicion1 = {this.getWidth()/4-80,(int)(this.getHeight()*.7),
							this.getWidth()/4*3-80,(int)(this.getHeight()*.7)};

					g2d.rotate(Math.toRadians(20),posicion1[0], posicion1[1]);
					this.pintarNaipes(g, 1, posicion1[0], posicion1[1]);
					g2d.rotate(Math.toRadians(-20),posicion1[0], posicion1[1]);

					g2d.rotate(Math.toRadians(-20),posicion1[2], posicion1[3]);
					this.pintarNaipes(g, 2, posicion1[2], posicion1[3]);
					g2d.rotate(Math.toRadians(20),posicion1[2], posicion1[3]);

					break;
				case 3:
					int[] posicion2 = {this.getWidth()/4-80,(int)(this.getHeight()*.7),
							this.getWidth()/2-60, (int)(this.getHeight()*.78),
							3*this.getWidth()/4-80, (int)(this.getHeight()*.7)};

					g2d.rotate(Math.toRadians(20),posicion2[0], posicion2[1]);
					this.pintarNaipes(g, 1, posicion2[0], posicion2[1]);
					g2d.rotate(Math.toRadians(-20),posicion2[0], posicion2[1]);

					this.pintarNaipes(g, 2, posicion2[2], posicion2[3]);

					g2d.rotate(Math.toRadians(-20),posicion2[4], posicion2[5]);
					this.pintarNaipes(g, 3, posicion2[4], posicion2[5]);
					g2d.rotate(Math.toRadians(20),posicion2[4], posicion2[5]);

					break;
				case 4:
					g2d.setColor(Color.WHITE);
					int[] posicion3 = {this.getWidth()/8-80,
							(int)(this.getHeight()*.6),
							3*this.getWidth()/8-80,
							(int)(this.getHeight()*.75),
							5*this.getWidth()/8-80,
							(int)(this.getHeight()*.78),
							7*this.getWidth()/8-80,
							(int)(this.getHeight()*.63)};

					g2d.rotate(Math.toRadians(30),posicion3[0],posicion3[1]);
					this.pintarNaipes(g, 1, posicion3[0], posicion3[1]);
					g2d.rotate(Math.toRadians(-30),posicion3[0],posicion3[1]);

					g2d.rotate(Math.toRadians(12),posicion3[2],posicion3[3]);
					this.pintarNaipes(g, 2, posicion3[2], posicion3[3]);
					g2d.rotate(Math.toRadians(-12),posicion3[2],posicion3[3]);

					g2d.rotate(Math.toRadians(-12),posicion3[4],posicion3[5]);
					this.pintarNaipes(g, 3, posicion3[4], posicion3[5]);
					g2d.rotate(Math.toRadians(12),posicion3[4],posicion3[5]);

					g2d.rotate(Math.toRadians(-30),posicion3[6],posicion3[7]);
					this.pintarNaipes(g, 4, posicion3[6], posicion3[7]);
					g2d.rotate(Math.toRadians(30),posicion3[6],posicion3[7]);

					break;

				default:
					break;
				}
			}
		}
		else if(this.ganadorFinal==6){
			this.jbCargar.setVisible(false);
			this.jbGuardar.setVisible(false);
			this.jbPedirCarta.setVisible(false);
			this.jbMantenerCarta.setVisible(false);
			this.setBackground(this.mesaVerde);
			g2d.setColor(Color.YELLOW);
			g.setFont(new Font("Garamond", Font.PLAIN, 100));
			g.drawString("Todos perdieron!", 250,this.getHeight()/2);
		}
		else{
			this.jbCargar.setVisible(false);
			this.jbGuardar.setVisible(false);
			this.jbPedirCarta.setVisible(false);
			this.jbMantenerCarta.setVisible(false);
			this.setBackground(this.mesaVerde);
			g2d.setColor(new Color(153,196,213));
			g.setFont(new Font("Times New Roman", Font.PLAIN, 100));
			g.drawString("El jugador "+(this.ganadorFinal+1)+" gana el juego!", 50,this.getHeight()/2);

		}
	}

	/*public void setGanador(){
		this.ganador=true;
	}*/

	public int getNoPlayers() {
		return noPlayers;
	}

	public void montoApuesta(){
		int saldoMin=this.saldoMinimo();
		String stringTemporal;
		do{
			do {
				stringTemporal = JOptionPane.showInputDialog("Ingrese la apuesta ");
				if (stringTemporal == null) {
					System.exit(0);
				}
			} while (!isInteger(stringTemporal));
			this.apuesta = Integer.parseInt(stringTemporal);
			if(this.apuesta>saldoMin){
				System.out.println("La apuesta debe ser menor al saldo minimo " + saldoMin);
			}
		}while(this.apuesta>saldoMin);
	}

	public int getApuesta(){
		return this.apuesta;
	}

	public void jugadores(){
		this.jugadores = new Jugador[this.getNoPlayers()];
		for(int i=0;i<this.getNoPlayers();i++){
			this.jugadores[i]=this.nombreJugadores(i+1);
		}

	}

	public int saldoMinimo(){
		int menorSaldo=jugadores[0].getSaldo();
		for(int i=0; i<this.getNoPlayers();i++){
			menorSaldo=menorSaldo<jugadores[i].getSaldo()?menorSaldo:jugadores[i].getSaldo();
		}
		return menorSaldo;
	}

	public int saldoMaximo(){
		int mayorSaldo=jugadores[0].getSaldo();
		for(int i=0; i<this.noPlayers;i++){
			mayorSaldo=mayorSaldo>jugadores[i].getSaldo()?mayorSaldo:jugadores[i].getSaldo();
		}
		return mayorSaldo;
	}

	public void crearMM(){					//Metodo para preguntar el monto para ganar
		int saldoMax=this.saldoMaximo();
		String stringTemporal;
		do{
			do{
				stringTemporal = JOptionPane.showInputDialog("Ingrese la cantidad para ganar mayor a "+saldoMax);
				if (stringTemporal == null) {
					System.exit(0);
				}
			}while(!this.isInteger(stringTemporal));
			this.montoMeta = Integer.parseInt(stringTemporal);
			if(this.montoMeta<saldoMax){
				System.out.println("La cantidad debe ser mayor al saldo maximo " + saldoMax);
			}
		}while(this.montoMeta<saldoMax);
	}

	public int getMontoMeta(){
		return this.montoMeta;
	}

	public Jugador getJugadores(int num) {
		return this.jugadores[num];
	}

	public Dealer getDealer() {
		return this.dealer;
	}
	
	public Baraja getBaraja(){
		return this.bar;
	}
	
	public boolean isInteger(String str) {
		if (str == null) {
			return false;
		}
		int length = str.length();
		if (length == 0) {
			return false;
		}
		int i = 0;
		if (str.charAt(0) == '-') {
			if (length == 1) {
				return false;
			}
			i = 1;
		}
		for (; i < length; i++) {
			char c = str.charAt(i);
			if (c <= '/' || c >= ':') {
				return false;
			}
		}
		return true;
	}
	
	public void mezclarBaraja(){
		this.bar=new Baraja();
		this.bar.mezclar();
	}

	public void imprimirBaraja(Baraja bar){
		//Imprimir baraja
		for(int i=0;i<52;i++){
			System.out.println(bar.getBaraja(i));
		}
		System.out.println();
	}

	private void imprimirCartasJug(){
		//Imprime cartas de cada jugador
		for(int i=0;i<this.getNoPlayers();i++){
			System.out.println("Jugador "+(i+1));
			for(int j=0; j<5;j++){
				System.out.println(this.getJugadores(i).regresaNaipe(j));
			}
			System.out.println();
		}
		System.out.println("---");

	}

	public void imprimirCartasDealer(){
		System.out.println("Cartas Dealer");
		for(int h=0;h<5;h++){
			System.out.println(this.getDealer().regresaNaipe(h));
		}
		System.out.println("---");
	}
	
	public void guardar(){
		try {
			PrintWriter pw=new PrintWriter(new FileWriter("actual.txt"));
			//Guarda los valores y las figuras de los naipes
			for(int i=0;i<52;i++){
				pw.println(this.bar.getBaraja(i).getValGuard());
				pw.println(this.bar.getBaraja(i).getFigura());
			}
			
			//Guarda la posicion de baraja
			pw.println(this.bar.getPos());
			
			//Guarda los jugadores
			pw.println(this.noPlayers);
			for(int i=0;i<this.getNoPlayers();i++){
				pw.println(this.jugadores[i].getSaldo());
				pw.println(this.jugadores[i].getNombre());
				pw.println(this.jugadores[i].getPerdio());
				/*for(int j=0;j<this.jugadores[i].numNaipes();j++){
					pw.println(this.jugadores[i].regresaNaipe(j).getValGuard());
					pw.println(this.jugadores[i].regresaNaipe(j).getFigura());
				}*/
			}
			
			//Guarda apuesta, montoMeta
			pw.println(this.apuesta);
			pw.println(this.montoMeta);
			pw.close();																		
		} catch (IOException e) {
			System.out.println("Error de archivo: "+e);
		}
	}
	
	public void abrir(){
		int decision=JOptionPane.showConfirmDialog(null,"¿Desea seguir la partida anterior?"," ",JOptionPane.YES_NO_OPTION);
		if(decision==JOptionPane.YES_OPTION){
			try {
				BufferedReader br=new BufferedReader(new FileReader("actual.txt"));					//Se crea el file reader y se pasa como parametro
				String linea;
				//Set de la baraja
				for(int i=0;i<52;i++){
					this.bar.setBaraja(i,Integer.parseInt(br.readLine()),Integer.parseInt(br.readLine()));
				}
				
				//Set de la posicion
				this.bar.setPos(Integer.parseInt(br.readLine()));
				
				//Set no. de jugadores
				this.noPlayers=Integer.parseInt(br.readLine());
				this.jugadores = new Jugador[this.getNoPlayers()];
				for(int i=0;i<this.getNoPlayers();i++){
					this.jugadores[i] = new Jugador(Integer.parseInt(br.readLine()),br.readLine());
					this.jugadores[i].setPerdio(Boolean.parseBoolean(br.readLine()));
				}
				
				//Set apueta y montoMeta
				this.apuesta=Integer.parseInt(br.readLine());
				this.montoMeta=Integer.parseInt(br.readLine());
				
				br.close();
			} catch (FileNotFoundException e) {												//\Caracter de escape, tomarlo como diagonal invertida entonces se pone doble diagonal
				System.out.println("El arhcivo no se encuentra");
			}
			catch(IOException e){
				System.out.println("No se pudo leer el archivo "+e);
			}
		
		}
		else{
			this.jbUnJugador.setVisible(true);
			this.jbDosJugadores.setVisible(true);
			this.jbTresJugadores.setVisible(true);
			this.jbCuatroJugadores.setVisible(true);
			this.jbJugar.setVisible(false);
			this.jbCargar.setVisible(true);
		}

	}


	public static void main(String[] args){
		JFrame frame  = new JFrame("BlackJack");	
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		BlackJack tablero = new BlackJack(); 
		frame.add(tablero);		
		//Baraja bar=new Baraja();
		//bar.mezclar();
		frame.pack();		
		frame.setVisible(true); 
		
		

		boolean terminaJuego=false;						//Variable para finalizar el juego
		int todosPerdieron=0;							//Variable para contar que todos perdieron
		boolean unBlackJack=false;						//Variable para que no de dinero dos veces en caso de BJ

		//--------------------Comienza el juego----------------------	


		while(tablero.start==false){									//Espera hasta que el usuario presione Jugar
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//Revisar si alguno de los jugadores ya perdio cuando se abre una partida guardada
		for(int i=0;i<tablero.getNoPlayers();i++){
			if(tablero.getJugadores(i).getPerdio()){			
				todosPerdieron++;
			}
		}

		boolean nMostrar=false;		//Para no mostrar la ventana de siguiente la primer vez
		
		while(terminaJuego==false && todosPerdieron<tablero.getNoPlayers()){	
			for(int rep=0;rep<3;rep++){									//Cada 3 partidas se vuelve a barajear
				if(terminaJuego==false && todosPerdieron<tablero.getNoPlayers()){
					if(nMostrar){
						//Mostrar ventana de siguiente partida
						int seleccion = JOptionPane.showOptionDialog(null, "Seleccione opción", "Selector",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE,
								null,  
								new Object[] { "Siguiente", "Guardar" }, null);

						if (seleccion == 1){
							tablero.guardar();
							System.exit(0); 
						}
					}
					
					//Borrar las cartas de los jugadores y si gana o empata
					for(int i=0;i<tablero.getNoPlayers();i++){
						tablero.getJugadores(i).borraNaipe();
						tablero.getJugadores(i).setGanoPartida(false);
						tablero.getJugadores(i).setEmpatePartida(false);
					}

					//Oculta la carta del Dealer
					tablero.oculto=true;
					
					//Borra si tiene BlackJack a la primera
					unBlackJack=false;

					//Borrar las cartas del Dealer
					tablero.getDealer().borraNaipe();


					//Restar apuesta del saldo de los jugadores
					for(int i=0;i<tablero.getNoPlayers();i++){
						if(tablero.getJugadores(i).getPerdio()==false){			//Se salta a los jugadores que ya perdieron por falta de dinero
							tablero.getJugadores(i).perdioPartida(tablero.getApuesta());
						}
					}

					tablero.repaint();
					//Repartir dos veces la carta;
					for(int j=0;j<2;j++){
						//Se reparte una carta a todos los jugadores
						for(int i=0;i<tablero.getNoPlayers();i++){
							if(tablero.getJugadores(i).getPerdio()==false){		//Jugadores que no han perdido
								tablero.getJugadores(i).setJuego(tablero.getBaraja().next());
								tablero.dormirJuego();
							}
						}

						//Se reparte una carta al dealer
						tablero.getDealer().setJuego(tablero.getBaraja().next());
						tablero.dormirJuego();
					}

					//Imprimir baraja
					tablero.imprimirBaraja(tablero.getBaraja());

					//Imprimir cartas de los jugadores
					tablero.imprimirCartasJug();

					//Imprimir cartas Dealer
					tablero.imprimirCartasDealer();

					//Revisa para cada jugador si alguno tiene black jack y si lo tiene entonces llama a ganoPartida
					for (int i = 0; i < tablero.getNoPlayers(); i++){
						if(tablero.getJugadores(i).getPerdio()==false){			//Jugadores que no han perdido
							if(tablero.getJugadores(i).isBlackjack()){
								tablero.getJugadores(i).ganoPartida(tablero.getApuesta());
								tablero.getJugadores(i).setGanoPartida(true);
								unBlackJack=true;
								System.out.println("El jugador "+(i+1)+ " gana la partida");
							}
						}
					}
					tablero.imprimirCartaGrafica=true;
					
					//Revisa si la suma se pasa de 21 y se tiene un as entonces cambia su valor a uno
					for(int i=0;i<tablero.getNoPlayers();i++){
						if(tablero.getJugadores(i).getPerdio()==false && tablero.getJugadores(i).getTotal()>21){   //Jugadores que no han perdido
							for(int j=0;j<tablero.getJugadores(i).numNaipes();j++){
								if(tablero.getJugadores(i).regresaNaipe(j).getValor()==11){
									tablero.getJugadores(i).regresaNaipe(j).setCambio();
								}
							}
						}
					}

					//Preguntar a cada jugador si quiere otra carta mientras no pase de 21
					for(int i=0;i<tablero.getNoPlayers();i++){
						if(tablero.getJugadores(i).getPerdio()==false){			//Jugadores que no han perdido
							tablero.nomJugador=i;
							tablero.repaint();

							while(tablero.getJugadores(i).getTotal()<21 && tablero.mantener==false){
								while(tablero.pedir==false && tablero.mantener==false){
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
								if(tablero.getJugadores(i).otraCarta(tablero.pedir)){
									tablero.getJugadores(i).setJuego(tablero.getBaraja().next());
									tablero.dormirJuego();

									//Imprimir cartas de los jugadores
									tablero.imprimirCartasJug();

									//Revisa si la suma se pasa de 21 y se tiene un as entonces cambia su valor a uno
									if(tablero.getJugadores(i).getTotal()>21){
										for(int j=0;j<tablero.getJugadores(i).numNaipes();j++){
											if(tablero.getJugadores(i).regresaNaipe(j).getValor()==11){
												tablero.getJugadores(i).regresaNaipe(j).setCambio();
											}
										}
									}
									tablero.pedir=false;
								}	
							}
							tablero.mantener=false;
						}
						tablero.nomJugador=5;
						tablero.imprimirCartasJug();
					}

					//Mostrar carta oculta del Dealer
					tablero.oculto=false;
					tablero.dormirJuego();
					
					//Ver si el dealer tiene menos de 17 puntos
					while(tablero.getDealer().getTotal()<17){
						tablero.getDealer().setJuego(tablero.getBaraja().next());

						//Imprime Dealer
						tablero.imprimirCartasDealer();
						tablero.dormirJuego();

						//Revisa si la suma se pasa de 21 y se tiene un as entonces cambia su valor a uno
						if(tablero.getDealer().getTotal()>21){
							for(int j=0;j<tablero.getDealer().numNaipes();j++){
								if(tablero.getDealer().regresaNaipe(j).getValor()==11){
									tablero.getDealer().regresaNaipe(j).setCambio();
								}
							}
						}

					}

					int totalDealer=tablero.getDealer().getTotal();

					//Revisa quien gana, empata o pierde para dar el dinero
					for(int i=0;i<tablero.getNoPlayers();i++){
						if(tablero.getJugadores(i).getPerdio()==false){			//Jugadores que no han perdido
							if(tablero.getJugadores(i).getTotal()<=21 && unBlackJack==false){
								if(tablero.getJugadores(i).getTotal()>totalDealer || totalDealer>21){
									tablero.getJugadores(i).ganoPartida(tablero.getApuesta());
									tablero.getJugadores(i).setGanoPartida(true);
									System.out.println("El jugador "+(i+1)+ " gana la partida");
								}
								if(tablero.getJugadores(i).getTotal()==totalDealer){
									tablero.getJugadores(i).empatoPartida(tablero.getApuesta());
									tablero.getJugadores(i).setEmpatePartida(true);
								}
							}
							if(tablero.getJugadores(i).getSaldo()<tablero.getApuesta()){
								tablero.getJugadores(i).perdioDinero();
								todosPerdieron++;
							}
							if(tablero.getJugadores(i).getSaldo()>=tablero.getMontoMeta()){
								terminaJuego=true;
							}
						}
					}

					//Imprime una ultima vez el tablero para ver ganadores o empate
					tablero.dormirJuego();
					nMostrar=true;
				}
			}

			//Barajear de nuevo
			tablero.mezclarBaraja();
		}	

		if(terminaJuego){
			tablero.ganadorFinal=tablero.terminarPartida();				//Pantalla para desplegar al ganador
		}
		else{
			tablero.ganadorFinal=6;										//Pantalla para desplegar que todos perdieron

		}

		tablero.repaint();
		


	}

	//Regresa el ganador cuando se termina la partida
	public int terminarPartida(){
		int jugadorGanador=0;
		for (int i = 0; i < this.jugadores.length; i++) {
			if (jugadores[i].getSaldo()>=this.montoMeta) {
				jugadorGanador = i;
			}
		}
		return jugadorGanador;
	}


	//Operaciones default para los botones iniciales
	public void ocultarJBJugadores(){
		this.jbUnJugador.setVisible(false);
		this.jbDosJugadores.setVisible(false);
		this.jbTresJugadores.setVisible(false);
		this.jbCuatroJugadores.setVisible(false);
		this.jbJugar.setVisible(true);
		this.jbCargar.setVisible(false);

	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource()== this.jbUnJugador) {
			System.out.println("estoy aqui");
			ocultarJBJugadores();
			this.noPlayers = 1;
			this.jugadores();
			this.montoApuesta();
			this.crearMM();
		}
		else if (evt.getSource()== this.jbDosJugadores) {
			System.out.println("estoy aqui 2");
			ocultarJBJugadores();
			this.noPlayers = 2;
			this.jugadores();
			this.montoApuesta();
			this.crearMM();

		}
		else if (evt.getSource()== this.jbTresJugadores) {
			System.out.println("estoy aqui 3");
			ocultarJBJugadores();
			this.noPlayers = 3;
			this.jugadores();
			this.montoApuesta();
			this.crearMM();
		}
		else if (evt.getSource()== this.jbCuatroJugadores) {
			System.out.println("estoy aqui 4");
			ocultarJBJugadores();
			this.noPlayers = 4;
			this.jugadores();
			this.montoApuesta();
			this.crearMM();

		}
		else if (evt.getSource()== this.jbJugar) {
			this.start = true;
			this.imprimirCartaGrafica=true;
			this.jbJugar.setVisible(false);
			this.jbMantenerCarta.setVisible(true);
			this.jbPedirCarta.setVisible(true);
			this.pintarJugadores=true;
		}
		else if (evt.getSource() == this.jbPedirCarta) {
			this.pedir=true;
		}
		else if (evt.getSource() == this.jbMantenerCarta) {
			this.mantener=true;
		}
		else if(evt.getSource()==this.jbGuardar){
			this.guardar();
		}
		else if(evt.getSource()==this.jbCargar){
				this.jbUnJugador.setVisible(false);
				this.jbDosJugadores.setVisible(false);
				this.jbTresJugadores.setVisible(false);
				this.jbCuatroJugadores.setVisible(false);
				this.jbJugar.setVisible(true);
				this.jbCargar.setVisible(false);
				this.abrir();
	
		}

	}

	public void run() {

	}

}
