package com.cyendra.tetris.object;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** 
 * 大方块类
 * 包含多个基本方块
 * @author cyendra
 * */
public class Piece {
	
	//小方块的边长
	public final static int SQUARE_BORDER = 16;
	
	//小方块颜色编号
	private int key;
	
	//该大方块所包含的小方块
	private List<Square> squares = null;
	
	//该大方块四种方向变化
	protected List<List<Square>> changes = new ArrayList<List<Square>>();
	
	//当前变化在changes集合中的索引
	protected int currentIndex = 0;
	
	//获得当前大方块
	public List<Square> getSquares() {
		return squares;
	}
	
	//设置当前大方块
	public void setSquares(List<Square> squares) {
		this.squares = squares;
	}

	//随机取得在预览区预设的大方块
	public List<Square> getDefault() {
		Random random = new Random();
		this.currentIndex =  random.nextInt(changes.size());
		return changes.get(this.currentIndex);
	}

	//翻转当前方块
	public void change(){
		if (this.changes.size() == 0) return;
		this.currentIndex = (this.currentIndex + 1) % this.changes.size();
		this.squares = this.changes.get(this.currentIndex);
	}
	
	/**
	 * 获取下一个翻转
	 * */
	public List<Square> getNextSquares() {
		if (this.changes.size() == 0) return null;
		int Index = (this.currentIndex + 1) % this.changes.size();
		List<Square> sq = this.changes.get(Index);
		return sq;
	}

	/**
	 * 获取方块颜色
	 * */
	public int getColor() {
		return key;
	}
	
	//让Piece对象中的所有Square对象的x座标都加上参数x
	public void setSquaresXLocation(int x) {
		for (int i = 0; i < this.changes.size(); i++) {
			for (int j = 0; j < this.changes.get(i).size(); j++) {
				Square s = changes.get(i).get(j);
				s.setBeginX(s.getBeginX() + x);
			}
		}
	}
	
	//让Piece对象中的所有Square对象的y座标都加上参数y
	public void setSquaresYLocation(int y) {
		for (int i = 0; i < this.changes.size(); i++) {
			for (int j = 0; j < this.changes.get(i).size(); j++) {
				Square s = changes.get(i).get(j);;
				s.setBeginY(s.getBeginY() + y);
			}
		}
	}
	
	//得到当前变化中X座标最大的值
	
	public int getMaxXLocation() {
		int result = 0;
		for (int i = 0; i < this.squares.size(); i++) {
			Square s = this.squares.get(i);
			if (s.getBeginX() > result) result = s.getBeginX();
		}
		return result + SQUARE_BORDER;
	}
	
	//得到当前变化中X座标的最小值
	public int getMinXLocation() {
		int result = Integer.MAX_VALUE;
		for (int i = 0; i < this.squares.size(); i++) {
			Square s = this.squares.get(i);
			if (s.getBeginX() < result) result = s.getBeginX();
		}
		return result;
	}
	
	//得到当前变化中Y座标最大值
	public int getMaxYLocation() {
		int result = 0;
		for (int i = 0; i < this.squares.size(); i++) {
			Square s = this.squares.get(i);
			if (s.getBeginY() > result) result = s.getBeginY();
		}
		return result + SQUARE_BORDER;
	}
	
	//得到当前变化中Y座标最小值
	public int getMinYLocation() {
		int result = Integer.MAX_VALUE;
		for (int i = 0; i < this.squares.size(); i++) {
			Square s = this.squares.get(i);
			if (s.getBeginY() < result) result = s.getBeginY();
		}
		return result + SQUARE_BORDER;
	}

	public void setColor(int rand) {
		this.key = rand;
	}

}

