package com.cyendra.tetris.object;


public interface PieceCreator {
	
	/**
	 * 在x和y座标中创建一个Piece对象
	 * @return Piece 
	 */
	Piece createPiece(int x, int y);
	
}
