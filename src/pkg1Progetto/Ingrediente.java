package pkg1Progetto;

import java.io.Serializable;

public class Ingrediente implements Serializable {
    /*La classe ingrediente definisce
    * gli ingredienti presenti in dispensa, é priva di metodi di rilievo, i suoi campi definiscono solamente le proprieta di di ogni ingrediente
    * presente in dispensa compresa la quantita totale presente.
    */

    private String nome;
    private tipo Tipo;
    private int quantita;
    public enum tipo{
        NORMALE,VEGETARIANO,VEGANO
    }

    public Ingrediente(String nome, tipo tipo, int quantita) {
        super();
        this.nome = nome;
        Tipo = tipo;
        this.quantita = quantita;
    }

    @Override
    public String toString() {
        return "pkg1Progetto.Ingrediente{" +
                "nome='" + nome + '\'' +
                ", Tipo=" + Tipo +
                ", quantità=" + quantita +
                '}';
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTipo(tipo tipo) {
        Tipo = tipo;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public String getNome() {
        return this.nome;
    }

    public tipo getTipo() {
        return Tipo;
    }

    public int getQuantita() {
        return quantita;
    }
}
