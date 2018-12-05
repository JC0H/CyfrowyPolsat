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
    private static String url;
    private static int chunkSize;

    public static void main(String s[]) throws IOException, InterruptedException {
        System.out.print("Type URL to download: ");
        Scanner scan = new Scanner(System.in);
        url = scan.nextLine();

        System.out.println ("Chunk size : ");
        chunkSize =scan.nextInt();

        System.out.println(new Date());
        int downloadFrom = 0;
        int downloadTo = chunkSize;
        int fileSize = getFileSize();

        ExecutorService pool = Executors.newFixedThreadPool(6);
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

    private static int getFileSize() throws IOException {
        URL url = new URL(DownloadManager.url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        if (connection.getResponseCode() != 200) {
            System.out.println("Error return code != 200");
        }
        int contentLength = connection.getContentLength();
        connection.disconnect();
        return contentLength;
    }
}
