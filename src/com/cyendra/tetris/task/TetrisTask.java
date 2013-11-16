package com.cyendra.tetris.task;

import java.util.TimerTask;

import com.cyendra.tetris.GameManager;
import com.cyendra.tetris.TetrisFrame;
import com.cyendra.tetris.object.Piece;

/**
 * 游戏所用定时器类
 * 每隔一段时间将方块下降一格
 * */
public class TetrisTask extends TimerTask {
	private TetrisFrame frame;
	private GameManager gameManager;
	public TetrisTask(TetrisFrame frame) {
		this.frame = frame;
		this.gameManager = frame.getGameManager();
	}
	@Override
	public void run() {
		Piece currentPiece = gameManager.getCurrentPiece();
		if (currentPiece == null) return;
		if (gameManager.isBlock() || gameManager.isButtom()) {
			gameManager.showNext();
			return;
		}
		currentPiece.setSquaresYLocation(Piece.SQUARE_BORDER);
		frame.getGamePanel().repaint();
	}
	
}