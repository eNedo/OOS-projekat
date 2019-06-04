import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.FileInputStream;
import java.util.Scanner;
public class MainClass {
	private static long FILESIZE=1024*1024*20;
	public static void main(String args[])
	{
	try {
		File f=new File("./FILESYSTEM"); 
		FileOutputStream OUT=new FileOutputStream("./FILESYSTEM");
		FileInputStream  IN=new  FileInputStream("./FILESYSTEM");
		x.setLength(FILESIZE);
		//if (f.exists())	
		firstStart(x); 
			//fajl nije postojao u sistemu, kreiraj novi i upisi sve potrebno
		
		x.close();
		}
	catch(FileNotFoundException e)
	{} 
	catch(IOException f)
	{}
	
	
		
	}
	public static  void firstStart(RandomAccessFile x)
	{
		String name; 
		Scanner s=new Scanner(System.in); 
		do
		{
		System.out.println("Unesite ime novog fajl sistema!"); 
		name=s.nextLine();
		}
		while(name.length()>15); 
		while(name.length()<=15)
			name+=" ";

		FILEHeader p=new FILEHeader(name);
		p.write(x); 
	}
}
