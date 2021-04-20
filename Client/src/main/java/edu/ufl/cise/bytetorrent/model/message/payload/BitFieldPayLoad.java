package edu.ufl.cise.bytetorrent.model.message.payload;

public class BitFieldPayLoad extends PayLoad {

	private static final long serialVersionUID = -3029384910659431585L;
	private byte[] bitfield;

	public BitFieldPayLoad(byte[] bitfield) {
		super();
		this.bitfield = bitfield;
	}

	public byte[] getBitfield() {
		return bitfield;
	}

	public void setBitfield(byte[] bitfield) {
		this.bitfield = bitfield;
	}

}
