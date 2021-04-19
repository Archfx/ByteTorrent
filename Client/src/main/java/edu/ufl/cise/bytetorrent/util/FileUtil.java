package edu.ufl.cise.bytetorrent.util;

public class FileUtil {

	private FileUtil(){

	}

	public static byte toByte(boolean[] bool) {
		if (bool.length > 8)
			throw new RuntimeException("boolean array length needed to be greater than 8");
		byte val = 0;
		for (boolean x : bool) {
			val = (byte) (val << 1);
			val = (byte) (val | (x ? 1 : 0));
		}
		return val;
	}

	public static boolean[] toBool(byte val) {
		boolean[] bool = new boolean[8];
		for (int i = 0; i < 8; i++) {
			bool[7 - i] = (val & 1) == 1 ? true : false;
			val = (byte) (val >> 1);
		}
		return bool;
	}

	public static void updateBitfield(long index, byte[] bitfield) {
		int i = (int) (index / 8);
		int u = (int) (index % 8);
		byte update = 1;
		update = (byte) (update << u);
		bitfield[i] = (byte) (bitfield[i] | update);
	}

}