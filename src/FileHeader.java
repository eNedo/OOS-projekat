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

        //TODO: NE KONTAM OVO ISPOD
        this.isMFTfile = isMFTfile;
        if (isMFTfile == 1) DataBlock = datablocks;
    }

    public void writeToFile(RandomAccessFile x, int position)
    {
        try
        {
            x.seek(position);
            if (isMFTfile == 1) x.write(DataBlock); //64
            x.writeByte(isAllocated);       //2
            x.writeByte(isMFTfile);
            x.writeUTF(NameOfFile);      //20  -18 karaktera ime
            x.writeInt(fileSize);               //4
            x.writeInt(startBlock);         //4
            x.writeInt(numberOfBlocks);     //4
            x.writeUTF(DateCreated);     //24
            x.writeUTF(DateLastUsed);    //24
            x.writeUTF(DateLastModified);//24
        } catch (Exception e)
        {
        }
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
