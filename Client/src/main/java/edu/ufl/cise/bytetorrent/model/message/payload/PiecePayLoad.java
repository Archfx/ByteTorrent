package edu.ufl.cise.bytetorrent.model.message.payload;

public class PiecePayLoad extends PayLoad {

	private static final long serialVersionUID = 5268098704512772356L;
	private byte[] content = null;
	private int index = 0;

	public PiecePayLoad(byte[] content, int index) {
		super();
		this.content = content;
		this.index = index;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
