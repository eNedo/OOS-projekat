import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DirectoryClass
{
    private String NameOfDirectory;
    private String DateCreated;
    private String DateUsed;
    private String DateModified;
    private byte isDirectory=1;
    private byte FileAllocationFlag;
    private byte DepthFlag;
    private byte ResFlag;
    private int Size;
    private int signature ;
    public DirectoryClass(String name, byte Depth, int sig, int size)
    { 
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'u' HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        DateCreated = formatter.format(date);
    	Size=size;
    	NameOfDirectory=name; 
    	DepthFlag=Depth; 
    	signature=sig; 
    	
    }
    public void writedir(RandomAccessFile x, FileHeader fh) 
    { 
    	try { 
    	x.seek(0); 
    	x.seek(102); 
    	if (fh.NumOfDirs(x)!=1)    
    	{
    		for (int i=fh.NumOfDirs(x); i>0; i--) 
    	x.seek(x.getFilePointer()+168); 
    	}
  		     	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'u' HH:mm:ss");
  		        Date date = new Date(System.currentTimeMillis());
 		        DateCreated = formatter.format(date);
 		        DateUsed=formatter.format(date); 
 		        DateModified=formatter.format(date); 
  		       for(; NameOfDirectory.length()<15; )
 		       NameOfDirectory=NameOfDirectory+" ";
  		       x.writeChars(NameOfDirectory);
  		       x.writeChars(DateUsed);
  		       System.out.println("xxxx");
  		       x.writeChars(DateCreated);
  		       x.writeChars(DateModified); 
  		       x.write(isDirectory);
  		       x.writeInt(signature);
  		       x.write(DepthFlag);
  		       x.write(FileAllocationFlag);
  		       x.writeInt(Size);
 		} 
    	catch (Exception e) {} 
    }
}

