
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

public class RootHeader
{
    //16+24*3+4*12+81*2 = 350
    public static final int ROOTHEADERSIZE = 347;

    private String nameOfFileSystem = "";    //16    == 14 + \0
    private int sizeOfMFTFiles;     //16
    private int sizeOfMFTFileHeaders;      //20
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
    private static final int BlockSize = 128;               //343

    public RootHeader()
    {}

    public RootHeader(String name)
    {
        //TODO: NE DIRAJ MOJE! filip

        for (; name.length()<14;) name=name+ " ";
        System.out.println(name + " " + name.length());
        nameOfFileSystem = name;
        lastUsed = dateCreated = lastModified = Utilities.getCurrentDate();
        numberOfDirectoriums = 0;
        numberOfFiles = 0;
        freeSpaceDS = 19 * MainClass.ONEMB;
        usedSpaceDS = 0;
        sizeOfMFTFiles = 0;
        sizeOfMFTFileHeaders = 0;
        arrayOfDirs= new short[103];
        Arrays.fill(arrayOfDirs,(short)32555);  // dodao filip
        this.writeFileHeader();

    }

    public void updateFileHeader()
    {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {
        	randomAccessFile.seek(0); 
        	nameOfFileSystem=randomAccessFile.readUTF();
        	sizeOfMFTFiles= randomAccessFile.readInt();
        	sizeOfMFTFileHeaders =randomAccessFile.readInt();
        	freeSpaceDS=randomAccessFile.readLong();
        	usedSpaceDS=randomAccessFile.readLong();
        	dateCreated=randomAccessFile.readUTF();
        	lastModified=randomAccessFile.readUTF();
        	lastUsed =randomAccessFile.readUTF();
        	numberOfDirectoriums =randomAccessFile.readInt();
        	numberOfFiles=randomAccessFile.readInt();
            for (int i = 0; i < 103; i++)
            	arrayOfDirs[i]=randomAccessFile.readShort();
 
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public void writeFileHeader()
    {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {
            randomAccessFile.writeUTF(nameOfFileSystem);
            randomAccessFile.writeInt(sizeOfMFTFiles);
            randomAccessFile.writeInt(sizeOfMFTFileHeaders);
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
            randomAccessFile.getFD().sync(); 
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

    public void updateDeletedDirectorium() {
        numberOfDirectoriums--;
    }

    public void updateWrittenDirectorium() {
        numberOfDirectoriums++;
    }

    public int getsizeOfMFTFileHeaders()
    {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {
            randomAccessFile.seek(20);
            return (this.sizeOfMFTFileHeaders = randomAccessFile.readInt());

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return 0;

    }

    public void setsizeOfMFTFileHeaders(int sizeOfMFTDirs)
    {
        this.sizeOfMFTFileHeaders = sizeOfMFTDirs;
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

    public static short[] getArrayOfDirs()
    {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {
            randomAccessFile.seek(117);
            short[] tempArray = new short[103];
            for (int i = 0; i < 103; i++)
                tempArray[i]=randomAccessFile.readShort();

//            return (this.arrayOfDirs=tempArray);
            return tempArray;

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public static void setArrayOfDirsAndFiles(short[] arrayOfDirsAndFiles)
    {
//        this.arrayOfDirs=arrayOfDirs;
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {
            randomAccessFile.seek(117);
            for (int i = 0; i < 103; i++)
                randomAccessFile.writeShort(arrayOfDirsAndFiles[i]);

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }


    public void stats()
    {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {
        	randomAccessFile.seek(0);
            System.out.println("Ime fajl sistema:"+randomAccessFile.readUTF());
            System.out.println("Velicina MFT fajlova:" + randomAccessFile.readInt());
            System.out.println("Velicina MFT direktorijuma:" + randomAccessFile.readInt());
            System.out.println("Slobodan prostor"+randomAccessFile.readLong( ));
            System.out.println("Iskoristeni prostor"+randomAccessFile.readLong( ));
            System.out.println("Datum kreiranja:"+randomAccessFile.readUTF());
            System.out.println("Datum modifikacije:"+randomAccessFile.readUTF());
            System.out.println("Datum zadnjeg koristenja:"+randomAccessFile.readUTF());
            System.out.println("Broj direktorijuma:"+randomAccessFile.readInt());
            System.out.println("Broj fajlova:"+randomAccessFile.readInt());
            for (int i=0;103>i;i++) randomAccessFile.readShort();
            System.out.println("Maks. broj fajlova u sistemu:"+randomAccessFile.readInt());
            System.out.println("Maks. broj direktorijuma u sistemu:"+randomAccessFile.readInt());
            System.out.println("Maksimalna velicina fajla:"+randomAccessFile.readInt());
            System.out.println("Maksimalni broj direktorijuma:" +randomAccessFile.readInt());
            System.out.println("Maksimalni broj fajlova:"+randomAccessFile.readInt());
            System.out.println("velicina bloka:"+randomAccessFile.readInt());

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
