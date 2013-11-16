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
	
	TetrisFrame frame = null;
	
	//���鹹����
	private PieceCreator creator = new PieceCreatorImpl();
	
	//��ͣ���
	private boolean pauseFlag = false;
	
	
	//��ǰ�����˶��ķ���
	private Piece currentPiece;
	
	//��һ����Ҫ����ķ���
	private Piece nextPiece;
	

	//��ǰ��Ϸ����
	private int currentLevel = 0;
	
	//���о�ֹ����
	private Square[][] squares;
	
	//��ǰ����
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
		//��ʼ�������ά����
		initSquares();
		createNextPiece();
		this.currentPiece = creator.createPiece(BEGIN_X, BEGIN_Y);		
		this.pauseFlag = false;
		this.currentLevel = 1;
		this.score = 0;
		
	}

	//��������ʱ�����ķ���
	public void change() {
		if (this.pauseFlag) return;
		if (this.currentPiece == null) return;
		this.currentPiece.change();
		//�ж�ת��������Ƿ�Խ��
		//�õ���ǰ������С��X����
		int minX = this.currentPiece.getMinXLocation();
		//���Խ��
		if (minX < 0) {
			//���Ƴ����Ĳ���
			this.currentPiece.setSquaresXLocation(-minX);
		}
		//�ж�ת�����ұ��Ƿ�Խ��
		int maxX = this.currentPiece.getMaxXLocation();
		int gamePanelWidth = frame.getGamePanel().getWidth();
		//�ұ�Խ��
		if (maxX > gamePanelWidth) {
			//���Ƴ���GamePanel��Ĳ���
			this.currentPiece.setSquaresXLocation(-(maxX - gamePanelWidth));
		}
		frame.getGamePanel().repaint();
	}


	public void down() {
		if (this.pauseFlag) return;
		if (this.currentPiece == null) return;
		//�жϿ����½����Ƿ����ϰ����ߵ��ײ�
		if (isBlock() || isButtom()) return;
		int distance = Piece.SQUARE_BORDER;
		this.currentPiece.setSquaresYLocation(distance);
		//�ı�λ�ú����ж��Ƿ���Ҫ��ʾ��һ��
		showNext();
		frame.getGamePanel().repaint();	
	}

	public void left(int size) {
		if (this.pauseFlag) return;
		if (this.currentPiece == null) return;
		//�ж�����Ƿ���Square
		if (isLeftBlock()) return;
		//�ж��Ƿ��Ѿ�������߽߱�
		if (this.currentPiece.getMinXLocation() <= 0) return;
		//�ó��ƶ�����
		int distance = -(Piece.SQUARE_BORDER * size);
		this.currentPiece.setSquaresXLocation(distance);
		frame.getGamePanel().repaint();
	}

	public void right(int size) {
		if (this.pauseFlag) return;
		if (this.currentPiece == null) return;
		//�ж��ұ��Ƿ���Square
		if (isRightBolck()) return;
		//�ж��Ƿ񳬹�GamePanel�Ŀ�
		if (this.currentPiece.getMaxXLocation() >= frame.getGamePanel().getWidth()) return;
		int distance = Piece.SQUARE_BORDER * size;
		this.currentPiece.setSquaresXLocation(distance);
		frame.getGamePanel().repaint();
	}
	
	private boolean isLeftBlock() {
		List<Square> squares = this.currentPiece.getSquares();
		for (int i = 0; i < squares.size(); i++) {
			Square s = squares.get(i);
			//���ҽ��������е�Square����
			Square block = getSquare(s.getBeginX() - Piece.SQUARE_BORDER, s.getBeginY());
			//block�ǿձ�ʾ�����ϰ�
			if (block != null) return true;
		}
		return false;
	}
	
	//�жϵ�ǰ��Piece�����ұ��Ƿ����ϰ�, ����true��ʾ��, ����false��ʾû��
	private boolean isRightBolck() {
		List<Square> squares = this.currentPiece.getSquares();
		for (int i = 0; i < squares.size(); i++) {
			Square s = squares.get(i);
			//���ҽ��������е�Square����
			Square block = getSquare(s.getBeginX() + Piece.SQUARE_BORDER, s.getBeginY());
			//block�ǿձ�ʾ�����ϰ�
			if (block != null) return true;
		}
		return false;
	}
	
	//���ݿ�ʼ�����ڵ�ǰ���������в�����Ӧ��Square
	private Square getSquare(int beginX, int beginY) {
		for (int i = 0; i < this.squares.length; i++) {
			for (int j = 0; j < this.squares[i].length; j++) {
				Square s = this.squares[i][j];
				//������Ŀ�ʼ������ͬ����ͼƬ��Ϊ��
				if (s.getImage() != null && (s.getBeginX() == beginX) && 
						(s.getBeginY() == beginY)) {
					return s;
				}
			}
		}
		return null;
	}
	
	//��ʼ�������ά����
	private void initSquares() {
		//�õ�����Դ�ŵķ������
		int xSize = frame.getGamePanel().getWidth()/Piece.SQUARE_BORDER;
		//�õ��߿��Դ�ŵķ������
		int ySize = frame.getGamePanel().getHeight()/Piece.SQUARE_BORDER;
		//size=15 23, Height=380, SQUARE=16
		//System.out.println("size = "+xSize+" "+ySize+" Height="+this.gamePanel.getHeight()+" SQUARE"+Piece.SQUARE_BORDER);
		//�������Ķ�ά����
		this.squares = new Square[xSize][ySize+1];
		for(int i = 0; i < this.squares.length; i++) {
			for (int j = 0; j < this.squares[i].length; j++) {
				this.squares[i][j] = new Square(Piece.SQUARE_BORDER * i, 
						Piece.SQUARE_BORDER * j);
			}
		}
	}
	

	//�ж��Ƿ���ײ�
	public boolean isButtom() {
		return this.currentPiece.getMaxYLocation() >= frame.getGamePanel().getHeight();
	}
	
	//�жϵ�ǰ��Piece�Ƿ������ϰ�, ����true��ʾ�����ϰ�, ����false��ʾû������
	public boolean isBlock() {
		List<Square> squares = this.currentPiece.getSquares();
		for (int i = 0; i < squares.size(); i++) {
			Square s = squares.get(i);
			//���ҽ��������е�Square����, ע��Ҫ��Y���ϱ߾�, ��ΪY�ǿ�ʼ����
			//��Ҫ��һ��Square�����Y����
			Square block = getSquare(s.getBeginX(), s.getBeginY() + Piece.SQUARE_BORDER);
			//block�ǿձ�ʾ�����ϰ�
			if (block != null) return true;
		}
		return false;
	}
	

	
	//�ж��Ƿ�ʧ��, trueΪʧ��, false��֮
	private boolean isLost() {
		for (int i = 0; i < this.squares.length; i++) {
			Square s = this.squares[i][0];
			if (s.getImage() != null) {
				return true;
			}
		}
		return false;
	}

	//������һ��
	public void showNext() {
		if (isBlock() || isButtom()) {
			//����ǰ��Piece�е�����Square����ӽ����ά������
			appendToSquares();
			//�ж��Ƿ�ʧ��
			if (isLost()) {
				frame.clearGame();
				this.currentPiece = null;
				JOptionPane.showConfirmDialog(frame, "��Ϸʧ��", "����", JOptionPane.OK_CANCEL_OPTION);
				return;
			}
			//������
			cleanRows();
			finishDown();
		}
	}
	
	//�õ����������м���
	private void cleanRows() {
		//ʹ��һ�����ϱ��汻ɾ�����е�����
		List<Integer> rowIndexs = new ArrayList<Integer>();
		for (int j = 0; j < this.squares[0].length; j++) {
			int k = 0;
			for (int i = 0; i < this.squares.length; i++) {
				Square s = this.squares[i][j];
				//����ø���ͼƬ, ��k+1
				if (s.getImage() != null) k++;
			}
			//������ж���ͼƬ, ������
			if (k == this.squares.length) {
				//�ٴζԸ��н��б���, ���ø������и��ͼƬΪnull
				for (int i = 0; i < this.squares.length; i++) {
					Square s = this.squares[i][j];
					s.setImage(null);
				}
				rowIndexs.add(j);
				addScore();
			}
		}
		//����������Square
		handleDown(rowIndexs);
	}
	
	//�������
	private void addScore() {
		//�ӷ�
		this.score += 10;
		frame.setScore(this.score);
		//������Ա�100����, ���һ��
		if ((this.score % 100) == 0) {
			this.currentLevel += 1;
			frame.setLevel(this.currentLevel);

		}
	}
	
	//����������������Square��"�½�", ����Ϊ��ɾ�����е���������
	private void handleDown(List<Integer> rowIndexs) {
		//�ӱ�ɾ���������ó�������С����
		if (rowIndexs.size() == 0) return;
		int minCleanRow = rowIndexs.get(0);
		int cleanRowSize = rowIndexs.size();
		//�����½���Square
		for (int j = this.squares[0].length - 1; j >= 0; j--) {
			if (j < minCleanRow) {
				//�����������, ����������
				for (int i = 0; i < this.squares.length; i++) {
					Square s = this.squares[i][j];
					if (s.getImage() != null) {
						//�õ��½�ǰ��ͼƬ
						Image image = s.getImage();
						s.setImage(null);
						//�õ��½����Ӧ��Square����, ����Ķ�άֵҪ���������е�����
						Square sdown = this.squares[i][j + cleanRowSize];
						sdown.setImage(image);
					}
				}
			}
		}
	}
	

	//һ��Piece��������½�
	private void finishDown() {
		//����ǰ��Piece����Ϊ��һ��Piece
		this.currentPiece = this.nextPiece;
		//�����µ�Piece����
		this.currentPiece.setSquaresXLocation(-NEXT_X);
		this.currentPiece.setSquaresXLocation(BEGIN_X);
		this.currentPiece.setSquaresYLocation(-NEXT_Y);
		this.currentPiece.setSquaresYLocation(BEGIN_Y);
		//������һ��Piece
		createNextPiece();
	}
	
	//��Piece�����е�Square�����뵽��ά������
	private void appendToSquares() {
		List<Square> squares = this.currentPiece.getSquares();
		//����Piece�е�Square
		for(Square square : squares) {
			for(int i = 0; i < this.squares.length; i++) {
				for (int j = 0; j < this.squares[i].length; j++) {
					//�õ���ǰ�����е�Square
					Square s = this.squares[i][j];
					//�ж�Square�Ƿ�һ��
					if (s.equals(square)) this.squares[i][j] = square;
				}
			}
		}
		frame.getGamePanel().repaint();
	}

	//������һ��
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
	
	
	//�Ƿ�����ͣ״̬
	public boolean getPauseFlag() {
		return this.pauseFlag;
	}
	
	//��ȡ����
	public int getScore() {
		return this.score;
	}
	
	//��ȡ��ǰ�ȼ�
	public int getCurrentLevel() {
		return this.currentLevel;
	}
}
