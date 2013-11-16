package com.cyendra.tetris.util;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.cyendra.tetris.exception.GameException;
import com.cyendra.tetris.object.Piece;
import com.cyendra.tetris.object.Square;
/**
 * 
 * 
 * */
public class ImageUtil {
	
	/**
	 * ʹ��ImageIO��ȡͼƬ
	 * @param imagePath ͼƬ��ַ
	 * @return imagePath��ַ�µ�ͼƬ
	 * @exception IOException ��ȡͼƬ�����쳣
	 * */
	public static BufferedImage getImage(String imagePath) {
    try {
   		return ImageIO.read(new File(imagePath));
   	} 
    catch (IOException e) {
   		throw new GameException("Image��ȡ����");
    	}
	}
	
	/**
	 * �ڽ����ϻ�һ��Piece����
	 * @param g Graphics
	 * @param piece Piece
	 */
	public static void paintPiece(Graphics g, Piece piece) {
		if (piece == null) return;
		for (int i = 0; i < piece.getSquares().size(); i++) {
			Square s = piece.getSquares().get(i);
			//System.out.println(s.getImage().toString());
			g.drawImage(s.getImage(), s.getBeginX(), s.getBeginY(), null);
		}
	}
}
