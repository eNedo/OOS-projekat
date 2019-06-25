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
        {
            byte GLOBALROOT = 0;
            int DIR_POSITION_OF_CMD = 1;
            final String Warning = "Pogresan broj argumenata!";
            String takeInput = "";
            String DIRposition = "root";
            String Parent = "root";
            while (input != null && !("exit".equals(takeInput)))
            {
                System.out.print(DIRposition + ":");

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
                            //byte tmp[] = new byte[64];
                           // byte temp2 = 1;
                            //for (int i = 0; 64 > i; i++)
                             //   tmp[i] = 1;
                           // FileHeader novifajl = new FileHeader(words[1], 64, 0, 0, temp2, tmp);
                            //System.out.println(novifajl.getNameOfFile(f).length());
                            //novifajl.writeFiletoMFTheader(rootheader, file);
                            //System.out.println("uspjeh ili ne" + novifajl.searchMFTfiles(rootheader, file, words[1]));
                            //String in=input.nextLine();
                            //if (in.equals("da")) novifajl.deleteMFTFile(rootheader, file, words[1]);
                         
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
                        DirectoryClass dir = new DirectoryClass(" ", (byte) 0);
                        // dir.listDirectory(f, p);
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

                    case "stat":
                        System.out.println("stat je unesen");
                        rootheader.stats();
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
                    case "cd":
                        // putanja ka direktorijumu
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

    public static RootHeader firstStart(RandomAccessFile x, Scanner s)
    {
        System.out.println("ojsaaa");
        String name;
        do
        {
            System.out.println("Unesite ime novog fajl sistema!");
            name = s.nextLine();
        } while (name.length() > 14);

          RootHeader p = new RootHeader(name);
        System.out.println("Uspjesno smo kreirali novi fajl sistem! ");
        System.out.println("Mozete poceti manipulisati sa fajl sistemom.");
        return p;
    }

}
