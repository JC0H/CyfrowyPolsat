package download;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FileWriter implements Runnable {
    private int downloadedStartingByte;
    private HttpURLConnection connection;
    private static final int MAX_BUFFER_SIZE = 1024 * 64 * 4;
    private static final String targetFileName = "Plik ";

    public FileWriter(int downloadStartingByte, HttpURLConnection connection) {
        this.downloadedStartingByte=downloadStartingByte;
        this.connection=connection;
    }

    private void writeToFile(int startingByte,HttpURLConnection connection) throws IOException {
        int downloaded = 0;
        int contentLength = connection.getContentLength();
        RandomAccessFile file = new RandomAccessFile(targetFileName + startingByte +".dat", "rw");
        file.seek(0);
        InputStream stream = connection.getInputStream();
        while (true) {
            byte buffer[];
            if (contentLength - downloaded > MAX_BUFFER_SIZE) {
                buffer = new byte[MAX_BUFFER_SIZE];
            } else {
                buffer = new byte[contentLength - downloaded];
            }
            int read = stream.read(buffer);
            if (read == -1 || downloaded == contentLength)
                break;
            file.write(buffer, 0, read);
            downloaded += read;
        }
        file.close();
    }

    @Override
    public void run() {
        try {
            writeToFile(downloadedStartingByte,connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
