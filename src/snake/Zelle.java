package snake;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Diese Klasse stellt eine Zelle des Spiels dar. Sie faerbt sich automatisch je nachdem, ob sie frei, voll Futter oder Teil der Schlange ist.
 * 
 * @author Lukas Schramm
 * @version 1.0
 *
 */
public class Zelle extends JPanel {
	
	private int farbe;
	
	protected Zelle(int farbe) {
	    this.farbe = farbe;
	}
	
	@Override
    protected void paintComponent(Graphics gr) {
        super.paintComponent(gr);
        if(farbe==0) {
        	this.setBackground(new Color(0xF0E68C));
        } else if(farbe==1) {
        	this.setBackground(Color.black);
        } else if(farbe==2) {
        	this.setBackground(Color.blue);
        }
    }

	/**
	 * Nimmt die zu faerbende Farbe entgegen und faerbt die Zelle neu.
	 * @param farbe Nimmt einen int entgegen, welche Farbe die Zelle haben soll.
	 */
	public void setFarbe(int farbe) {
		this.farbe = farbe;
		this.repaint();
	}

	public int getFarbe() {
		return farbe;
	}

}