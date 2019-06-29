import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class MainClass
{
    public static final long ONEMB = 1024 * 1024;
    private static final long SYSTEMSIZE = 1024 * 1024 * 20;
    public static String FileSystemPath = "./FILESYSTEM";
    public static void main(String args[]) throws IOException
    {
        File FS = new File(FileSystemPath);
        RandomAccessFile FAJL = new RandomAccessFile(FS, "rw");
        Scanner input = new Scanner(System.in);
        RootHeader fh = new RootHeader(" ");
        FAJL.setLength(SYSTEMSIZE);
        DataSegment.prepairDataSegment(); 
        if (checkExistance(FAJL, input))
            fh = firstStart(FAJL, input);
        else
            fh.updateFileHeader(); 
        cmd(fh, FAJL, input);
        FAJL.close();
        input.close();
    }

    private static void cmd(RootHeader rootheader, RandomAccessFile file, Scanner input)
    {
        try
        {		//TODO-dodati pozicioniranje kroz fajl sistem (promjena trenutnog direktorijuma)
              final String Warning = "Pogresan broj argumenata!";
            String takeInput = "";
             while (input != null && !("exit".equals(takeInput)))
            {
                System.out.print("root:");
                takeInput = input.nextLine();
                String words[] = takeInput.split(" ");  // napravi niz rijeci od unjetog teksta

                switch (words[0])
                {
                    case "mkdir":   // kreiranje novog direktorijuma na zadatoj putanji,
                        if (!(words.length>2))
                        {			// pogresan unos 
                            System.out.println(Warning);
                            break;
                        } else
                        {
                    DirectoryClass object=new DirectoryClass(words[2],(byte)1);
                    String temppath[]=words[1].split("/"); 
                    if (temppath[0].equals("root")) { 
                     	if (temppath.length==2) // root/dir1
                    		{ 
                    		if (object.searchDirectoryByName(rootheader, file, temppath[1])>=0) 
                    					{
                    					object.createNewDirectory(rootheader, file);
                    					int positionfornewdir=object.searchDirectoryByName(rootheader, file, words[2]);
                    					if(object.mkdir(rootheader, file, words[1],(short)positionfornewdir)) System.out.println("Uspjesno je napravljen "+ words[2] +" direktorijum!"); 
                    					else System.out.println("Neuspjesno pravljenje direktorijuma!"); 
                    					}
                    		else System.out.println("Ne postoji: " + temppath[1] + " direktorijum!"); 
                    		}
                    	else if (temppath.length==1) //root
                    	{ 
                    		object.createNewDirectory(rootheader, file);
        					int positionfornewdir=object.searchDirectoryByName(rootheader, file, words[2]);
        					if(object.mkdir(rootheader, file, words[1],(short)positionfornewdir)) System.out.println("Uspjesno je napravljen "+ words[2] +" direktorijum!"); 
        					else System.out.println("Neuspjesno pravljenje direktorijuma!"); 
                    	}	//root/dir1/dir2
                    	if(temppath.length==3) System.out.println("Ne mozete kreirati direktorijum na toj dubini!!!"); 
                    }
                  }
                        break;
                    case "create": // kreiranje nove datoteke na zadatoj putanji,
                    	  if (!(words.length>2))
                          {			// pogresan unos 
                              System.out.println(Warning);
                           } else
                          {
                      DirectoryClass object=new DirectoryClass(words[2],(byte)1);
                      String temppath[]=words[1].split("/"); 
                      if (temppath[0].equals("root")) { 
                      	System.out.println(temppath.length);
                      	if (temppath.length==2) // root/dir1
                      		{ 
                      		if (object.searchDirectoryByName(rootheader, file, temppath[1])>=0) 
                      					{
                       					if(object.create(rootheader, file, words[2], temppath[1])) System.out.println("Uspjesno je napravljen "+ words[2] +" fajl!"); 
                       					else System.out.println("Neuspjesno pravljenje fajla!"); 
                      					}
                      		else System.out.println("Ne postoji: " + temppath[2] + " fajl!"); 
                      		}
                      	else if (temppath.length==1) //root
                      	{ 
           					 
          					if(object.create(rootheader, file, words[2], temppath[0])) System.out.println("Uspjesno je napravljen "+ words[2] +" fajl!"); 
          					else System.out.println("Neuspjesno pravljenje fajla!"); 
                      	}	//root/dir1/dir2
                      	if(temppath.length==3)  
                      	{
                      		if(object.create(rootheader, file, words[2], temppath[2])) System.out.println("Uspjesno je napravljen "+ words[2] +" fajl!"); 
       					else System.out.println("Neuspjesno pravljenje fajla!");  
                      		
                      	}
                      }
                       }
                        break;
                    case "put": // "upload" datoteke sa postojeceg fajl sistema na novi fajl sistem,
                    	 if (!(words.length>2))
                         {			// pogresan unos 
                             System.out.println(Warning);
                          } else
                         {
                     DirectoryClass object=new DirectoryClass(words[2],(byte)1);
                     String temppath[]=words[1].split("/"); 
                     if (temppath[0].equals("root")) { 
                      	if (temppath.length==2) // root/dir1
                     		{ 
                     		if (object.searchDirectoryByName(rootheader, file, temppath[1])>=0) 
                     					{
                      					if(object.put(rootheader, file, words[2], temppath[1])) System.out.println("Uspjesno je napravljen "+ words[2] +" fajl!"); 
                      					else System.out.println("Neuspjesno pravljenje fajla!"); 
                     					}
                     		else System.out.println("Ne postoji: " + temppath[1] + " fajl!"); 
                     		}
                     	else if (temppath.length==1) //root
                     	{ 
          					 
         					if(object.put(rootheader, file, words[2], temppath[0])) System.out.println("Uspjesno je napravljen "+ words[2] +" fajl!"); 
         					else System.out.println("Neuspjesno pravljenje fajla!"); 
                     	}	//root/dir1/dir2
                     	if(temppath.length==3)  
                     	{
                     		if(object.put(rootheader, file, words[2], temppath[2])) System.out.println("Uspjesno je napravljen "+ words[2] +" fajl!"); 
      					else System.out.println("Neuspjesno pravljenje fajla!");  
                     		
                     	}
                     }
                      }
                        break;

                    case "get": // "download" datoteke sa novog fajl sistema na postojeÄ‡i,
                   	 if (!(words.length>2))
                   	 	{			// pogresan unos 
                         System.out.println(Warning);
                   	 	}
                   else
                  {
                  	if (words[2].length()>0) // root/dir1
                 		{ 
                 		if (FileHeader.searchMFTHeadersByName(rootheader, file,words[1])>=0) 
                 		DirectoryClass.get(rootheader, file, words[1],words[2]);
                 		else System.out.println("Ne postoji: " + words[1] + " fajl!"); 
                 		}
                  }
                        break;
                        
                    case "ls": // izlistavanje sadrzaja direktorijuma na zadatoj putanji,
                    	
                   	 if (!(words.length>1))
                	 	{			// pogresan unos 
                      System.out.println(Warning);
                	 	} else {
                        System.out.println("ls je unesen");
                        if(words[1].length()>0) 
                         DirectoryClass.listDirectoriums(rootheader, file, words[1]); 
                	 	}
                         break;

                    case "cp": // kreiranje kopije datoteke na zadatoj putanji,
                        if (!takeInput.matches("^cp [\\w\\/]+ [\\w\\/]+")) // TODO 
                        {
                            System.out.println(Warning);
                        } else
                        {
                            System.out.println("cp je unesen");
                        }
                        break;

                    case "mv":  
                    	 if (!(words.length>2))
                         {			// pogresan unos 
                             System.out.println(Warning);
                          } else
                        {
                            System.out.println("Move je unesen");
                            DirectoryClass.move(rootheader, file, words[1], words[2]);
                        }
                        break;

                    case "rename": // promjena naziva datoteke/direktorijuma,
                    	 if (!(words.length>2))
                         {			// pogresan unos 
                             System.out.println(Warning);
                          } else
                        {
                            System.out.println("Rename je unesen");
                            DirectoryClass object=new DirectoryClass(); 
                            if(object.rename(rootheader, file, words[1], words[2])) System.out.println("Uspjesno ste preimenovali datoteku/fajl!"); 
                            else System.out.println("Ne postoji datoteka ili fajl sa navedenim imenom!"); 
                            
                        }
                        break;

                    case "echo": // upis proizvoljnog tekstualnog sadrÅ¾aja u datoteku,
                    	if (!(words.length>1))
                        {			// pogresan unos 
                            System.out.println(Warning);
                         } else
                        {
                        	 System.out.println("Unesite tekst koji zelite upisati u "+words[1]+" datoteku!"); 
                        	 String temporary;
                        	 temporary=input.nextLine();
                        	 DirectoryClass.echo(rootheader, file, words[1], temporary); 
                        	 
                     }
                        break;

                    case "cat": // prikaz sadrzaja (tekstualne) datoteke,
                    	if (!(words.length>1))
                        {			// pogresan unos 
                            System.out.println(Warning);
                         } else
                        {
                        if (DirectoryClass.cat(rootheader, file, words[1])) System.out.println("Uspjesna komanda cat!");
                        else System.out.println("Neuspjesna komanda cat!"); 
                     }
                        break;

                    case "rm": // brisanje datoteke/direktorijuma uz mogucnost brisanja kompletnog sadrzaja direktorijuma (opcija -r),
                    	if (!(words.length>1))
                	 	{			// pogresan unos 
                      System.out.println(Warning);
                	 	} else {
                        System.out.println("RM je unesen");
                        if(words[1].length()>0) 
                         DirectoryClass.deleteDirectory(rootheader, file, words[1]); 
                	 	}
                        break;

                    case "stat":
                        System.out.println("stat je unesen");
                    	if(words.length==2) 
                    	{
                    	DirectoryClass object=new DirectoryClass(); 
                    	int position=object.searchDirectoryByName(rootheader, file, words[1]); 
                    	file.seek(RootHeader.ROOTHEADERSIZE+position*DirectoryClass.DIRHEADERSIZE); 
                    	object.DirectoryInfo(rootheader, file);
                    	}		// ako ne izaberemo direktorijum, ispisi informacije o fajl sistemu!
                    	else     rootheader.stats();	 
                        break; 
                    case "exit":
                        System.out.println("exit je unesen!");
                        System.out.println("Zdravo!");
                        System.exit(0);
                        break;
                       default:
                        // dodati 'ulazenje' u direktorijume, promjena trenutne pozicije^^
                }

            }
            System.exit(0);
        } catch (Exception e)
        {
        }
    }
    public static boolean CheckDirectoriums(RootHeader p, RandomAccessFile f, String x)
    {
        try
        {
            f.seek(0);
            String filename = "";
            char c;
            StringBuilder sb = new StringBuilder();
            for (int count = p.getNumberOfDirectoriums(); count > 0; count--)
            {
                f.seek(f.getFilePointer() + 102);
                if (count < 0 || count == 0) System.out.println("error");
                for (int i = 0; 15 > i; i++)
                {
                    c = f.readChar();
                    System.out.println(count);
                    System.out.println(c);
                    sb.append(c);
                }
                filename = sb.toString();
                System.out.println(filename);
                if (x.compareTo(filename) == 0) return true;
            }

        } catch (Exception e)
        {
        }
        return false;
    }

    
    public static RootHeader firstStart(RandomAccessFile x, Scanner s)
    {
         String name;
        do
        {
            System.out.println("Unesite ime novog fajl sistema!");
            name = s.nextLine();
        } while (name.length() > 14);

          RootHeader p = new RootHeader(name);
        System.out.println("Uspjesno smo kreirali novi fajl sistem! ");
        System.out.println("Mozete poceti manipulisati sa fajl sistemom.");
        p.writeFileHeader();
        return p;
    }
    public static boolean checkExistance(RandomAccessFile x, Scanner input)
    {
        try
        {
            x.setLength(SYSTEMSIZE);
            String f = " ";
            do
            {
                System.out.println("Fajl sistem vec postoji, da li zelite kreirati novi?");
                System.out.println("Za koristenje starog unesite 'n'  Za kreiranje novog 'y'");
                f = input.nextLine();

            } while (!"y".equals(f) && !"n".equals(f));
            if ("y".equals(f))
            {
                return true;
            }
            if ("n".equals(f))
            {
                return false;
            }
        } catch (Exception e)
        {
        }
        return false;
    }
 
}
