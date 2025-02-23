package pkg1Progetto;

import java.io.Serializable;
import java.util.ArrayList;


public class Piatto implements  Comparable<Piatto>, Serializable {
    /*La classe piatto rappresenta un generico piatto presente sul menù.
     *un piatto è definito dalla lista dei suoi ingredienti(e relative quantità necessarie per la preparazione)
     *e dai suoi attributi principali(nome,prezzo,ricetta).
     */

    private final  String nome;
    private final String ricetta;
    private final double  Prezzo_piatto;
    private final ArrayList<Ingrediente> ingredienti_piatto;
    public int[]quantita_ingredienti;

    public Piatto(String nome, String ricetta, double prezzo_piatto, ArrayList<Ingrediente> ingredienti_piatto, int[]quantita_ingredienti) {
        super();
        this.nome = nome;
        this.ricetta = ricetta;
        this.Prezzo_piatto = prezzo_piatto;
        this.ingredienti_piatto = ingredienti_piatto;
        this.quantita_ingredienti = quantita_ingredienti;
    }

    public boolean isDisponibile(){//verifica la disponibilità del piatto
        for(int i=0;i<ingredienti_piatto.size();i++) {
            if((ingredienti_piatto.get(i).getQuantita()/quantita_ingredienti[i])<1)
                return false;
                }
        return true;
    }

    public int numeroPorzioniDisponibili() {
        int porzioni_disponibili=10000;
        for (int i = 0; i < ingredienti_piatto.size(); i++) {
            int appoggio=ingredienti_piatto.get(i).getQuantita()/quantita_ingredienti[i];
            if(appoggio<1)
                return 0;
            if((appoggio<=porzioni_disponibili))
                porzioni_disponibili=appoggio;
        }
        return porzioni_disponibili;

    }

    public boolean isVegano() {
        for (Ingrediente ingr : ingredienti_piatto){
            if (ingr.getTipo() != Ingrediente.tipo.VEGANO)
                return false;}
            return true;
    }

    public void preparaPiatto(){

        if(isDisponibile()) {
            for (int i = 0; i < ingredienti_piatto.size(); i++)
                ingredienti_piatto.get(i).setQuantita(ingredienti_piatto.get(i).getQuantita() - quantita_ingredienti[i]);

        }
        else
            System.out.println("piatto non disponibile");
    }

    public void aggiungiIngrdiente(Ingrediente newIngrediente){
        ingredienti_piatto.add(newIngrediente);
    }

    @Override
    public int compareTo(Piatto piatto) {
        if(this.Prezzo_piatto>piatto.getPrezzo_piatto())
            return -1;
        else if(this.Prezzo_piatto<piatto.getPrezzo_piatto())
            return 1;
        else
            return 0;
    }

    @Override
    public String toString() {
        return (this.nome +",€ "+this.Prezzo_piatto);
    }

    public String getNome() {
        return nome;
    }

    public String getRicetta() {
        return ricetta;
    }

    public double getPrezzo_piatto() {
        return Prezzo_piatto;
    }

    public ArrayList<Ingrediente> getIngredienti_piatto() {
        return ingredienti_piatto;
    }

    public int[] getQuantita_ingredienti() {
        return quantita_ingredienti;
    }

}
