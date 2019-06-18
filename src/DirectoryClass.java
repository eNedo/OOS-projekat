import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class DirectoryClass
{
    public static final int DIRHEADERSIZE = 255;

    private byte isAllocatedFlag;        //1
    private String nameOfDirectory;        //17
    private byte depthFlag;                //18
    private String dateCreated;            //18+24=42
    private String dateUsed;            //42+24=68
    private String dateModified;        //68+24=92
    private int size;                    //92+4
    private short[] arrayOfDirs; // vrijednost 55555 ako prazan element

    public DirectoryClass(String name, byte Depth)
    {
        this.isAllocatedFlag = 1;
        dateModified = dateUsed = dateCreated = Utilities.getCurrentDate();
        nameOfDirectory = String.format("%-14s", name);
        depthFlag = Depth;
        arrayOfDirs = new short[82];
        for (int i = 0; i < arrayOfDirs.length; i++)
            arrayOfDirs[i] = (short) 5555;

        this.size = 0;

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
        for (int i = 0; i < arrayOfDirs.length; i++)
            randomAccessFile.writeShort(arrayOfDirs[i]);

        randomAccessFile.seek(randomAccessFile.getFilePointer() - DIRHEADERSIZE);
    }


    public byte getIsAllocatedFlag(RandomAccessFile randomAccessFile) throws IOException
    {
        this.isAllocatedFlag = randomAccessFile.readByte();
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 1);
        return isAllocatedFlag;
    }

    public void setIsAllocatedFlag(RandomAccessFile randomAccessFile, byte isAllocatedFlag) throws IOException
    {
        this.isAllocatedFlag = isAllocatedFlag;
        randomAccessFile.writeByte(isAllocatedFlag);
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 1);
    }

    public String getNameOfDirectory(RandomAccessFile randomAccessFile) throws IOException
    {
        randomAccessFile.seek(randomAccessFile.getFilePointer() + 1);
        nameOfDirectory = randomAccessFile.readUTF();
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 17); // 16+1
        return nameOfDirectory.trim();
    }

    public void setNameOfDirectory(RandomAccessFile randomAccessFile, String nameOfDirectory) throws IOException
    {

        this.nameOfDirectory = String.format("%-14s", nameOfDirectory);
        randomAccessFile.seek(randomAccessFile.getFilePointer() + 1);
        randomAccessFile.writeUTF(this.nameOfDirectory);
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 17);

    }

    public byte getDepthFlag(RandomAccessFile randomAccessFile) throws IOException
    {
        randomAccessFile.seek(randomAccessFile.getFilePointer() + 17);
        this.depthFlag = randomAccessFile.readByte();
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 18);
        return depthFlag;
    }

    public void setDepthFlag(RandomAccessFile randomAccessFile, byte depthFlag) throws IOException
    {
        this.depthFlag = depthFlag;
        randomAccessFile.seek(randomAccessFile.getFilePointer() + 17);
        randomAccessFile.writeByte(depthFlag);
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 18);

    }

    public String getDateCreated(RandomAccessFile randomAccessFile) throws IOException
    {
        randomAccessFile.seek(randomAccessFile.getFilePointer() + 18);
        this.dateCreated = randomAccessFile.readUTF();
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 41);    // 18+23
        return dateCreated;

    }

    public void setDateCreated(RandomAccessFile randomAccessFile, String dateCreated) throws IOException
    {
        this.dateCreated = dateCreated;
        randomAccessFile.seek(randomAccessFile.getFilePointer() + 18);
        randomAccessFile.writeUTF(dateCreated);
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 41);
    }

    public String getDateUsed(RandomAccessFile randomAccessFile) throws IOException
    {
        randomAccessFile.seek(randomAccessFile.getFilePointer() + 41);    //18+24
        this.dateUsed = randomAccessFile.readUTF();
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 64);
        return dateUsed;
    }

    public void setDateUsed(RandomAccessFile randomAccessFile, String dateUsed) throws IOException
    {
        this.dateUsed = dateUsed;
        randomAccessFile.seek(randomAccessFile.getFilePointer() + 41);    //18+24
        randomAccessFile.writeUTF(dateUsed);
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 64);    //42+24
    }

    public String getDateModified(RandomAccessFile randomAccessFile) throws IOException
    {
        randomAccessFile.seek(randomAccessFile.getFilePointer() + 64);    //
        this.dateModified = randomAccessFile.readUTF();
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 87);
        return dateModified;
    }

    public void setDateModified(RandomAccessFile randomAccessFile, String dateModified) throws IOException
    {
        this.dateModified = dateModified;
        randomAccessFile.seek(randomAccessFile.getFilePointer() + 64);
        randomAccessFile.writeUTF(dateModified);
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 87);
    }

    public int getSize(RandomAccessFile randomAccessFile) throws IOException
    {
        randomAccessFile.seek(randomAccessFile.getFilePointer() + 87);
        this.size = randomAccessFile.readInt();
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 91);
        return size;
    }

    public void setSize(RandomAccessFile randomAccessFile, int size) throws IOException
    {
        this.size = size;
        randomAccessFile.seek(randomAccessFile.getFilePointer() + 87);
        randomAccessFile.writeInt(size);
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 91);
    }

    public short[] getArrayOfDirs(RandomAccessFile randomAccessFile) throws IOException
    {
        short[] readArray = new short[82];
        randomAccessFile.seek(randomAccessFile.getFilePointer() + 91);
        for (int i = 0; i < readArray.length; i++)
        {
            readArray[i] = randomAccessFile.readShort();
        }
        randomAccessFile.seek(randomAccessFile.getFilePointer() - DIRHEADERSIZE);
        return readArray;
    }

    public void setNewDirectory(RandomAccessFile randomAccessFile, short blockNumber) throws IOException
    {
        long currentPosition = randomAccessFile.getFilePointer();
        randomAccessFile.seek(randomAccessFile.getFilePointer() + 91);

        boolean flag = false;
        for (int i = 0; i < arrayOfDirs.length; i++)
        {
            short tmp;
            tmp = randomAccessFile.readShort();
            if (tmp == (short) 55_555)
            {
                flag = true;
                randomAccessFile.seek(randomAccessFile.getFilePointer() - 2);
                randomAccessFile.writeShort(blockNumber);
                break;
            }
        }
        if (!flag)
        {
            System.out.println("no space for new Direcotry");
        }
        randomAccessFile.seek(currentPosition);
    }

	public void listDirectory(RandomAccessFile x,RootHeader fh)
	{ 
		try {
	    	x.seek(0);
	    	x.seek(102);
	     
	    		for (int i=fh.getNumberOfDirectoriums(); i>0; i--){
 	  		       System.out.println("Ime direktorijuma:"+x.readUTF());
 	  		       System.out.println("Datum koristenja:"+x.readUTF());
 	  		       System.out.println("Datum kreiranja:"+x.readUTF());
 	  		       System.out.println("Datum modifikovanja:"+x.readUTF());
 	  		       x.seek(x.getFilePointer()+2);
 	  		       System.out.println("Velicina:"+x.readInt());
	  		       x.write(isAllocatedFlag);
	  		       x.writeInt(size);
	 		}
		}
	    	catch (Exception e) {}
	} 
}

