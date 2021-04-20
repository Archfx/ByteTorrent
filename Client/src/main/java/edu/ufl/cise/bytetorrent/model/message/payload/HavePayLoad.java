package edu.ufl.cise.bytetorrent.model.message.payload;

public class HavePayLoad extends PayLoad {

	private static final long serialVersionUID = -8313533564107545387L;
	private int index;

	public HavePayLoad(int index) {
		super();
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
