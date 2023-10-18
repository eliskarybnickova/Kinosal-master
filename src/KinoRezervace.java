import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Film {
    // Třída Film s vlastnostmi
    private String nazev; // Název filmu
    private String pristupnost; // Věkové omezení
    private String reziser; // Režisér
    private boolean podpora3D; // Podpora 3D

    public Film(String nazev, String pristupnost, String reziser, boolean podpora3D) {
        this.nazev = nazev;
        this.pristupnost = pristupnost;
        this.reziser = reziser;
        this.podpora3D = podpora3D;
    }

    // Metody pro získání hodnot vlastností filmu
    public String getNazev() {
        return nazev;
    }

    public String getPristupnost() {
        return pristupnost;
    }

    public String getReziser() {
        return reziser;
    }

    public boolean isPodpora3D() {
        return podpora3D;
    }
}

class KinoSal {
    // Třída KinoSal s vlastnostmi
    private int cisloSalu; // Číslo sálu
    private int pocetRad; // Počet řad
    private int pocetKreselVRade; // Počet křesel v řadě
    private List<Film> filmy; // Seznam filmů v sále
    private boolean podpora3D; // Podpora 3D

    public KinoSal(int cisloSalu, int pocetRad, int pocetKreselVRade, boolean podpora3D) {
        this.cisloSalu = cisloSalu;
        this.pocetRad = pocetRad;
        this.pocetKreselVRade = pocetKreselVRade;
        this.filmy = new ArrayList<>();
        this.podpora3D = podpora3D;
    }

    // Metody pro získání hodnot vlastností sálu
    public int getCisloSalu() {
        return cisloSalu;
    }

    public int getPocetRad() {
        return pocetRad;
    }

    public int getPocetKreselVRade() {
        return pocetKreselVRade;
    }

    public List<Film> getFilmy() {
        return filmy;
    }

    public boolean isPodpora3D() {
        return podpora3D;
    }
}

class NeplatnyFilmException extends Exception {
    public NeplatnyFilmException(String message) {
        super(message);
    }
}

class NeplatnySalException extends Exception {
    public NeplatnySalException(String message) {
        super(message);
    }
}

class NeplatneKresloException extends Exception {
    public NeplatneKresloException(String message) {
        super(message);
    }
}

