import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;

public class CommonConfig {
    public static final String FILE_NAME = "Common.cfg";

	private static int NumberOfdPreferredNeighbors = 3;
    private static int UnchokingInterval = 5;
	private static int OptimisticUnchokingInterval = 10;
	private static String FileName = "thefile";
	private static int FileSize = 2167705;
	private static int PieceSize = 16384;

    public CommonConfig() {
        try {
            BufferedReader in = new BufferedReader(new FileReader(FILE_NAME));
            for (String line; (line = in.readLine()) != null;) {
                String[] config = line.split(" ");
                String name = config[0];
                String value = config[1];

                // TODO: Hasini read config gile
            }
        }
        catch (Exception e) {

        }
    } 
}