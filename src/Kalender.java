import java.util.*;
import java.io.*;

public class Kalender
{
    String[][] termine = new String[31][24];
    Scanner s;
        
    public Kalender() {
        s = new Scanner(System.in);
        for (int i=0; i<31; i++) {
            for (int j=0; j<24; j++) {
                termine[i][j] = "-";
            }
        }
        
        while (true) {
            printMenu();
            byte input = s.nextByte();
            if (input < 1 || input > 5) {
                System.out.println("Ungültige Eingabe!");
                continue;
            }
            
            switch (input) {
                case 1:
                    neuerEintrag();
                    break;
                case 2:
                    termineAusgeben();
                    break;
                case 3:
                    load();
                    break;
                case 4:
                    save();
                    break;
                case 5:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Ungültige Eingabe!");
            }
        }
    }
    
    public void printMenu() {
        clearScreen();
        System.out.println("-------------------");
        System.out.println("1 = Neuer eintrag");
        System.out.println("2 = Termine ausgeben");
        System.out.println("3 = Laden");
        System.out.println("4 = Speichern");
        System.out.println("5 = Beenden");
        System.out.println("-------------------\n");
    }
    
    public void neuerEintrag() {
        System.out.println("Geben Sie den Tag ein:");
        byte day = s.nextByte();
        if (day < 1 || day > 31) {
            System.out.println("Ungültige Eingabe!");
            return;
        }
        
        System.out.println("Geben Sie die Stunde an:");
        byte hour = s.nextByte();
        if (hour < 0 || hour > 23) {
            System.out.println("Ungültige Eingabe!");
            return;
        }
        
        s.nextLine(); // leck mich am arsch es funktioniert halt
        System.out.println("Geben Sie den Name ein:");
        String name = s.nextLine();
        
        termine[day-1][hour] = name;
        System.out.println("Termin erfolgreich hinzugefügt!");
    }
    
    public void termineAusgeben() {
        System.out.println("Geben Sie den Tag ein:");
        byte day = s.nextByte();
        if (day < 1 || day > 31) {
            System.out.println("Ungültige Eingabe!");
            return;
        }
        
        for (int i=0; i<24; i++) {
            System.out.println(i+":00 Uhr " + termine[day-1][i]);
        }
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /** Read the object from Base64 string. */
    private static Object fromString( String s ) throws IOException ,
            ClassNotFoundException {
        byte [] data = Base64.getDecoder().decode( s );
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(  data ) );
        Object o  = ois.readObject();
        ois.close();
        return o;
    }

    /** Write the object to a Base64 string. */
    private static String toString( Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public void save() {
        try {
            FileWriter myWriter = new FileWriter("termine.txt");
//            for (int day = 0; day<termine.length; day++) {
//                for (int hour=0; hour<termine[day].length; hour++) {
//                    String n = termine[day][hour];
//                    if (Objects.equals(n, "-")) {
//                        continue;
//                    }
//                    myWriter.append((day+1)+":"+hour+":"+termine[day][hour]+"\n");
//                }
//            }
            myWriter.write(toString(termine));
            myWriter.close();
            System.out.println("Erfolgreich gespeichert!");
        } catch (IOException e) {
            System.out.println("Ein Fehler ist aufgetreten!");
            e.printStackTrace();
        }   
    }
    
    public void load() {
        try {
            File myObj = new File("termine.txt");
            StringBuilder content = new StringBuilder();
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                content.append(data);
//
//                String[] parts = data.split(":");
//                int day = Integer.parseInt(parts[0]);
//                int hour = Integer.parseInt(parts[1]);
//                String name = String.join(":", Arrays.copyOfRange(parts, 2, parts.length));
//                termine[day-1][hour] = name;
            }
            termine = (String[][]) fromString(content.toString());
            myReader.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new Kalender();
    }
}