public class KinoRezervace {
    public static void main(String[] args) {
        // Vstupní bod programu

        // Vytvoření filmů
        Film film1 = new Film("Matrix", "PG-13", "Wachowski Brothers", true);
        Film film2 = new Film("Piráti z Karibiku", "PG-13", "Gore Verbinski", false);
        Film film3 = new Film("Avengers: Endgame", "PG-13", "Anthony & Joe Russo", true);

        // Vytvoření kinosalů a přidání filmů
        KinoSal sala1 = new KinoSal(1, 10, 10, true);
        sala1.getFilmy().add(film1);
        sala1.getFilmy().add(film2);
        sala1.getFilmy().add(film3);

        KinoSal sala2 = new KinoSal(2, 8, 12, false);
        sala2.getFilmy().add(film1);
        sala2.getFilmy().add(film2);

        // Seznam dostupných filmů
        List<Film> dostupneFilmy = new ArrayList<>();
        dostupneFilmy.add(film1);
        dostupneFilmy.add(film2);
        dostupneFilmy.add(film3);

        // Inicializace scanneru pro vstup od uživatele
        Scanner scanner = new Scanner(System.in);

        System.out.println("Vítejte v našem kině!");

        System.out.println("Dostupné filmy:");
        int filmIndex = 1;
        for (Film film : dostupneFilmy) {
            System.out.println(filmIndex + ". " + film.getNazev());
            filmIndex++;
        }

        // Výběr filmu od uživatele
        int vybranyFilmIndex = -1;
        Film vybranyFilm = null;

        while (vybranyFilmIndex < 1 || vybranyFilmIndex > dostupneFilmy.size()) {
            System.out.print("Vyberte číslo filmu: ");
            vybranyFilmIndex = scanner.nextInt();

            if (vybranyFilmIndex >= 1 && vybranyFilmIndex <= dostupneFilmy.size()) {
                vybranyFilm = dostupneFilmy.get(vybranyFilmIndex - 1);
            } else {
                System.out.println("Neplatný výběr filmu. Zadejte platné číslo filmu.");
            }
        }

        System.out.println("Dostupné sály pro film \"" + vybranyFilm.getNazev() + "\":");

        // Výpis sálů pro vybraný film
        for (KinoSal sala : new KinoSal[]{sala1, sala2}) {
            if (sala.getFilmy().contains(vybranyFilm)) {
                System.out.println("Sál č. " + sala.getCisloSalu());
            }
        }

        // Výběr sálu od uživatele
        int vybranySalCislo = -1;
        while (vybranySalCislo != 1 && vybranySalCislo != 2) {
            System.out.print("Vyberte číslo sálu: ");
            vybranySalCislo = scanner.nextInt();
        }

        KinoSal vybranySal = (vybranySalCislo == 1) ? sala1 : sala2;

        System.out.println("Rozložení křesel v sálu č. " + vybranySal.getCisloSalu() + ":");

        // Výpis rozložení křesel v sále

        for (int row = 1; row <= vybranySal.getPocetRad(); row++) {
            for (int seat = 1; seat <= vybranySal.getPocetKreselVRade(); seat++) {
                char seatLetter = (char) ('A' + seat - 1);
                System.out.print(seatLetter + String.format("%02d", row) + " ");
            }
            System.out.println();
        }

        String vybraneKreslo = "";
        while (true) {
            System.out.print("Vyberte křeslo (např. A1): ");
            vybraneKreslo = scanner.next();

            try {
                rezervovatKreslo(vybranySal, vybranyFilm, vybraneKreslo);
                System.out.println("Rezervace úspěšně provedena pro film: " + vybranyFilm.getNazev() + " v sálu č. " + vybranySal.getCisloSalu() + ", křeslo: " + vybraneKreslo);
                break;
            } catch (NeplatnyFilmException | NeplatnySalException | NeplatneKresloException e) {
                System.out.println("Chyba: " + e.getMessage());
            }
        }
    }

    /**
     * Metoda pro rezervaci křesla v daném sále.
     *
     * @param sál, ve kterém se provádí rezervace
     * @param kreslo  Křeslo, které se má rezervovat
     * @throws NeplatnyFilmException   Pokud film není v seznamu filmů v sále
     * @throws NeplatnySalException    Pokud sál není platný pro vybraný film
     * @throws NeplatneKresloException Pokud křeslo není platné
     */
    public static void rezervovatKreslo(KinoSal sál, Film vybranyFilm, String kreslo) throws NeplatnyFilmException, NeplatnySalException, NeplatneKresloException {
        if (!sál.getFilmy().contains(vybranyFilm)) {
            throw new NeplatnyFilmException("Tento film není k dispozici v tomto sále.");
        }

        char radka = kreslo.charAt(0);
        int cisloKresla = Integer.parseInt(kreslo.substring(1));

        if (radka < 'A' || radka > 'H' || cisloKresla < 1 || cisloKresla > sál.getPocetKreselVRade()) {
            throw new NeplatneKresloException("Neplatné křeslo.");
        }

        if (jeKresloObsazeno(sál, radka, cisloKresla)) {
            throw new NeplatneKresloException("Křeslo " + kreslo + " je již obsazeno.");
        }

        oznacKresloObsazene(sál, radka, cisloKresla);
    }

    private static boolean jeKresloObsazeno(KinoSal sal, char radka, int cisloKresla) {
        // Tato metoda by měla ověřit, zda je křeslo obsazeno.
        // Implementaci této metody by bylo třeba dodat.
        return false;
    }

    private static void oznacKresloObsazene(KinoSal sal, char radka, int cisloKresla) {
        // Tato metoda by měla označit křeslo jako obsazeno.
        // Implementaci této metody by bylo třeba dodat.
    }
}
