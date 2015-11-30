package snake;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Diese Klasse ist die Hauptklasse des Spiels Snake. Sie kontrolliert sowohl die interne Steuerung als auch die graphische Anzeige des Spiels.
 * 
 * @author Lukas Schramm
 * @version 1.0
 *
 */
public class Snake {
	
	private JFrame frame1 = new JFrame("Snake");
	private int breite=30,hoehe=15;
	private Zelle[][] spielfeld = new Zelle[breite][hoehe];
	private ArrayList<Integer> schlangeX = new ArrayList<Integer>();
	private ArrayList<Integer> schlangeY = new ArrayList<Integer>();
	private boolean start = false;
	private enum Richtung {UP,LEFT,DOWN,RIGHT};
	private Richtung richtung = Richtung.RIGHT;
	private int[] futter = {0,0};
	
	public Snake() {
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setPreferredSize(new Dimension(breite*20,hoehe*20));
		frame1.setMinimumSize(new Dimension(breite*20,hoehe*20));
		frame1.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(!start) {
					starten();
					start = true;
				} else if(e.getKeyCode()== KeyEvent.VK_LEFT || e.getKeyCode()== KeyEvent.VK_RIGHT || e.getKeyCode()== KeyEvent.VK_UP || e.getKeyCode()== KeyEvent.VK_DOWN) {
					int keyCode = e.getKeyCode();
				    switch(keyCode) { 
				        case KeyEvent.VK_UP:
				        	if(!richtung.equals(Richtung.DOWN)) {
				        		richtung = Richtung.UP;
				        	}
				            break;
				        case KeyEvent.VK_DOWN:
				        	if(!richtung.equals(Richtung.UP)) {
				        		richtung = Richtung.DOWN;
				        	}
				            break;
				        case KeyEvent.VK_LEFT:
				        	if(!richtung.equals(Richtung.RIGHT)) {
				        		richtung = Richtung.LEFT;
				        	}
				            break;
				        case KeyEvent.VK_RIGHT:
				        	if(!richtung.equals(Richtung.LEFT)) {
				        		richtung = Richtung.RIGHT;
				        	}
				            break;
				        default: break;
				     }
				    bewegen();
				}
			} 
		});
		
		Container cp = frame1.getContentPane();
		cp.setLayout(new GridLayout(hoehe,breite));
		
		for(int y=0;y<hoehe;y++) {
			for(int x=0;x<breite;x++) {
				spielfeld[x][y] = new Zelle(0);
				spielfeld[x][y].setOpaque(true);
				frame1.add(spielfeld[x][y]);
			}
		}
		neuesSpiel();
		
		frame1.pack();
		frame1.setLocationRelativeTo(null);
		frame1.setVisible(true);
		frame1.repaint();
	}
	
	/**
	 * Diese Methode setzt Tastatureingaben des Spielers um und steuert die Schlange in die richtige Richtung.<br>
	 * Wenn das angesteuerte Feld ausserhalb des Spielfeldes liegt, wird die ArrayIndexOutOfBoundsException gecatcht und das Spiel beendet.
	 */
	private void bewegen() {
		int newX = 0;
		int newY = 0;
		try {
			if(richtung.equals(Richtung.UP)) {
				newX = schlangeX.get(schlangeX.size()-1);
				newY = schlangeY.get(schlangeY.size()-1)-1;
				richtung = Richtung.UP;
			} else if(richtung.equals(Richtung.DOWN)) {
				newX = schlangeX.get(schlangeX.size()-1);
				newY = schlangeY.get(schlangeY.size()-1)+1;
				richtung = Richtung.DOWN;
			} else if(richtung.equals(Richtung.LEFT)) {
				newX = schlangeX.get(schlangeX.size()-1)-1;
				newY = schlangeY.get(schlangeY.size()-1);
				richtung = Richtung.LEFT;
			} else if(richtung.equals(Richtung.RIGHT)) {
				newX = schlangeX.get(schlangeX.size()-1)+1;
				newY = schlangeY.get(schlangeY.size()-1);
				richtung = Richtung.RIGHT;
			}
			if(!(newX== futter[0] && newY==futter[1])) {
				spielfeld[schlangeX.get(0)][schlangeY.get(0)].setFarbe(0);
				schlangeX.remove(0);
				schlangeY.remove(0);
			} else {
				neuesFutter();
			}
			if(spielfeld[newX][newY].getFarbe()==1) {
				verloren();
			} else {
				schlangeX.add(newX);
				schlangeY.add(newY);
				spielfeld[schlangeX.get(schlangeX.size()-1)][schlangeY.get(schlangeY.size()-1)].setFarbe(1);
			}
		} catch(ArrayIndexOutOfBoundsException e) {
			verloren();
		}
	}
	
	/**
	 * Diese Methode setzt die Schlange in Bewegung.
	 */
	private void starten() {
		Thread thread = new Thread(new Runnable() {
	        public void run() {
	            try {
	            	while(start) {
	            		SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								bewegen();
							}
						});
		            	Thread.sleep(300);
	            	}
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
	    });
		thread.setDaemon(true);
		thread.start();
	}
	
	/**
	 * Diese Methode wird ausgeloest, wenn das Spiel vorbei ist. Sie zeigt dem Spieler die Laenge seiner Schlange an und fragt ihn, ob er ein neues Spiel starten moechte.<br>
	 * Abhaengig davon wird ein neues Spiel gestartet oder das Programm komplett beendet.
	 */
	private void verloren() {
		start = false;
		JOptionPane.showMessageDialog(null, "Das Spiel ist vorbei.\nDeine Schlange konnte eine\nLänge von "+(schlangeX.size()+1)+" Metern erreichen.", "Spielende", JOptionPane.PLAIN_MESSAGE);
		int dialogneustart = JOptionPane.showConfirmDialog(null, "Möchtest Du eine neue Runde starten?", "Neue Runde?", JOptionPane.YES_NO_OPTION);
        if(dialogneustart == 0) {
        	neuesSpiel();
        } else {
        	System.exit(0);
        }
	}
	
	/**
	 * Diese Methode generiert neues Futter an einer freien Stelle, die nicht von der Schlange belegt wird.
	 */
	private void neuesFutter() {
		Random random = new Random();
		int x = -1;
		int y = -1;
		do{
			x = random.nextInt(breite);
			y = random.nextInt(hoehe);
		}while((schlangeX.contains(x) && schlangeY.contains(y)));
		futter[0] = x;
		futter[1] = y;
		spielfeld[x][y].setFarbe(2);
		if((schlangeX.contains(x) && schlangeY.contains(y))) {
			neuesFutter();
		}
	}
	
	/**
	 * Diese Methode startet ein komplett neues Spiel.
	 */
	private void neuesSpiel() {
		richtung = Richtung.RIGHT;
		schlangeX.clear();
		schlangeY.clear();
		for(int y=0;y<hoehe;y++) {
			for(int x=0;x<breite;x++) {
				spielfeld[x][y].setFarbe(0);
			}
		}
		for(int i=0;i<5;i++) {
			schlangeX.add(i);
			schlangeY.add(hoehe-1);
			spielfeld[i][hoehe-1].setFarbe(1);
		}
		neuesFutter();
		frame1.repaint();
	}

	public static void main(String[] args) {
		new Snake();
	}
}