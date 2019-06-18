import groovyjarjarantlr.StringUtils;
import org.codehaus.groovy.tools.shell.Main;

import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

public class RootHeader
{
    //16+24*3+4*12+81*2 = 350
    public static final int ROOTHEADERSIZE = 347;

    private String nameOfFileSystem = "";    //16    == 14 + \0
    private int sizeOfMFTFiles;     //16
    private int sizeOfMFTDirs;      //20
    private long freeSpaceDS;           //24
    private long usedSpaceDS;           //32
    private String dateCreated;         //40
    private String lastModified;        //62
    private String lastUsed;            //84
    private int numberOfDirectoriums;   //109
    private int numberOfFiles;          //113
    private short[] arrayOfDirs;        //117
    private static final int maxNumOfFilesInSystem = 3495;  //117+103*2=323
    private static final int maxNumOfDirs = 7653;           //327
    private static final int MaximumSizeOfFile = 65536;     //331
    private static final int MaximumNumberOfDirs = 3495;    //335
    private static final int MaximumNumberOfFiles = 7653;   //339
    private static final int BlockSize = 256;               //343

    public RootHeader()
    {}

    public RootHeader(String name)
    {
        nameOfFileSystem = name;
        this.nameOfFileSystem = String.format("%-14s", name);       // bilo %-14s
        lastUsed = dateCreated = lastModified = Utilities.getCurrentDate();
        numberOfDirectoriums = 0;
        numberOfFiles = 0;
        freeSpaceDS = 19 * MainClass.ONEMB;
        usedSpaceDS = 0;
        sizeOfMFTFiles = 0;
        sizeOfMFTDirs = 0;
        arrayOfDirs= new short[103];    // bilo 103
        this.writeFileHeader();

    }

    public void writeFileHeader()
    {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
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
            for (int i = 0; i < arrayOfDirs.length; i++)
                randomAccessFile.writeShort(arrayOfDirs[i]);

            randomAccessFile.writeInt(maxNumOfFilesInSystem);
            randomAccessFile.writeInt(maxNumOfDirs);
            randomAccessFile.writeInt(MaximumSizeOfFile);
            randomAccessFile.writeInt(MaximumNumberOfDirs);
            randomAccessFile.writeInt(MaximumNumberOfFiles);
            randomAccessFile.writeInt(BlockSize);

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public int getSizeOfMFTFiles()
    {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {
            randomAccessFile.seek(16);
            return (this.sizeOfMFTFiles = randomAccessFile.readInt());

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return 0;
    }

    public void setSizeOfMFTFiles(int sizeOfMFTFiles)
    {
        this.sizeOfMFTFiles = sizeOfMFTFiles;
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {
            randomAccessFile.seek(16);
            randomAccessFile.writeInt(sizeOfMFTFiles);

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    public int getSizeOfMFTDirs()
    {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {
            randomAccessFile.seek(20);
            return (this.sizeOfMFTDirs = randomAccessFile.readInt());

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return 0;

    }

    public void setSizeOfMFTDirs(int sizeOfMFTDirs)
    {
        this.sizeOfMFTDirs = sizeOfMFTDirs;
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {
            randomAccessFile.seek(20);
            randomAccessFile.writeInt(sizeOfMFTDirs);

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public String getNameOfFileSystem()
    {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {
            randomAccessFile.seek(0);
            return randomAccessFile.readUTF();

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public long getFreeSpaceDS()
    {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {

            randomAccessFile.seek(24);
            return (this.freeSpaceDS = randomAccessFile.readLong());

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return 0;
    }

    public void setFreeSpaceDS(long freeSpaceDS)
    {
        this.freeSpaceDS = freeSpaceDS;
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {
            randomAccessFile.seek(24);
            randomAccessFile.writeLong(freeSpaceDS);

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public long getUsedSpaceDS()
    {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {
            randomAccessFile.seek(32);
            return (this.usedSpaceDS = randomAccessFile.readLong());

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return 0;
    }

    public void setUsedSpaceDS(long usedSpaceDS)
    {
        this.usedSpaceDS = usedSpaceDS;
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {
            randomAccessFile.seek(32);
            randomAccessFile.writeLong(usedSpaceDS);
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public String getDateCreated()
    {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {
            randomAccessFile.seek(40);
            this.dateCreated = randomAccessFile.readUTF();
            return this.dateCreated;

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public void setDateCreated(String dateCreated)
    {
        this.dateCreated = dateCreated;
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {
            randomAccessFile.seek(40);
            randomAccessFile.writeUTF(dateCreated);

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public String getLastModified()
    {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {
            randomAccessFile.seek(63);
            return (this.lastModified = randomAccessFile.readUTF());

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public void setLastModified(String lastModified)
    {
        this.lastModified = lastModified;
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {
            randomAccessFile.seek(63);
            randomAccessFile.writeUTF(lastModified);

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public String getLastUsed()
    {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {
            randomAccessFile.seek(86);
            return (this.lastUsed = randomAccessFile.readUTF());

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public void setLastUsed(String lastUsed)
    {
        this.lastUsed = lastUsed;
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {
            randomAccessFile.seek(86);
            randomAccessFile.writeUTF(lastUsed);

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public int getNumberOfDirectoriums()
    {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {
            randomAccessFile.seek(109);
            return (this.numberOfDirectoriums = randomAccessFile.readInt());

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return 0;
    }

    public void setNumberOfDirectoriums(int numberOfDirectoriums)
    {
        this.numberOfDirectoriums = numberOfDirectoriums;
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {
            randomAccessFile.seek(109);
            randomAccessFile.writeInt(numberOfDirectoriums);

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public int getNumberOfFiles()
    {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {

            randomAccessFile.seek(113);
            return (this.numberOfFiles = randomAccessFile.readInt());

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return 0;
    }

    public void setNumberOfFiles(int numberOfFiles)
    {
        this.numberOfFiles = numberOfFiles;
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {
            randomAccessFile.seek(113);
            randomAccessFile.writeInt(numberOfFiles);

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public short[] getArrayOfDirs()
    {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {
            randomAccessFile.seek(117);
            short[] tempArray = new short[103];
            for (int i = 0; i < 103; i++)
                tempArray[i]=randomAccessFile.readShort();

            return (this.arrayOfDirs=tempArray);

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public void setArrayOfDirs(short[] arrayOfDirs)
    {
        this.arrayOfDirs=arrayOfDirs;
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {
            randomAccessFile.seek(117);
            for (int i = 0; i < 103; i++)
                randomAccessFile.writeShort(arrayOfDirs[i]);

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }


}





















