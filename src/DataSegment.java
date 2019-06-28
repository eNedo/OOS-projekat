import java.io.*;
import java.util.ArrayList;



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
            while (randomAccessFile.getFilePointer() <= ENDOFFILESYSTEM)
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
                randomAccessFile.seek(randomAccessFile.getFilePointer() + 127);

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


    public byte[] readDataFromDataSegment(int startingBlock, int numOfBlocks, int fileSize) // Filip version
    {
        ArrayList<Byte> buffeerArray = new ArrayList<>();
        try
        {
            RandomAccessFile randomAccessFileSystem = new RandomAccessFile(MainClass.FileSystemPath, "rw");
            randomAccessFileSystem.seek(MainClass.ONEMB + startingBlock * DataSegmentBlockSize );
            long currentPosition = MainClass.ONEMB + startingBlock * DataSegmentBlockSize;
            int nextBlock = 0;
            int blockSize=123;
            int usedBlock=1;
            byte isAlloc;

            for (int i = 0; i < numOfBlocks && nextBlock != -1; i++)
            {
                isAlloc=randomAccessFileSystem.readByte();

                for (int j = 0; j < blockSize; j++)
                {
                    buffeerArray.add(randomAccessFileSystem.readByte());
                }


                randomAccessFileSystem.seek(currentPosition+124);
                nextBlock = randomAccessFileSystem.readInt();
                usedBlock++;
                if(usedBlock==numOfBlocks)
                    blockSize=fileSize-(numOfBlocks-1)*123;

                randomAccessFileSystem.seek(MainClass.ONEMB + nextBlock * DataSegmentBlockSize );
                currentPosition=MainClass.ONEMB + nextBlock * DataSegmentBlockSize ;
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



    public int writeDataInDataSegment(int numOfBlocks, String nameOfOutputFile) // Filip version
    {
        int[] arrayOfFreeBlocks = findArrayOfFreeBlocks(numOfBlocks);
        byte[] outputArray;

        try
        {
            RandomAccessFile outputFile = new RandomAccessFile(nameOfOutputFile,"rw");
            RandomAccessFile randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath,"rw");
            outputArray = new byte[(int) outputFile.length()];
            outputFile.readFully(outputArray);


            int currentBlock=0;
            randomAccessFile.seek(calculatePointerWithBlockNum(arrayOfFreeBlocks[currentBlock]));
            byte isAllocated=randomAccessFile.readByte();



            for (int i = 1; i <outputArray.length+1; i++)
            {

                randomAccessFile.writeByte(outputArray[i-1]);
                System.out.print((char)outputArray[i-1]);
                if(i%123==0)
                {
                    currentBlock++;
                    randomAccessFile.writeInt(arrayOfFreeBlocks[currentBlock]);
                    randomAccessFile.close();
                    randomAccessFile = new RandomAccessFile(MainClass.FileSystemPath,"rw");
                    randomAccessFile.seek(calculatePointerWithBlockNum(arrayOfFreeBlocks[currentBlock]));
                    isAllocated=randomAccessFile.readByte();
                }


            }

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return arrayOfFreeBlocks[0];

    }








}
