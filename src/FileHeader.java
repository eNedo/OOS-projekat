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
            x.writeChars(NameOfFileSystem);	 
            x.writeChars(DateCreated);		 
            x.writeInt(BlockSize);			 
            x.writeInt(FreeSpace);
            x.writeInt(NumberOfFiles);
            x.writeInt(NumberOfDirectoriums);
            x.writeInt(MaximumSizeOfFile);
            x.writeInt(MaximumNumberOfFiles);
            x.writeInt(MaximumNumberOfDirs);
        } catch (Exception e)
        {
        } 
    }
    public FileHeader readfromfileandupdate(RandomAccessFile x)
    {
    	try 
    	{ 
    		 FileHeader fh;
    		x.seek(0); 
    		char c;
    		String filename=""; 
    		String date=""; 
    		StringBuilder sb1 =new StringBuilder();
    		StringBuilder sb2 =new StringBuilder();
    		for (int i=0;15>i;i++) 
    					{
    						c=x.readChar();
    						sb1.append(c); 
    					}
    		  fh=new FileHeader(sb1.toString());

    		for(int i=0;22>i;i++)
    		{
    			c=x.readChar(); 
    			sb2.append(c); 
    		}
    		fh.DateCreated=sb2.toString();
    		fh.FreeSpace=x.readInt();
    		fh.NumberOfFiles=x.readInt();
    		fh.NumberOfDirectoriums=x.readInt();
    		return fh; 
     	}
    	catch(Exception e) 
    	{ }
    	return null; 
    } 
    public void readINFOfromfile(RandomAccessFile x)
    { 
    	try {
    		x.seek(0); 
    		char c;
    		String filename=""; 
    		String date=""; 
    		StringBuilder sb1 =new StringBuilder();
    		StringBuilder sb2 =new StringBuilder();
    		for (int i=0;15>i;i++) 
    					{
    						c=x.readChar();
    						sb1.append(c); 
    					}
    		filename=sb1.toString(); 
    		for(int i=0;22>i;i++)
    		{
    			c=x.readChar(); 
    			sb2.append(c); 
    		}
    		date=sb2.toString();
    		System.out.println("************INFORMACIJE-O-FAJL-SISTEMU**************");
    		System.out.println("Ime:"+filename); 
    		System.out.println("Datum kreiranja:"+date); 
    		System.out.println("Velicina bloka:"+x.readInt()); 
    		System.out.println("Slobodan prostor:"+x.readInt()); 
    		System.out.println("Broj fajlova:"+x.readInt()); 
    		System.out.println("Broj direktorijuma:"+x.readInt()); 
    		System.out.println("Maksimalna velicina fajla:"+x.readInt()); 
    		System.out.println("Maksimalan broj fajlova :"+x.readInt()); 
    		System.out.println("Maksimalan broj direktorijuma:"+x.readInt()); 
    	}catch (Exception e) {} 
    }
   public  void updateNumberOfDirectoriums(RandomAccessFile f)
    {
		try { 
		    f.seek(0); 
		    f.seek(2*37+4*3);
		    int temp=f.readInt();
		    f.seek(2*37+12); 
		    f.writeInt(++temp); 
		     	} 
		    	catch(Exception e) {} 
     }
    public void updateNumberOfFiles(RandomAccessFile f,int x)
    {
    	try { 
		    f.seek(0); 
		    f.seek(2*37+4*2);
		    f.writeInt(x);
		     	} 
		    	catch(Exception e) {} 	
    }
    public void updateFreeSpace(RandomAccessFile f,int x)
    {
    	try { 
		    f.seek(0); 
		    f.seek(2*37+4*1);
		    f.writeInt(x);
		     	} 
		    	catch(Exception e) {} 	
    }


	public int NumOfDirs(RandomAccessFile f)
	{
		try {
			f.seek(0); 
			f.seek(86); 
			return f.readInt(); 
		} 
		catch(Exception e) {} 
		return 0; 
	} 
	
	}
