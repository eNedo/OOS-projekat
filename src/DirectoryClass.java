import java.io.IOException;
import java.io.RandomAccessFile;

public class DirectoryClass
{
    public static final int DIRHEADERSIZE = 255;

    public byte isAllocatedFlag;        //1
    public String nameOfDirectory;        //17
    public byte depthFlag;                //18
    public String dateCreated;            //18+24=42
    public String dateUsed;            //42+24=68
    public String dateModified;        //68+24=92
    public int size;                    //92+4
    public short[] arrayOfDirsAndFiles; // vrijednost 32555 ako prazan element

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

    public DirectoryClass(byte isAllocatedFlag, String nameOfDirectory, byte depthFlag, String dateCreated, String dateUsed, String dateModified, int size, short[] arrayOfDirsAndFiles)
    {
        this.isAllocatedFlag = isAllocatedFlag;
        this.nameOfDirectory = nameOfDirectory;
        this.depthFlag = depthFlag;
        this.dateCreated = dateCreated;
        this.dateUsed = dateUsed;
        this.dateModified = dateModified;
        this.size = size;
        this.arrayOfDirsAndFiles = arrayOfDirsAndFiles;
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

    //vraca poziciju kraja directory headera
    public static long getEndOfDirectoryHeaderBlock()   // TODO: treba testirati
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
                //                System.out.println("pre getEndOfDirectoryHeaderBlock: "+randomAccessFile.getFilePointer());
                directoryClass = makeDirectoryObject(randomAccessFile);
                //                System.out.println("posle getEndOfDirectoryHeaderBlock: "+randomAccessFile.getFilePointer());
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

    public static short findDirectoryByName(RandomAccessFile randomAccessFile, String name) throws IOException
    {
        // long previousPointerPosition = randomAccessFile.getFilePointer();
        // TODO: nisam vise siguran u ovaj zivot. Nije GOTOVO!
        short[] tempArrayOfDirsAndFiles;
        if ("root".contains(name))  //apsolutna putanja
        {
            //  npr: root/dir1  pathArray[0] == root i pathArray[1]==dir1
            String[] pathArray = name.split("/");
            tempArrayOfDirsAndFiles = RootHeader.getArrayOfDirs();

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

    public static DirectoryClass makeDirectoryObject(RandomAccessFile randomAccessFile) throws IOException
    {
        return new DirectoryClass(getIsAllocatedFlag(randomAccessFile), getNameOfDirectory(randomAccessFile), getDepthFlag(randomAccessFile),
                getDateCreated(randomAccessFile), getDateUsed(randomAccessFile), getDateModified(randomAccessFile),
                getSize(randomAccessFile), getArrayOfDirsAndFiles(randomAccessFile));
    }

    public static byte getIsAllocatedFlag(RandomAccessFile randomAccessFile) throws IOException
    {
        //        this.isAllocatedFlag = randomAccessFile.readByte();
        byte flag = randomAccessFile.readByte();
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 1);
        //        return isAllocatedFlag;
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

    //prima pocetak trenutnog bloka direktorijuma ili rootHeadera i odredjuje gdje ce napraviti novi
    //TODO: trenutno stavlja samo na kraj
    public boolean createNewDirectoryOrFile(RandomAccessFile randomAccessFile, String nameOfDirectory) throws IOException
    {
        long currentPosition = randomAccessFile.getFilePointer();
        short currentBlockNum = calculateDirectoryBlockWithPointer(randomAccessFile.getFilePointer());
        //        System.out.println("current: "+currentPosition+"   blokcnum: "+blockNum);
        int indexOf35222 = 0;

        if (checkFreeSpaceInDirectoryArray(randomAccessFile))
        {
            short[] tempArray;
            if (currentBlockNum == 0)
                tempArray = RootHeader.getArrayOfDirs();
            else
                tempArray = DirectoryClass.getArrayOfDirsAndFiles(randomAccessFile);

            for (int i = 0; i < tempArray.length; i++)
            {
                if (tempArray[i] == (short) 32555)
                {
                    indexOf35222 = i;
                    break;
                }
            }

            tempArray[indexOf35222] = calculateDirectoryBlockWithPointer(getEndOfDirectoryHeaderBlock());
            if(currentBlockNum==0)
                RootHeader.setArrayOfDirsAndFiles(tempArray);
            else
                DirectoryClass.setArrayOfDirsAndFiles(randomAccessFile,tempArray);


            //TODO: treba sve pobdatke o novom direktorijumu u konstruktor
            DirectoryClass newDir = new DirectoryClass(nameOfDirectory, getDepthFlag(randomAccessFile));
            randomAccessFile.seek(getEndOfDirectoryHeaderBlock());

            System.out.println("WRITING on position: " + randomAccessFile.getFilePointer() +
                    " bloknum: " + calculateDirectoryBlockWithPointer(randomAccessFile.getFilePointer()));
            newDir.writeDirInFile(randomAccessFile);

            randomAccessFile.seek(currentPosition);
            return true;
        } else
            System.out.println("Not enough free space");

        return false;

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

    //Lipa pisao: ne znam sta radi
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

