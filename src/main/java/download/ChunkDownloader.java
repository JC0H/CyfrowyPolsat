package download;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ChunkDownloader implements Runnable {
    private String fileURL ;
    private int downloadStartingByte;
    private int downloadEndingByte;
    private int IOthreads = 2;

    public ChunkDownloader(int downloadStartingByte, int downloadEndingByte, String fileURL) {
        this.downloadStartingByte = downloadStartingByte;
        this.downloadEndingByte = downloadEndingByte;
        this.fileURL = fileURL;
    }

    public void readAndWrite(int startFromByte, String endWithByte) throws IOException, InterruptedException {
        HttpURLConnection bytesRange = getBytePortionFromUrl(startFromByte, endWithByte);
        writeBytePortionToFile(startFromByte, bytesRange);
    }

    private HttpURLConnection getBytePortionFromUrl(int startingByte, String endingByte) throws IOException {
        HttpDownloader downloader = new HttpDownloader(startingByte,endingByte,fileURL);
        return downloader.getBytes(startingByte,endingByte,fileURL);
    }

    private void writeBytePortionToFile(int startingByte, HttpURLConnection connection) throws InterruptedException {
        ExecutorService IOpool = Executors.newFixedThreadPool(IOthreads);
        IOpool.submit(new FileWriter(startingByte,connection));
        IOpool.shutdown();
        IOpool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    }

    public void run() {
        try {
            if (downloadEndingByte == -1)
                readAndWrite(downloadStartingByte, "");
            else
                readAndWrite(downloadStartingByte, String.valueOf(downloadEndingByte));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
