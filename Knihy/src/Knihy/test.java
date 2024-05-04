package Knihy;

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class test {

    public static int pouzeCelaCisla(Scanner sc) {
    	
    	
        int cislo = 0;
        
        
        
        try {
            cislo = sc.nextInt();
        } catch (Exception e) {
            System.out.println("Vznikla výnimka" + e.toString());
            System.out.println("ZADAJETE CELÉ ČÍSLO");
            sc.nextLine();
            cislo = pouzeCelaCisla(sc);
        }
        return cislo;
    }

    
    
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Scanner sc = new Scanner(System.in);
        databaza mojeDatabaze = new databaza(1);
        int volba;
        boolean run = true;
        while (run) {
        	System.out.println(" ");
            System.out.println("ZVOĽTE FUNKCIU KTORÚ CHCETE POUŽIŤ\n");
            System.out.println("1) Pridanie novej knihy");
            System.out.println("2) Úprava knihy");
            System.out.println("3) Zmazanie knihy");
            System.out.println("4) Označenie knihy ako vypožičaná/vrátená");
            System.out.println("5) Abecedný výpis kníh");
            System.out.println("6) Vyhladanie knihy");
            System.out.println("7) Výpis všetkých knih daného autora v chronologickom poradí");
            System.out.println("8) Výpis všetkých knih podla žánru");
            System.out.println("9) Výpis všetkých vypožičaných kníh:");
            System.out.println("10) Uloženie informacie o vybranej knihe do súboru");
            System.out.println("11) Načítanie informácií o danej knihe zo súboru");
            
            System.out.println("12) Ukončenie a uloženie do databázy");
            volba = pouzeCelaCisla(sc);
            switch (volba) {
                case 1:
                    System.out.println("Pridanie novej knihy:");
                    System.out.println(" ");
                    mojeDatabaze.setKniha(sc);
                    break;
                case 2:
                    System.out.println("Úprava knihy:");
                    System.out.println(" ");
                    mojeDatabaze.upravitKniha(sc);
                    break;
                case 3:
                    System.out.println("Zmazanie knihy:");
                    System.out.println(" ");
                    mojeDatabaze.zmazanieKniha(sc);
                    break;
                case 4:
                    System.out.println("Označenie knihy ako vypožičaná/vrátená:");
                    System.out.println(" ");
                    mojeDatabaze.vypozicanie(sc);
                    break;
                case 5:
                    System.out.println("Výpis kníh:");
                    System.out.println(" ");
                    mojeDatabaze.vypisKnih();
                    break;
                case 6:
                    System.out.println("Vyhladanie knihy:");
                    System.out.println(" ");
                    mojeDatabaze.vyhladavanie(sc);
                    break;
                case 7:
                    System.out.println("Výpis všetkých kníh daného autora v chronologickom poradí");
                    System.out.println(" ");
                    mojeDatabaze.vypisKnihAutor(sc);
                    break;
                case 8:
                    System.out.println("Výpis všetkých knih ktoré patria do konkrétneho žánru");
                    System.out.println(" ");
                    mojeDatabaze.vypisKnihZaner(sc);
                    break;
                case 9:
                    System.out.println("Výpis všetkých vypožičaných kníh:");
                    System.out.println(" ");
                    mojeDatabaze.vypisZapozicane();
                    break;
                case 10:
                    System.out.println("Uloženie informacie o vybranej knihe do súboru");
                    System.out.println(" ");
                    mojeDatabaze.zapisDoSuboru(sc);
                    break;
                case 11:
                    System.out.println("Načítanie informácií o danej knihe zo súboru");
                    System.out.println(" ");
                    mojeDatabaze.VypisZoSuboru(sc);
                    break;                
                case 12:
                	System.out.println("Ukončenie a uloženie do databázy");
                    mojeDatabaze.writeToDatabase();
                    run = false;
                    break;
                	
                    
            }
        }
    }
}