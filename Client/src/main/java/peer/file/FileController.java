package peer.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Hashtable;


import config.CommonConfig;

public class FileController {

    private static boolean[] filePiecesOwned;
	/**
	 * Table to keep track of requested file pieces at any instant so that redundant
	 * requests are not made
	 */
	private static Hashtable<Integer, Integer> piecesNeeded = new Hashtable<Integer, Integer>();

	/** Number of file pieces the file can be broken into */
	private static final int numFilePieces = (int) Math
			.ceil((double) CommonConfig.getFileSize() / CommonConfig.getPieceSize());
	/** File pieces available by the peer */
	private static int numPiecesIHave = 0;
	private static String path = null;
	private static String fName = CommonConfig.getFileName();
	private static int fSize = CommonConfig.getFileSize();
	private static File file = null;


	public FileController(int peerId, boolean hasFile) {
		path = "peer_" + peerId + "/";

		filePiecesOwned = new boolean[numFilePieces];

		if (hasFile) {
			Arrays.fill(filePiecesOwned, true);
			numPiecesIHave = numFilePieces;
		}

		File folder = new File(path);

		if (!folder.exists()) {
			folder.mkdirs();
		}

		file = new File(path + fName);

		if (!file.exists()) {
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
				System.out.println("Writing file.");
				fos.write(new byte[fSize]);
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}


	public static boolean[] getFilePiecesOwned() {
		return filePiecesOwned;
	}


	public static void setFilePiecesOwned(boolean[] filePiecesOwned) {
		FileController.filePiecesOwned = filePiecesOwned;
	}


	public static Hashtable<Integer, Integer> getRequestedPieces() {
		return piecesNeeded;
	}


	public static void setRequestedPieces(Hashtable<Integer, Integer> piecesNeeded) {
		FileController.piecesNeeded = piecesNeeded;
	}


	public static int getNoOfPiecesAvailable() {
		return numPiecesIHave;
	}


	public static void setNoOfPiecesAvailable(int numPiecesIHave) {
		FileController.numPiecesIHave = numPiecesIHave;
	}


	public static String getDirectory() {
		return path;
	}


	public static void setDirectory(String path) {
		FileController.path = path;
	}


	public static String getFileName() {
		return fName;
	}


	public static void setFileName(String fName) {
		FileController.fName = fName;
	}


	public static int getFileSize() {
		return fSize;
	}


	public static void setFileSize(int fSize) {
		FileController.fSize = fSize;
	}


	public static File getFile() {
		return file;
	}


	public static void setFile(File file) {
		FileController.file = file;
	}


	public static int getNooffilepieces() {
		return numFilePieces;
	}


	public static boolean isInteresting(int index) {
		return filePiecesOwned[index] ? true : false;
	}


	public static synchronized boolean hasCompleteFile() {
		return numPiecesIHave == numFilePieces ? true : false;
	}


	public static synchronized byte[] getBitField() throws Exception {
		int size = (int) Math.ceil((double) numFilePieces / 8);
		byte[] bitfield = new byte[size];
		int counter = 0;
		int indexI = 0;
		// TODO Implement Professor Logic
		while (indexI < numFilePieces) {
			int temp;
			if (numFilePieces > indexI + 8) {
				temp = indexI + 8;
			} else {
				temp = numFilePieces;
			}
			bitfield[counter++] = FileUtils.toByte(Arrays.copyOfRange(filePiecesOwned, indexI, temp));
			indexI = indexI + 8;
		}
		return bitfield;
	}


	public static synchronized byte[] getFilePart(int index) {
		try {
			FileInputStream fis = new FileInputStream(file);
			int loc = CommonConfig.getPieceSize() * index;
			fis.skip(loc);
			int contentSize = CommonConfig.getPieceSize();
			if (fSize - loc < CommonConfig.getPieceSize())
				contentSize = fSize - loc;
			byte[] content = new byte[contentSize];
			fis.read(content);
			fis.close();
            System.out.println("reading chunk of file.");
			return content;
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}


	public static synchronized void store( byte[] content, int index) throws Exception {
		int loc = CommonConfig.getPieceSize() * index;
		RandomAccessFile fos = null;
		try {
			fos = new RandomAccessFile(file, "rw");
			fos.seek(loc);
			fos.write(content);
			fos.close();

			numPiecesIHave++;
			filePiecesOwned[index] = true;

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static boolean compareBitfields(byte[] neighborBitfield, byte[] bitfield) {
		boolean flag = false;
		int size = (int) Math.ceil((double) numFilePieces / 8);
		byte[] interesting = new byte[size];
		if (neighborBitfield == null) {
			return flag;
		}
		int indexI = 0;
		while (indexI < bitfield.length) {
			interesting[indexI] = (byte) ((bitfield[indexI] ^ neighborBitfield[indexI]) & neighborBitfield[indexI]);
			if (interesting[indexI] != 0) {
				flag = true;
			}
			indexI++;
		}
		return flag;
	}


	public static int requestPiece(byte[] neighborBitfield, byte[] bitfield, int nPID) {
		int size = (int) Math.ceil((double) numFilePieces / 8);
		byte[] interesting = new byte[size];
		boolean[] interestingPieces = new boolean[numFilePieces];
		int finLength;

		finLength = size > 1 ? numFilePieces % (8) : numFilePieces;

		int start, end;

		int indexI = 0, indexJ = 0;
		while (indexI < bitfield.length) {
			interesting[indexI] = (byte) ((bitfield[indexI] ^ neighborBitfield[indexI]) & neighborBitfield[indexI]);
			start = indexI == size - 1 ? 8 - finLength : 0;
			end = indexI == size - 1 ? finLength : 8;
			boolean[] x = FileUtils.toBool(interesting[indexI]);
			System.arraycopy(x, start, interestingPieces, indexJ, end);
			indexJ = indexJ + 8 < numFilePieces ? indexJ + 8 : numFilePieces - finLength;
			indexI++;
		}
		int indexK = 0;
		while (indexK < numFilePieces) {
			if (interestingPieces[indexK] == true && !piecesNeeded.containsKey(indexK)) {
				piecesNeeded.put(indexK, indexK);
				return indexK;
			}
			indexK++;
		}
		return -1;
	}

	public static void checker() {

		(new Thread() {
			@Override
			public void run() {
				try {
					do {
						Thread.sleep(60000);
						for (Integer ind : piecesNeeded.keySet()) {
							if (!filePiecesOwned[ind])
								piecesNeeded.remove(ind);
						}
					} while (numPiecesIHave < numFilePieces);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Testing file manager");
        FileController f1 = new FileController(1005, false);  

	}
    
}
