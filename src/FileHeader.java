import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileHeader
{
    private byte isAllocated;       //2
    private byte isMFTfile; 
    private String NameOfFile;      //20
    private int size;               //4
    private int startBlock;         //4
    private int numberOfBlocks;     //4
    private String DateCreated;     //24
    private String DateLastUsed;    //24
    private String DateLastModified;//24
    private byte[] DataBlock;       //64
    public FileHeader(String name, int filesize, int numberofblocks,int startingblock,byte isMFTfile,byte []datablocks)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'u' HH:mm:ss");
        NameOfFileSystem = name;
        Date date = new Date(System.currentTimeMillis());
        DateCreated = formatter.format(date);
        NumberOfDirectoriums = 1;
        numberofblocks=numberOfBlocks; 
        filesize=size;
        startingblock=startBlock;
        this.isMFTfile=isMFTfile;
        if(isMFTfile==1) DataBlock=datablocks;
    }
    public void writeToFile(RandomAccessFile x, int position)
    {
        try
        { 
            x.seek(position);
            x.writeByte(isAllocated);       //2
            x.writeByte(isMFTfile); 
            x.writeUTF(NameOfFile);      //20  -18 karaktera ime
            x.writeInt(size);               //4
            x.writeInt(startBlock);         //4
            x.writeInt(numberOfBlocks);     //4
            x.writeUTF(DateCreated);     //24
            x.writeUTF(DateLastUsed);    //24
            x.writeUTF(DateLastModified);//24
             if (isMFTfile==1)  x.write(DataBlock);
        } catch (Exception e)
        {
        }
    }
    public void getDataFromFile(RandomAccessFile x, byte[] temp)
    { 
    x.seek(x.getFilePointer()+106);
    x.read(temp); 
    x.seek(x.getFilePointer()-170);
    } 
    
  public void setNameOfFile(RandomAccessFile x, String name) throws IOException
  {
      x.seek(x.getFilePointer()+2); 
        x.writeUTF(name); 
       x.seek(x.getFilePointer()-22); 
  }
 public String getNameOfFile(RandomAccessFile x) throws IOException
 {
     x.seek(x.getFilePointer()+2); 
     String temp=x.readUTF(); 
     x.seek(x.getFilePointer()-22); 
     return temp; 
 }
    public void setsize(RandomAccessFile x, int SIZE) throws IOException
    {
        x.seek(x.getFilePointer() + 22); 
        x.writeInt(SIZE); 
        x.seek(x.getFilePointer()-26); 
    }
    public int getSize(RandomAccessFile x) throws IOException
    { 
        x.seek(x.getFilePointer() + 22);
        int temp=x.readInt(); 
        x.seek(x.getFilePointer()-26); 
        return temp; 
    }
public void setFileFreeForWrite(RandomAccessFile x, byte l) throws IOException
  {
        x.writeByte(l); 
        x.seek(x.getFilePointer()-1); 
  }
  public void setisMFTFILE(RandomAccessFile x,byte l) throws IOException
  {
      x.seek(x.getFilePointer()+1); 
       x.writeByte(l);
      x.seek(x.getFilePointer()-2); 
  }
  
  public byte isFileFreeForWrite(RandomAccessFile x) throws IOException
  {
        return x.read(); 
  }
  public byte getisMFTFILE(RandomAccessFile x) throws IOException
  {
      x.seek(x.getFilePointer()+1); 
      byte temp=x.read();
      x.seek(x.getFilePointer()-2); 
      return temp; 
  }
  public String getDateLastModified(RandomAccessFile x) throws IOException 
  {
      
      x.seek(x.getFilePointer()+82); 
      String temp=x.readUTF();
      x.seek(x.getFilePointer()-106); 
      return temp;
  }
  public String getDateLastUsed(RandomAccessFile x) throws IOException
  {
 
      x.seek(x.getFilePointer()+58); 
      String temp=x.readUTF();
      x.seek(x.getFilePointer()-82); 
      return temp;
   }
  public String getDateCreated(RandomAccessFile x) throws IOException
  {
      
      x.seek(x.getFilePointer()+34); 
      String temp=x.readUTF();
      x.seek(x.getFilePointer()-58); 
      return temp;
  }
  
  public void setDateLastModified(RandomAccessFile x,String date) throws IOException 
  {
      
      x.seek(x.getFilePointer()+82); 
      x.writeUTF(date); 
      x.seek(x.getFilePointer()-106); 
  }
  public void  setDateLastUsed(RandomAccessFile x,String date) throws IOException
  {
 
      x.seek(x.getFilePointer()+58); 
      x.writeUTF(date);
      x.seek(x.getFilePointer()-82); 
   }
  public void setDateCreated(RandomAccessFile x, String date) throws IOException
  {
      
      x.seek(x.getFilePointer()+34); 
      x.writeUTF(date);
      x.seek(x.getFilePointer()-58); 
  }
  
