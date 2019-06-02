
public class MFT {
	private final static int BLOCK_SIZE=36864;
	private byte MBA[]= new byte[BLOCK_SIZE];

	
	
	
	
boolean isFree(int block)
{
	if (MBA[block]==1) return true; 
	else return false; 
}

}
