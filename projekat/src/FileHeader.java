import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FileHeader
{
    private String NameOfFileSystem;
    private String DateCreated;
    private int NumberOfDirectoriums;
    private int NumberOfFiles;
    private int FreeSpace;
    private static final int MaximumSizeOfFile = 65536;
    private static final int MaximumNumberOfDirs = 3495;
    private static final int MaximumNumberOfFiles = 7653;
    private static final int BlockSize = 256;

    public FileHeader(String name)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'u' HH:mm:ss");
        NameOfFileSystem = name;
        Date date = new Date(System.currentTimeMillis());
        DateCreated = formatter.format(date);
        NumberOfDirectoriums = 1;
        NumberOfFiles = 0;
        FreeSpace = 19922944;
    }


    public void writeToFile(RandomAccessFile x)
    {
        try
        {
            x.seek(0);
            x.writeChars(NameOfFileSystem);	//15
            x.writeChars(DateCreated);		//30
            x.writeInt(BlockSize);			//
            x.writeInt(FreeSpace);
            x.writeInt(NumberOfFiles);
            x.writeInt(NumberOfDirectoriums);
            x.writeInt(MaximumSizeOfFile);
            x.writeInt(MaximumNumberOfFiles);
            x.writeInt(MaximumNumberOfDirs);
        } catch (Exception e)
        {
        }finally
        {
        }
    }
    void updateNumberOfDirectoriums(int x)
    {
    	//this.
    }
    void updateNumberOfFiles(int x)
    {
    	
    }
    

}