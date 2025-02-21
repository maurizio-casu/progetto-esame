package pkg1Progetto;

import java.util.ArrayList;
import java.util.List;

public class Globals {
    /*la classe Globals contiene solamente le Liste statiche globali in cui vengono inseriti i dati letti da file(dispensa e menù)
    e generati dell'applicazione(le ordinazioni che vengono effetuate dall'utente)*/

    public static List<Ingrediente> dispensa;
    public static List<Piatto> menu;
    public static List<Ordinazione> ordinazioni_effettuate=new ArrayList<>();
}
