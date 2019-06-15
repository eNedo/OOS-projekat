import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
public class MainClass
{
    private static final long SYSTEMSIZE = 1024 * 1024 * 20;
    public static final long ONEMB = 1024 * 1024;
    public static String FileSystemPath = "./FILESYSTEM";
  
    
    public static void main(String args[]) throws IOException
    {
//    	File FS = new File(FileSystemPath);
//        RandomAccessFile FAJL=new RandomAccessFile(FS,"rw");
//        Scanner input = new Scanner(System.in);
//        FileHeader fh=new FileHeader(""); 
//        if(checkExistance(FAJL,input))
//            fh=firstStart(FAJL,input);
//        else fh.readfromfileandupdate(FAJL); 
//        cmd(fh,FAJL,input); 
//        FAJL.close();
//        input.close();




        

    }

    private static void cmd(FileHeader p,RandomAccessFile f,Scanner input)
    {
    	byte GLOBALROOT=0;
        int DIR_POSITION_OF_CMD=1;
         final String Warning = "Pogresan broj argumenata!";
        String takeInput = "";
        String DIRposition="root";
        String Parent="root";
        while (input != null && !("exit".equals(takeInput)))
        {
            System.out.print(DIRposition+":");

            takeInput = input.nextLine();
            String words[] = takeInput.split(" ");  // napravi niz rijeci od unjetog teksta

            switch (words[0])
            {
                case "mkdir":   // kreiranje novog direktorijuma na zadatoj putanji,
                    if (!takeInput.matches("(^mkdir (\"[\\w ]+\"$))|(^mkdir (\\w+$))"))
                    {
                        System.out.println(Warning);
                        break;
                    } else
                    {
                    	p.updateNumberOfDirectoriums(f);
                          System.out.println("mkdir je unesen");
                        System.out.println(words[0]+" "+ words[1]); 
                        DirectoryClass x=new DirectoryClass(words[1],GLOBALROOT,DIR_POSITION_OF_CMD,105);
                        x.writedir(f,p);
                    }

                    break;

                case "create": // kreiranje nove datoteke na zadatoj putanji,
                    if (!takeInput.matches("^create [\\w\\/]*\\w+\\.\\w{1,4}"))
                    {
                        System.out.println(Warning);
                        break;
                    } else
                    {
                        System.out.println("create je unesen");
                    }
                    break;

                case "put": // "upload" datoteke sa postojeceg fajl sistema na novi fajl sistem,
                    if (!takeInput.matches("^put [\\w\\/]+ [\\w\\/]+")) // TODO: mozda ograniciti ime datoteke
                    {
                        System.out.println(Warning);
                    } else
                    {
                        System.out.println("put je unesen!");
                    }
                    break;

                case "get": // "download" datoteke sa novog fajl sistema na postojeÄ‡i,
                    if (!takeInput.matches("^get [\\w\\/]+ [\\w\\/]+")) // TODO: mozda ograniciti ime datoteke
                    {
                        System.out.println(Warning);
                    } else
                    {
                        System.out.println("get je unesen");
                    }
                    break;

                case "ls": // izlistavanje sadrzaja direktorijuma na zadatoj putanji,
                    System.out.println("ls je unesen");
                    readdir(f,p);
                    break;

                case "cp": // kreiranje kopije datoteke na zadatoj putanji,
                    if (!takeInput.matches("^cp [\\w\\/]+ [\\w\\/]+")) // TODO: mozda ograniciti ime datoteke
                    {
                        System.out.println(Warning);
                    } else
                    {
                        System.out.println("cp je unesen");
                    }
                    break;

                case "mv": // premjeÅ¡tanje datoteke iz izvornog u odrediÅ¡ni direktorijum,
                    if (!takeInput.matches("^mv \\w+ \\w+")) // TODO: mozda ograniciti ime datoteke
                    {
                        System.out.println(Warning);
                    } else
                    {
                        System.out.println("mv je unesen");
                    }
                    break;

                case "rename": // promjena naziva datoteke/direktorijuma,
                    if (!takeInput.matches("^rename \\w+ \\w+")) // TODO: mozda ograniciti ime datoteke
                    {
                        System.out.println(Warning);
                    } else
                    {
                        System.out.println("rename je unesen");
                    }
                    break;

                case "echo": // upis proizvoljnog tekstualnog sadrÅ¾aja u datoteku,
                    if (!takeInput.matches("^echo (\"?)[\\w\\/ ]+(\\1) > \\w+\\.\\w{1,4}")) // TODO: mozda ograniciti ime datoteke
                    {
                        System.out.println(Warning);
                    } else
                    {
                        System.out.println("echo je unesen!");
                    }
                    break;

                case "cat": // prikaz sadrzaja (tekstualne) datoteke,
                    if (!takeInput.matches("^cat [\\w\\/]+\\.\\w{1,4}"))
                    {
                        System.out.println(Warning);
                    } else
                    {
                        System.out.println("cat je unesen");
                    }
                    break;

                case "rm": // brisanje datoteke/direktorijuma uz mogucnost brisanja kompletnog sadrzaja direktorijuma (opcija -r),
                    if (!takeInput.matches("^rm [\\w\\/]+\\.\\w{1,4}"))
                    {
                        System.out.println(Warning);

                    } else
                    {
                        System.out.println("rm je unesen");
                    }
                    break;

                case "stat": // prikaz informacija o datoteci, ukljucujuci informacije o MFT zapisu (NTFS), lokacijij svih blokova koji cine sadrzaj datoteke.
                    System.out.println("stat je unesen");
                    p.readINFOfromfile(f); 
                     break;

                case "ln": // ln - make links between files (OPCIONO)
                    if (!takeInput.matches("^ln [\\w\\/]+ [\\w\\/]+")) // TODO: mozda ograniciti ime datoteke
                    {
                        System.out.println(Warning);
                    } else
                    {
                        System.out.println("ln je unesen");
                    }
                    break;

                case "grep": // ovo je bas tesko napraviti (OPCIONO)
                    System.out.println("grep je unesen");
                    break;

                case "exit":
                    System.out.println("exit je unesen!");
                    System.out.println("Zdravo!");
                    System.exit(0);
                    break;
                default:
                   if ( !CheckDirectoriums(p,f,takeInput)) System.out.println(Warning); 
                   else System.out.println("Usli ste u:" + takeInput); 
                   Parent=takeInput;
            }

        }
        System.exit(0);

    }
      
