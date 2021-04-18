package peer.message.payload;


public class RequestPayLoad extends PayLoad {

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
