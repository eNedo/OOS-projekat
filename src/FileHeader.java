import java.io.*;

public class FileHeader
{
    private byte isAllocated;       //2
    private byte isMFTfile;
    private String NameOfFile;      //20
    private int fileSize;               //4
    private int startBlock;         //4
    private int numberOfBlocks;     //4
    private String DateCreated;     //24
    private String DateLastUsed;    //24
    private String DateLastModified;//24
    private byte[] DataBlock;       //64

    public FileHeader(String name, int filesize, int numberofblocks, int startingblock, byte isMFTfile, byte[] datablocks)
    {
        NameOfFile = name;
        this.DateCreated=Utilities.getCurrentDate();
        this.DateLastModified=Utilities.getCurrentDate();
        this.DateLastUsed=Utilities.getCurrentDate();
        this.fileSize =filesize;
        this.numberOfBlocks = numberofblocks;
        startBlock=startingblock;
        this.isMFTfile = isMFTfile;
        if (isMFTfile == 1) DataBlock = datablocks;
    }

    public void writeFiletoMFTheader(RootHeader rh,RandomAccessFile x)
    {
        try
        {
        	x.seek(MainClass.ONEMB-106);
        	 int numberofMFTfiles=0;
        	    int numberofMFTheaders=0;
        	    System.out.println(rh.getSizeOfMFTDirs()+" "+rh.getSizeOfMFTFiles());
        	    byte allocationflag, mftflag;  					//prvo pokretanje
  if((rh.getSizeOfMFTDirs()+rh.getSizeOfMFTFiles())==0)    x.seek(x.getFilePointer()-64);
  else {
        	    for(int i=(rh.getSizeOfMFTDirs()+rh.getSizeOfMFTFiles());i>0;i--)
        	    { 
        	    allocationflag=x.readByte(); 
        	    mftflag=x.readByte(); 
        	    if(mftflag==1) numberofMFTfiles++; else numberofMFTheaders++; 
        	    if (isMFTfile==1 && allocationflag==1 && mftflag==1){ x.seek(x.getFilePointer()-66); break;} 
        	    if (isMFTfile==0 && allocationflag==1) {x.seek(x.getFilePointer()-2); break; }
        	   if (i==rh.getSizeOfMFTDirs()+rh.getSizeOfMFTFiles()) {
        	    if (170>((rh.getNumberOfDirectoriums()*255+347)+(numberofMFTfiles*170)+(numberofMFTheaders*106)) )
					{x.seek(x.getFilePointer()-66); break;}  
        	    else 		// provjera za granicne slucajeve tj. da li ima memorije za upis
				if(106>((rh.getNumberOfDirectoriums()*255+347)+(numberofMFTfiles*170)+(numberofMFTheaders*106)) )			
					 {x.seek(x.getFilePointer()-2); break; }
        	    x.seek(x.getFilePointer()-106);	//pomjeranje na naredni heder
        	   }
        	  } 
  }	    
        	// upis 
        	if (isMFTfile == 1) x.write(DataBlock); //64
            x.writeByte(isAllocated);       //2
            x.writeByte(isMFTfile);
            for (;NameOfFile.length()<18;)
            	NameOfFile=NameOfFile+" "; 
            x.writeUTF(NameOfFile);      //20  -18 karaktera ime (UTF) 
            System.out.println(NameOfFile+ " "+ NameOfFile.length()); 
            x.writeInt(fileSize);               //4
            x.writeInt(startBlock);         //4
            x.writeInt(numberOfBlocks);     //4
            x.writeUTF(DateCreated);     //24
            x.writeUTF(DateLastUsed);    //24
            x.writeUTF(DateLastModified);//24
            if (isMFTfile==1) rh.setSizeOfMFTFiles(rh.getSizeOfMFTFiles()+1); 
            else rh.setSizeOfMFTDirs(rh.getSizeOfMFTDirs()+1); 
            rh.stats();
         } catch (Exception e)
        {
        }
    }
    public int searchMFTfiles(RootHeader rh,RandomAccessFile x,String filename)
    {
        try
        {
        	rh.stats(); 
        	x.seek(MainClass.ONEMB-105);
         	    byte  mftflag; 
        	    for(int i=rh.getSizeOfMFTDirs()+rh.getSizeOfMFTFiles();i>0;i--)
        	    { 
         	    mftflag=x.readByte(); 
         	    if (mftflag==1) 
         	    	{ 
         	    		x.seek(x.getFilePointer()+2); 
         	    		String temp=x.readUTF(); 
         	    		System.out.println(temp+ " "+ temp.length()); 
         	    		if(filename.compareTo(x.readUTF())==0) return 1;
         	    	}
         	    if (mftflag==1) x.seek(x.getFilePointer()-171);
         	    else x.seek(x.getFilePointer()-107);
        	    } 
        	    return 0; 		// 0-fajl nije pronadjen
        } catch (Exception e)	// 1-fajl pronadjen
        {
        }
        return -1;			//greska 
    }
    public void getDataFromFile(RandomAccessFile x, byte[] temp) throws IOException
    {
        x.read(temp);
        x.seek(x.getFilePointer() - 64);
    }

