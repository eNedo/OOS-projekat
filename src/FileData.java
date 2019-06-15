import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class FileData
{
    public static final int FILESIZE =128;

    private byte FileAllocate;
    private byte[] dataArray;//125
    private short nextBlock;


 //
    // dodati bit mapu i funkcije



    public FileData(String NameOfFile)
    {
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath,"rw"))
        {


        }catch(Exception ex)
        {
        	 
        }


    }

}
