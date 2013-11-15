package com.cyendra.tetris.object;

import java.awt.Image;

/**
 * 基本方块类  v1.00
 * @author cyendra
 */
public class Square {

	//方块所用图片
	private Image image;
	
	//方块的坐标
	private int beginX;
	private int beginY;
	
	/**
	 * 构造一个方块
	 * @param image 方块所用图片
	 * @param beginX 横坐标
	 * @param beginY 纵坐标
	 */
	public Square(Image image, int beginX, int beginY) {
		this.image = image;
		this.beginX = beginX;
		this.beginY = beginY;
	}
	
	/**
	 * 构造一个透明方块
	 * @param beginX 横坐标
	 * @param beginY 纵坐标
	 */
	public Square(int beginX, int beginY) {
		this.beginX = beginX;
		this.beginY = beginY;
	}
	
	/**
	 * 获取方块的图像
	 * @return 方块的Image
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * 设置方块的图像
	 * @param image 方块的Image
	 */
	public void setImage(Image image) {
		this.image = image;
	}
	
	/**
	 * 设置方块的横坐标
	 * @param beginX 横坐标
	 */
	public void setBeginX(int beginX) {
		this.beginX = beginX;
	}
	
	/**
	 * 获取方块的横坐标
	 * @return 横坐标
	 */
	public int getBeginX() {
		return beginX;
	}

	/**
	 * 设置方块的纵坐标
	 * @param beginY 纵坐标
	 */
	public void setBeginY(int beginY) {
		this.beginY = beginY;
	}
	
	/**
	 * 获取方块的纵坐标
	 * @return 纵坐标
	 */
	public int getBeginY() {
		return beginY;
	}
	
	/**
	 * 判断两个方块的位置是否相同
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
