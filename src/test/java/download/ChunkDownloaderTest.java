package download;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class ChunkDownloaderTest {

    private String url = "http://redirector.redefine.pl/vm2movies/7t6ry9bhbrz53of87a4xpky3wbiec1a5.mp4";

    @Test
    public void shouldWriteFileWhenGiveByteRangeAndUrl() {
        
        try {
            new ChunkDownloader(0,999999,url).readAndWrite(0,"999999");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        File shouldExist = new File("/home/jcoh/prog/homeWork/CyfrowyPolsat/Plik 0.dat");
        assertTrue(shouldExist.exists());
    }

    @Test
    public void shouldHaveRightNameWhenWriteFile() {
        String name = "Plik 0";
        File shouldExist = new File("/home/jcoh/prog/homeWork/CyfrowyPolsat/"+ name +".dat");
        assertTrue(shouldExist.exists());
    }


}