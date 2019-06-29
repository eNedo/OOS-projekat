import java.io.*;
import java.lang.*;
import java.util.Arrays;
public class FileHeader
{
    private byte isAllocated=1;       //2
    private byte isMFTfile;
    private String NameOfFile;      //20
    private int fileSize;           //4
    private int startBlock=-1;         //4
    private int numberOfBlocks=-1;     //4
    private String DateCreated;     //24
    private String DateLastUsed;    //24
    private String DateLastModified;//24
    private byte[] DataBlock;       //64
// obicni fajl heder 106bajta
// mft fajl heder (106+64)bajta
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
        if (isMFTfile == 1)  this.DataBlock =datablocks;
        else if(filesize>64) this.DataBlock=datablocks;
    }

    public   int  writeFiletoMFTheader(RootHeader rootheader,RandomAccessFile file, String filename)
    {
        try
        {
        	NameOfFile=filename;
        	int helpcounter=0;
        	file.seek(MainClass.ONEMB-106);
        	int maincounter=0;
        	 int numberofMFTfiles=0;
        	    int numberofMFTheaders=0;
        	    byte allocationflag, mftflag=0;  					//prvo pokretanje
  if((rootheader.getsizeOfMFTFileHeaders()+rootheader.getSizeOfMFTFiles())==0 && isMFTfile==1)    file.seek(file.getFilePointer()-64);
  else if(rootheader.getsizeOfMFTFileHeaders()+rootheader.getSizeOfMFTFiles()==0 && isMFTfile==0) file.seek(file.getFilePointer()); 
  else {
        	    for(int i=(rootheader.getsizeOfMFTFileHeaders()+rootheader.getSizeOfMFTFiles());i>0;i--)
        	    { 
        	    maincounter++;
        	    allocationflag=file.readByte(); 
        	    mftflag=file.readByte(); 
        	    if(mftflag==1) numberofMFTfiles++; else numberofMFTheaders++; 
        	    if (isMFTfile==1 && allocationflag==1 && mftflag==1){helpcounter++; file.seek(file.getFilePointer()-66); break ; } 
        	    if (isMFTfile==0 && allocationflag==1) {helpcounter++; file.seek(file.getFilePointer()-2);  break; }
          	    if (i!=1) {if(mftflag==1)
          	    	file.seek(file.getFilePointer()-172);
          	    else file.seek(file.getFilePointer()-108); 
        	    }
        	    }
        	    if (helpcounter==0){
if (isMFTfile==1 && mftflag==0 && 170<(MainClass.ONEMB-(rootheader.getNumberOfDirectoriums()*255+347)-(numberofMFTfiles*170)-(numberofMFTheaders*106)) )  
	file.seek(file.getFilePointer()-172); 
        	    else // provjera za granicne slucajeve tj. da li ima memorije za upis
        	    	if (isMFTfile==1 && mftflag==1 && 170<(MainClass.ONEMB-(rootheader.getNumberOfDirectoriums()*255+347)-(numberofMFTfiles*170)-(numberofMFTheaders*106)) )  
        	    		file.seek(file.getFilePointer()-236); 
        	    	else 
        	    		if (isMFTfile==0 && mftflag==0 && 170<(MainClass.ONEMB-(rootheader.getNumberOfDirectoriums()*255+347)-(numberofMFTfiles*170)-(numberofMFTheaders*106)) )  
        	    			file.seek(file.getFilePointer()-108); 
        	    		else 
if(isMFTfile==0 && mftflag==1 && 106<(MainClass.ONEMB-(rootheader.getNumberOfDirectoriums()*255+347)-(numberofMFTfiles*170)-(numberofMFTheaders*106)) )	
	file.seek(file.getFilePointer()-172);
        	    }
        	  } 
  // upis 
  if(isMFTfile==1 || fileSize<64) {
  byte temp[]=new byte[64];
  for (int i=0;DataBlock.length>i;i++) temp[i]=DataBlock[i]; 
  Arrays.fill(temp, DataBlock.length, 64,  (byte)32);
  file.write(temp); //64
  }
        	file.writeByte(isAllocated);       //2
        	file.writeByte(isMFTfile);
            for (;NameOfFile.length()<18;)
            	NameOfFile=NameOfFile+" "; 
            file.writeUTF(NameOfFile);      //20  -18 karaktera ime (UTF) 
            file.writeInt(fileSize);               //4
            DataSegment object=new DataSegment();
            if(fileSize!=0) 	{ 
				numberOfBlocks=fileSize/123+1;
				Math.ceil(numberOfBlocks);
            } else 
            	{	fileSize=0; numberOfBlocks=-1; }  
            if(fileSize>64)  startBlock=object.writeDataInDataSegment(numberOfBlocks, NameOfFile);
            file.writeInt(startBlock);         //4
            file.writeInt(numberOfBlocks);     //4
            file.writeUTF(DateCreated);     //24
            file.writeUTF(DateLastUsed);    //24
            file.writeUTF(DateLastModified);//24
            if (isMFTfile==1) rootheader.setSizeOfMFTFiles(rootheader.getSizeOfMFTFiles()+1); 
            else rootheader.setsizeOfMFTFileHeaders(rootheader.getsizeOfMFTFileHeaders()+1); 
            if (isMFTfile==0)
            	{
            	rootheader.setUsedSpaceDS(rootheader.getUsedSpaceDS()+fileSize); 
            	rootheader.setFreeSpaceDS(rootheader.getFreeSpaceDS()-fileSize);
            	}
            rootheader.setLastModified(Utilities.getCurrentDate());
             return maincounter;
         } catch (Exception e)
        {}
 return 0; 
    }
    public static int searchMFTfiles(RootHeader rootheader,RandomAccessFile file,String filename)
    {
        try
        {
         		for (; filename.length()<18;) filename=filename+" ";
        		file.seek(MainClass.ONEMB-106);
         	    byte  mftflag; 
         	    String temp; 
         	    rootheader.setLastUsed(Utilities.getCurrentDate());
        	    for(int i=rootheader.getsizeOfMFTFileHeaders()+rootheader.getSizeOfMFTFiles();i>0;i--)
        	    { 
        	    file.readByte();		//preskacemo allocate fleg
         	    mftflag=file.readByte(); 
         	    if (mftflag==1) 
         	    	{ 
         	    	 temp=file.readUTF();
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
    public static int searchMFTHeadersByName(RootHeader rootheader,RandomAccessFile file,String filename)
    {
        try
        {
         		for (; filename.length()<18;) filename=filename+" ";
        		file.seek(MainClass.ONEMB-106);
         	    byte  mftflag; 
         	    String temp; 
         	    rootheader.setLastUsed(Utilities.getCurrentDate());
        	    for(int i=rootheader.getsizeOfMFTFileHeaders()+rootheader.getSizeOfMFTFiles();i>0;i--)
        	    { 
        	    file.readByte();		//preskacemo allocate fleg
         	    mftflag=file.readByte(); 
          	    	 
         	    	 temp=file.readUTF();
            	    		if(filename.equals(temp)) return 1;
         	    	 
         	    if (mftflag==1) file.seek(file.getFilePointer()-194);
         	    else file.seek(file.getFilePointer()-108);
        	    } 
        	    return 0; 		// 0-fajl nije pronadjen
        } catch (Exception e)	// 1-fajl pronadjen
        {
        }
        return -1;			//greska 
    }
    public static String searchFileHeaderBYPosition(RootHeader rootheader,RandomAccessFile file,int position)
    { 
    	  try
          {
           		file.seek(MainClass.ONEMB-106);
           	    byte  mftflag; 
            	rootheader.setLastUsed(Utilities.getCurrentDate());
          	    for(int i=position;i>0;i--)
          	    { 
          	    file.readByte();		 
           	    mftflag=file.readByte(); 
           	    if (mftflag==1) file.seek(file.getFilePointer()-194);
           	    else file.seek(file.getFilePointer()-108);
          	    } 
          	    file.readByte();
          	    file.readByte(); 
          	    return file.readUTF(); 
          	   
          } catch (Exception e)	
          {}
    	  return null; 
    }
	public void printFileInfo() {
		System.out.println("Da li je fajl alociran na memoriji: " + ((isAllocated==1)?"jeste":"nije"));
		System.out.println("Da li je fajl MFT fajl: " + ((isMFTfile==1)?"jeste":"nije"));
		System.out.println("Ime fajla: " + NameOfFile);
		System.out.println("Veličina fajla: " + fileSize);
		System.out.println("Datum kreacije: " + DateCreated);
		System.out.println("Datum kada je posljednje korišten: " + DateLastUsed);
		System.out.println("Datum kada je posljednje modifikovan: " + DateLastModified);
	}

    public  static byte [] READFILE(RootHeader rootheader,RandomAccessFile file,String filename)
    {
        try
        {
         		for (; filename.length()<18;) filename=filename+" ";
        		file.seek(MainClass.ONEMB-106);
         	    byte  mftflag; 
         	    String temp; 
         	    rootheader.setLastModified(Utilities.getCurrentDate());
         	    byte [] array=new byte[64];
        	    for(int i=rootheader.getsizeOfMFTFileHeaders()+rootheader.getSizeOfMFTFiles();i>0;i--)
        	    { 
        	    file.readByte();	 
         	    mftflag=file.readByte(); 
          	    	 temp=file.readUTF();
             	    		if(filename.equals(temp) && mftflag==0)  //citanje obicnog (data) fajla 
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
    public int recoverFile(RootHeader rootheader,RandomAccessFile file,String filename)	//opciono
    {
        try
        {
         		for (; filename.length()<18;) filename=filename+" ";
        		file.seek(MainClass.ONEMB-106);
         	    byte  mftflag; 
         	    String temp; 
        	    for(int i=rootheader.getsizeOfMFTFileHeaders()+rootheader.getSizeOfMFTFiles();i>0;i--)
        	    { 
        	    file.readByte();		
         	    mftflag=file.readByte(); 
         	    temp=file.readUTF();
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
        return -1;			
    } 
    public  static void deleteMFTFile(RootHeader rootheader,RandomAccessFile file)
    {
        try
        {
          	    byte  mftflag; 
        	    file.writeByte(0);		 
         	    mftflag=file.readByte();  	    	 
    					file.readUTF(); 
					int size=file.readInt(); 
					int  startblock=file.readInt(); 
					int numberofblocks=file.readInt(); 	
					if (size>64) {
					DataSegment object=new DataSegment(); 
					object.deleteFileInDataSegment(startblock, numberofblocks); }
        } catch (Exception e)	 
        {}
          
    }
    public static  long  getFileHeaderPosition(RootHeader rootheader,RandomAccessFile file,String filename)
    {
        try
        {
         		for (; filename.length()<18;) filename=filename+" ";
        		file.seek(MainClass.ONEMB-106);
          	    String temp; 
          	    byte mftflag;
        	    for(int i=rootheader.getsizeOfMFTFileHeaders()+rootheader.getSizeOfMFTFiles();i>0;i--)
        	    { 
        	    file.readByte();		//preskacemo allocate fleg
         	    mftflag=file.readByte(); 
         	    	 temp=file.readUTF();
             	    		if(filename.equals(temp)) 
           	    					{
           	    						return file.getFilePointer()-22;
           	    					}
          	    if (mftflag==1) file.seek(file.getFilePointer()-192);
         	    else file.seek(file.getFilePointer()-128);
        	    } 
        } catch (Exception e)	 
        {
        }
        return -1;			//greska 
    }
    public static  void getDataFromFile(RandomAccessFile x, byte[] temp, long  position) throws IOException
    {
    	x.seek(position);
        x.read(temp);
     }

    public static void setNameOfFile(RandomAccessFile x, String name,long  position) throws IOException
    {
        x.seek(position+2);
        x.writeUTF(name);
     }

    public static  String getNameOfFile(RandomAccessFile x,long position) throws IOException
    {
        x.seek(position + 2);
         return   x.readUTF();
    }

    public static  void setsize(RandomAccessFile x, int SIZE,long position) throws IOException
    {
        x.seek(position+ 22);
        x.writeInt(SIZE);
     }

    public static int getSize(RandomAccessFile x,long position) throws IOException
    {
        x.seek(position + 22);
        return x.readInt();
    }

    public static void setFileFreeForWrite(RandomAccessFile x, byte l,long position) throws IOException
    {
        x.seek(position);
        x.writeByte(l);
     }

    public static void setisMFTFILE(RandomAccessFile x, byte l,long position) throws IOException
    {
        x.seek(position + 1);
        x.writeByte(l);
     }

    public static byte isFileFreeForWrite(RandomAccessFile x,long position) throws IOException
    {
        x.seek(position);
         return x.readByte();
    }

    public static  byte getisMFTFILE(RandomAccessFile x,long position) throws IOException
    {
        x.seek(position  +1);
          return x.readByte();
        }

    public static  String getDateLastModified(RandomAccessFile x,long position) throws IOException
    {

        x.seek(position+ 82);
          return  x.readUTF();
    }

    public static String getDateLastUsed(RandomAccessFile x,long position) throws IOException
    {

        x.seek(position + 58);
        return x.readUTF();
    }
    public static String getDateCreated(RandomAccessFile x,long position) throws IOException
    {
        x.seek(position + 34);
          return  x.readUTF();
    }
    public static void setDateLastModified(RandomAccessFile x, String date,long position) throws IOException
    {
        x.seek(position + 82);
        x.writeUTF(date);
     }
    public static void setDateLastUsed(RandomAccessFile x, String date,long position) throws IOException
    {
        x.seek(position + 58);
        x.writeUTF(date);
     }
    public static void setDateCreated(RandomAccessFile x, String date,long position) throws IOException
    {
        x.seek(position + 34);
        x.writeUTF(date);
     }
}
