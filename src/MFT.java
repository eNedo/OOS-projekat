
public class MFT
{
    private final static int BLOCK_SIZE = 36864;
    private byte MBA[] = new byte[BLOCK_SIZE];

    public MFT()
    { 
    for (int i=0;36864>i;i++) 
    	MBA[i]=0; 
    }
    boolean isFree(int block)
    {  
        if (MBA[block] == 1) return true;
        else return false;
    }
    public int firstFreeBlock()
    { 
    	for (int i=0;36864>i;i++) 
    		if(MBA[i]==0) return i; 
    	return -1; 
    }
    public void setBlockAsUsed(int position) 
    { 
    	MBA[position]=1; 
    }
    public void freeBlock(int position) 
    { 
    	MBA[position]=0; 
    } 
} 
