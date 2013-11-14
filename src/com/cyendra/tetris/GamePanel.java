package com.cyendra.tetris;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final Image img = null;
	
	public void paint(Graphics g) {
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
	}
	
}
