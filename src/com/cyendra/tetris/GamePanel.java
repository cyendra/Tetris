package com.cyendra.tetris;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import com.cyendra.tetris.object.Piece;
import com.cyendra.tetris.object.Square;
import com.cyendra.tetris.util.ImageUtil;

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Image background = ImageUtil.getImage("images/background.jpg");
	private TetrisFrame Frame = null;
	public GamePanel(TetrisFrame Frame) {
		this.Frame = Frame;
	}

	public void paint(Graphics g) {
		g.drawImage(this.background, 0, 0, this.getWidth(), 
				this.getHeight() , null);
		Piece currentPiece = this.Frame.getCurrentPiece();
		ImageUtil.paintPiece(g, currentPiece);
		Square[][] squares = this.Frame.getSquares();
		if (squares == null) return;
		for (int i = 0; i < squares.length; i++) {
			for (int j = 0; j < squares[i].length; j++) {
				Square s = squares[i][j];
				if (s != null) {
					g.drawImage(s.getImage(), s.getBeginX(), s.getBeginY(), this);
				}
			}
		}
	}
	
}
