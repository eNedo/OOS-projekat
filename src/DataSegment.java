import java.io.*;
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


    public byte[] readDataFromDataSegment(int startingBlock, int numOfBlocks)  
    {
        byte []bufferArray=new byte[123*numOfBlocks];
        try
        {
            RandomAccessFile randomAccessFileSystem = new RandomAccessFile(MainClass.FileSystemPath, "rw");
            randomAccessFileSystem.seek(MainClass.ONEMB + startingBlock * DataSegmentBlockSize );
            int blockSize=123;
            int nextBlock=0;
             int helpcounter=0;		//povecavanje indeksa za bufferArray u skladu sa brojem bloka
             for (int i = 0; i < numOfBlocks && nextBlock != -1; i++)
            {
                if(randomAccessFileSystem.readByte()==1)
                {

                for (int j = 123*helpcounter; j <blockSize*(helpcounter+1)  ; j++)
                {
                     bufferArray[j]=(randomAccessFileSystem.readByte());
                }
                if(nextBlock>0 && i!=(numOfBlocks-1)) {randomAccessFileSystem.seek(randomAccessFileSystem.getFilePointer()-1);}
                if((numOfBlocks-1)==i) randomAccessFileSystem.seek(randomAccessFileSystem.getFilePointer()-2);
                  nextBlock = randomAccessFileSystem.readInt();  
                 helpcounter++;
                 if (nextBlock==-1) break; 
                randomAccessFileSystem.seek(MainClass.ONEMB + nextBlock * DataSegmentBlockSize );
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return bufferArray;
    }

    public int writeDataInDataSegment(int numOfBlocks, String nameOfOutputFile)
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
            randomAccessFile.writeByte((byte)1);
             for (int i = 0; i <outputArray.length; i++)
            {

                randomAccessFile.writeByte(outputArray[i]);  
                 if(i%122==0 && i!=0)
                {
                	 
                    currentBlock++;
                    randomAccessFile.writeInt(arrayOfFreeBlocks[currentBlock]);
                      randomAccessFile.seek(calculatePointerWithBlockNum(arrayOfFreeBlocks[currentBlock]));
                    randomAccessFile.writeByte((byte)1);
                }
            }
             for (int i=0;(123*arrayOfFreeBlocks.length-outputArray.length)>i;i++)  
            { randomAccessFile.writeByte(0); } 
            randomAccessFile.writeInt(-1);
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return arrayOfFreeBlocks[0];

    }
    public static int rewriteBlocksECHO(int startblock,int numOfBlocks, byte[] data)
    {
         try
        {
            RandomAccessFile file = new RandomAccessFile(MainClass.FileSystemPath,"rw");
             int currentBlock=0;
            file.seek(calculatePointerWithBlockNum(startblock));
            file.writeByte((byte)1);
            int curentBlock=0;
            int nextBlock=0;
            for (int i = 0; i <numOfBlocks; i++)
            {
            	file.writeByte(data[i]);
                 if(i%123==0 && i!=0)
                {
                    currentBlock++;
                    nextBlock=file.readInt();
                     file.seek(calculatePointerWithBlockNum(nextBlock));
                    file.writeByte((byte)1);
                }
            }
             for (int i=0;(123*numOfBlocks-data.length)>i;i++)  //dopuna za zadnji blok, koji ne mora 
            { file.writeByte(0); } 								//popuniti cijeli blok
            file.writeInt(-1);		//nema narednih blokova za citanje 
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
         return 1;
    }

}
