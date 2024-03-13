import java.io.*;
import java.time.YearMonth;
import java.util.*;

public class KalenderV2 {
    class Appointment {
        private Date date;
        private byte hour;
        private String name;

        public Appointment(Date date, byte hour, String name) {
            this.date = date;
            this.hour = hour;
            this.name = name;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public byte getHour() {
            return hour;
        }

        public void setHour(byte hour) {
            this.hour = hour;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    HashMap<Date, Appointment> appointments;
    Scanner s;

    public KalenderV2() {
        s = new Scanner(System.in);
        appointments = new HashMap<>();

        while (true) {
            printMenu();
            byte input = s.nextByte();

            switch (input) {
                case 1:
                    newAppointment();
                    break;
                case 2:
                    getAppointment();
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
        System.out.println("\n-------------------");
        System.out.println("1 = Neuer eintrag");
        System.out.println("2 = Termine ausgeben");
        System.out.println("3 = Laden");
        System.out.println("4 = Speichern");
        System.out.println("5 = Beenden");
        System.out.println("-------------------\n");
    }

    public int getInt(int min, int max) {
        // for exit e
        // if exit return -1
        while (true) {
            String input = s.nextLine();
            if (input.equals("e")) {
                return -1;
            }
            try {
                int i = Integer.parseInt(input);
                if ((min != -1 && i < min) || (max != -1 && i > max)) {
                    System.out.println("Ungültige Eingabe! (e zum abbrechen)");
                    continue;
                }
                return i;
            } catch (NumberFormatException e) {
                System.out.println("Ungültige Eingabe! (e zum abbrechen)");
            }
        }
    }

    public void newAppointment() {
        System.out.println("Geben Sie das Jahr ein:");
        s.nextLine();
        int year = getInt(-1, -1);
        if (year == -1) {
            System.out.println("Abgebrochen!");
            return;
        }

        System.out.println("Geben Sie den Monat ein:");
        int month = getInt(1, 12);
        if (month == -1) {
            System.out.println("Abgebrochen!");
            return;
        }

        System.out.println("Geben Sie den Tag ein:");
        int day = getInt(1, getMonthDays(year, month));
        if (day == -1) {
            System.out.println("Abgebrochen!");
            return;
        }

        System.out.println("Geben Sie die Stunde an:");
        int hour = getInt(0, 23);
        if (hour == -1) {
            System.out.println("Abgebrochen!");
            return;
        }

        System.out.println("Geben Sie den Name ein:");
        String name = s.nextLine();

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hour, 0);
        Date date = cal.getTime();

        appointments.put(date, new Appointment(date, (byte) hour, name));
        System.out.println("Termin erfolgreich hinzugefügt!");
    }

    public void getAppointment() {
        System.out.println("Geben Sie das Jahr ein:");
        int year = getInt(-1, -1);
        if (year == -1) {
            System.out.println("Abgebrochen!");
            return;
        }

        System.out.println("Geben Sie den Monat ein:");
        int month = getInt(1, 12);
        if (month == -1) {
            System.out.println("Abgebrochen!");
            return;
        }

        System.out.println("Geben Sie den Tag ein:");
        int day = getInt(1, getMonthDays(year, month));
        if (day == -1) {
            System.out.println("Abgebrochen!");
            return;
        }

        Calendar cal = Calendar.getInstance();
        for (int i=0; i<24; i++) {
            cal.set(year, month, day, i, 0);
            Date date = cal.getTime();
            Appointment appointment = appointments.get(date);

            if (appointment != null) {
                System.out.println(i+":00 Uhr " + appointment.getName());
            }
        }
    }


    public int getMonthDays(int year, int month) {
        YearMonth yM = YearMonth.of(year, month);
        return yM.lengthOfMonth();
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /** Read the object from Base64 string. */
    private static Object fromString( String s ) throws IOException,
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
        ByteArrayOutputStream baStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream( baStream );
        objStream.writeObject( o );
        objStream.close();
        return Base64.getEncoder().encodeToString(baStream.toByteArray());
    }

    public void save() {
        System.out.println("In welcher Datei möchten Sie speichern?");
        s.nextLine();
        String filename = s.nextLine();

        try {
            FileWriter w = new FileWriter(filename);
            w.write(toString(appointments));
            w.close();
            System.out.println("Termine in " + filename + " gespeichert!");
        } catch (IOException e) {
            System.out.println("Ein Fehler ist aufgetreten!");
            e.printStackTrace();
        }
    }

    public void load() {
        System.out.println("Welche Datei möchten Sie laden?");
        s.nextLine();
        String filename = s.nextLine();

        try {
            File myObj = new File(filename);
            StringBuilder content = new StringBuilder();
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                content.append(data);
            }
            appointments = (HashMap<Date, Appointment>) fromString(content.toString());
            myReader.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new KalenderV2();
    }
}
