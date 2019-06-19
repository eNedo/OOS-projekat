import java.io.IOException;
import java.io.RandomAccessFile;

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
    private short[] arrayOfDirsAndFiles; // vrijednost 32555 ako prazan element

    //    private static int numOfBlocksInMFT;    // globalna koja pamti koliko ima blokova u mft-u da se mozemo pozicionirati na kraj

    public DirectoryClass(String name, byte Depth)
    {
        this.isAllocatedFlag = 1;
        dateModified = dateUsed = dateCreated = Utilities.getCurrentDate();
        nameOfDirectory = String.format("%-14s", name);
        depthFlag = Depth;
        arrayOfDirsAndFiles = new short[82];
        for (int i = 0; i < arrayOfDirsAndFiles.length; i++)
            arrayOfDirsAndFiles[i] = (short) 32555;

        this.size = 0;

    }

    public static String getNameOfDirectory(RandomAccessFile randomAccessFile) throws IOException
    {
        randomAccessFile.seek(randomAccessFile.getFilePointer() + 1);
        //        nameOfDirectory = randomAccessFile.readUTF();
        String readName = randomAccessFile.readUTF();
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 17); // 16+1
        //        return nameOfDirectory.trim();
        return readName.trim();
    }

    public static long getPointerPositionForGivenBlock(short blockNum)
    {
        if (blockNum == 0)
            return 0;

        return RootHeader.ROOTHEADERSIZE + (blockNum - 1) * DirectoryClass.DIRHEADERSIZE;
    }

    public static short findDirectoryByName(RandomAccessFile randomAccessFile, String name) throws IOException
    {
        // long previousPointerPosition = randomAccessFile.getFilePointer();
        // TODO: nisam vise siguran u ovaj zivot. Nije GOTOVO!
        short[] tempArrayOfDirsAndFiles;
        if ("root".contains(name))  //apsolutna putanja
        {
            //  npr: root/dir1  pathArray[0] == root i pathArray[1]==dir1
            String[] pathArray= name.split("/");
            tempArrayOfDirsAndFiles = RootHeader.getArrayOfDirsAndFiles();

            String readName;
            for (int i = 0; i < 103; i++)
            {
                if (tempArrayOfDirsAndFiles[i] == 32555)
                    continue;

                randomAccessFile.seek(getPointerPositionForGivenBlock(tempArrayOfDirsAndFiles[i]));
                readName = DirectoryClass.getNameOfDirectory(randomAccessFile);

                if (pathArray[1].equals(readName))
                    return tempArrayOfDirsAndFiles[i];
            }
        } else
        {

        }
        return 0;
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
        for (int i = 0; i < arrayOfDirsAndFiles.length; i++)
            randomAccessFile.writeShort(arrayOfDirsAndFiles[i]);

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
        for (int i = 0; i < arrayOfDirsAndFiles.length; i++)
        {
            short tmp;
            tmp = randomAccessFile.readShort();
            if (tmp == (short) 32555) ;
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
