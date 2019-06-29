import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class DirectoryClass
{
    public static final int DIRHEADERSIZE = 255;

    public byte isAllocatedFlag;        //1
    public String nameOfDirectory;        //11
    public byte depthFlag;                //18
    public String dateCreated;            //18+24=42
    public String dateUsed;            //42+24=68
    public String dateModified;        //68+24=92
    public int size;                    //92+4
    public short[] arrayOfDirsAndFiles; // vrijednost 32555 ako je prazan element


    public DirectoryClass(String name, byte Depth)
    {
        this.isAllocatedFlag = 1;
        dateModified = dateUsed = dateCreated = Utilities.getCurrentDate();
        for(;name.length()<11;) name=name+ " " ;
        nameOfDirectory = name;
        depthFlag = Depth;
        arrayOfDirsAndFiles = new short[82];
        for (int i = 0; i < arrayOfDirsAndFiles.length; i++)
            arrayOfDirsAndFiles[i] = (short)0;
        this.size = 0;

    }
    public DirectoryClass () { } 
    public DirectoryClass(byte isAllocatedFlag, String nameOfDirectory, byte depthFlag, String dateCreated, String dateUsed, String dateModified, int size, short[] arrayOfDirsAndFiles)
    {
        this.isAllocatedFlag = isAllocatedFlag;		//1- upisan direktorijum   0-obrisani direktorijum
        for(;nameOfDirectory.length()<11;) nameOfDirectory=nameOfDirectory+" "; 
        this.nameOfDirectory = nameOfDirectory;
        this.depthFlag = depthFlag;
        this.dateCreated = dateCreated;
        this.dateUsed = dateUsed;
        this.dateModified = dateModified;
        this.size = size;
        this.arrayOfDirsAndFiles = arrayOfDirsAndFiles;
    }
public static int searchDirectoryByPosition(String dirname,RandomAccessFile file, int position) throws Exception 
{ 			 
	file.seek(347);
	String temp;
 	for (;dirname.length()<11;) dirname=dirname+" "; 
	for (int i=0;position>i;i++) {
	if(file.readByte()==1) { 
	 temp=file.readUTF(); 
	if(temp.equals(dirname)) return 1;
	}
	file.seek(file.getFilePointer()-18); 
	file.seek(file.getFilePointer()+255); 
	}
return 0;
} 
public static int searchDirectoryByName(RootHeader rootheader,RandomAccessFile file, String dirname) throws Exception 
{ 			 
	file.seek(347);
	String temp;
 	for (;dirname.length()<11;) dirname=dirname+" "; 
	for (int i=0;103>i;i++) {
	if(file.readByte()==1) { 
	 temp=file.readUTF(); 
	if(temp.equals(dirname)) return i;
	}
	file.seek(file.getFilePointer()-14); 
	file.seek(file.getFilePointer()+255); 
	}
return -1;
} 
public   void  DirectoryInfo(RootHeader rootheader,RandomAccessFile file) throws IOException
{
			if (file.readByte()==1) { 		
 	        System.out.println("Ime: "+file.readUTF()); 
 	        file.readByte(); 
	        System.out.println("Datum kreiranja: " + file.readUTF());
	        System.out.println("Datum koristenja: "+file.readUTF());
	        System.out.println("Datum modifikacije: "+file.readUTF());
	        System.out.println("Velicina: "+file.readInt()); 
	        System.out.println("Zauzetosti blokova!   * 0-slobodan blok  ");
	        for (int i=0;82>i;i++) 
	        		System.out.print(file.readShort());
	        System.out.println(); 
			}
 }
public   boolean createNewDirectory(RootHeader rootheader,RandomAccessFile randomAccessFile) throws IOException
{
randomAccessFile.seek(347); 
for (int i=0;rootheader.getNumberOfDirectoriums()>i;i++)
{ 
if (randomAccessFile.readByte()==0) {//prepis direktorijuma preko obrisanog 
	randomAccessFile.seek(randomAccessFile.getFilePointer()-1);
	        randomAccessFile.writeByte(this.isAllocatedFlag);
	        randomAccessFile.writeUTF(this.nameOfDirectory);
	        randomAccessFile.writeByte(this.depthFlag);
	        randomAccessFile.writeUTF(this.dateCreated);
	        randomAccessFile.writeUTF(this.dateUsed);
	        randomAccessFile.writeUTF(this.dateModified);
	        randomAccessFile.writeInt(this.size);
	        for (int j = 0; j < arrayOfDirsAndFiles.length; j++)
	        randomAccessFile.writeShort(arrayOfDirsAndFiles[j]);
	        rootheader.setNumberOfDirectoriums(rootheader.getNumberOfDirectoriums()+1); 
}
randomAccessFile.seek(randomAccessFile.getFilePointer()-1); 
randomAccessFile.seek(randomAccessFile.getFilePointer()+DirectoryClass.DIRHEADERSIZE);

}
randomAccessFile.seek(RootHeader.ROOTHEADERSIZE); 
	    	for(int i=0;rootheader.getNumberOfDirectoriums()>i;i++)
	    		randomAccessFile.seek(randomAccessFile.getFilePointer()+255);	
	    	   randomAccessFile.writeByte(this.isAllocatedFlag);// ako nije pronadjen obrisani direktorijum
		        randomAccessFile.writeUTF(this.nameOfDirectory);
		        randomAccessFile.writeByte(this.depthFlag);
		        randomAccessFile.writeUTF(this.dateCreated);
		        randomAccessFile.writeUTF(this.dateUsed);
		        randomAccessFile.writeUTF(this.dateModified);
		        randomAccessFile.writeInt(this.size);
		        for (int i = 0; i < arrayOfDirsAndFiles.length; i++)
		        randomAccessFile.writeShort(arrayOfDirsAndFiles[i]);
		        rootheader.setNumberOfDirectoriums(rootheader.getNumberOfDirectoriums()+1); 
    return true;
}
public boolean create(RootHeader rootheader, RandomAccessFile file, String filename, String path) throws Exception
{ 
 	int position;
 	byte []tempx=new byte[64]; 
 	Arrays.fill(tempx, (byte)0); 
 	FileHeader object=new FileHeader(filename,0,0,0,(byte)0,tempx);
	int fileposition=object.writeFiletoMFTheader(rootheader, file, filename); 
 	  if (path.equals("root")) 		// ako upisujemo direktorijum u root 
	 {		
 		  short tmp[]=rootheader.getArrayOfDirs(); 
 		  for(int i=0;103>i;i++) 
 		  	{ 
 			  if(tmp[i]!=0) { tmp[i]=(short)fileposition; break; } 
 		  	}
	 	 return true; 
	 }
	 	 	//inace, trebamo doci do pune putanje
	 		 
	 		String temp[]=path.split("/"); 
	 			if (temp.length==1) { file.seek(0);
	 				if (searchDirectoryByName(rootheader, file, temp[0])!=-1)  // root/dir
	 				{ 
	 				 position=searchDirectoryByName(rootheader, file, temp[0]);
	 				file.seek(RootHeader.ROOTHEADERSIZE+position*255); 
	 				short []array=getArrayOfDirsAndFiles(file);
	   				for(int i=0;82>i;i++) 
	   					if(array[i]== 0) {array[i]=(short)fileposition; break;} 
	   				file.seek(RootHeader.ROOTHEADERSIZE+position*255); 
	   				setArrayOfDirsAndFiles(file,array); 
	   				return true;
	 				}
	  			}
	 			else if (temp.length==2) if (temp[1].length()>0) { 
	 				if(searchDirectoryByName(rootheader,file,temp[1])!=0) // root/dir1/dir2
	 				{
	 				position=searchDirectoryByName(rootheader,file,temp[1]);
	 				file.seek(RootHeader.ROOTHEADERSIZE+position*255); 
	 				short []array=getArrayOfDirsAndFiles(file);
	   				for(int i=0;82>i;i++) 
	   					if(array[i]==0) {array[i]=(short)fileposition; break;} 
	   				setArrayOfDirsAndFiles(file,array);
	   				return true;
	 				}
	 		}
	 		 
 	return false;
}
public boolean put(RootHeader rootheader, RandomAccessFile file, String filename, String path) throws Exception
{ 
File newfile=new File(filename);
if (newfile.exists()){
 	int position;
	RandomAccessFile filepointer=new RandomAccessFile(filename,"rw"); 
	byte []tempx=new byte[(int)filepointer.length()]; 
	filepointer.readFully(tempx); 
	FileHeader object;
	if (filepointer.length()<64)   object=new FileHeader(filename,(int)filepointer.length(),(int)filepointer.length()/123 +1,0,(byte)1,tempx);
	else   object=new FileHeader(filename,(int)filepointer.length(),(int)filepointer.length()/123 +1,0,(byte)0,tempx);
	int fileposition=object.writeFiletoMFTheader(rootheader, file, filename); 
 	  if (path.equals("root")) 		// ako upisujemo direktorijum u root 
	 {		
 		  short tmp[]=rootheader.getArrayOfDirs(); 
 		  for(int i=0;103>i;i++) 
 		  	{ 
 			  if(tmp[i]!=0) { tmp[i]=(short)fileposition; break; } 
 		  	}
	 	 return true; 
	 }
	 	 	//inace, trebamo doci do pune putanje
	 		 
	 		String temp[]=path.split("/"); 
	 			if (temp.length==1) { file.seek(0);
	 				if (searchDirectoryByName(rootheader, file, temp[0])!=-1)  // root/dir
	 				{ 
	 				 position=searchDirectoryByName(rootheader, file, temp[0]);
	 				file.seek(RootHeader.ROOTHEADERSIZE+position*255); 
	 				short []array=getArrayOfDirsAndFiles(file);
	   				for(int i=0;82>i;i++) 
	   					if(array[i]== 0) {array[i]=(short)-(fileposition+1); break;} 
	   				file.seek(RootHeader.ROOTHEADERSIZE+position*255); 
	   				setArrayOfDirsAndFiles(file,array); 
	   				return true;
	 				}
	  			}
	 			else if (temp.length==2) if (temp[1].length()>0) { 
	 				if(searchDirectoryByName(rootheader,file,temp[1])!=0) // root/dir1/dir2
	 				{
	 				position=searchDirectoryByName(rootheader,file,temp[1]);
	 				file.seek(RootHeader.ROOTHEADERSIZE+position*255); 
	 				short []array=getArrayOfDirsAndFiles(file);
	   				for(int i=0;82>i;i++) 
	   					if(array[i]==0) {array[i]=(short)-(fileposition+1); break;} 
	   				setArrayOfDirsAndFiles(file,array);
	   				return true;
	 				}
	 		}
} else 
	System.out.println("Greska! Nepostojeci fajl!");  		 
 	return false;
}	
public static void get(RootHeader rootheader, RandomAccessFile file, String filename, String downloadfilename) throws Exception
{ 
  	int position;
	File newfile=new File(downloadfilename);
	if(newfile.createNewFile()) {
		RandomAccessFile object=new RandomAccessFile(newfile,"rw");
	byte[]temp=FileHeader.READFILE(rootheader, file, filename); 
	for (int i=0;temp.length>i;i++)
		object.writeByte(temp[i]); 
	System.out.println("Uspjesno ste snimili: " +downloadfilename+" fajl!");
	object.close();
 	}
}	
public static void move(RootHeader rootheader,RandomAccessFile file,String movingdir,String destdir)throws Exception
{
	if (searchDirectoryByName(rootheader,file,movingdir)==-1) System.out.println("Navedeni direktorijum ne postoji!"); 
	else 
	{ 	
		 short []tmp=rootheader.getArrayOfDirs();
		 int movingdirposition=searchDirectoryByName(rootheader,file,movingdir); 
		 boolean existinroot=false;
		 for(int i=0;tmp.length>i;i++) if (tmp[i]==movingdirposition) existinroot=true;
		 if (existinroot) {
 		  for (int j=0;tmp.length>j;j++) {if (tmp[j]==movingdirposition) {tmp[j]=32555;  break;}}  
		  rootheader.setArrayOfDirsAndFiles(tmp);			//izbacujemo direktorijum iz root-a
 		  int destdirposition=searchDirectoryByName(rootheader,file,destdir); 
 		  file.seek(RootHeader.ROOTHEADERSIZE+destdirposition*DirectoryClass.DIRHEADERSIZE); 
 		  short []array= getArrayOfDirsAndFiles(file);		//dohvatamo niz direktorijuma koji pripadaju odredisnom
 		  for(int k=0;array.length>k;k++) {if (array[k]==0)  {array[k]=(short)movingdirposition; break;}    }
 		  file.seek(RootHeader.ROOTHEADERSIZE+destdirposition*DirectoryClass.DIRHEADERSIZE); 
 		  setArrayOfDirsAndFiles(file,array);
 		 }
		 else System.out.println("Navedeni direktorijum se ne nalazi u root-u!");
  	}								// ako direktorijum postoji, ali se ne nalazi u root-u
}
public boolean mkdir(RootHeader rootheader,RandomAccessFile file, String path,short  positionfornewdir) throws Exception
{ 
	int position;
  if (path.equals("root")) 		// ako upisujemo direktorijum u root
 {		
	  short []tmp=rootheader.getArrayOfDirs(); 
	  for (int i=0;tmp.length>i;i++) {if (tmp[i]==32555) {tmp[i]=positionfornewdir; break;} } 
	  rootheader.setArrayOfDirsAndFiles(tmp);
 	 return true; 
 }
 	if(path.contains("/"))		//inace, trebamo doci do pune putanje
 		{
 		String temp[]=path.split("/");
  		if(temp[0].equals("root")) {
 			if (temp.length==1) {
 				if (searchDirectoryByName(rootheader,file,temp[1])!=-1)  // root/dir
 				{ 
 				position=searchDirectoryByName(rootheader,file,temp[1]);
 				file.seek(RootHeader.ROOTHEADERSIZE+position*255); 
 				short []array=getArrayOfDirsAndFiles(file);
   				for(int i=0;82>i;i++) 
   					if(array[i]== 0) {array[i]=(short)positionfornewdir; break;} 
   				file.seek(RootHeader.ROOTHEADERSIZE+position*255); 
   				setArrayOfDirsAndFiles(file,array); 
   				return true;
 				}
  			}
 			else if (temp.length==2)  { 
 				if(searchDirectoryByName(rootheader,file,temp[1])!=-1) // root/dir1/dir2
 				{
 				position=searchDirectoryByName(rootheader,file,temp[1]);
 				file.seek(RootHeader.ROOTHEADERSIZE+position*255); 
 				short []array=getArrayOfDirsAndFiles(file);
   				for(int i=0;82>i;i++) 
   					if(array[i]==0) {array[i]=(short)positionfornewdir; break;} 
   				setArrayOfDirsAndFiles(file,array);
   				return true;
 				}
 		}
  		}
 		}
return false;
}
public   boolean rename(RootHeader rootheader,RandomAccessFile file,String namefiledir,String newname) throws Exception
{ 
 	if (searchDirectoryByName(rootheader,file,namefiledir)>=0) 
 	{
 		file.seek(RootHeader.ROOTHEADERSIZE+searchDirectoryByName(rootheader,file,namefiledir)*255); 
 		setNameOfDirectory(file,newname); 
 		return true; 
 	} 
	else 
		{
			if(FileHeader.searchMFTfiles(rootheader, file, namefiledir)>0) {
	FileHeader.setNameOfFile(file, newname, FileHeader.getFileHeaderPosition(rootheader, file, namefiledir));
			return true; 
			}
		}
	return false;
} 
public static boolean cat(RootHeader rootheader, RandomAccessFile file,String filename) throws Exception
{ 
  	if(FileHeader.READFILE(rootheader, file, filename)!=null)  
 	{
 		 byte []temp=FileHeader.READFILE(rootheader, file, filename); 
 		 for (int i=0;temp.length>i;i++) System.out.print((char)temp[i]); 
 		 System.out.println();
 		 return true; 
 	}	
	return false;
}
public static void  echo(RootHeader rootheader, RandomAccessFile file,String filename, String newstring) throws Exception
{ 
  	if(FileHeader.searchMFTHeadersByName(rootheader, file, filename)==1)  
 	{
  		file.seek(FileHeader.getFileHeaderPosition(rootheader, file, filename)); 
  	if(file.readByte()==1) { 		// ne mozemo upisivati u obrisan fjal
  		byte mftflag=file.readByte(); 
  		if (mftflag==1) { 
  		file.seek(file.getFilePointer()-66); 			// ako je mft fajl
  		for(;newstring.length()<64;) newstring+=" ";
  		byte []temp=newstring.getBytes(); 
  		for (int i=0;newstring.length()>i;i++)  file.writeByte(temp[i]);
  		System.out.println("Uspjesan upis!"); 
  		}
  		else {
  		String name=file.readUTF(); 
  		int filesize=file.readInt(); 
  		int startblock= file.readInt(); 
  		int numberofblocks=file.readInt(); 				// ako se fajl nalazi u data segmentu
  		byte []temp=new byte[newstring.length()];
  		temp=newstring.getBytes(); 
  		DataSegment.rewriteBlocksECHO(startblock, numberofblocks, temp);
  		}
 	}	
 	}
  	else System.out.println("Nije pronadjen fajl!"); 
 }
    
public static void listDirectoriums(RootHeader rootheader,RandomAccessFile file,String name )throws Exception
{ 
	if (name.equals("root")) { 
		short array[]=rootheader.getArrayOfDirs(); 
		for (int i=0;103>i;i++)  
			if (array[i]!=32555 && array[i]>=0)
			{ 
				file.seek(RootHeader.ROOTHEADERSIZE+array[i]*DirectoryClass.DIRHEADERSIZE); 
				System.out.print(getNameOfDirectory(file)+ " "); 
				System.out.println();
			}
			else   if (array[i]!=32555 && array[i]<=0) 
				System.out.print(FileHeader.searchFileHeaderBYPosition(rootheader, file, -(array[i]+1)));
	 
	} else { 
int position=searchDirectoryByName(rootheader,file,name); 
file.seek(RootHeader.ROOTHEADERSIZE+position*DirectoryClass.DIRHEADERSIZE); 
short tmp[]=getArrayOfDirsAndFiles(file);
for (int i=0;82>i;i++) 
{ 
	if(tmp[i]!=0 && tmp[i]>=0)		//listanje direktorijuma
		{
		file.seek(RootHeader.ROOTHEADERSIZE+tmp[i]*DirectoryClass.DIRHEADERSIZE); 
		System.out.print(getNameOfDirectory(file)+ " "); 
		System.out.println();
		}
	else if (tmp[i]!=0 && tmp[i]<=0) //listanje fajlova
		System.out.print(FileHeader.searchFileHeaderBYPosition(rootheader, file, -(tmp[i]+1)));
	 
	}	
	}
}
public static void deleteDirectory(RootHeader rootheader,RandomAccessFile file, String name) throws Exception 
{ 
	 short []tmp=rootheader.getArrayOfDirs();
	 int movingdirposition=searchDirectoryByName(rootheader,file,name); 
	 boolean exist =false;
	 for(int i=0;tmp.length>i;i++) if (tmp[i]==movingdirposition) { exist =true; tmp[i]=32555; } 
	 if (exist) {
		 		rootheader.setArrayOfDirsAndFiles(tmp);
		 		rootheader.setNumberOfDirectoriums(rootheader.getNumberOfDirectoriums()-1);
		 		rootheader.setLastModified(Utilities.getCurrentDate()); 
		 		file.seek(RootHeader.ROOTHEADERSIZE+movingdirposition*DirectoryClass.DIRHEADERSIZE); 
		 		setIsAllocatedFlag(file,(byte)0); 
	 }
	 else {  	 
			 int position=searchDirectoryByName(rootheader,file,name); 
			 file.seek(RootHeader.ROOTHEADERSIZE+position*DirectoryClass.DIRHEADERSIZE); 
			 setIsAllocatedFlag(file,(byte)0); 
			 rootheader.setNumberOfDirectoriums(rootheader.getNumberOfDirectoriums()-1);
		 		rootheader.setLastModified(Utilities.getCurrentDate()); 		 
	 }
		  if(FileHeader.searchMFTHeadersByName(rootheader, file, name)==1) { 
		 	file.seek(FileHeader.getFileHeaderPosition(rootheader, file, name));
		 	FileHeader.deleteMFTFile(rootheader, file);
		 	rootheader.setNumberOfFiles(rootheader.getNumberOfFiles()-1);
	 		rootheader.setLastModified(Utilities.getCurrentDate()); 
		  }
}
    public static String getNameOfDirectory(RandomAccessFile randomAccessFile) throws IOException
    {
        randomAccessFile.seek(randomAccessFile.getFilePointer() + 1);
         String readName = randomAccessFile.readUTF();
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 17); 
         return readName.trim();
    }

    public static long calculatePointerWithBlockNum(short blockNum)
    {
        if (blockNum == 0)
            return 0;

        return RootHeader.ROOTHEADERSIZE + (blockNum - 1) * DirectoryClass.DIRHEADERSIZE;
    }

    //vraca poziciju kraja directory headera
    public static long getEndOfDirectoryHeaderBlock()   //  testirati
    {
        long currentPointerPosition = RootHeader.ROOTHEADERSIZE;
        long tempPosition = currentPointerPosition;
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {
            randomAccessFile.seek(currentPointerPosition);
            DirectoryClass directoryClass = null;
            do
            {
                tempPosition = currentPointerPosition;
                 directoryClass = makeDirectoryObject(randomAccessFile);
                 randomAccessFile.seek(randomAccessFile.getFilePointer() + DirectoryClass.DIRHEADERSIZE);
                currentPointerPosition = randomAccessFile.getFilePointer();
            } while (directoryClass != null && !"".equals(directoryClass.nameOfDirectory));

        } catch (Exception ex)
        {
            return tempPosition;
        }

        return currentPointerPosition;
    }

    // za dobijenu poziciju u MTFblocku izracunaj redni broj bloka
    public static short calculateDirectoryBlockWithPointer(long currentPosition)
    {

        long position = currentPosition - RootHeader.ROOTHEADERSIZE;
        if (position < 0)
            return 0;
        return (short) (position / (long) DirectoryClass.DIRHEADERSIZE + 1);
    }


     

    public static DirectoryClass makeDirectoryObject(RandomAccessFile randomAccessFile) throws IOException
    {
        return new DirectoryClass(getIsAllocatedFlag(randomAccessFile), getNameOfDirectory(randomAccessFile), getDepthFlag(randomAccessFile),
                getDateCreated(randomAccessFile), getDateUsed(randomAccessFile), getDateModified(randomAccessFile),
                getSize(randomAccessFile), getArrayOfDirsAndFiles(randomAccessFile));
    }
 

    public static byte getIsAllocatedFlag(RandomAccessFile randomAccessFile) throws IOException
    {
         byte flag = randomAccessFile.readByte();
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 1);
         return flag;
    }

    public static byte getDepthFlag(RandomAccessFile randomAccessFile) throws IOException
    {
        randomAccessFile.seek(randomAccessFile.getFilePointer() + 17);
        byte depthFlag = randomAccessFile.readByte();
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 18);
        return depthFlag;
    }

    public static String getDateCreated(RandomAccessFile randomAccessFile) throws IOException
    {
        randomAccessFile.seek(randomAccessFile.getFilePointer() + 18);
        String dateCreated = randomAccessFile.readUTF();
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 41);    // 18+23
        return dateCreated;

    }

    public static String getDateUsed(RandomAccessFile randomAccessFile) throws IOException
    {
        randomAccessFile.seek(randomAccessFile.getFilePointer() + 41);    //18+24
        String dateUsed = randomAccessFile.readUTF();
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 64);
        return dateUsed;
    }

    public static String getDateModified(RandomAccessFile randomAccessFile) throws IOException
    {
        randomAccessFile.seek(randomAccessFile.getFilePointer() + 64);    //
        String dateModified = randomAccessFile.readUTF();
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 87);
        return dateModified;
    }

    public static int getSize(RandomAccessFile randomAccessFile) throws IOException
    {
        randomAccessFile.seek(randomAccessFile.getFilePointer() + 87);
        int size = randomAccessFile.readInt();
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 91);
        return size;
    }

    public static void setArrayOfDirsAndFiles(RandomAccessFile randomAccessFile, short[] arrayOfDirsAndFiles) throws IOException
    {
        randomAccessFile.seek(randomAccessFile.getFilePointer() + 91);
        for (int i = 0; i < arrayOfDirsAndFiles.length; i++)
            randomAccessFile.writeShort(arrayOfDirsAndFiles[i]);

        randomAccessFile.seek(randomAccessFile.getFilePointer() - DIRHEADERSIZE);

    }

    public static short[] getArrayOfDirsAndFiles(RandomAccessFile randomAccessFile) throws IOException
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

    public static void setIsAllocatedFlag(RandomAccessFile randomAccessFile, byte isAllocatedFlag) throws IOException
    {
        //        this.isAllocatedFlag = isAllocatedFlag;
        randomAccessFile.writeByte(isAllocatedFlag);
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 1);
    }

    public boolean checkFreeSpaceInDirectoryArray(RandomAccessFile randomAccessFile) throws IOException
    {

        short[] temp = DirectoryClass.getArrayOfDirsAndFiles(randomAccessFile);
        short emptyValue = 32555;
        for (int i = 0; i < temp.length; i++)
        {
            if (temp[i] == emptyValue)
                return true;
        }
        return false;

    }

    

  

    public  void setNameOfDirectory(RandomAccessFile randomAccessFile, String nameOfDirectory) throws IOException
    {
    	for(;nameOfDirectory.length()<11;) nameOfDirectory+=" ";
        randomAccessFile.seek(randomAccessFile.getFilePointer() + 1);
        randomAccessFile.writeUTF(nameOfDirectory);
    }

    public void setDepthFlag(RandomAccessFile randomAccessFile, byte depthFlag) throws IOException
    {
        this.depthFlag = depthFlag;
        randomAccessFile.seek(randomAccessFile.getFilePointer() + 17);
        randomAccessFile.writeByte(depthFlag);
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 18);

    }

    public void setDateCreated(RandomAccessFile randomAccessFile, String dateCreated) throws IOException
    {
        this.dateCreated = dateCreated;
        randomAccessFile.seek(randomAccessFile.getFilePointer() + 18);
        randomAccessFile.writeUTF(dateCreated);
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 41);
    }

    public void setDateUsed(RandomAccessFile randomAccessFile, String dateUsed) throws IOException
    {
        this.dateUsed = dateUsed;
        randomAccessFile.seek(randomAccessFile.getFilePointer() + 41);    //18+24
        randomAccessFile.writeUTF(dateUsed);
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 64);    //42+24
    }

    public void setDateModified(RandomAccessFile randomAccessFile, String dateModified) throws IOException
    {
        this.dateModified = dateModified;
        randomAccessFile.seek(randomAccessFile.getFilePointer() + 64);
        randomAccessFile.writeUTF(dateModified);
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 87);
    }

    public void setSize(RandomAccessFile randomAccessFile, int size) throws IOException
    {
        this.size = size;
        randomAccessFile.seek(randomAccessFile.getFilePointer() + 87);
        randomAccessFile.writeInt(size);
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 91);
    }

   
    public long getLastDirPosition()
    {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "r"))
        {
            //root numOfDir * sizeDirs

        } catch (Exception ex)
        {

        }
        return 0;
    }

}
