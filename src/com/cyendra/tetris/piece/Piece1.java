package com.cyendra.tetris.piece;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import com.cyendra.tetris.object.Piece;
import com.cyendra.tetris.object.Square;

public class Piece1 extends Piece {
	
	public Piece1(Image image) {

		List<Square> squares0 = new ArrayList<Square>();
		squares0.add(new Square(image, image.getWidth(null), image.getHeight(null)));
		squares0.add(new Square(image, 0, image.getHeight(null)*2));
		squares0.add(new Square(image, image.getWidth(null), image.getHeight(null)*2));
		squares0.add(new Square(image, image.getWidth(null)*2, image.getHeight(null)*2));

		List<Square> squares1 = new ArrayList<Square>();
		squares1.add(new Square(image, 0, 0));
		squares1.add(new Square(image, 0, image.getHeight(null)));
		squares1.add(new Square(image, image.getWidth(null), image.getHeight(null)));
		squares1.add(new Square(image, 0, image.getHeight(null)*2));

		List<Square> squares2 = new ArrayList<Square>();
		squares2.add(new Square(image, 0, 0));
		squares2.add(new Square(image, image.getWidth(null), 0));
		squares2.add(new Square(image, image.getWidth(null)*2, 0));
		squares2.add(new Square(image, image.getWidth(null), image.getHeight(null)));

		List<Square> squares3 = new ArrayList<Square>();
		squares3.add(new Square(image, image.getWidth(null), image.getHeight(null)));
		squares3.add(new Square(image, image.getWidth(null)*2, 0));
		squares3.add(new Square(image, image.getWidth(null)*2, image.getHeight(null)));
		squares3.add(new Square(image, image.getWidth(null)*2, image.getHeight(null)*2));
		
		super.changes.add(squares0);
		super.changes.add(squares1);
		super.changes.add(squares2);
		super.changes.add(squares3);

		super.setSquares(getDefault());
	}
	
}