    public static boolean  CheckDirectoriums(FileHeader p,RandomAccessFile f,String x)
    { 
    	try { 
    			f.seek(0);
      			String filename=""; 
     			char c; 
         		StringBuilder sb =new StringBuilder();
         for (int count=p.NumOfDirs(f); count>0;count--)
         {
        	 f.seek(f.getFilePointer()+102);
          	 if (count<0 || count ==0) System.out.println("error");
        		for (int i=0;15>i;i++) 
        					{
        						c=f.readChar();
        						System.out.println(count); 
        						System.out.println(c);
        						sb.append(c); 
        					}
        		filename=sb.toString();

        		System.out.println(filename);
        		if (x.compareTo(filename)==0) return true; 
    	}
         
    	}catch(Exception e) {} 
    	return false; 
    }
    public static boolean checkExistance(RandomAccessFile x,Scanner input)
    {
        try
        {
        x.setLength(SYSTEMSIZE);
        	String f=" ";
         
             do
            {
                System.out.println("Fajl sistem vec postoji, da li zelite kreirati novi?");
                System.out.println("Za koristenje starog unesite 'n'  Za kreiranje novog 'y'"); 
                f= input.nextLine();

            } while (!"y".equals(f) && !"n".equals(f));
             if ("y".equals(f)){ 
               return true; 
             }
             if ("n".equals(f)) 
             { 
            	 
             }
        } catch(Exception e) {}
        return false;
    }
    
		

        
    public static FileHeader firstStart(RandomAccessFile x, Scanner s)
    {
        String name;
         do
        {
            System.out.println("Unesite ime novog fajl sistema!");
            name = s.nextLine();x
        } while (name.length() > 15);

        while (name.length() <= 15)
            name += " "; 
        FileHeader p = new FileHeader(name);
        p.writeToFile(x);
        System.out.println("Uspjesno smo kreirali novi fajl sistem! "); 
        System.out.println("Mozete poceti manipulisati sa fajl sistemom."); 
        return p;
     }
}

public static long getFileHeaderFreeBlock(RandomAccessFile x,RootHeader rh) throws IOException        //TODO
{ 
    x.seek(ONEMB-106); 
    int count=0;                //imamo broj filehedera, toliko puta cemo kroz petlju proci i provjeriti 
    byte allocationflag, mftflag;// allocationflag i mftflag, kad prodjemo kroz sve, trebamo provjeriti 
    do { //velicinu frispejsa i vratiti pokazivac
        allocationflag=x.read(); //slobodno za alokaciju
        mftflag=x.read(); //ako smo naisli na slobodan mft fajl heder onda preskocimo i cekamo naredni mft fajl za upisivanje da nemamo rupe u memoriji 
    if (x1==0 && x2==1) return x.getFilePointer()-64-106;         //slobodno za alokaciju 
    else {                              //dodati provjeru za freespace
        count++; 
                if(x1==1 && x2==0)   x.seek(x.getFilePointer()-106); 
        } 
        x.seek(x.getFilePointer-106);
    }
    while ((rh.getSizeOfMFTDirs -170*count) >170); 
}

