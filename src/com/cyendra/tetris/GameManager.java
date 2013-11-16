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
	
	//��һ�������λ��
	private final static int NEXT_X = 270;
	private final static int NEXT_Y = 320;
	
	//��ǰ����Ŀ�ʼ����
	private final static int BEGIN_X = Piece.SQUARE_BORDER * 6;
	private final static int BEGIN_Y = -32;
	
	private TetrisFrame frame = null;
	
	//���鹹����
	private PieceCreator creator = new PieceCreatorImpl();
	
	//��ͣ���
	private boolean pauseFlag = false;
	
	//��ǰ�����˶��ķ���
	private Piece currentPiece;
	
	//��һ����Ҫ����ķ���
	private Piece nextPiece;
	
	//���о�ֹ����
	private Square[][] squares;
	
	//��ǰ����
	private int score = 0;
	
	//��ǰ��Ϸ����
	private int currentLevel = 1;

	/**
	 * Ĭ�Ϲ��췽��
	 * */
	public GameManager() {
		super();
	}
	
	/**
	 * ����GameManager
	 * @param frame ��������
	 * */
	public GameManager(TetrisFrame frame) {
		this.frame = frame;
		this.pauseFlag = false;
		this.currentLevel = 1;
		this.score = 0;
	}
	
	/**
	 * ��ͣ��Ϸ
	 * */
	public void resume() {
		this.pauseFlag = false;
	}
	
	/**
	 * ������Ϸ
	 * */
	public void pause() {
		this.pauseFlag = true;
	}

	/**
	 * ��ʼ����Ϸ
	 * */
	public void start() {
		initSquares();// ��ʼ�������ά����
		createNextPiece();// ������Ԥ����һ������
		this.currentPiece = creator.createPiece(BEGIN_X, BEGIN_Y);		
		this.pauseFlag = false;
		this.currentLevel = 1;
		this.score = 0;
		frame.setScore(this.score);
		frame.setLevel(this.currentLevel);
	}

	/**
	 * ���·����-��
	 * */
	public void change() {
		if (this.pauseFlag) return;
		if (this.currentPiece == null) return;
		if (!this.canChange()) return;
		this.currentPiece.change();
		frame.getGamePanel().repaint();
	}

	/**
	 * ���·����-��
	 * */
	public void down() {
		if (this.pauseFlag) return;
		if (this.currentPiece == null) return;
		if (isBlock() || isButtom()) return;
		int distance = Piece.SQUARE_BORDER;
		this.currentPiece.setSquaresYLocation(distance);
		showNext();// ����ǰ״̬,׼��������һ��
		frame.getGamePanel().repaint();	
	}

	/**
	 * ���·����-��
	 * */
	public void left(int size) {
		if (this.pauseFlag) return;
		if (this.currentPiece == null) return;
		if (isLeftBlock()) return;// �ж�����Ƿ��з���
		if (this.currentPiece.getMinXLocation() <= 0) return;
		int distance = -(Piece.SQUARE_BORDER * size);
		this.currentPiece.setSquaresXLocation(distance);
		frame.getGamePanel().repaint();
	}
	
	/**
	 * ���·����-��
	 * */
	public void right(int size) {
		if (this.pauseFlag) return;
		if (this.currentPiece == null) return;
		if (isRightBolck()) return;// �ж��ұ��Ƿ��з���
		if (this.currentPiece.getMaxXLocation() >= frame.getGamePanel().getWidth()) return;
		int distance = Piece.SQUARE_BORDER * size;
		this.currentPiece.setSquaresXLocation(distance);
		frame.getGamePanel().repaint();
	}
	
	/**
	 * �ж��Ƿ񵽴���ײ�
	 * */
	public boolean isButtom() {
		return this.currentPiece.getMaxYLocation() >= frame.getGamePanel().getHeight();
	}
	
	/**
	 * �жϵ�ǰ�����Ƿ������ϰ��޷�����
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
	 * �ж��Ƿ����ڷ��������ת
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
	 * ����ǰ״̬׼��������һ��
	 * */
	public void showNext() {
		if (isBlock() || isButtom()) {
			appendToSquares();// ����ǰ�󷽿��е����з������ӽ����ά������
			if (isLost()) {
				frame.clearGame();
				this.currentPiece = null;
				JOptionPane.showConfirmDialog(frame, "��Ϸʧ��", "����", JOptionPane.OK_CANCEL_OPTION);
				return;
			}
			cleanRows();// ����������һ��
			finishDown();// ���¸��յķ���
		}
	}
	
	//�ж�����Ƿ��з���
	private boolean isLeftBlock() {
		List<Square> squares = this.currentPiece.getSquares();
		for (int i = 0; i < squares.size(); i++) {
			Square s = squares.get(i);
			Square block = getSquare(s.getBeginX() - Piece.SQUARE_BORDER, s.getBeginY());
			if (block != null) return true;
		}
		return false;
	}
	
	//�ж��ұ��Ƿ��з���
	private boolean isRightBolck() {
		List<Square> squares = this.currentPiece.getSquares();
		for (int i = 0; i < squares.size(); i++) {
			Square s = squares.get(i);
			Square block = getSquare(s.getBeginX() + Piece.SQUARE_BORDER, s.getBeginY());
			if (block != null) return true;
		}
		return false;
	}
	
	//���Ҳ����ؽ���������ָ��λ�õķ���
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
	
	//��ʼ�������ά����
	private void initSquares() {
		
		//�õ�����߿��Դ�ŵķ������  size=(15,23), Height=380, SQUARE=16
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
	
	//�ж��Ƿ�ʧ��
	private boolean isLost() {
		for (int i = 0; i < this.squares.length; i++) {
			Square s = this.squares[i][0];
			if (s.getImage() != null) return true;
		}
		return false;
	}
	
	//����������һ��
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
				addScore();// �ӷ�
			}
		}
		handleDown(rowIndexs);//���������ķ���
	}
	
	//�ӷ�
	private void addScore() {
		this.score += 10;
		frame.setScore(this.score);
		if ((this.score % 100) == 0) {
			this.currentLevel += 1;
			frame.setLevel(this.currentLevel);
		}
	}
	
	//���������ķ���
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
	
	//һ���󷽿�����½�
	private void finishDown() {
		this.currentPiece = this.nextPiece;
		
		//�����µ�Piece����
		this.currentPiece.setSquaresXLocation(-NEXT_X);
		this.currentPiece.setSquaresXLocation(BEGIN_X);
		this.currentPiece.setSquaresYLocation(-NEXT_Y);
		this.currentPiece.setSquaresYLocation(BEGIN_Y);
		
		//������һ��Piece
		createNextPiece();
	}
	
	//����ǰ�󷽿��е����з������ӽ����ά������
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

	//������Ԥ����һ������
	private void createNextPiece() {
		this.nextPiece = this.creator.createPiece(NEXT_X, NEXT_Y);
		frame.repaint();
	}
	
	/**
	 * ��ȡ��һ���󷽿�
	 * */
	public Piece getNextPiece() {
		return nextPiece;
	}
	
	/**
	 * ��ȡ��������
	 * */
	public Square[][] getSquares() {
		return this.squares;
	}
	
	/**
	 * ��ȡ��ǰ�󷽿�
	 * */
	public Piece getCurrentPiece() {
		return this.currentPiece;
	}
	
	/**
	 * �Ƿ�����ͣ״̬
	 * */
	public boolean getPauseFlag() {
		return this.pauseFlag;
	}
	
	/**
	 * ��ȡ����
	 * */
	public int getScore() {
		return this.score;
	}
	
	/**
	 * ��ȡ��ǰ�ȼ�
	 * */
	public int getCurrentLevel() {
		return this.currentLevel;
	}
	
}
