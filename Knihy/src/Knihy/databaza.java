package Knihy;

import java.beans.Statement;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class databaza {
	private ArrayList<kniha> books; 

    public databaza(int pocetPrvkov) {
        prvok = new kniha[pocetPrvkov];
        sc = new Scanner(System.in);
        poslednaKniha = 0;
        books = new ArrayList<>();
    }

    public void loadFromDatabase() {
        try {
            Connection connection = dbconnect.getDBConnection();

            String query = "SELECT * FROM Books";

            Statement statement = (Statement) connection.createStatement();
            ResultSet resultSet = ((java.sql.Statement) statement).executeQuery(query);

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                int year = resultSet.getInt("year");
                boolean available = resultSet.getBoolean("available");

                String genreQuery = "SELECT genre_name FROM Genres WHERE book_title = ?";
                PreparedStatement genreStatement = connection.prepareStatement(genreQuery);
                genreStatement.setString(1, title);
                ResultSet genreResult = genreStatement.executeQuery();
                String genre = "";
                if (genreResult.next()) {
                    genre = genreResult.getString("genre_name");
                }

                String gradeQuery = "SELECT grade_name FROM Grades WHERE book_title = ?";
                PreparedStatement gradeStatement = connection.prepareStatement(gradeQuery);
                gradeStatement.setString(1, title);
                ResultSet gradeResult = gradeStatement.executeQuery();
                String grade = "";
                if (gradeResult.next()) {
                    grade = gradeResult.getString("grade_name");
                }

                if (!genre.isEmpty()) {
                    books.add(new kniha(title, author, year, available, genre));
                } else if (!grade.isEmpty()) {
                    books.add(new kniha(title, author, year, available, grade));
                }
            }

            dbconnect.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Chyba pri načítaní údajov z databázy.");
        }
    }
	
	
	

	public void vypisKnih() {
		
	    kniha[] kopiaDatabazy = Arrays.copyOf(prvok, poslednaKniha);
	    
	    
	    Arrays.sort(kopiaDatabazy, (k1, k2) -> k1.getNazov().compareToIgnoreCase(k2.getNazov()));

	    
	    for (int i = 0; i < kopiaDatabazy.length; i++) {
	        if (kopiaDatabazy[i] != null) {
	            System.out.println(
	                "Nazov: " + kopiaDatabazy[i].getNazov() +
	                "\t Autor: " + kopiaDatabazy[i].getAutor() +
	                "\t Rok vydania: " + kopiaDatabazy[i].getRokVydania() +
	                "\t Typ: " + kopiaDatabazy[i].getTyp() + " - " + kopiaDatabazy[i].getZanerRocnik() +
	                "\t Dostupne: " + kopiaDatabazy[i].getDostupne()
	            );
	        } else {
	            System.out.println("Neexistujú žiadne známe knihy");
	        }
	    }
	}
	
	
	
	
	

    private void zvetsitPole() {
        kniha[] novePole = new kniha[prvok.length * 2];
        for (int i = 0; i < prvok.length; i++) {
            novePole[i] = prvok[i];
        }
        prvok = novePole;
    }
    
    
    
    
    
    

    public void setKniha(Scanner sc) {
        if (poslednaKniha >= prvok.length) {
            zvetsitPole();
        }

        System.out.println("NÁZOV KNIHY: ");
        String nazov = sc.next();
        
        System.out.println("AUTOR: ");
        String autor = sc.next();
        
        System.out.println("ROK VYDANIA: ");
        int rokVydania = test.pouzeCelaCisla(sc);
        
        System.out.println("TYP (ucebnica, roman): ");
        String typ = sc.next();
        
        if (!typ.equals("roman") && !typ.equals("ucebnica")) {
            System.out.println("TENTO TYP KNIHY NEEXISTUJE - ZADAJTE ZNOVA: ");
            setKniha(sc);
            return;
        }
        
        
        String zaner = "";
        if (typ.equals("roman")) {
            System.out.println("Zvoľte žáner: (biograficky, socialny, fantasy, vojnovy, historicky)");
            zaner = sc.next();
            if (!zaner.equals("biograficky") && !zaner.equals("socialny") && !zaner.equals("fantasy") && !zaner.equals("vojnovy") && !zaner.equals("historicky")) {
                System.out.println("TENTO ŽÁNER NEEXISTUJE - ZADAJTE ZNOVA: ");
                setKniha(sc);
                return;
            }
            
            
        } else if (typ.equals("ucebnica")) {
            System.out.println("Zvoľte určnie knihy pre ročník: (1, 2, 3, 4, 5, 6, 7, 8, 9, SS, VS)");
            zaner = sc.next();
            if (!zaner.equals("1") && !zaner.equals("2") && !zaner.equals("3") && !zaner.equals("4") && !zaner.equals("5") && !zaner.equals("6") && !zaner.equals("7") && !zaner.equals("8") && !zaner.equals("9") && !zaner.equals("SS") && !zaner.equals("VS")) {
                System.out.println("TENTO ROČNÍK NEEXISTUJE - ZADAJTE ZNOVA: ");
                setKniha(sc);
                return;
            }
        }
        
        boolean dostupnost = true;
        prvok[poslednaKniha++] = new kniha(nazov, autor, rokVydania, typ, zaner, dostupnost);
    }

    
    
    
    
    
    
    
    
    public void upravitKniha(Scanner sc) {
        System.out.println("NÁZOV KNIHY KTORÚ CHCETE UPRAVIŤ: ");
        String nazov = sc.next();
        
        
        for (int i = 0; i < poslednaKniha; i++) {
            if (prvok[i] != null && prvok[i].getNazov().equals(nazov)) {
                System.out.println("KTORÝ PARAMETER SA BUDE MENIŤ:");
                System.out.println("1) Meno autora");
                System.out.println("2) Rok vydania");
                int volba = test.pouzeCelaCisla(sc);
                
                
                switch (volba) {
                    case 1:
                        System.out.println("NOVÉ MENO AUTORA: ");
                        prvok[i] = new kniha(nazov, sc.next(), prvok[i].getRokVydania(), prvok[i].getTyp(), prvok[i].getZanerRocnik(), prvok[i].getDostupne());
                        System.out.println("Autor bol zmenený");
                        break;
                    case 2:
                        System.out.println("NOVÝ ROK VYDANIA: ");
                        prvok[i] = new kniha(nazov, prvok[i].getAutor(), test.pouzeCelaCisla(sc), prvok[i].getTyp(), prvok[i].getZanerRocnik(), prvok[i].getDostupne());
                        System.out.println("Rok vydania bol zmenený.");
                        break;
                    default:
                        System.out.println("TÁTO VOĽBA NEEXISTUJE");
                }
                return;
            }
        }
        System.out.println("TÁTO KNIHA NEEXISTUJE - SKÚSTE ZNOVA");
    }

    
    
    
    
    
    
    
    
    public void zmazanieKniha(Scanner sc) {
        System.out.println("NÁZOV KNIHY KTORÚ CHCETE ODSTRÁNIŤ: ");
        String nazov = sc.next();
        
        
        for (int i = 0; i < prvok.length; i++) {
            if (prvok[i].getNazov().equals(nazov)) {
                prvok[i] = null;
                System.out.println("KNIHA BOLA ODSTRÁNENÁ");
                return;
            }
        }
        
        System.out.println("TÁTO KNIHA NEEXISTUJE - SKÚSTE ZNOVA");
    }

    
    
    
    
    
    
    
    public void vypozicanie(Scanner sc) {
        System.out.println("NÁZOV KNIHY KTOREJ CHCETE ZMENIŤ DOSTUPNOSŤ: ");
        String nazov = sc.next();
        
        
        
        for (int i = 0; i < prvok.length; i++) {
            if (prvok[i].getNazov().equals(nazov)) {
                System.out.println("ZVOĽTE DOSTUPNOSŤ:");
                System.out.println("1) Zapožičaná");
                System.out.println("2) Dostupná");
                int volba = test.pouzeCelaCisla(sc);
                
                
                switch (volba) {
                    case 1:
                        prvok[i] = new kniha(nazov, prvok[i].getAutor(), prvok[i].getRokVydania(), prvok[i].getTyp(), prvok[i].getZanerRocnik(), false);
                        System.out.println("\nStav dostupnosti knihy - zapožičaná");
                        break;
                    case 2:
                        prvok[i] = new kniha(nazov, prvok[i].getAutor(), prvok[i].getRokVydania(), prvok[i].getTyp(), prvok[i].getZanerRocnik(), true);
                        System.out.println("\nStav dostupnosti knihy - dostupná");
                        break;
                    default:
                        System.out.println("TÁTO VOĽBA NEEXISTUJE - SKÚSTE ZNOVA");
                }
                return;
            }
        }
        
        System.out.println("TÁTO KNIHA NEEXISTUJE - SKÚSTE ZNOVA");
    }

    
    
    
    
    
    
    public void vyhladavanie(Scanner sc) {
        System.out.println("NÁZOV HĽADANEJ KNIHY: ");
        String nazov = sc.next();
        
        
        
        for (int i = 0; i < prvok.length; i++) {
            if (prvok[i].getNazov().equals(nazov)) {
                System.out.println(
                        "Nazov: " + prvok[i].getNazov() +
                                "\t Autor: " + prvok[i].getAutor() +
                                "\t Rok vydania: " + prvok[i].getRokVydania() +
                                "\t Typ: " + prvok[i].getTyp() + " - " + prvok[i].getZanerRocnik() +
                                "\t Dostupne: " + prvok[i].getDostupne());
                return;
            }
        }
        
        System.out.println("TÁTO KNIHA NEEXISTUJE - SKÚSTE ZNOVA");
    }

    
    
    
    
    
    
    
    public void vypisKnihAutor(Scanner sc) {
    	System.out.println("MENO AUTORA HĽADANÝCH KNÍH: ");
        String autor = sc.next();
        
        
        ArrayList<kniha> knihyAutora = new ArrayList<>();
        for (int i = 0; i < poslednaKniha; i++) {
            if (prvok[i] != null && prvok[i].getAutor().equals(autor)) {
                knihyAutora.add(prvok[i]);
            }
        }        
        
        
        knihyAutora.sort(Comparator.comparingInt(kniha::getRokVydania));
        
        

       
        for (kniha kniha : knihyAutora) {
            System.out.println(
                "Nazov: " + kniha.getNazov() +
                "\t Autor: " + kniha.getAutor() +
                "\t Rok vydania: " + kniha.getRokVydania() +
                "\t Typ: " + kniha.getTyp() + " - " + kniha.getZanerRocnik() +
                "\t Dostupne: " + kniha.getDostupne()
            );
        }
    }
    
    
    
    
    
    
    
    

    public void vypisKnihZaner(Scanner sc) {
        System.out.println("ŽÁNER KNÍH: ");
        String zanr = sc.next();
        
        
        for (int i = 0; i < poslednaKniha; i++) {
            if (prvok[i] != null && prvok[i].getZanerRocnik().equals(zanr)) {
                System.out.println(
                        "Nazov: " + prvok[i].getNazov() +
                                "\t Autor: " + prvok[i].getAutor() +
                                "\t Rok vydania: " + prvok[i].getRokVydania() +
                                "\t Typ: " + prvok[i].getTyp() + " - " + prvok[i].getZanerRocnik() +
                                "\t Dostupne: " + prvok[i].getDostupne());
            }
        }
    }
    
    
    
    
    
    
    
    
    
    
    public void vypisZapozicane() {
    	
        boolean existujeVypujcenaKniha = false;
        System.out.println("Zapožičané knihy:");
        
        
        for (int i = 0; i < poslednaKniha; i++) {
            if (prvok[i] != null && !prvok[i].getDostupne()) {
                existujeVypujcenaKniha = true;
                System.out.println(
                        "Nazov: " + prvok[i].getNazov() +
                                "\t Autor: " + prvok[i].getAutor() +
                                "\t Typ: " + prvok[i].getTyp() + " - " + prvok[i].getZanerRocnik()
                );
            }
        }
        if (!existujeVypujcenaKniha) {
            System.out.println("Všetky knihy sú dostupné");
        }
    }
    
    
    
    
    
    
    

    public void zapisDoSuboru(Scanner sc) {
        System.out.println("NÁZOV KNIHY KTORÁ MÁ BYŤ ULOŽENÁ DO SÚBORU: ");
        String nazov = sc.next();
        
        
        for (int i = 0; i < prvok.length; i++) {
            if (prvok[i].getNazov().equals(nazov)) {
                File vystupniSoubor = new File("vystup.txt");
                try (FileWriter fw = new FileWriter(vystupniSoubor)) {
                    fw.write(
                            "Nazov: " + prvok[i].getNazov() +
                                    "\t Autor: " + prvok[i].getAutor() +
                                    "\t Rok vydania: " + prvok[i].getRokVydania() +
                                    "\t Typ: " + prvok[i].getTyp() + " - " + prvok[i].getZanerRocnik() +
                                    "\t Dostupne: " + prvok[i].getDostupne());
                    System.out.println("Uloženie do súboru bolo úspešné");
                } catch (IOException e) {
                	
                    System.out.println("Súbor " + vystupniSoubor + "nie je možné spustiť");
                }
                return;
            }
        }
        System.out.println("TÁTO KNIHA NEEXISTUJE - SKÚSTE ZNOVA");
    }
    
    
    
    
    

    public void VypisZoSuboru(Scanner sc) {
        System.out.println("STLAČTE ENTER: ");
        String nazov = sc.next();
        for (int i = 0; i < prvok.length; i++) {
            if (prvok[i].getNazov().equals(nazov)) {
                try {
                    String celyText = "";
                    FileReader fr = new FileReader("vystup.txt");
                    BufferedReader in = new BufferedReader(fr);
                    String radek;
                    while ((radek = in.readLine()) != null) {
                        System.out.println(".řádek: " + radek);
                        celyText = new String(celyText + radek + "\n");
                    }
                    System.out.print("\nOBSAH\n" + celyText);
                    fr.close();
                    in.close();
                    return;
                } catch (IOException e) {
                    System.out.println("Súbor nie je možné otvoriť");
                }
            }
        }
        System.out.println("TÁTO KNIHA NEEXISTUJE - SKÚSTE ZNOVA");
    }
    
    public void writeToDatabase() {
        try {
            Connection connection = dbconnect.getDBConnection();

            String query = "INSERT INTO Knihy (Nazov_knihy, Rok_vydani, Stav_dostupnosti, Typ_knihy) VALUES (?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(query);
            for (kniha book : books) {
                statement.setString(1, book.getNazov());
                statement.setInt(2, book.getRokVydania());
                statement.setString(3, book.getDostupne() ? "dostupna" : "nezapozicana");
                statement.setString(4, book.getTyp());
                statement.executeUpdate();
            }

            dbconnect.closeConnection();
            System.out.println("Data boli úspešne uložené do databázy.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Chyba pri ukladaní údajov do databázy.");
        }
    }
    
    
    
    

    private Scanner sc;
    private kniha[] prvok;
    private int poslednaKniha;
}
