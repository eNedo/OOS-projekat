import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class MainClass {
	private static long FILESIZE=1024*1024*20;
	public static void main(String args[])
	{
	try {
		RandomAccessFile x=new RandomAccessFile("./FILESYSTEM","rw");
		x.setLength(FILESIZE);		
		
		
		
		
		
		x.close();
		}
	catch(FileNotFoundException e)
	{} 
	catch(IOException f)
	{}
	
	
		
	}
}
