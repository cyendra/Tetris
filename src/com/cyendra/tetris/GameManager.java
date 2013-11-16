package com.cyendra.tetris;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.cyendra.tetris.object.Piece;
import com.cyendra.tetris.object.PieceCreator;
import com.cyendra.tetris.object.PieceCreatorImpl;
import com.cyendra.tetris.object.Square;

public class GameManager {
	
	//下一个方块的位置
	private final static int NEXT_X = 270;
	private final static int NEXT_Y = 320;
	
	//当前方块的开始座标
	private final static int BEGIN_X = Piece.SQUARE_BORDER * 6;
	private final static int BEGIN_Y = -32;
	
	private TetrisFrame frame = null;
	
	//方块构造器
	private PieceCreator creator = new PieceCreatorImpl();
	
	//暂停标记
	private boolean pauseFlag = false;
	
	//当前正在运动的方块
	private Piece currentPiece;
	
	//下一个将要下落的方块
	private Piece nextPiece;
	
	//所有静止方块
	private Square[][] squares;
	
	//当前分数
	private int score = 0;
	
	//当前游戏级别
	private int currentLevel = 1;

	/**
	 * 默认构造方法
	 * */
	public GameManager() {
		super();
	}
	
	/**
	 * 构造GameManager
	 * @param frame 关联窗体
	 * */
	public GameManager(TetrisFrame frame) {
		this.frame = frame;
		this.pauseFlag = false;
		this.currentLevel = 1;
		this.score = 0;
	}
	
	/**
	 * 暂停游戏
	 * */
	public void resume() {
		this.pauseFlag = false;
	}
	
	/**
	 * 继续游戏
	 * */
	public void pause() {
		this.pauseFlag = true;
	}

	/**
	 * 开始新游戏
	 * */
	public void start() {
		initSquares();// 初始化界面二维数组
		createNextPiece();// 创建并预览下一个方块
		this.currentPiece = creator.createPiece(BEGIN_X, BEGIN_Y);		
		this.pauseFlag = false;
		this.currentLevel = 1;
		this.score = 0;
		frame.setScore(this.score);
		frame.setLevel(this.currentLevel);
	}

	/**
	 * 按下方向键-上
	 * */
	public void change() {
		if (this.pauseFlag) return;
		if (this.currentPiece == null) return;
		if (!this.canChange()) return;
		this.currentPiece.change();
		frame.getGamePanel().repaint();
	}

	/**
	 * 按下方向键-下
	 * */
	public void down() {
		if (this.pauseFlag) return;
		if (this.currentPiece == null) return;
		if (isBlock() || isButtom()) return;
		int distance = Piece.SQUARE_BORDER;
		this.currentPiece.setSquaresYLocation(distance);
		showNext();// 处理当前状态,准备进行下一步
		frame.getGamePanel().repaint();	
	}

	/**
	 * 按下方向键-左
	 * */
	public void left(int size) {
		if (this.pauseFlag) return;
		if (this.currentPiece == null) return;
		if (isLeftBlock()) return;// 判断左边是否有方块
		if (this.currentPiece.getMinXLocation() <= 0) return;
		int distance = -(Piece.SQUARE_BORDER * size);
		this.currentPiece.setSquaresXLocation(distance);
		frame.getGamePanel().repaint();
	}
	
	/**
	 * 按下方向键-右
	 * */
	public void right(int size) {
		if (this.pauseFlag) return;
		if (this.currentPiece == null) return;
		if (isRightBolck()) return;// 判断右边是否有方块
		if (this.currentPiece.getMaxXLocation() >= frame.getGamePanel().getWidth()) return;
		int distance = Piece.SQUARE_BORDER * size;
		this.currentPiece.setSquaresXLocation(distance);
		frame.getGamePanel().repaint();
	}
	
	/**
	 * 判断是否到达最底部
	 * */
	public boolean isButtom() {
		return this.currentPiece.getMaxYLocation() >= frame.getGamePanel().getHeight();
	}
	
	/**
	 * 判断当前方块是否遇到障碍无法下落
	 * */
	public boolean isBlock() {
		List<Square> squares = this.currentPiece.getSquares();
		for (int i = 0; i < squares.size(); i++) {
			Square s = squares.get(i);
			Square block = getSquare(s.getBeginX(), s.getBeginY() + Piece.SQUARE_BORDER);
			if (block != null) return true;
		}
		return false;
	}
	
	/**
	 * 判断是否能在方块堆里旋转
	 * */
	public boolean canChange() {
		List<Square> sq = this.currentPiece.getNextSquares();
		for (int i = 0; i < sq.size(); i++) {
			Square s = sq.get(i);
			int X = s.getBeginX();
			if (X < 0) return false;
			if (X + Piece.SQUARE_BORDER > frame.getGamePanel().getWidth()) return false;
			Square block = getSquare(s.getBeginX(), s.getBeginY());
			if (block != null) return false;
		}
		return true;
	}
	
	/**
	 * 处理当前状态准备进行下一步
	 * */
	public void showNext() {
		if (isBlock() || isButtom()) {
			appendToSquares();// 将当前大方块中的所有方块加入加界面二维数组中
			if (isLost()) {
				frame.clearGame();
				this.currentPiece = null;
				JOptionPane.showConfirmDialog(frame, "游戏失败", "警告", JOptionPane.OK_CANCEL_OPTION);
				return;
			}
			cleanRows();// 消除填满的一行
			finishDown();// 落下浮空的方块
		}
	}
	
