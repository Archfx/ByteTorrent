package edu.ufl.cise.bytetorrent.model.message.payload;


public class RequestPayLoad extends PayLoad {

	private static final long serialVersionUID = -7231898069513751867L;
	private int index;

	public RequestPayLoad(int index) {
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
