import java.io.*;
import java.lang.Math; 
import java.lang.*;
public class FileHeader
{
    private byte isAllocated;       //2
    private byte isMFTfile;
    private String NameOfFile;      //20
    private int fileSize;           //4
    private int startBlock=-1;         //4
    private int numberOfBlocks=-1;     //4
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
        	    System.out.println(rootheader.getsizeOfMFTFileHeaders()+" "+rootheader.getSizeOfMFTFiles());
  
        	    byte allocationflag, mftflag;  					//prvo pokretanje
  if((rootheader.getsizeOfMFTFileHeaders()+rootheader.getSizeOfMFTFiles())==0)    file.seek(file.getFilePointer()-64);
  else {
        	    for(int i=(rootheader.getsizeOfMFTFileHeaders()+rootheader.getSizeOfMFTFiles());i>0;i--)
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
            DataSegment object=new DataSegment();
            if(fileSize!=0) 	{ 
				numberOfBlocks=fileSize/123;
				Math.ceil(numberOfBlocks);
            } else 
            	{	fileSize=0; numberOfBlocks=-1; }  
            startBlock=object.writeDataInDataSegment(numberOfBlocks, NameOfFile);
            file.writeInt(startBlock);         //4
            file.writeInt(numberOfBlocks);     //4
            file.writeUTF(DateCreated);     //24
            file.writeUTF(DateLastUsed);    //24
            file.writeUTF(DateLastModified);//24
            if (isMFTfile==1) rootheader.setSizeOfMFTFiles(rootheader.getSizeOfMFTFiles()+1); 
            else rootheader.setsizeOfMFTFileHeaders(rootheader.getsizeOfMFTFileHeaders()+1); 
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
        	    for(int i=rootheader.getsizeOfMFTFileHeaders()+rootheader.getSizeOfMFTFiles();i>0;i--)
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
    public byte [] READFILE(RootHeader rootheader,RandomAccessFile file,String filename)
    {
        try
        {
         		for (; filename.length()<18;) filename=filename+" ";
        		file.seek(MainClass.ONEMB-106);
         	    byte  mftflag; 
         	    String temp; 
         	    byte [] array=new byte[64];
        	    for(int i=rootheader.getsizeOfMFTFileHeaders()+rootheader.getSizeOfMFTFiles();i>0;i--)
        	    { 
        	    file.readByte();	 
         	    mftflag=file.readByte(); 
          	    	 temp=file.readUTF();
         	    	System.out.println(temp+ " " +  temp.length());
         	    	System.out.println(filename+ " "+filename.length());		 
           	    		if(filename.equals(temp))  //citanje obicnog (data) fajla 
           	    			{
 	    						file.readInt(); 
	    						int  startblock=file.readInt(); 
	    						int numberofblocks=file.readInt(); 
	    						DataSegment object=new DataSegment(); 
	    						array=object.readDataFromDataSegment (startblock, numberofblocks);
	    						return array;
           	    			}
           	    if (filename.equals(temp) && mftflag==1) //citanje mft fajla 
           	    			{ 
            	    			file.seek(file.getFilePointer()-86);
           	    				file.read(array);
           	    				return array;
           	    			}
          	    if (mftflag==1) file.seek(file.getFilePointer()-194);
         	    else file.seek(file.getFilePointer()-108);
        	    } 
        	    return null; 		 
        } catch (Exception e) 
        {
        }
        return null; 
    }
    public int recoverFile(RootHeader rootheader,RandomAccessFile file,String filename)
    {
        try
        {
        		rootheader.stats(); 
        		for (; filename.length()<18;) filename=filename+" ";
        		file.seek(MainClass.ONEMB-106);
         	    byte  mftflag; 
         	    String temp; 
        	    for(int i=rootheader.getsizeOfMFTFileHeaders()+rootheader.getSizeOfMFTFiles();i>0;i--)
        	    { 
        	    file.readByte();		
         	    mftflag=file.readByte(); 
         	    temp=file.readUTF();
         	    	System.out.println(temp+ " " +  temp.length());
         	    	System.out.println(filename+ " "+filename.length());		//TODO  
           	    		if(filename.equals(temp)) 
           	    					{
           	    						file.seek(file.getFilePointer()-22); byte temp2=1;
           	    						file.writeByte(temp2);
           	    						file.readUTF(); 
           	    						file.readInt(); 
           	    						int  startblock=file.readInt(); 
           	    						int numberofblocks=file.readInt(); 
           	    						DataSegment object=new DataSegment(); 
           	    						object.recoverFileInDataSegment(startblock, numberofblocks);
           	    					}      	    	 
         	    if (mftflag==1) file.seek(file.getFilePointer()-194);
         	    else file.seek(file.getFilePointer()-108);
        	    } 
        	    return 0; 		 
        } catch (Exception e)	 
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
        	    for(int i=rootheader.getsizeOfMFTFileHeaders()+rootheader.getSizeOfMFTFiles();i>0;i--)
        	    { 
        	    file.readByte();		//preskacemo allocate fleg
         	    mftflag=file.readByte(); 
         	    	 temp=file.readUTF();
         	    	System.out.println(temp+ " " +  temp.length());
         	    	System.out.println(filename+ " "+filename.length());		//TODO  
           	    		if(filename.equals(temp)) 
           	    					{
           	    						file.seek(file.getFilePointer()-22); byte temp2=0;
           	    						file.writeByte(temp2);
           	    						file.readUTF(); 
           	    						file.readInt(); 
           	    						int  startblock=file.readInt(); 
           	    						int numberofblocks=file.readInt(); 
           	    						DataSegment object=new DataSegment(); 
           	    						object.deleteFileInDataSegment(startblock, numberofblocks);
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
    public long getFileHeaderPosition(RootHeader rootheader,RandomAccessFile file,String filename)
    {
        try
        {
         		for (; filename.length()<18;) filename=filename+" ";
        		file.seek(MainClass.ONEMB-106);
         	    byte  mftflag; 
         	    String temp; 
        	    for(int i=rootheader.getsizeOfMFTFileHeaders()+rootheader.getSizeOfMFTFiles();i>0;i--)
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
           	    						file.seek(file.getFilePointer()-22);  
            	    					return file.getFilePointer();
           	    					}
         	    	}
         	    if (mftflag==1) file.seek(file.getFilePointer()-194);
         	    else file.seek(file.getFilePointer()-108);
        	    } 
        	    return -1; 		 
        } catch (Exception e)	 
        {
        }
        return -1;			//greska 
    }
    public void getDataFromFile(RandomAccessFile x, byte[] temp, long  position) throws IOException
    {
    	x.seek(position);
        x.read(temp);
     }

    public void setNameOfFile(RandomAccessFile x, String name,long  position) throws IOException
    {
        x.seek(position+2);
        x.writeUTF(name);
     }

    public String getNameOfFile(RandomAccessFile x,long position) throws IOException
    {
        x.seek(position + 2);
         return   x.readUTF();
    }

    public void setsize(RandomAccessFile x, int SIZE,long position) throws IOException
    {
        x.seek(position+ 22);
        x.writeInt(SIZE);
     }

    public int getSize(RandomAccessFile x,long position) throws IOException
    {
        x.seek(position + 22);
        return x.readInt();
    }

    public void setFileFreeForWrite(RandomAccessFile x, byte l,long position) throws IOException
    {
        x.seek(position);
        x.writeByte(l);
     }

    public void setisMFTFILE(RandomAccessFile x, byte l,long position) throws IOException
    {
        x.seek(position + 1);
        x.writeByte(l);
     }

    public byte isFileFreeForWrite(RandomAccessFile x,long position) throws IOException
    {
        x.seek(position);
         return x.readByte();
    }

    public byte getisMFTFILE(RandomAccessFile x,long position) throws IOException
    {
        x.seek(position  +1);
          return x.readByte();
        }

    public String getDateLastModified(RandomAccessFile x,long position) throws IOException
    {

        x.seek(position+ 82);
          return  x.readUTF();
    }

    public String getDateLastUsed(RandomAccessFile x,long position) throws IOException
    {

        x.seek(position + 58);
        return x.readUTF();
    }
    public String getDateCreated(RandomAccessFile x,long position) throws IOException
    {
        x.seek(position + 34);
          return  x.readUTF();
    }
    public void setDateLastModified(RandomAccessFile x, String date,long position) throws IOException
    {
        x.seek(position + 82);
        x.writeUTF(date);
     }
    public void setDateLastUsed(RandomAccessFile x, String date,long position) throws IOException
    {
        x.seek(position + 58);
        x.writeUTF(date);
     }
    public void setDateCreated(RandomAccessFile x, String date,long position) throws IOException
    {
        x.seek(position + 34);
        x.writeUTF(date);
     }
}
