package com.cyendra.tetris.object;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Piece {
	
	private List<Square> squares;
	protected List<List<Square>> changes = new ArrayList<List<Square>>();
	
	protected Random random = new Random();
	protected int currentIndex;
	public final static int SQUARE_BORDER = 16;
	
	public List<Square> getSquares() {
		return squares;
	}

	public void setSquares(List<Square> squares) {
		this.squares = squares;
	}

	public List<Square> getDefault() {
		int defaultChange = random.nextInt(changes.size());
		this.currentIndex = defaultChange;
		return changes.get(defaultChange);
	}

	public void change(){
		if (this.changes.size() == 0) return;
		this.currentIndex += 1;
		if (this.currentIndex >= this.changes.size()) this.currentIndex = 0; 
		this.squares = this.changes.get(this.currentIndex);
	}

	public void setSquaresXLocation(int x) {
		for (int i = 0; i < this.changes.size(); i++) {
			List<Square> change = this.changes.get(i);
			for (int j = 0; j < change.size(); j++) {
				Square s = change.get(j);
				s.setBeginX(s.getBeginX() + x);
			}
		}
	}
	
	public void setSquaresYLocation(int y) {
		for (int i = 0; i < this.changes.size(); i++) {
			List<Square> change = this.changes.get(i);
			for (int j = 0; j < change.size(); j++) {
				Square s = change.get(j);
				s.setBeginY(s.getBeginY() + y);
			}
		}
	}
	
	public int getMaxXLocation() {
		int result = 0;
		for (int i = 0; i < this.squares.size(); i++) {
			Square s = this.squares.get(i);
			if (s.getBeginX() > result) result = s.getBeginX();
		}
		return result + SQUARE_BORDER;
	}
	
	public int getMinXLocation() {
		int result = Integer.MAX_VALUE;
		for (int i = 0; i < this.squares.size(); i++) {
			Square s = this.squares.get(i);
			if (s.getBeginX() < result) result = s.getBeginX();
		}
		return result;
	}
	
	public int getMaxYLocation() {
		int result = 0;
		for (int i = 0; i < this.squares.size(); i++) {
			Square s = this.squares.get(i);
			if (s.getBeginY() > result) result = s.getBeginY();
		}
		return result + SQUARE_BORDER;
	}
	
	public int getMinYLocation() {
		int result = Integer.MAX_VALUE;
		for (int i = 0; i < this.squares.size(); i++) {
			Square s = this.squares.get(i);
			if (s.getBeginY() < result) result = s.getBeginY();
		}
		return result + SQUARE_BORDER;
	}
}

