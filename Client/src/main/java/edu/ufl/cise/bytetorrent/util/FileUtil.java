package edu.ufl.cise.bytetorrent.util;

public class FileUtil {
	private static final int byte_const = 8;

	private FileUtil(){ }

	public static int bitCount(byte value) {
		int count = 0;
		for (int i = 0; i < byte_const; i++) {
			if((value & 1) == 1 )
				count= count + 1;
			value = (byte) (value >> 1);
		}
		return count;
	}

	public static int bitCount(byte[] value) {
		int count = 0;
		for (byte i : value) {
			count = count + bitCount(i);
		}
		return count;
	}

	// Update the bit_field value
	public static byte[] updateBitfield(long index, byte[] bit_field) {
		byte u = 1;
		int i = (int) (index / byte_const);
		int k = (int) (index % byte_const);

		u = (byte) (u << k);
		bit_field[i] = (byte) (bit_field[i] | u);
		return bit_field;
	}

	// Convert boolean array to byte
	public static byte toByte(boolean[] bool_array) {
		if (bool_array.length > byte_const)
			throw new RuntimeException("boolean array length needed to be greater than 8");
		byte value = 0;
		for (boolean bool : bool_array) {
			value = (byte) (value << 1);
			value = (byte) (value | (bool ? 1 : 0));
		}
		return value;
	}

	// Convert byte to bool array
	public static boolean[] toBool(byte value) {
		boolean[] bool_array = new boolean[byte_const];
		for (int i = 0; i < byte_const; i++) {
			bool_array[7 - i] = (value & 1) == 1;
			value = (byte) (value >> 1);
		}
		return bool_array;
	}

}