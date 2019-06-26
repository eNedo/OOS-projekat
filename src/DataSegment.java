import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
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
                randomAccessFile.seek(randomAccessFile.getFilePointer()+127);

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
        try (
                RandomAccessFile randomAccessFile = new RandomAccessFile(nameOfFile, "r");
                RandomAccessFile randomAccessFileSystem = new RandomAccessFile(MainClass.FileSystemPath, "rw");)
        {
            byte writeByteUsed = 1;
            int i = 0;
            while (i < numOfBlocks)
            {
                randomAccessFileSystem.seek(MainClass.ONEMB + arrayOfFreeBlocks[i] * DataSegmentBlockSize);
                randomAccessFileSystem.writeByte(writeByteUsed);
                for (int j = 0; j < randomAccessFile.length(); j++)
                {
                    if ((j+1) % 124 == 0)
                    {
                        randomAccessFileSystem.writeInt(arrayOfFreeBlocks[++i]);
//                        randomAccessFileSystem.seek(MainClass.ONEMB + arrayOfFreeBlocks[i] * DataSegmentBlockSize);
//                        randomAccessFileSystem.writeByte(writeByteUsed);
                    }
                    randomAccessFileSystem.writeByte(randomAccessFile.readByte());


                }

            }
            randomAccessFileSystem.seek(MainClass.ONEMB + arrayOfFreeBlocks[i] * DataSegmentBlockSize + 124);
            randomAccessFileSystem.writeInt(-1);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return arrayOfFreeBlocks[0];
    }


    public byte[] readDataFromDataSegment(int startingBlock,int numOfBlocks) // Filip version
    {
        ArrayList<Byte> buffeerArray = new ArrayList<>();
        try
        {
            RandomAccessFile randomAccessFileSystem = new RandomAccessFile(MainClass.FileSystemPath, "rw");
            randomAccessFileSystem.seek(MainClass.ONEMB + startingBlock * DataSegmentBlockSize + 1);
            int nextBlock = 0;
            for (int i = 0; i < numOfBlocks && nextBlock != -1; i++)
            {
                for (int j = 0; j < 123; j++)
                {
                    buffeerArray.add(randomAccessFileSystem.readByte());
                }
                nextBlock = randomAccessFileSystem.readInt();

                randomAccessFileSystem.seek(MainClass.ONEMB + nextBlock * DataSegmentBlockSize + 1);
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



    public int writeDataInDataSegment1(int numOfBlocks, String nameOfFile)
    {
        int[] arrayOfFreeBlocks = findArrayOfFreeBlocks(numOfBlocks);

        try (   RandomAccessFile randomAccessFile = new RandomAccessFile(nameOfFile, "r");
                RandomAccessFile randomAccessFileSystem = new RandomAccessFile(MainClass.FileSystemPath, "rw");)
        {
            byte writeByteUsed=1;
            int i=1;
//            long currentBlock=0;
            long fileSize=randomAccessFile.length();

            System.out.println("filesize: "+fileSize);
            do
            {
                randomAccessFileSystem.seek(calculatePointerWithBlockNum(arrayOfFreeBlocks[i-1]));
                randomAccessFileSystem.writeByte(writeByteUsed);

                while (randomAccessFile.getFilePointer() != randomAccessFile.length())
                {
                    byte test = randomAccessFile.readByte();
                    randomAccessFileSystem.writeByte(test);
                    System.out.print((char)test);
                    if((randomAccessFile.getFilePointer()%122)==0)
                    {
                        break;
                    }
                }

//                for (long j = 0; j < fileSize ; j++)
//                {
//                    if((j+1)%123==0)
//                    {
//                        randomAccessFileSystem.writeInt(arrayOfFreeBlocks[i++]);
//                        break;
//                    }
//                    byte test = randomAccessFile.readByte();
//                    randomAccessFileSystem.writeByte(test);
//                    System.out.print((char)test);
//
//                }
//                fileSize-=123;
//                System.out.println("\nfilesize: "+fileSize);

            }while(i<numOfBlocks);
            randomAccessFileSystem.seek(calculatePointerWithBlockNum(arrayOfFreeBlocks[i-1])+124);
            randomAccessFileSystem.writeInt(-1);

        }catch(Exception ex)
        {
            ex.printStackTrace();

        }

        return 0;
    }

    // nedo.png
    public int writeDataInDataSegment2(int numOfBlocks, String nameOfFile)
    {
        int[] arrayOfFreeBlocks = findArrayOfFreeBlocks(numOfBlocks);

        try (   RandomAccessFile randomAccessFile = new RandomAccessFile(nameOfFile, "r");
                RandomAccessFile randomAccessFileSystem = new RandomAccessFile(MainClass.FileSystemPath, "rw");)
        {

            ArrayList<Byte> buffeerArray = new ArrayList<>();
            for (int i = 0; i < randomAccessFile.length(); i++)
            {
                buffeerArray.add(randomAccessFile.readByte());
            }

            byte[][] myArray=new byte[numOfBlocks][123];
            for (int i = 0; i < numOfBlocks; i++)
            {
                myArray[i]=new byte[123];
            }

            int k=0;
            for (int i = 0; i < buffeerArray.size(); k++)
            {
                for (int j = 0; j < 123 && i<buffeerArray.size(); j++)
                {
                    myArray[k][j]=buffeerArray.get(i++);
                }
            }

            for (int i = 0; i < numOfBlocks; i++)
            {
                randomAccessFileSystem.seek(MainClass.ONEMB+arrayOfFreeBlocks[i]*DataSegmentBlockSize);
                randomAccessFileSystem.writeByte((byte)1);
                for (int j = 0; j < myArray[i].length; j++)
                {
                    randomAccessFile.writeByte(myArray[i][j]);
                }
                randomAccessFileSystem.writeInt(arrayOfFreeBlocks[i]);
            }


        }catch(Exception ex)
        {
            ex.printStackTrace();

        }

        return 0;
    }


}
