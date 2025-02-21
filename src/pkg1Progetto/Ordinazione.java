package pkg1Progetto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Ordinazione implements Serializable {
/*Un' ordinazione consiste fondamentalmente in una collezione di piatti.
* i metodi implementati forniscono le principali funzionalità connesse:l'aggiunta di piatti all' ordinazione,
* prezzo complessivo, un riepilogo dell' ordinazione(piatti presenti e prezzo complessivo),invio effettivo dell' ordine
* (nel programma esso consiste solo nella preparazione dei piatti richiesti e conseguente decremento delle quantità degli ingredienti utilizzati.
*/
    private  ArrayList<Piatto> piatti=new ArrayList<>();

    public Ordinazione() {
        super();
    }

    public boolean aggiungi_piatto(String nuovo_piatto, int quanti) {
        for (Piatto piatto : Globals.menu) {
            if (piatto.getNome().equals(nuovo_piatto)) {
                int gia_aggiunti=0;
                for (Piatto current_piatto : piatti) {
                    if (nuovo_piatto.equals(current_piatto.getNome()))
                        gia_aggiunti++;
                }
                if((piatto.numeroPorzioniDisponibili()-gia_aggiunti)>=quanti){
                    for(int i=0;i<quanti;i++)
                        this.piatti.add(piatto);
                    return true;
                }
                else {
                    System.out.println("piatto non aggiunto all'ordinazione...quantità di piatti:"+piatto.getNome()+
                            " ancora disponibili:"+(piatto.numeroPorzioniDisponibili()-gia_aggiunti));
                    return false;
                }
            }
        }
        System.out.println("il piatto selezionato non è presente sul menù...");
        return false;
    }

    public void riepilogo(){
        for(Piatto piatto:this.piatti){
            System.out.println(piatto.toString());
        }
        System.out.println("prezzo complessivo:€ "+getPrezzo());
    }

    public double getPrezzo() {
        double final_prezzo = 0.0;
        for (Piatto ptr : this.piatti)
            final_prezzo += ptr.getPrezzo_piatto();
        return final_prezzo;
    }

    public void inviaOrdine() {
        for (Piatto ptr : this.piatti) {
            ptr.preparaPiatto();
        }
        Globals.ordinazioni_effettuate.add(this);
    }

    @Override
    public String toString() {
        String str="";
        for(Piatto piatto:piatti)
            str+=piatto.toString()+";";
        return str;
    }

    public List<Piatto> getPiatti() {
        return this.piatti;
    }

}
