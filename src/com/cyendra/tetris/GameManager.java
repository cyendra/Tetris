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
	
	TetrisFrame frame = null;
	
	//方块构造器
	private PieceCreator creator = new PieceCreatorImpl();
	
	//暂停标记
	private boolean pauseFlag = false;
	
	
	//当前正在运动的方块
	private Piece currentPiece;
	
	//下一个将要下落的方块
	private Piece nextPiece;
	

	//当前游戏级别
	private int currentLevel = 0;
	
	//所有静止方块
	private Square[][] squares;
	
	//当前分数
	private int score = 0;
	
	
	
	public GameManager() {
		super();
	}
	
	public GameManager(TetrisFrame frame) {
		this.frame = frame;
		this.currentLevel = 1;
		this.score = 0;
	}
	
	public void resume() {
		this.pauseFlag = false;
	}

	public void pause() {
		this.pauseFlag = true;
	}

	public void start() {
		//初始化界面二维数组
		initSquares();
		createNextPiece();
		this.currentPiece = creator.createPiece(BEGIN_X, BEGIN_Y);		
		this.pauseFlag = false;
		this.currentLevel = 1;
		this.score = 0;
		
	}

	//按键盘上时触发的方法
	public void change() {
		if (this.pauseFlag) return;
		if (this.currentPiece == null) return;
		this.currentPiece.change();
		//判断转换后左边是否越界
		//得到当前方块最小的X座标
		int minX = this.currentPiece.getMinXLocation();
		//左边越界
		if (minX < 0) {
			//右移超过的部分
			this.currentPiece.setSquaresXLocation(-minX);
		}
		//判断转换后右边是否越界
		int maxX = this.currentPiece.getMaxXLocation();
		int gamePanelWidth = frame.getGamePanel().getWidth();
		//右边越界
		if (maxX > gamePanelWidth) {
			//左移超过GamePanel宽的部分
			this.currentPiece.setSquaresXLocation(-(maxX - gamePanelWidth));
		}
		frame.getGamePanel().repaint();
	}


	public void down() {
		if (this.pauseFlag) return;
		if (this.currentPiece == null) return;
		//判断快整下降后是否有障碍或者到底部
		if (isBlock() || isButtom()) return;
		int distance = Piece.SQUARE_BORDER;
		this.currentPiece.setSquaresYLocation(distance);
		//改变位置后再判断是否需要显示下一个
		showNext();
		frame.getGamePanel().repaint();	
	}

	public void left(int size) {
		if (this.pauseFlag) return;
		if (this.currentPiece == null) return;
		//判断左边是否有Square
		if (isLeftBlock()) return;
		//判断是否已经在最左边边界
		if (this.currentPiece.getMinXLocation() <= 0) return;
		//得出移动距离
		int distance = -(Piece.SQUARE_BORDER * size);
		this.currentPiece.setSquaresXLocation(distance);
		frame.getGamePanel().repaint();
	}

	public void right(int size) {
		if (this.pauseFlag) return;
		if (this.currentPiece == null) return;
		//判断右边是否有Square
		if (isRightBolck()) return;
		//判断是否超过GamePanel的宽
		if (this.currentPiece.getMaxXLocation() >= frame.getGamePanel().getWidth()) return;
		int distance = Piece.SQUARE_BORDER * size;
		this.currentPiece.setSquaresXLocation(distance);
		frame.getGamePanel().repaint();
	}
	
	private boolean isLeftBlock() {
		List<Square> squares = this.currentPiece.getSquares();
		for (int i = 0; i < squares.size(); i++) {
			Square s = squares.get(i);
			//查找界面数组中的Square对象
			Square block = getSquare(s.getBeginX() - Piece.SQUARE_BORDER, s.getBeginY());
			//block非空表示遇到障碍
			if (block != null) return true;
		}
		return false;
	}
	
	//判断当前的Piece对象右边是否有障碍, 返回true表示有, 返回false表示没有
	private boolean isRightBolck() {
		List<Square> squares = this.currentPiece.getSquares();
		for (int i = 0; i < squares.size(); i++) {
			Square s = squares.get(i);
			//查找界面数组中的Square对象
			Square block = getSquare(s.getBeginX() + Piece.SQUARE_BORDER, s.getBeginY());
			//block非空表示遇到障碍
			if (block != null) return true;
		}
		return false;
	}
	
	//根据开始座标在当前界面数组中查找相应的Square
	private Square getSquare(int beginX, int beginY) {
		for (int i = 0; i < this.squares.length; i++) {
			for (int j = 0; j < this.squares[i].length; j++) {
				Square s = this.squares[i][j];
				//与参数的开始座标相同并且图片不为空
				if (s.getImage() != null && (s.getBeginX() == beginX) && 
						(s.getBeginY() == beginY)) {
					return s;
				}
			}
		}
		return null;
	}
	
	//初始化界面二维数组
	private void initSquares() {
		//得到宽可以存放的方块个数
		int xSize = frame.getGamePanel().getWidth()/Piece.SQUARE_BORDER;
		//得到高可以存放的方块个数
		int ySize = frame.getGamePanel().getHeight()/Piece.SQUARE_BORDER;
		//size=15 23, Height=380, SQUARE=16
		//System.out.println("size = "+xSize+" "+ySize+" Height="+this.gamePanel.getHeight()+" SQUARE"+Piece.SQUARE_BORDER);
		//构造界面的二维数组
		this.squares = new Square[xSize][ySize+1];
		for(int i = 0; i < this.squares.length; i++) {
			for (int j = 0; j < this.squares[i].length; j++) {
				this.squares[i][j] = new Square(Piece.SQUARE_BORDER * i, 
						Piece.SQUARE_BORDER * j);
			}
		}
	}
	

	//判断是否到最底部
	public boolean isButtom() {
		return this.currentPiece.getMaxYLocation() >= frame.getGamePanel().getHeight();
	}
	
	//判断当前的Piece是否遇到障碍, 返回true表示遇到障碍, 返回false表示没有遇到
	public boolean isBlock() {
		List<Square> squares = this.currentPiece.getSquares();
		for (int i = 0; i < squares.size(); i++) {
			Square s = squares.get(i);
			//查找界面数组中的Square对象, 注意要将Y加上边距, 因为Y是开始座标
			//需要拿一个Square的最大Y座标
			Square block = getSquare(s.getBeginX(), s.getBeginY() + Piece.SQUARE_BORDER);
			//block非空表示遇到障碍
			if (block != null) return true;
		}
		return false;
	}
	

	
	//判断是否失败, true为失败, false反之
	private boolean isLost() {
		for (int i = 0; i < this.squares.length; i++) {
			Square s = this.squares[i][0];
			if (s.getImage() != null) {
				return true;
			}
		}
		return false;
	}

	//进行下一个
	public void showNext() {
		if (isBlock() || isButtom()) {
			//将当前的Piece中的所有Square加入加界面二维数组中
			appendToSquares();
			//判断是否失败
			if (isLost()) {
				frame.clearGame();
				this.currentPiece = null;
				JOptionPane.showConfirmDialog(frame, "游戏失败", "警告", JOptionPane.OK_CANCEL_OPTION);
				return;
			}
			//消除行
			cleanRows();
			finishDown();
		}
	}
	
	//得到可以清理行集合
	private void cleanRows() {
		//使用一个集合保存被删除的行的索引
		List<Integer> rowIndexs = new ArrayList<Integer>();
		for (int j = 0; j < this.squares[0].length; j++) {
			int k = 0;
			for (int i = 0; i < this.squares.length; i++) {
				Square s = this.squares[i][j];
				//如果该格有图片, 则k+1
				if (s.getImage() != null) k++;
			}
			//如果整行都有图片, 则消除
			if (k == this.squares.length) {
				//再次对该行进行遍历, 设置该行所有格的图片为null
				for (int i = 0; i < this.squares.length; i++) {
					Square s = this.squares[i][j];
					s.setImage(null);
				}
				rowIndexs.add(j);
				addScore();
			}
		}
		//处理悬浮的Square
		handleDown(rowIndexs);
	}
	
	//加入分数
	private void addScore() {
		//加分
		this.score += 10;
		frame.setScore(this.score);
		//如果可以被100整除, 则加一级
		if ((this.score % 100) == 0) {
			this.currentLevel += 1;
			frame.setLevel(this.currentLevel);

		}
	}
	
	//处理行消除后其他Square的"下降", 参数为被删除的行的索引集合
	private void handleDown(List<Integer> rowIndexs) {
		//从被删除的行中拿出索引最小的行
		if (rowIndexs.size() == 0) return;
		int minCleanRow = rowIndexs.get(0);
		int cleanRowSize = rowIndexs.size();
		//处理下降的Square
		for (int j = this.squares[0].length - 1; j >= 0; j--) {
			if (j < minCleanRow) {
				//遍历上面的行, 即悬浮的行
				for (int i = 0; i < this.squares.length; i++) {
					Square s = this.squares[i][j];
					if (s.getImage() != null) {
						//得到下降前的图片
						Image image = s.getImage();
						s.setImage(null);
						//得到下降后对应的Square对象, 数组的二维值要加上消除行的行数
						Square sdown = this.squares[i][j + cleanRowSize];
						sdown.setImage(image);
					}
				}
			}
		}
	}
	

	//一个Piece对象完成下降
	private void finishDown() {
		//将当前的Piece设置为下一个Piece
		this.currentPiece = this.nextPiece;
		//设置新的Piece座标
		this.currentPiece.setSquaresXLocation(-NEXT_X);
		this.currentPiece.setSquaresXLocation(BEGIN_X);
		this.currentPiece.setSquaresYLocation(-NEXT_Y);
		this.currentPiece.setSquaresYLocation(BEGIN_Y);
		//创建下一个Piece
		createNextPiece();
	}
	
	//将Piece中所有的Square都加入到二维数组中
	private void appendToSquares() {
		List<Square> squares = this.currentPiece.getSquares();
		//遍历Piece中的Square
		for(Square square : squares) {
			for(int i = 0; i < this.squares.length; i++) {
				for (int j = 0; j < this.squares[i].length; j++) {
					//得到当前界面中的Square
					Square s = this.squares[i][j];
					//判断Square是否一致
					if (s.equals(square)) this.squares[i][j] = square;
				}
			}
		}
		frame.getGamePanel().repaint();
	}

	//创建下一个
	private void createNextPiece() {
		this.nextPiece = this.creator.createPiece(NEXT_X, NEXT_Y);
		frame.repaint();
	}
	
	public Piece getNextPiece() {
		return nextPiece;
	}
	
	public Square[][] getSquares() {
		return this.squares;
	}
	
	public Piece getCurrentPiece() {
		return this.currentPiece;
	}
	
	
	//是否处于暂停状态
	public boolean getPauseFlag() {
		return this.pauseFlag;
	}
	
	//获取分数
	public int getScore() {
		return this.score;
	}
	
	//获取当前等级
	public int getCurrentLevel() {
		return this.currentLevel;
	}
}
