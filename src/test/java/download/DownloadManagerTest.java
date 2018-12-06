package download;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class DownloadManagerTest {

    private String url = "http://redirector.redefine.pl/vm2movies/7t6ry9bhbrz53of87a4xpky3wbiec1a5.mp4";

    @Test
    public void shouldWriteEveryNextFileWithSameRangeWhenGiveByteRangeAndUrl() {

        try {
            new DownloadManager().execute(url,1000000);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<File> files = new ArrayList<File>();
        File Plik0 = new File("/home/jcoh/prog/homeWork/CyfrowyPolsat/Plik 0.dat");
        File Plik1000000= new File("/home/jcoh/prog/homeWork/CyfrowyPolsat/Plik 1000000.dat");
        File Plik2000000 = new File("/home/jcoh/prog/homeWork/CyfrowyPolsat/Plik 2000000.dat");
        files.add(Plik0);
        files.add(Plik1000000);
        files.add(Plik2000000);
        for (File f: files) {
            assertTrue(f.exists());
        }
    }
}