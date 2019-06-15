import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

public class FileHeaderRoot
{
    //16+24*3+4*12+81*2 = 350
    private final String nameOfFileSystem;    //16    == 14 + \0
    private int sizeOfMFTFiles;
    private int sizeOfMFTDirs;
    private long freeSpaceDS;           //8
    private long usedSpaceDS;           //8
    private String dateCreated;         //22+2
    private String lastModified;        //24
    private String lastUsed;            //24
    private int numberOfDirectoriums;   //4
    private int numberOfFiles;          //4
    private static final int maxNumOfFilesInSystem=3495;
    private static final int maxNumOfDirs=7653;
    private static final int MaximumSizeOfFile = 65536;
    private static final int MaximumNumberOfDirs = 3495;
    private static final int MaximumNumberOfFiles = 7653;
    private static final int BlockSize = 256;
    private Vector<Short> arrayOfDirs = new Vector(103);

    public void writeFileHeader()
    {
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath,"rw"))
        {
            randomAccessFile.writeUTF(nameOfFileSystem);
            randomAccessFile.writeInt(sizeOfMFTFiles);
            randomAccessFile.writeInt(sizeOfMFTDirs);
            randomAccessFile.writeLong(freeSpaceDS);
            randomAccessFile.writeLong(usedSpaceDS);
            randomAccessFile.writeUTF(dateCreated);
            randomAccessFile.writeUTF(lastModified);
            randomAccessFile.writeUTF(lastUsed);
            randomAccessFile.writeInt(numberOfDirectoriums);
            randomAccessFile.writeInt(numberOfFiles);
            randomAccessFile.writeInt(maxNumOfFilesInSystem);
            randomAccessFile.writeInt(maxNumOfDirs);
            randomAccessFile.writeInt(MaximumSizeOfFile);
            randomAccessFile.writeInt(MaximumNumberOfDirs);
            randomAccessFile.writeInt(MaximumNumberOfFiles);
            randomAccessFile.writeInt(BlockSize);

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void setSizeOfMFTFiles(int sizeOfMFTFiles)
    {
        this.sizeOfMFTFiles=sizeOfMFTFiles;
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath,"rw"))
        {
            randomAccessFile.seek(16);
            randomAccessFile.writeInt(sizeOfMFTFiles);

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }

    }

    public void setSizeOfMFTDirs(int sizeOfMFTDirs)
    {
        this.sizeOfMFTDirs=sizeOfMFTDirs;
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath,"rw"))
        {
            randomAccessFile.seek(20);
            randomAccessFile.writeInt(sizeOfMFTDirs);

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void setFreeSpaceDS(long freeSpaceDS)
    {
        this.freeSpaceDS=freeSpaceDS;
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath,"rw"))
        {
            randomAccessFile.seek(24);
            randomAccessFile.writeLong(freeSpaceDS);

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void setUsedSpaceDS(long usedSpaceDS)
    {
        this.usedSpaceDS=usedSpaceDS;
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath,"rw"))
        {
            randomAccessFile.seek(32);
            randomAccessFile.writeLong(usedSpaceDS);
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void setDateCreated(String dateCreated)
    {
        this.dateCreated=dateCreated;
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath,"rw"))
        {
            randomAccessFile.seek(40);
            randomAccessFile.writeUTF(dateCreated);

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void setLastModified(String lastModified)
    {
        this.lastModified=lastModified;
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath,"rw"))
        {
            randomAccessFile.seek(64);
             randomAccessFile.writeUTF(lastModified);

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void setLastUsed(String lastUsed)
    {
        this.lastUsed=lastUsed;
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath,"rw"))
        {
            randomAccessFile.seek(88);
            randomAccessFile.writeUTF(lastUsed);

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void setNumberOfDirectoriums(int numberOfDirectoriums)
    {
        this.numberOfDirectoriums=numberOfDirectoriums;
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath,"rw"))
        {
            randomAccessFile.seek(112);
            randomAccessFile.writeInt(numberOfDirectoriums);

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void setNumberOfFiles(int numberOfFiles)
    {
        this.numberOfFiles=numberOfFiles;
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath,"rw"))
        {
            randomAccessFile.seek(116);
            randomAccessFile.writeInt(numberOfFiles);

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public FileHeaderRoot(String name)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'u' HH:mm:ss");
        nameOfFileSystem = name;
        Date date = new Date(System.currentTimeMillis());
        lastUsed = dateCreated = dateCreated = formatter.format(date);
        numberOfDirectoriums = 0;
        numberOfFiles = 0;
        freeSpaceDS = 19 * MainClass.ONEMB;
        usedSpaceDS = 0;
        sizeOfMFTFiles=0;
        sizeOfMFTDirs=0;
        this.writeFileHeader();

    }


    public int getSizeOfMFTFiles()
    {
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath,"rw"))
        {
            randomAccessFile.seek(16);
            return (this.sizeOfMFTFiles=randomAccessFile.readInt());

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return 0;
    }

    public int getSizeOfMFTDirs()
    {
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath,"rw"))
        {
            randomAccessFile.seek(20);
            return (this.sizeOfMFTDirs=randomAccessFile.readInt());

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return 0;

    }

    public String getNameOfFileSystem()
    {
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath,"rw"))
        {
            randomAccessFile.seek(0);
            return randomAccessFile.readUTF();

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public long getFreeSpaceDS()
    {
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath,"rw"))
        {
            randomAccessFile.seek(24);
            return (this.freeSpaceDS=randomAccessFile.readInt());

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return 0;
    }

    public long getUsedSpaceDS()
    {
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath,"rw"))
        {
            randomAccessFile.seek(32);
            return (this.usedSpaceDS=randomAccessFile.readInt());

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return 0;
    }

    public String getDateCreated()
    {
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath,"rw"))
        {
            randomAccessFile.seek(40);
            return (this.dateCreated=randomAccessFile.readUTF());

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public String getLastModified()
    {
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath,"rw"))
        {
            randomAccessFile.seek(64);
            return (this.lastModified=randomAccessFile.readUTF());

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public String getLastUsed()
    {
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath,"rw"))
        {
            randomAccessFile.seek(88);
            return (this.lastUsed=randomAccessFile.readUTF());

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public int getNumberOfDirectoriums()
    {
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath,"rw"))
        {
            randomAccessFile.seek(112);
            return (this.numberOfDirectoriums=randomAccessFile.readInt());

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return 0;
    }

    public int getNumberOfFiles()
    {
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath,"rw"))
        {
            randomAccessFile.seek(116);
            return (this.numberOfFiles=randomAccessFile.readInt());

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return 0;
    }



}





















