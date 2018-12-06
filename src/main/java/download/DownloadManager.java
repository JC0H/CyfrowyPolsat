package download;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DownloadManager {
    private static String url;
    private static int chunkSize;
    private int httpCommunicationThreads= 6;

    public void execute(String url, int chunkSize) throws IOException, InterruptedException {
        int downloadFrom = 0;
        int downloadTo = chunkSize;
        int fileSize = getFileSize(url);

        ExecutorService pool = Executors.newFixedThreadPool(httpCommunicationThreads);
        while (fileSize > downloadFrom) {
            if (downloadTo == fileSize)
                pool.submit(new ChunkDownloader(downloadFrom, -1, url));
            else
                pool.submit(new ChunkDownloader(downloadFrom, downloadTo, url));
            downloadFrom = downloadTo;
            if (downloadTo + chunkSize <= fileSize)
                downloadTo = downloadTo + chunkSize;
            else
                downloadTo = fileSize;
        }
        pool.shutdown();
        pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    }

    public static int getFileSize(String endpoint) throws IOException {
        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        if (connection.getResponseCode() != 200) {
            System.out.println("Error return code != 200");
        }
        int contentLength = connection.getContentLength();
        connection.disconnect();
        return contentLength;
    }

    public static void main(String s[]) throws IOException, InterruptedException {
        System.out.print("Type URL to main.java.download: ");
        Scanner scan = new Scanner(System.in);
        url = scan.nextLine();

        System.out.println ("Chunk size : ");
        chunkSize =scan.nextInt();
        new DownloadManager().execute(url,chunkSize);
    }
}
