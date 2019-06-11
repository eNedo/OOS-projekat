import java.io.*;
import java.util.Scanner;

public class MainClass
{
    private static final long SYSTEMSIZE = 1024 * 1024 * 20;

    public static final long ONEMB = 1024 * 1024;


    public static String FileSystemPath = "./FILESYSTEM";

    //
    public static void main(String args[]) throws IOException
    {
            checkIfOSexists();  // provjeri da li OS vec postoji sacuvan

        //getUserCommand();

        FileHeader fileHeader = new FileHeader("MyOS");
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(FileSystemPath, "rw"))
        {
            randomAccessFile.seek(ONEMB);
            DataSegment.firstFreeSpace("test");



        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(FileSystemPath, "rw"))
        {
            //  randomAccessFile.writeUTF("hello"); randomAccessFile.writeUTF("my friend");
            //  treba se pozicionirati (seek) za ''7'' mjesta da bi se ocitao string "my frined"

            //randomAccessFile.seek(ONEMB - FileClass.FILESIZE + 7);


        } catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }

    private static void getUserCommand()
    {
        final String WrongNumOfArguments = "Wrong Number of Arguments";
        Scanner input = new Scanner(System.in);
        String takeInput = "";

        while (input != null && !("exit".equals(takeInput)))
        {
            System.out.print("enter command: ");

            takeInput = input.nextLine();
            String words[] = takeInput.split(" ");  // napravi niz rijeci od unjetog teksta

            switch (words[0])
            {
                case "mkdir":   // kreiranje novog direktorijuma na zadatoj putanji,
                    if (!takeInput.matches("(^mkdir (\"[\\w ]+\"$))|(^mkdir (\\w+$))"))
                    {
                        System.out.println("wrong number of arguments");
                        break;
                    } else
                    {
                        System.out.println("mkdir is entered");
                    }

                    break;

                case "create": // kreiranje nove datoteke na zadatoj putanji,
                    if (!takeInput.matches("^create [\\w\\/]*\\w+\\.\\w{1,4}"))
                    {
                        System.out.println("wrong number of arguments");
                        break;
                    } else
                    {
                        System.out.println("create is entered");
                    }
                    break;

                case "put": // "upload" datoteke sa postojeceg fajl sistema na novi fajl sistem,
                    if (!takeInput.matches("^put [\\w\\/]+ [\\w\\/]+")) // TODO: mozda ograniciti ime datoteke
                    {
                        System.out.println(WrongNumOfArguments);
                    } else
                    {
                        System.out.println("put is entered");
                    }
                    break;

                case "get": // "download" datoteke sa novog fajl sistema na postojeÄ‡i,
                    if (!takeInput.matches("^get [\\w\\/]+ [\\w\\/]+")) // TODO: mozda ograniciti ime datoteke
                    {
                        System.out.println(WrongNumOfArguments);
                    } else
                    {
                        System.out.println("get is entered");
                    }
                    break;

                case "ls": // izlistavanje sadrzaja direktorijuma na zadatoj putanji,
                    System.out.println("ls is entered");
                    break;

                case "cp": // kreiranje kopije datoteke na zadatoj putanji,
                    if (!takeInput.matches("^cp [\\w\\/]+ [\\w\\/]+")) // TODO: mozda ograniciti ime datoteke
                    {
                        System.out.println(WrongNumOfArguments);
                    } else
                    {
                        System.out.println("cp is entered");
                    }
                    break;

                case "mv": // premjeÅ¡tanje datoteke iz izvornog u odrediÅ¡ni direktorijum,
                    if (!takeInput.matches("^mv \\w+ \\w+")) // TODO: mozda ograniciti ime datoteke
                    {
                        System.out.println(WrongNumOfArguments);
                    } else
                    {
                        System.out.println("mv is entered");
                    }
                    break;

                case "rename": // promjena naziva datoteke/direktorijuma,
                    if (!takeInput.matches("^rename \\w+ \\w+")) // TODO: mozda ograniciti ime datoteke
                    {
                        System.out.println(WrongNumOfArguments);
                    } else
                    {
                        System.out.println("rename is entered");
                    }
                    break;

                case "echo": // upis proizvoljnog tekstualnog sadrÅ¾aja u datoteku,
                    if (!takeInput.matches("^echo (\"?)[\\w\\/ ]+(\\1) > \\w+\\.\\w{1,4}")) // TODO: mozda ograniciti ime datoteke
                    {
                        System.out.println(WrongNumOfArguments);
                    } else
                    {
                        System.out.println("echo is entered");
                    }
                    break;

                case "cat": // prikaz sadrzaja (tekstualne) datoteke,
                    if (!takeInput.matches("^cat [\\w\\/]+\\.\\w{1,4}"))
                    {
                        System.out.println(WrongNumOfArguments);
                    } else
                    {
                        System.out.println("cat is entered");
                    }
                    break;

                case "rm": // brisanje datoteke/direktorijuma uz mogucnost brisanja kompletnog sadrzaja direktorijuma (opcija -r),
                    if (!takeInput.matches("^rm [\\w\\/]+\\.\\w{1,4}"))
                    {
                        System.out.println(WrongNumOfArguments);

                    } else
                    {
                        System.out.println("rm is entered");
                    }
                    break;

                case "stat": // prikaz informacija o datoteci, ukljucujuci informacije o MFT zapisu (NTFS), lokacijij svih blokova koji cine sadrzaj datoteke.
                    System.out.println("stat is entered");
                    break;

                case "ln": // ln - make links between files (OPCIONO)
                    if (!takeInput.matches("^ln [\\w\\/]+ [\\w\\/]+")) // TODO: mozda ograniciti ime datoteke
                    {
                        System.out.println(WrongNumOfArguments);
                    } else
                    {
                        System.out.println("ln is entered");
                    }
                    break;

                case "grep": // ovo je bas tesko napraviti (OPCIONO)
                    System.out.println("grep is entered");
                    break;

                case "exit":
                    System.out.println("exit is entered");
                    break;
                default:
                    System.out.println("unknown command!");
            }

        }
        System.exit(0);

    }


    public static void checkIfOSexists()
    {
        File myOS = new File(FileSystemPath);

        if (!myOS.exists())
        {
            try
            {
                myOS.createNewFile();
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        } else
        {
            String input = "";
            do
            {
                System.out.println("OS already exist! Do you wanna create a new one? y/n");
                input = (new Scanner(System.in)).nextLine();

            } while (!"y".equals(input) && !"n".equals(input));
            if ("y".equals(input))
            {
                try
                {
                    myOS.delete();
                    myOS.createNewFile();

                   // initializeFlags();  //TODO: postavi sve potrebne flagove

                    // TODO: treba ga popuniti novim sadrzajem
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }


        }

    }

    public static void initializeFlags()
    {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(FileSystemPath, "rw"))
        {
//            randomAccessFile.seek(ONEMB);   // flag za pocetak dataSegmenta
//            randomAccessFile.writeByte(Byte.MAX_VALUE);

        }catch(IOException ex)
        {
            ex.printStackTrace();
        }

    }


    public static void firstStart(RandomAccessFile x)
    {
        String name;
        Scanner s = new Scanner(System.in);
        do
        {
            System.out.println("Unesite ime novog fajl sistema!");
            name = s.nextLine();
        } while (name.length() > 15);

        while (name.length() <= 15)
            name += " ";

        FileHeader p = new FileHeader(name);
        p.writeToFile(x);
    }
}
