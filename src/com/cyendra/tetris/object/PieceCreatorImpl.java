package com.cyendra.tetris.object;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.cyendra.tetris.piece.*;
import com.cyendra.tetris.util.ImageUtil;

public class PieceCreatorImpl implements PieceCreator {

	//�������ɫ��
	private final static int COLOR_SIZE = 7;
	
	//�����������
	private final static int SQUARE_SIZE = 7;
	
	//����Square���õ�Image
	private Map<Integer, Image> images = new HashMap<Integer, Image>();

	//�����������
	private Random random = new Random();

	//��ʼ������һ���󷽿�
	private Piece initPiece(Image image) {
		Piece piece = null;
		int pieceType = random.nextInt(SQUARE_SIZE);
		switch (pieceType) {
		case 0:
			piece = new Piece0(image);
			break;
		case 1:
			piece = new Piece1(image);
			break;
		case 2:
			piece = new Piece2(image);
			break;
		case 3:
			piece = new Piece3(image);
			break;
		case 4:
			piece = new Piece4(image);
			break;
		case 5:
			piece = new Piece5(image);
			break;
		case 6:
			piece = new Piece6(image);
			break;
		default:
			piece = new Piece0(image);
			break;
		}
		return piece;
	}

	//��map�еõ�ͼƬ�������map��û�д���ͼƬ����, �򴴽�
	private Image getImage(int key) {
		if (this.images.get(key) == null) {
			Image s = ImageUtil.getImage("images/square" + key + ".jpg");
			this.images.put(key, s);
		}
		return this.images.get(key);
	}
	
	@Override
	public Piece createPiece(int x, int y) {
		Image image = getImage(random.nextInt(COLOR_SIZE));
		Piece piece = initPiece(image);
		piece.setSquaresXLocation(x);
		piece.setSquaresYLocation(y);
		return piece;
	}

}
