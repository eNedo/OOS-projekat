import java.io.*;
import java.util.Scanner;

public class MainClass
{
    private static long FILESIZE = 1024 * 1024 * 20;

    public static void main(String args[])
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

        try
        {
            File f = new File("./FILESYSTEM");
            FileOutputStream OUT = new FileOutputStream("./FILESYSTEM");
            FileInputStream IN = new FileInputStream("./FILESYSTEM");
            //            x.setLength(FILESIZE);
            //            //if (f.exists())
            //            firstStart(x);
            //            //fajl nije postojao u sistemu, kreiraj novi i upisi sve potrebno
            //
            //            x.close();
        } catch (FileNotFoundException e)
        {
        } catch (IOException f)
        {
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
        }
        while (name.length() > 15);
        while (name.length() <= 15)
            name += " ";

        FILEHeader p = new FILEHeader(name);
        p.write(x);
    }
}
