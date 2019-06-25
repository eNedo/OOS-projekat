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

    public void writeFiletoMFTheader(RootHeader rootheader,RandomAccessFile file)
    {
        try
        {
        	int helpcounter=0;
        	file.seek(MainClass.ONEMB-106);
        	 int numberofMFTfiles=0;
        	    int numberofMFTheaders=0;
        	    System.out.println(rootheader.getSizeOfMFTDirs()+" "+rootheader.getSizeOfMFTFiles());
  
        	    byte allocationflag, mftflag;  					//prvo pokretanje
  if((rootheader.getSizeOfMFTDirs()+rootheader.getSizeOfMFTFiles())==0)    file.seek(file.getFilePointer()-64);
  else {
        	    for(int i=(rootheader.getSizeOfMFTDirs()+rootheader.getSizeOfMFTFiles());i>0;i--)
        	    { 
        	    allocationflag=file.readByte(); 
        	    mftflag=file.readByte(); 
        	    if(mftflag==1) numberofMFTfiles++; else numberofMFTheaders++; 
        	    if (isMFTfile==1 && allocationflag==1 && mftflag==1){helpcounter++; file.seek(file.getFilePointer()-66); break ; } 
        	    if (isMFTfile==0 && allocationflag==1) {helpcounter++; file.seek(file.getFilePointer()-2);  break; }
          	    if (i!=1) {if(mftflag==1)
          	    	file.seek(file.getFilePointer()-174);
          	    else file.seek(file.getFilePointer()-108); 
        	    }
        	    }
        	    if (helpcounter==0){
if (isMFTfile==1 && 170<(MainClass.ONEMB-(rootheader.getNumberOfDirectoriums()*255+347)-(numberofMFTfiles*170)-(numberofMFTheaders*106)) )  
	file.seek(file.getFilePointer()-238); 
        	    else // provjera za granicne slucajeve tj. da li ima memorije za upis
if(isMFTfile==0  && 106<(MainClass.ONEMB-(rootheader.getNumberOfDirectoriums()*255+347)-(numberofMFTfiles*170)-(numberofMFTheaders*106)) )	
	file.seek(file.getFilePointer()-108);
        	    }
        	  } 
  // upis 
        	if (isMFTfile == 1) 
        		file.write(DataBlock); //64
        	file.writeByte(isAllocated);       //2
        	file.writeByte(isMFTfile);
            for (;NameOfFile.length()<18;)
            	NameOfFile=NameOfFile+" "; 
            file.writeUTF(NameOfFile);      //20  -18 karaktera ime (UTF) 
            System.out.println(NameOfFile+ " "+ NameOfFile.length()); 
            file.writeInt(fileSize);               //4
            file.writeInt(startBlock);         //4
            file.writeInt(numberOfBlocks);     //4
            file.writeUTF(DateCreated);     //24
            file.writeUTF(DateLastUsed);    //24
            file.writeUTF(DateLastModified);//24
            if (isMFTfile==1) rootheader.setSizeOfMFTFiles(rootheader.getSizeOfMFTFiles()+1); 
            else rootheader.setSizeOfMFTDirs(rootheader.getSizeOfMFTDirs()+1); 
            rootheader.stats();
         } catch (Exception e)
        {
        }
    }
    public int searchMFTfiles(RootHeader rootheader,RandomAccessFile file,String filename)
    {
        try
        {
        		rootheader.stats(); 
        		for (; filename.length()<18;) filename=filename+" ";
        		file.seek(MainClass.ONEMB-106);
         	    byte  mftflag; 
         	    String temp; 
        	    for(int i=rootheader.getSizeOfMFTDirs()+rootheader.getSizeOfMFTFiles();i>0;i--)
        	    { 
        	    file.readByte();		//preskacemo allocate fleg
         	    mftflag=file.readByte(); 
         	    if (mftflag==1) 
         	    	{ 
         	    	 temp=file.readUTF();
         	    	System.out.println(temp+ " " +  temp.length());
         	    	System.out.println(filename+ " "+filename.length());		//TODO  
           	    		if(filename.equals(temp)) return 1;
         	    	}
         	    if (mftflag==1) file.seek(file.getFilePointer()-194);
         	    else file.seek(file.getFilePointer()-108);
        	    } 
        	    return 0; 		// 0-fajl nije pronadjen
        } catch (Exception e)	// 1-fajl pronadjen
        {
        }
        return -1;			//greska 
    }
    public int deleteMFTFile(RootHeader rootheader,RandomAccessFile file,String filename)
    {
        try
        {
        		rootheader.stats(); 
        		for (; filename.length()<18;) filename=filename+" ";
        		file.seek(MainClass.ONEMB-106);
         	    byte  mftflag; 
         	    String temp; 
        	    for(int i=rootheader.getSizeOfMFTDirs()+rootheader.getSizeOfMFTFiles();i>0;i--)
        	    { 
        	    file.readByte();		//preskacemo allocate fleg
         	    mftflag=file.readByte(); 
         	    if (mftflag==1) 
         	    	{ 
         	    	 temp=file.readUTF();
         	    	System.out.println(temp+ " " +  temp.length());
         	    	System.out.println(filename+ " "+filename.length());		//TODO  
           	    		if(filename.equals(temp)) 
           	    					{
           	    						file.seek(file.getFilePointer()-22); byte temp2=1;
           	    						file.writeByte(temp2);
           	    					}
         	    	}
         	    if (mftflag==1) file.seek(file.getFilePointer()-194);
         	    else file.seek(file.getFilePointer()-108);
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
