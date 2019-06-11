import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class MainClass
{
    private static long FILESIZE = 1024 * 1024 * 20;

    public static long ONEMB= 1024*1024;


    public static String FileSystemPath= "./FILESYSTEM.txt";

    public static void main(String args[]) throws IOException
    {
        checkIfOSexists();  // provjeri da li OS vec postoji sacuvan

       // getUserCommand();

        FileHeader fileHeader=new FileHeader("MyOS");
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(FileSystemPath,"rw"))
        {
            fileHeader.writeToFile(randomAccessFile);

            FileClass newFile = new FileClass("filip");


        }catch(Exception ex)
        {
            ex.printStackTrace();
        }



    }

    private static void getUserCommand()
    {
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
                    System.out.println("mkdir is entered");
                    break;

                case "create": // kreiranje nove datoteke na zadatoj putanji,
                    System.out.println("create is entered");
                    break;

                case "put": // „upload“ datoteke sa postojećeg fajl sistema na novi fajl sistem,
                    System.out.println("put is entered");
                    break;

                case "get": // „download“ datoteke sa novog fajl sistema na postojeći,
                    System.out.println("get is entered");
                    break;

                case "ls": // izlistavanje sadržaja direktorijuma na zadatoj putanji,
                    System.out.println("ls is entered");
                    break;

                case "cp": // kreiranje kopije datoteke na zadatoj putanji,
                    System.out.println("cp is entered");
                    break;

                case "mv": // premještanje datoteke iz izvornog u odredišni direktorijum,
                    System.out.println("mv is entered");
                    break;

                case "rename": // promjena naziva datoteke/direktorijuma,
                    System.out.println("rename is entered");
                    break;

                case "echo": // upis proizvoljnog tekstualnog sadržaja u datoteku,
                    System.out.println("echo is entered");
                    break;

                case "cat": // prikaz sadržaja (tekstualne) datoteke,
                    System.out.println("cat is entered");
                    break;

                case "rm": // brisanje datoteke/direktorijuma uz mogućnost brisanja kompletnog sadržaja direktorijuma (opcija -r),
                    System.out.println("rm is entered");
                    break;

                case "stat": // prikaz informacija o datoteci, uključujući informacije o i-čvoru (ext4), MFT zapisu (NTFS), lokacijij svih blokova koji čine sadržaj datoteke.
                    System.out.println("stat is entered");
                    break;

                case "ln": // ln - make links between files (OPCIONO)
                    System.out.println("ln is entered");
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

        if(!myOS.exists())
        {
            try
            {
                myOS.createNewFile();
            }catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }else
        {
            String input="";
            do
            {
                System.out.println("OS already exist! Do you wanna create a new one? y/n");
                input = (new Scanner(System.in)).nextLine();

            }while(!"y".equals(input) && !"n".equals(input));
            if("y".equals(input))
            {
                try
                {
                    myOS.delete();
                    myOS.createNewFile();

                    // TODO: treba ga popuniti novim sadrzajem
                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }


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
        }while (name.length() > 15);

        while (name.length() <= 15)
            name += " ";

        FileHeader p = new FileHeader(name);
        p.writeToFile(x);
    }
}
