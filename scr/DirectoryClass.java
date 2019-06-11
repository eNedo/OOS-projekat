import java.io.IOException;
import java.io.RandomAccessFile;

public class DirectoryClass
{
    private String NameOfDirectory;
    private String DateCreated;
    private String DateUsed;
    private String DateModified;
    private byte isDirectory;
    private byte FileAllocationFlag;
    private byte DepthFlag;
    private byte ResFlag;
    private int Size;
    // block?
    private byte signature[] = new byte[64];
    // funkcije..
 //

    public void flagAfterDirectory(RandomAccessFile randomAccessFile) throws IOException
    {
        randomAccessFile.writeChars("FFFFFFFF");

    }
}
