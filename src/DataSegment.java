import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;


public class DataSegment
{
    public static final int DataSegmentBlockSize = 128;


    // vraca prvu slobodnu poziciju u data segmentu za upisivanje
    public static long firstFreeSpace(String path)
    {

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw");)
        {
            randomAccessFile.seek(MainClass.ONEMB);

            int i=1;
            byte readByte;
            while (randomAccessFile.getFilePointer() < randomAccessFile.length() - 1 && (readByte = randomAccessFile.readByte()) != Byte.MAX_VALUE)
            {
//                System.out.println(readByte+" while: " + randomAccessFile.getFilePointer());

                randomAccessFile.seek(MainClass.ONEMB + i * DataSegmentBlockSize);  // pomjeranje na naredni blok
                i++;
            }



            return randomAccessFile.getFilePointer();  // vraca trazenu poziciju

        } catch (IOException ex)
        {

            ex.printStackTrace();
        }
        return 0;   // doslo do neke greske, to be continue...

    }

}