	//判断左边是否有方块
	private boolean isLeftBlock() {
		List<Square> squares = this.currentPiece.getSquares();
		for (int i = 0; i < squares.size(); i++) {
			Square s = squares.get(i);
			Square block = getSquare(s.getBeginX() - Piece.SQUARE_BORDER, s.getBeginY());
			if (block != null) return true;
		}
		return false;
	}
	
	//判断右边是否有方块
	private boolean isRightBolck() {
		List<Square> squares = this.currentPiece.getSquares();
		for (int i = 0; i < squares.size(); i++) {
			Square s = squares.get(i);
			Square block = getSquare(s.getBeginX() + Piece.SQUARE_BORDER, s.getBeginY());
			if (block != null) return true;
		}
		return false;
	}
	
	//查找并返回界面数组中指定位置的方块
	private Square getSquare(int beginX, int beginY) {
		for (int i = 0; i < this.squares.length; i++) {
			for (int j = 0; j < this.squares[i].length; j++) {
				Square s = this.squares[i][j];
				if (s.getImage() != null && (s.getBeginX() == beginX) && (s.getBeginY() == beginY)) {
					return s;
				}
			}
		}
		return null;
	}
	
	//初始化界面二维数组
	private void initSquares() {
		
		//得到宽与高可以存放的方块个数  size=(15,23), Height=380, SQUARE=16
		int xSize = frame.getGamePanel().getWidth()/Piece.SQUARE_BORDER;
		int ySize = frame.getGamePanel().getHeight()/Piece.SQUARE_BORDER;
		if (xSize*Piece.SQUARE_BORDER<frame.getGamePanel().getWidth()) xSize++;
		if (ySize*Piece.SQUARE_BORDER<frame.getGamePanel().getHeight()) ySize++;
		
		this.squares = new Square[xSize][ySize];
		for(int i = 0; i < this.squares.length; i++) {
			for (int j = 0; j < this.squares[i].length; j++) {
				this.squares[i][j] = new Square(Piece.SQUARE_BORDER * i, Piece.SQUARE_BORDER * j);
			}
		}
	}
	
	//判断是否失败
	private boolean isLost() {
		for (int i = 0; i < this.squares.length; i++) {
			Square s = this.squares[i][0];
			if (s.getImage() != null) return true;
		}
		return false;
	}
	
	//消除填满的一行
	private void cleanRows() {
		List<Integer> rowIndexs = new ArrayList<Integer>();
		for (int j = 0; j < this.squares[0].length; j++) {
			int k = 0;
			for (int i = 0; i < this.squares.length; i++) {
				Square s = this.squares[i][j];
				if (s.getImage() != null) k++;
			}
			if (k == this.squares.length) {
				for (int i = 0; i < this.squares.length; i++) {
					Square s = this.squares[i][j];
					s.setImage(null);
				}
				rowIndexs.add(j);
				addScore();// 加分
			}
		}
		handleDown(rowIndexs);//处理悬浮的方块
	}
	
	//加分
	private void addScore() {
		this.score += 10;
		frame.setScore(this.score);
		if ((this.score % 100) == 0) {
			this.currentLevel += 1;
			frame.setLevel(this.currentLevel);
		}
	}
	
	//处理悬浮的方块
	private void handleDown(List<Integer> rowIndexs) {
		if (rowIndexs.size() == 0) return;
		for (int minCleanRow : rowIndexs){
			boolean ok = true;
			int j = 0;
			for (j = minCleanRow - 1; j >= 0; j--) {
				for (int i = 0; i < this.squares.length; i++) {
					Square s = this.squares[i][j];
					Square sdown = this.squares[i][j+1];
					Image image = s.getImage();		
					if (image != null) sdown.setImage(image);
					else sdown.setImage(null);
					s.setImage(null);
					if (image != null) ok = false;
				}
				if (ok) break;
			}
		}
	}
	
	//一个大方块完成下降
	private void finishDown() {
		this.currentPiece = this.nextPiece;
		
		//设置新的Piece座标
		this.currentPiece.setSquaresXLocation(-NEXT_X);
		this.currentPiece.setSquaresXLocation(BEGIN_X);
		this.currentPiece.setSquaresYLocation(-NEXT_Y);
		this.currentPiece.setSquaresYLocation(BEGIN_Y);
		
		//创建下一个Piece
		createNextPiece();
	}
	
	//将当前大方块中的所有方块加入加界面二维数组中
	private void appendToSquares() {
		List<Square> squares = this.currentPiece.getSquares();
		for(Square square : squares) {
			for(int i = 0; i < this.squares.length; i++) {
				for (int j = 0; j < this.squares[i].length; j++) {
					Square s = this.squares[i][j];
					if (s.equals(square)) this.squares[i][j] = square;
				}
			}
		}
		frame.getGamePanel().repaint();
	}

	//创建并预览下一个方块
	private void createNextPiece() {
		this.nextPiece = this.creator.createPiece(NEXT_X, NEXT_Y);
		frame.repaint();
	}
	
	/**
	 * 获取下一个大方块
	 * */
	public Piece getNextPiece() {
		return nextPiece;
	}
	
	/**
	 * 获取方块数组
	 * */
	public Square[][] getSquares() {
		return this.squares;
	}
	
	/**
	 * 获取当前大方块
	 * */
	public Piece getCurrentPiece() {
		return this.currentPiece;
	}
	
	/**
	 * 是否处于暂停状态
	 * */
	public boolean getPauseFlag() {
		return this.pauseFlag;
	}
	
	/**
	 * 获取分数
	 * */
	public int getScore() {
		return this.score;
	}
	
	/**
	 * 获取当前等级
	 * */
	public int getCurrentLevel() {
		return this.currentLevel;
	}
	
}
