package download;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpDownloader /*implements Runnable*/{
    private String fileURL;
    private int downloadStartingByte;
    private String downloadEndingByte;
    private HttpURLConnection connection;

    public HttpDownloader(int startingByte, String endingByte,String fileURL) {
        this.downloadStartingByte=startingByte;
        this.downloadEndingByte=endingByte;
        this.fileURL = fileURL;
    }

    public HttpURLConnection getBytes(int startingByte, String endingByte,String fileURL) throws IOException {
        URL url = new URL(fileURL);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Range", "bytes=" + startingByte + "-" + endingByte);
        connection.connect();
        if (connection.getResponseCode() != 206) {
            System.out.println("Error return code != 206");
        }
        return connection;
    }
}
