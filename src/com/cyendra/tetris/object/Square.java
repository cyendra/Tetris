package com.cyendra.tetris.object;

import java.awt.Image;

/**
 * ����������  v1.00
 * @author cyendra
 */
public class Square {

	//��������ͼƬ
	private Image image;
	
	//���������
	private int beginX;
	private int beginY;
	
	/**
	 * ����һ������
	 * @param image ��������ͼƬ
	 * @param beginX ������
	 * @param beginY ������
	 */
	public Square(Image image, int beginX, int beginY) {
		this.image = image;
		this.beginX = beginX;
		this.beginY = beginY;
	}
	
	/**
	 * ����һ��͸������
	 * @param beginX ������
	 * @param beginY ������
	 */
	public Square(int beginX, int beginY) {
		this.beginX = beginX;
		this.beginY = beginY;
	}
	
	/**
	 * ��ȡ�����ͼ��
	 * @return �����Image
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * ���÷����ͼ��
	 * @param image �����Image
	 */
	public void setImage(Image image) {
		this.image = image;
	}
	
	/**
	 * ���÷���ĺ�����
	 * @param beginX ������
	 */
	public void setBeginX(int beginX) {
		this.beginX = beginX;
	}
	
	/**
	 * ��ȡ����ĺ�����
	 * @return ������
	 */
	public int getBeginX() {
		return beginX;
	}

	/**
	 * ���÷����������
	 * @param beginY ������
	 */
	public void setBeginY(int beginY) {
		this.beginY = beginY;
	}
	
	/**
	 * ��ȡ�����������
	 * @return ������
	 */
	public int getBeginY() {
		return beginY;
	}
	
	/**
	 * �ж����������λ���Ƿ���ͬ
	 * @return True/False
	 */
	public boolean equals(Object obj) {
		if (obj instanceof Square) {
			Square s = (Square)obj;
			if ((s.getBeginX() == this.getBeginX() && (s.getBeginY() == this.getBeginY()))) {
				return true;
			}
		}
		return false;
	}
}
