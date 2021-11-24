import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CarClient extends JFrame implements KeyListener {
	
	JPanel F, L, B, R;
	
	Socket conn = null;
	DataOutputStream dout = null;
	JFrame steeringFrame = null;
	
	//Konstruktor ktorego zadaniem bedzie kontrolowanie polaczenia z serwerem
	CarClient(){

		//Pola do wpisania ip i portu
		JTextField textIP = new JTextField(5);
		JTextField textPort = new JTextField(5);
		
		//Etykiety do opisu ktore pole co powinno zawierac
		JLabel ipMsg = new JLabel("IP serwera: ");
		JLabel portMsg = new JLabel("Port serwera: ");
		
		//Przyciski do laczenia sie z serwerem
		JButton connect = new JButton("Połącz");
		
		//Panele do organizacji elementow w oknie
		JPanel fields = new JPanel();
		JPanel buttons = new JPanel();
		JPanel main = new JPanel();
		
		//Panele do organizacji pol tekstowych
		JPanel ipField = new JPanel();
		JPanel portField = new JPanel();
		
		//Dodawanie odpowiednich elementow do odpowiadajacych im paneli
		ipField.add(ipMsg);
		ipField.add(textIP);
		
		portField.add(portMsg);
		portField.add(textPort);
		
		fields.add(ipField);
		fields.add(portField);
		fields.setLayout(new GridLayout(2,1));
		
		main.add(fields);
		
		buttons.add(connect);
		
		main.add(buttons);
		
		//Dodawanie zdarzen do przyciskow
		
		//Przycisk laczenia
		connect.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				String ip = textIP.getText();
				int port = Integer.parseInt(textPort.getText());
				
				steeringFrame = new CarClient(ip, port);
//				dispose();
			}});

		//Dodanie glownego panelu do okna
		add(main);
		
		//Ustawianie opcji okna
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(2,1));
		setSize(300,300);
		setTitle("Car connection Frame");
		setBackground(Color.decode("#2e2e2e"));
		
	}//Koniec konstruktora
	
	
	
	
	
	
	// Konstruktor przy pomocy ktorego bedzie mozna kontrolowac samochodzik
	// Konstruktor ten ma byc wywolywany gdy nastapi polaczenei z serwerem
	CarClient(String ip, int port){
		
		//Nawiazywanie polaczenia z serwerem
		try {

			conn = new Socket(ip, port);
			dout = new DataOutputStream(conn.getOutputStream());
			
		}catch(IOException exc) {
			exc.printStackTrace();
			
		}
		
		//Jeżeli nawiazano polaczenie i polaczenie ne jes nullem, to ma wykonac kod
		if(conn != null && conn.isConnected()) {
			
			//Tworzenie paneli 
			F = new JPanel();
			L = new JPanel();
			B = new JPanel();
			R = new JPanel();
			
			//tworzenie golwnego panelu
			JPanel main = new JPanel();
			
			//Ustawianie teł paneli na czerwone - nie aktywne
			F.setBackground(Color.red);
			L.setBackground(Color.red);
			B.setBackground(Color.red);
			R.setBackground(Color.red);
			
			//Tworzenie etykiet z literami
			JLabel W = new JLabel("W");
			JLabel A = new JLabel("A");
			JLabel S = new JLabel("S");
			JLabel D = new JLabel("D");
			
			//Dodawanie etykiet do paneli
			F.add(W);
			L.add(A);
			B.add(S);
			R.add(D);
			
			//Dodawanie paneli do glownego panelu
			main.add(F);
			main.add(L);
			main.add(B);
			main.add(R);
			
			//Dodawanei glownego panelu do okna
			add(main);
			
			//Ustawianie opcji okna
			addKeyListener(this);
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			setVisible(true);
			setSize(100,100);
			setResizable(false);
			setTitle("Car steering Frame"); //Nadawnie nazwy oknu
			revalidate();
		}else {
			System.out.println("Nie dziala polaczenie");
		}
	} //Koniec konstruktora
	
	
	
	
	
	
	//Nadpisywanie zaimplementowanych metod
	
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		char src = e.getKeyChar();
		
		try {
			//Warunek dla ESC - nie ma char dla esc wiec uzyty jesrt jego kod (27)
			//Warunek pozbywa sie okna, wysyla na serwer ze wychodzi, czysci polaczenie wychodzace, zamyka polaczenie wychodzace i zamyka polaczenie
			if(e.getKeyCode() == 27) {dispose(); dout.writeUTF("exit"); dout.flush(); dout.close(); conn.close();}
			
			//Warunki dla W A S D
			// Ustawienie koloru zielonego (wcisniety), wysylanie na serwer danych o wcisnietym przycisku
			if(src == 'w') {F.setBackground(Color.green); dout.writeUTF("Pw");}
			if(src == 'a') {L.setBackground(Color.green); dout.writeUTF("Pa");}
			if(src == 's') {B.setBackground(Color.green); dout.writeUTF("Ps");}
			if(src == 'd') {R.setBackground(Color.green); dout.writeUTF("Pd");}
			
		}catch(IOException exc) {exc.printStackTrace();}
		
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		char src = e.getKeyChar();
		
		try {
			//Warunki dla puszczonych W A S D
			//Ustawianie czerwonego koloru (nie wcisniety), wysylanie na serwer danych o puszczeniu danego klawisza
			if(src == 'w') {F.setBackground(Color.red); dout.writeUTF("Rw");}
			if(src == 'a') {L.setBackground(Color.red); dout.writeUTF("Ra");}
			if(src == 's') {B.setBackground(Color.red); dout.writeUTF("Rs");}
			if(src == 'd') {R.setBackground(Color.red); dout.writeUTF("Rd");}
		}catch(IOException exc) {exc.printStackTrace();}
	}
	

	
	// Uruchamianie programu, tworzac jego obiekt
	public static void main(String args[]) {
		
		JFrame connFrame = new CarClient();
		
	}
}



/*
 * Trzeba dodac kod ktory zunfocusuje uzytkownika, co spowoduje ze bedzie mogl sterowac 'wasd'
 * 
 * */

