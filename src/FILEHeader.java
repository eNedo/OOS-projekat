import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FILEHeader {
private String NameOfFileSystem;
private String DateCreated; 
private int NumberOfDirectoriums; 
private int NumberOfFiles;
private int FreeSpace;
private static final int MaximumSizeOfFile=65536;
private static final int MaximumNumberOfDirs=3495;
private static final int MaximumNumberOfFiles=7653;
private static final int BlockSize=256;
 
	public FILEHeader(String name)
	{
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'u' HH:mm:ss z");  
		NameOfFileSystem=name;
		Date date = new Date(System.currentTimeMillis());  
		DateCreated=formatter.format(date);
		NumberOfDirectoriums=1;
		NumberOfFiles=0;
		FreeSpace=19922944;
	}
	public void write(RandomAccessFile x) 
	{ 
		try {
		x.seek(0);
		x.writeChars(NameOfFileSystem);
		x.writeChars(DateCreated);
		x.writeInt(BlockSize);
		x.writeInt(FreeSpace);
		x.writeInt(NumberOfFiles);
		x.writeInt(NumberOfDirectoriums);
		x.writeInt(MaximumSizeOfFile);
		x.writeInt(MaximumNumberOfFiles);
		x.writeInt(MaximumNumberOfDirs);
		}
		catch(Exception e)
		{}
	} 
}
