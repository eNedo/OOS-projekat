import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class FileClass
{
    public static final int FILESIZE =134;

    private String NameOfFile;
    private byte FileAllocate;
    private byte isDirectory;
    private String DateCreated;
    private String DateUpdated;
    private String DateUsed;
    private int filesize;

 //
    // dodati bit mapu i funkcije



    public FileClass(String NameOfFile)
    {
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath,"rw"))
        {
            randomAccessFile.seek(MainClass.ONEMB - FILESIZE);
            randomAccessFile.writeUTF("hello");
            randomAccessFile.writeUTF("ma' nigga");

        }catch(Exception ex)
        {
        	 
        }


    }

}