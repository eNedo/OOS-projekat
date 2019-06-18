import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class FileData
{
    public static final int FILESIZE =128;

    private byte FileAllocate;
    private byte[] dataArray;//125
    private short nextBlock;

	public FileClass(short x)
	{ 
		nextBlock=x;
 	}
	
	public void writeToFile(/* fajl?*/ ) 
	{ 						
		
	} 
    
	//public //return fajl? reconstructFileFromBlocks() 
	//{ 
		
	//}
 

}
