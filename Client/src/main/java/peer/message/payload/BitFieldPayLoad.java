package peer.message.payload;

public class BitFieldPayLoad extends PayLoad {

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
