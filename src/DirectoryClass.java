import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class DirectoryClass
{
	public static final int DIRHEADERSIZE = 256;

    private byte isAllocatedFlag;		//1
    private String nameOfDirectory;		//17
    private byte depthFlag;				//18
    private String dateCreated;			//18+24=42
    private String dateUsed;			//42+24=68
    private String dateModified;		//68+24=92
    private int size;					//92+4
//	private Vector<Short> arrayOfDirs = new Vector(81);
	private short[] arrayOfDirs; // vrijednost 55555 ako prazan element

    public DirectoryClass(String name, byte Depth)
    {
    	this.isAllocatedFlag=1;
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'u' HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        dateModified=dateUsed=dateCreated = formatter.format(date);
    	nameOfDirectory =name;
    	depthFlag =Depth;
    	arrayOfDirs= new short[81];
    	for(int i=0; i<arrayOfDirs.length; i++)
    	    arrayOfDirs[i]=(short)55555;

    	this.size=0;

    }

	public byte getIsAllocatedFlag(RandomAccessFile randomAccessFile) throws IOException
	{
		this.isAllocatedFlag=randomAccessFile.readByte();
		randomAccessFile.seek(randomAccessFile.getFilePointer()-1);
		return isAllocatedFlag;
	}

	public void setIsAllocatedFlag(RandomAccessFile randomAccessFile,byte isAllocatedFlag) throws IOException
	{
		this.isAllocatedFlag=isAllocatedFlag;
		randomAccessFile.writeByte(isAllocatedFlag);
		randomAccessFile.seek(randomAccessFile.getFilePointer()-1);
	}

	public String getNameOfDirectory(RandomAccessFile randomAccessFile) throws IOException
	{
		randomAccessFile.seek(randomAccessFile.getFilePointer()+1);
		nameOfDirectory=randomAccessFile.readUTF();
		randomAccessFile.seek(randomAccessFile.getFilePointer()-17); // 16+1
		return nameOfDirectory;
	}

	public void setNameOfDirectory(RandomAccessFile randomAccessFile,String nameOfDirectory) throws IOException
	{
		this.nameOfDirectory = nameOfDirectory;
		randomAccessFile.seek(randomAccessFile.getFilePointer()+1);
		randomAccessFile.writeUTF(nameOfDirectory);
		randomAccessFile.seek(randomAccessFile.getFilePointer()-17);

	}

	public byte getDepthFlag(RandomAccessFile randomAccessFile) throws IOException
	{
		randomAccessFile.seek(randomAccessFile.getFilePointer()+17);
		this.depthFlag=randomAccessFile.readByte();
		randomAccessFile.seek(randomAccessFile.getFilePointer()-18);
		return depthFlag;
	}

	public void setDepthFlag(RandomAccessFile randomAccessFile,byte depthFlag)throws IOException
	{
		this.depthFlag = depthFlag;
		randomAccessFile.seek(randomAccessFile.getFilePointer()+17);
		randomAccessFile.writeByte(depthFlag);
		randomAccessFile.seek(randomAccessFile.getFilePointer()-18);

	}

	public String getDateCreated(RandomAccessFile randomAccessFile) throws IOException
	{
		randomAccessFile.seek(randomAccessFile.getFilePointer()+18);
		this.dateCreated=randomAccessFile.readUTF();
		randomAccessFile.seek(randomAccessFile.getFilePointer()-42);	// 18+24
		return dateCreated;

	}

	public void setDateCreated(RandomAccessFile randomAccessFile,String dateCreated) throws IOException
	{
		this.dateCreated = dateCreated;
		randomAccessFile.seek(randomAccessFile.getFilePointer()+18);
		randomAccessFile.writeUTF(dateCreated);
		randomAccessFile.seek(randomAccessFile.getFilePointer()-42);
	}

	public String getDateUsed(RandomAccessFile randomAccessFile) throws IOException
	{
		randomAccessFile.seek(randomAccessFile.getFilePointer()+42);	//18+24
		this.dateUsed=randomAccessFile.readUTF();
		randomAccessFile.seek(randomAccessFile.getFilePointer()-68);
		return dateUsed;
	}

	public void setDateUsed(RandomAccessFile randomAccessFile, String dateUsed) throws IOException
	{
		this.dateUsed = dateUsed;
		randomAccessFile.seek(randomAccessFile.getFilePointer()+42);	//18+24
		randomAccessFile.writeUTF(dateUsed);
		randomAccessFile.seek(randomAccessFile.getFilePointer()-68);	//42+24
	}

	public String getDateModified(RandomAccessFile randomAccessFile) throws IOException
	{
		randomAccessFile.seek(randomAccessFile.getFilePointer()+68);	//
		this.dateModified=randomAccessFile.readUTF();
		randomAccessFile.seek(randomAccessFile.getFilePointer()-92);
		return dateModified;
	}

	public void setDateModified(RandomAccessFile randomAccessFile,String dateModified) throws IOException
	{
		this.dateModified = dateModified;
		randomAccessFile.seek(randomAccessFile.getFilePointer()+68);
		randomAccessFile.writeUTF(dateModified);
		randomAccessFile.seek(randomAccessFile.getFilePointer()-92);
	}

	public int getSize(RandomAccessFile randomAccessFile)throws IOException
	{
		randomAccessFile.seek(randomAccessFile.getFilePointer()+92);
		this.size=randomAccessFile.readInt();
		randomAccessFile.seek(randomAccessFile.getFilePointer()-96);
		return size;
	}

	public void setSize(RandomAccessFile randomAccessFile,int size)throws IOException
	{
		this.size = size;
		randomAccessFile.seek(randomAccessFile.getFilePointer()+92);
		randomAccessFile.writeInt(size);
		randomAccessFile.seek(randomAccessFile.getFilePointer()-96);
	}
	public short[] getArrayOfDirs(RandomAccessFile randomAccessFile) throws IOException
	{
		short[] readArray = new short[81];
		randomAccessFile.seek(randomAccessFile.getFilePointer()+96);
		for(int i=0; i<readArray.length; i++)
		{
		    readArray[i]=randomAccessFile.readByte();
		}
		randomAccessFile.seek(randomAccessFile.getFilePointer()-DIRHEADERSIZE);
		return readArray;
	}

	public void setNewDirectory(RandomAccessFile randomAccessFile, short blockNumber) throws IOException
	{
		long currentPosition = randomAccessFile.getFilePointer();
		randomAccessFile.seek(randomAccessFile.getFilePointer()+96);

		boolean flag = false;
		for(int i=0; i<arrayOfDirs.length; i++)
		{
			short tmp;
			tmp=randomAccessFile.readShort();
			if(tmp==(short)55_555)
			{
				flag=true;
				randomAccessFile.seek(randomAccessFile.getFilePointer()-2);
				randomAccessFile.writeShort(blockNumber);
				break;
			}
		}
		if(!flag)
		{
			System.out.println("no space for new Direcotry");
		}
		randomAccessFile.seek(currentPosition);
	}

	public void writeDirInFile(RandomAccessFile randomAccessFile) throws IOException
	{
		randomAccessFile.writeByte(this.isAllocatedFlag);
		randomAccessFile.writeUTF(this.nameOfDirectory);
		randomAccessFile.writeByte(this.depthFlag);
		randomAccessFile.writeUTF(this.dateCreated);
		randomAccessFile.writeUTF(this.dateUsed);
		randomAccessFile.writeUTF(this.dateModified);
		randomAccessFile.writeInt(this.size);
		for(int i=0; i<arrayOfDirs.length; i++)
		    randomAccessFile.writeShort(arrayOfDirs[i]);

		randomAccessFile.seek(randomAccessFile.getFilePointer()-DIRHEADERSIZE);
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
 		        dateCreated = formatter.format(date);
 		        dateUsed =formatter.format(date);
 		        dateModified =formatter.format(date);
  		       for(; nameOfDirectory.length()<15; )
 		       nameOfDirectory = nameOfDirectory +" ";
  		       x.writeChars(nameOfDirectory);
  		       x.writeChars(dateUsed);
  		       System.out.println("xxxx");
  		       x.writeChars(dateCreated);
  		       x.writeChars(dateModified);
  		       x.write(depthFlag);
  		       x.write(isAllocatedFlag);
  		       x.writeInt(size);
 		}
    	catch (Exception e) {}
    }
}

