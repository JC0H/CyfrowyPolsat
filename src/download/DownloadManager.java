package download;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DownloadManager {
    private String url;
    private int chunkSize;
    private int httpCommunicationThreads= 6;

    private void execute() throws IOException, InterruptedException {
        System.out.print("Type URL to download: ");
        Scanner scan = new Scanner(System.in);
        url = scan.nextLine();

        System.out.println ("Chunk size : ");
        chunkSize =scan.nextInt();

        System.out.println(new Date());
        int downloadFrom = 0;
        int downloadTo = chunkSize;
        int fileSize = getFileSize(url);

        ExecutorService pool = Executors.newFixedThreadPool(httpCommunicationThreads);
        while (fileSize > downloadFrom) {
            if (downloadTo == fileSize)
                pool.submit(new DownloadInChunks(downloadFrom, -1, url));
            else
                pool.submit(new DownloadInChunks(downloadFrom, downloadTo, url));
            downloadFrom = downloadTo;
            if (downloadTo + chunkSize <= fileSize)
                downloadTo = downloadTo + chunkSize;
            else
                downloadTo = fileSize;
        }
        pool.shutdown();
        pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    }

    private static int getFileSize(String endpoint) throws IOException {
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
        new DownloadManager().execute();
    }
}
