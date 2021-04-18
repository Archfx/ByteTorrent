package peer.message.payload;

public class HavePayLoad extends PayLoad {

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
