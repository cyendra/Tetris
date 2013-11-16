package com.cyendra.tetris.object;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** 
 * �󷽿���
 * ���������������
 * @author cyendra
 * */
public class Piece {
	
	//С����ı߳�
	public final static int SQUARE_BORDER = 16;
	
	//С������ɫ���
	private int key;
	
	//�ô󷽿���������С����
	private List<Square> squares = null;
	
	//�ô󷽿����ַ���仯
	protected List<List<Square>> changes = new ArrayList<List<Square>>();
	
	//��ǰ�仯��changes�����е�����
	protected int currentIndex = 0;
	
	//��õ�ǰ�󷽿�
	public List<Square> getSquares() {
		return squares;
	}
	
	//���õ�ǰ�󷽿�
	public void setSquares(List<Square> squares) {
		this.squares = squares;
	}

	//���ȡ����Ԥ����Ԥ��Ĵ󷽿�
	public List<Square> getDefault() {
		Random random = new Random();
		this.currentIndex =  random.nextInt(changes.size());
		return changes.get(this.currentIndex);
	}

	//��ת��ǰ����
	public void change(){
		if (this.changes.size() == 0) return;
		this.currentIndex = (this.currentIndex + 1) % this.changes.size();
		this.squares = this.changes.get(this.currentIndex);
	}
	
	/**
	 * ��ȡ��һ����ת
	 * */
	public List<Square> getNextSquares() {
		if (this.changes.size() == 0) return null;
		int Index = (this.currentIndex + 1) % this.changes.size();
		List<Square> sq = this.changes.get(Index);
		return sq;
	}

	/**
	 * ��ȡ������ɫ
	 * */
	public int getColor() {
		return key;
	}
	
	//��Piece�����е�����Square�����x���궼���ϲ���x
	public void setSquaresXLocation(int x) {
		for (int i = 0; i < this.changes.size(); i++) {
			for (int j = 0; j < this.changes.get(i).size(); j++) {
				Square s = changes.get(i).get(j);
				s.setBeginX(s.getBeginX() + x);
			}
		}
	}
	
	//��Piece�����е�����Square�����y���궼���ϲ���y
	public void setSquaresYLocation(int y) {
		for (int i = 0; i < this.changes.size(); i++) {
			for (int j = 0; j < this.changes.get(i).size(); j++) {
				Square s = changes.get(i).get(j);;
				s.setBeginY(s.getBeginY() + y);
			}
		}
	}
	
	//�õ���ǰ�仯��X��������ֵ
	
	public int getMaxXLocation() {
		int result = 0;
		for (int i = 0; i < this.squares.size(); i++) {
			Square s = this.squares.get(i);
			if (s.getBeginX() > result) result = s.getBeginX();
		}
		return result + SQUARE_BORDER;
	}
	
	//�õ���ǰ�仯��X�������Сֵ
	public int getMinXLocation() {
		int result = Integer.MAX_VALUE;
		for (int i = 0; i < this.squares.size(); i++) {
			Square s = this.squares.get(i);
			if (s.getBeginX() < result) result = s.getBeginX();
		}
		return result;
	}
	
	//�õ���ǰ�仯��Y�������ֵ
	public int getMaxYLocation() {
		int result = 0;
		for (int i = 0; i < this.squares.size(); i++) {
			Square s = this.squares.get(i);
			if (s.getBeginY() > result) result = s.getBeginY();
		}
		return result + SQUARE_BORDER;
	}
	
	//�õ���ǰ�仯��Y������Сֵ
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

