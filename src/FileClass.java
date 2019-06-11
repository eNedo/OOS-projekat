import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class FileClass
{
    public static final int FILESIZE =134;

    private byte FileAllocate;
    private byte isDirectory;
    private int filesize;
    private String NameOfFile;
    private String DateCreated;
    private String DateUpdated;
    private String DateUsed;

 //
    // dodati bit mapu i funkcije



    public FileClass(String NameOfFile)
    {
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath,"rw"))
        {


        }catch(Exception ex)
        {
        	 
        }


    }

}