    public void setNameOfFile(RandomAccessFile x, String name) throws IOException
    {
        x.seek(x.getFilePointer() + 66);
        x.writeUTF(name);
        x.seek(x.getFilePointer() - 86);
    }

    public String getNameOfFile(RandomAccessFile x) throws IOException
    {
        x.seek(x.getFilePointer() + 66);
        String temp = x.readUTF();
        x.seek(x.getFilePointer() - 86);
        return temp;
    }

    public void setsize(RandomAccessFile x, int SIZE) throws IOException
    {
        x.seek(x.getFilePointer() + 86);
        x.writeInt(SIZE);
        x.seek(x.getFilePointer() - 90);
    }

    public int getSize(RandomAccessFile x) throws IOException
    {
        x.seek(x.getFilePointer() + 86);
        int temp = x.readInt();
        x.seek(x.getFilePointer() - 90);
        return temp;
    }

    public void setFileFreeForWrite(RandomAccessFile x, byte l) throws IOException
    {
        x.seek(x.getFilePointer() + 64);
        x.writeByte(l);
        x.seek(x.getFilePointer() - 65);
    }

    public void setisMFTFILE(RandomAccessFile x, byte l) throws IOException
    {
        x.seek(x.getFilePointer() + 65);
        x.writeByte(l);
        x.seek(x.getFilePointer() - 66);
    }

    public byte isFileFreeForWrite(RandomAccessFile x) throws IOException
    {
        x.seek(x.getFilePointer() + 64);
        byte temp = x.readByte();
        x.seek(x.getFilePointer() - 65);
        return temp;
    }

    public byte getisMFTFILE(RandomAccessFile x) throws IOException
    {
        x.seek(x.getFilePointer() + 65);
        byte temp = x.readByte();
        x.seek(x.getFilePointer() - 66);
        return temp;
    }

    public String getDateLastModified(RandomAccessFile x) throws IOException
    {

        x.seek(x.getFilePointer() + 146);
        String temp = x.readUTF();
        x.seek(x.getFilePointer() - 170);
        return temp;
    }

    public String getDateLastUsed(RandomAccessFile x) throws IOException
    {

        x.seek(x.getFilePointer() + 122);
        String temp = x.readUTF();
        x.seek(x.getFilePointer() - 146);
        return temp;
    }

    public String getDateCreated(RandomAccessFile x) throws IOException
    {

        x.seek(x.getFilePointer() + 98);
        String temp = x.readUTF();
        x.seek(x.getFilePointer() - 122);
        return temp;
    }

    public void setDateLastModified(RandomAccessFile x, String date) throws IOException
    {

        x.seek(x.getFilePointer() + 146);
        x.writeUTF(date);
        x.seek(x.getFilePointer() - 170);
    }

    public void setDateLastUsed(RandomAccessFile x, String date) throws IOException
    {

        x.seek(x.getFilePointer() + 122);
        x.writeUTF(date);
        x.seek(x.getFilePointer() - 146);
    }

    public void setDateCreated(RandomAccessFile x, String date) throws IOException
    {

        x.seek(x.getFilePointer() + 98);
        x.writeUTF(date);
        x.seek(x.getFilePointer() - 122);
    }
}
