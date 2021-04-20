package edu.ufl.cise.bytetorrent.util;

public class FileUtil {
	private static final int byte_const = 8;

	private FileUtil(){ }

	public static int bitCount(byte bitVal) {
		int count = 0;
		for (int i = 0; i < byte_const; i++) {
			if((bitVal & 1) == 1 )
				count= count + 1;
			bitVal = (byte) (bitVal >> 1);
		}
		return count;
	}

	public static int bitCount(byte[] byteArray) {
		int count = 0;
		for (byte i : byteArray) {
			count = count + bitCount(i);
		}
		return count;
	}

	// Update the bitField value
	public static byte[] updateBitfield(long index, byte[] bitField) {
		byte u = 1;
		int i = (int) (index / byte_const);
		int k = (int) (index % byte_const);

		u = (byte) (u << k);
		bitField[i] = (byte) (bitField[i] | u);
		return bitField;
	}

	// Convert boolean array to byte
	public static byte toByte(boolean[] boolArray) {
		if (boolArray.length > byte_const)
			throw new RuntimeException("boolean array length needed to be greater than 8");
		byte value = 0;
		for (boolean bool : boolArray) {
			value = (byte) (value << 1);
			value = (byte) (value | (bool ? 1 : 0));
		}
		return value;
	}

	// Convert byte to bool array
	public static boolean[] toBool(byte byteVal) {
		boolean[] bool_array = new boolean[byte_const];
		for (int i = 0; i < byte_const; i++) {
			bool_array[7 - i] = (byteVal & 1) == 1;
			byteVal = (byte) (byteVal >> 1);
		}
		return bool_array;
	}

}