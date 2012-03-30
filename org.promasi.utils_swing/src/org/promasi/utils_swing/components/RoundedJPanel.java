/**
 * 
 */
package org.promasi.utils_swing.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * @author alekstheod
 * Represent a JPanel with a rounded corners.
 */
public class RoundedJPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	private Color _bgColor = new Color(255, 255, 255, 180);
	
	/**
	 * 
	 */
	public RoundedJPanel(){
		super();
		init();
	}
	
	/**
	 * 
	 * @param color
	 */
	public RoundedJPanel(Color color){
		super();
		_bgColor = color;
		init();
	}

	private void init(){
		int bpad = 20;
		setBorder(new EmptyBorder(bpad, bpad, bpad, bpad));
		setOpaque(false);
	}
	
	@Override
	public void paintComponent(Graphics g) {
	    int x = 10;
	    int y = 10;
	    int w = getWidth() - 20;
	    int h = getHeight() - 20;
	    int arc = 20;
	    Graphics2D g2 = (Graphics2D) g.create();
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	    g2.setColor(_bgColor);
	    g2.fillRoundRect(x, y, w, h, arc, arc);

	    g2.setStroke(new BasicStroke(3f));
	    g2.setColor(Color.WHITE);
	    g2.drawRoundRect(x, y, w, h, arc, arc); 
	    
	    super.paintComponent(g2);
	    g2.dispose();
	}
}