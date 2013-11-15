package com.cyendra.tetris.object;


public interface PieceCreator {
	
	/**
	 * ��x��y�����д���һ��Piece����
	 * @return Piece 
	 */
	Piece createPiece(int x, int y);
	
}
