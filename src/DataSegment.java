import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class DataSegment
{
    public static final long ENDOFFILESYSTEM = 20 * MainClass.ONEMB;
    public static final int DataSegmentBlockSize = 128;
    public static long lastUsedPosition = MainClass.ONEMB;
    public byte isAlocate;
    public byte[] data;
    public int nextBlock;
    // maksimalan je fajl je od 64KB potrebno je 533 bloka za njega

    public DataSegment()
    {

    }

    public static void deleteFileInDataSegment(int startBlock, int numOfBlocks)
    {
        int currentBlock = startBlock;
        try (RandomAccessFile randomAceesFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {

            do
            {
                randomAceesFile.seek(calculatePointerWithBlockNum(startBlock));
                randomAceesFile.writeByte((byte) 0);
                randomAceesFile.seek(randomAceesFile.getFilePointer() + 123);
                currentBlock = randomAceesFile.readInt();
                numOfBlocks--;

            } while (currentBlock != -1 && numOfBlocks > 0);


        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static void recoverFileInDataSegment(int startBlock, int numOfBlocks)
    {
        int currentBlock = startBlock;
        try (RandomAccessFile randomAceesFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {

            do
            {
                randomAceesFile.seek(calculatePointerWithBlockNum(startBlock));
                randomAceesFile.writeByte((byte) 1);
                randomAceesFile.seek(randomAceesFile.getFilePointer() + 123);
                currentBlock = randomAceesFile.readInt();
                numOfBlocks--;

            } while (currentBlock != -1 && numOfBlocks > 0);


        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    public static long calculatePointerWithBlockNum(int blockNumber)
    {
        return MainClass.ONEMB + blockNumber * DataSegment.DataSegmentBlockSize;
    }

    public static int calculateBlockWithPointer(long randAcessPointer)
    {
        return (int) ((randAcessPointer - MainClass.ONEMB) / DataSegmentBlockSize);
    }

    public static void prepairDataSegment()
    {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "rw"))
        {
            randomAccessFile.seek(MainClass.ONEMB);
            while (randomAccessFile.getFilePointer() < ENDOFFILESYSTEM)
            {
                randomAccessFile.writeByte(0);
                randomAccessFile.seek(randomAccessFile.getFilePointer() + 127);
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    // prije ove funkcije potrebno je imati fukciju koja provjerava da li ima dovoljno prostora u dataSegmentu
    public int[] findArrayOfFreeBlocks(int numOfBlocksNeeded)
    {
        int[] arrayOfBlocks = new int[numOfBlocksNeeded];
        int i = 0;
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath, "r"))
        {
            randomAccessFile.seek(lastUsedPosition);
            while (i < numOfBlocksNeeded)
            {
                if (randomAccessFile.readByte() == (byte) 0)
                {
                    arrayOfBlocks[i] = calculateBlockWithPointer(randomAccessFile.getFilePointer());
                    i++;
                }

                if (randomAccessFile.getFilePointer() >= ENDOFFILESYSTEM)
                    randomAccessFile.seek(MainClass.ONEMB);

            }

            lastUsedPosition = randomAccessFile.getFilePointer();

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return arrayOfBlocks;
    }

    public int writeDataInDataSegment(int numOfBlocks, String nameOfFile)
    {
        int[] arrayOfFreeBlocks = findArrayOfFreeBlocks(numOfBlocks);
        try
        {
            RandomAccessFile randomAccessFile = new RandomAccessFile(nameOfFile, "r");
            RandomAccessFile randomAccessFileSystem = new RandomAccessFile(MainClass.FileSystemPath, "rw");
            randomAccessFileSystem.seek(MainClass.ONEMB);
            byte[] bufferArray = new byte[123];
            byte writeByteUsed = 1;
            int i = 0;
            while (i < numOfBlocks)
            {
                randomAccessFileSystem.seek(MainClass.ONEMB+arrayOfFreeBlocks[i] * DataSegmentBlockSize); //TODO : ONEMB
                randomAccessFileSystem.writeByte(writeByteUsed);
                for (int j = 0; j < randomAccessFile.length(); j++)
                {
                    randomAccessFileSystem.writeByte(randomAccessFile.readByte());
                }
                if (i < numOfBlocks)
                    randomAccessFileSystem.writeInt(arrayOfFreeBlocks[i++]);    // ++ prefix to sufix
                else
                {
                    randomAccessFileSystem.seek(arrayOfFreeBlocks[i] * DataSegmentBlockSize + 124);
                    randomAccessFileSystem.writeInt(-1);
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return arrayOfFreeBlocks[0];
    }

    public void readDataFromDataSegment(int numOfBlocks, int startingBlock, byte[] buffeerArray)
    {

        try
        {
            RandomAccessFile randomAccessFileSystem = new RandomAccessFile(MainClass.FileSystemPath, "r");
            randomAccessFileSystem.seek(MainClass.ONEMB + startingBlock * DataSegmentBlockSize + 1);
            for (int i = 0; i < numOfBlocks; i++)
            {
                for (int j = 0; j < 123; j++)
                {
                    buffeerArray[i] = randomAccessFileSystem.readByte();
                }
                int nextBlock;
                nextBlock = randomAccessFileSystem.readInt();
                randomAccessFileSystem.seek(MainClass.ONEMB + nextBlock * DataSegmentBlockSize + 1);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public byte[] readDataFromDataSegment1(int numOfBlocks, int startingBlock) // Filip version
    {
        ArrayList<Byte> buffeerArray = new ArrayList<>();
        try
        {
            RandomAccessFile randomAccessFileSystem = new RandomAccessFile(MainClass.FileSystemPath, "r");
            randomAccessFileSystem.seek(MainClass.ONEMB + startingBlock * DataSegmentBlockSize+1 );
            int nextBlock=0;
            for (int i = 0; i < numOfBlocks && nextBlock!=-1; i++)
            {
                for (int j = 0; j < 123; j++)
                {
                    buffeerArray.add(randomAccessFileSystem.readByte());
                }
                nextBlock = randomAccessFileSystem.readInt();
                randomAccessFileSystem.seek(MainClass.ONEMB + nextBlock * DataSegmentBlockSize+1 );
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        byte[] byteArray = new byte[buffeerArray.size()];
        for (int i = 0; i < byteArray.length; i++)
        {
            byteArray[i] = buffeerArray.get(i);
        }
        return byteArray;
    }

}
