package pkg1Progetto;

import java.io.*;
import java.util.*;

public class Main {
    /*Il progetto vuole simulare la gestione delle ordinazioni effettuate dal tavolo di un ristorante.
    * Per la realizzazione sono definite 3 classi principali:Ingrediente,Piatto,ordinazione.
    * Il cliente puo sostanzialmente effettuare la sua ordinazione scegliendo tra i paitti presenti sul menù, secondo le disponibilità della dispensa
    * (è possibile aggiungere alla propria ordinazione solo piatti effettivamente preparabili, in virtu della quantità di prodotti presenti in dispensa).
    * una volta terminata un ordinazione si puo scegliere se effettuarne una successiva o terminare l' escuzione, al termine il programma salva tutte le ordinazioni
    * su file binario(serializzate).
    * */
    public static void main(String[] args) {
        //caricamento dati della dispensa e del menù
        try {
            Globals.dispensa = carica_dispensa();
            Globals.menu=carica_menu();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        Collections.sort(Globals.menu);
        run();
    }

    public static void run() {
        //metodo che gestisce l'interazione con l'utente (da terminale), finalizzato al test del funzionamento e delle interazioni tra le varie classi,
        //effettua anche un test sul salvataggio delle ordinazioni in file tipo binario(persistenza e serializzazione)
        Scanner scanner = new Scanner(System.in);
        boolean run = true;
        System.out.println("Benvenuto!");
        while (run) {
            System.out.println("-------------------------------------------------------------------------------");
            System.out.println("ORDINAZIONI PRECEDENTEMENTE EFFETUATE:" + Globals.ordinazioni_effettuate.size());
            for (int i = 1; i <= Globals.ordinazioni_effettuate.size(); i++) {//stampa di tutte le precedenti ordinzioni effetuutate;
                System.out.println(".......................");
                System.out.println("ORDINAZIONE " + i + ":");
                Globals.ordinazioni_effettuate.get(i - 1).riepilogo();
                System.out.println(".......................");
            }
            System.out.println("-------------------------------------------------------------------------------");

            if (!checkNuovaOrdinazione(scanner)) {
                //verifica che l'utente voglia continuare ad effettuare ordinazioni, in caso contrario salva le ordinazioni effettuate,
                // propone un test sull'effettiva persistenza degli stessi, dopo di che termina l'esecuzione del programma uscendo dal ciclo run
                salvaOrdini();
                System.out.println("Grazie e arrivederci!");
                System.out.println("test persistenza ordinazioni?");
                String rispost = scanner.nextLine();
                if (rispost.equals("si")) {
                    testPersistenza();
                    run = false;
                    break;
                } else if (rispost.equals("no")) {
                    run = false;
                    break;
                } else System.out.println("reinserire scelta...");
            }

            sceltaMenu(scanner);//visualizza i menu disponibili
            Ordinazione ordinazine = new Ordinazione();//nuovo oggetto ordinazione
            while (true) {
                while (true) {
                    System.out.println("Aggiungere un nuovo piatto all' ordinazione?si/no");
                    String rispost = scanner.nextLine();
                    if (rispost.equals("si")) {
                        if (aggiuntaPiatti(scanner, ordinazine)) {
                            System.out.println("aggiunta del piatto all'ordinazione effettuata...");
                            ordinazine.riepilogo();
                        }
                    } else if (rispost.equals("no"))
                        break;
                    else System.out.println("reinserire scelta...");
                }
                System.out.println("completare ordinazione?si/no");
                String rispost = scanner.nextLine();
                if (rispost.equals("si")) {
                    ordinazine.inviaOrdine();
                    System.out.println("ordinazione effettuata!");
                    break;
                } else if (rispost.equals("no")) {
                    System.out.println("ordine cancellato...");
                    break;
                } else System.out.println("reinserire scelta...");
            }
        }
    }

    public static List<Piatto> carica_menu() throws IOException {
        // metodo per la lettura da file del menù:dai dati letti vengono istanziati e inseriti nella lista menù oggetti di tipo pkg1Progetto.Piatto.
        List<Piatto> menu = new ArrayList<>();
        ArrayList<Ingrediente> ingredienti_piatto;
        int[] quantita_ingredienti;
        double prezzo_piatto;
        String nome_piatto;
        String descrizione_piatto;
        FileReader menu_file;
        BufferedReader buffer;
        try {
            menu_file = new FileReader("Menù");//definizione oggetto file reader(da menù)
            buffer = new BufferedReader(menu_file);// bufferReader per la lettura bufferizzata, lettura di linee intere dal file, maggior velocità.
        } catch (FileNotFoundException e) {
            System.out.println("File menù assente");
            throw new FileNotFoundException("errore caricamento del menù");
        }

        String str;
        String[] separatoreElem;
        while ((str = buffer.readLine()) != null) {// lettura degli ingredienti dal file Menù e popolamento della lista "menù
            quantita_ingredienti = new int[30];
            ingredienti_piatto = new ArrayList<>();
            separatoreElem = str.split(";");// il metodo split della classe string genera unarray di stringhe: la stringa chiamante viene spezzata in base al carattere passato come parametro al metodo
            nome_piatto = separatoreElem[0];
            prezzo_piatto = Double.parseDouble(separatoreElem[1]);
            for (int i = 2; i < separatoreElem.length; i++) {
                for (Ingrediente ingr : Globals.dispensa) {
                    if (separatoreElem[i].equals(ingr.getNome())) {
                        ingredienti_piatto.add(ingr);
                        break;
                    }
                }
            }
            str = buffer.readLine();
            separatoreElem = str.split(";");
            for (int i = 0; i < separatoreElem.length; i++) {
                quantita_ingredienti[i] = Integer.parseInt(separatoreElem[i]);
            }
            str = buffer.readLine();
            descrizione_piatto = str;
            menu.add(new Piatto(nome_piatto, descrizione_piatto, prezzo_piatto, ingredienti_piatto, quantita_ingredienti));
        }
        return (menu);
    }

    public static List<Ingrediente> carica_dispensa() throws IOException {
        // metodo per la lettura da file .txt della dispensa:dai dati letti vengono istanziati degli oggetti ingrediente, successivamente vengono inseriti nella lista dispensa.
        List<Ingrediente> dispensa = new LinkedList<>();
        FileReader disp_file;
        try {
            disp_file = new FileReader("Dispensa");//definizione oggetto file reader(da dispensa)
        } catch (FileNotFoundException e) {
            System.out.println("File dispensa assente");
            throw new FileNotFoundException("errore caricamento dispensa");
        }
        BufferedReader buffer = new BufferedReader(disp_file);// bufferReader per la lettura bufferizzata
        String str;
        String[] separatoreElem;
        while ((str = buffer.readLine()) != null) {// lettura degli ingredienti dal file dispensa e popolamento della lista "dispensa"
            Ingrediente.tipo tipo;
            separatoreElem = str.split(";");// il metodo split della classe string genera unarray di stringhe: la stringa chiamante viene spezzata in base al carattere passato come parametro al metodo
            tipo = switch (separatoreElem[1]) {
                case ("vegetariano") -> Ingrediente.tipo.VEGETARIANO;
                case ("vegano") -> Ingrediente.tipo.VEGANO;
                default -> Ingrediente.tipo.NORMALE;
            };
            int quantita = Integer.parseInt(separatoreElem[2]);
            dispensa.add(new Ingrediente(separatoreElem[0], tipo, quantita));
        }
        return (dispensa);
    }

    public static boolean checkNuovaOrdinazione(Scanner scanner) {
        while (true) {
            System.out.println("effettuare nuova ordinazione?si/no");
            String newOrdin = scanner.nextLine();
            if (newOrdin.equals("si"))
                return true;
            else if (newOrdin.equals("no")) {
                return false;
            } else System.out.println("reinserire scelta...");
        }
    }

    public static void sceltaMenu(Scanner scanner) {
        while (true) {
            System.out.println("seleziona il menù:standard/vegan");
            String newOrdin = scanner.nextLine();
            if (newOrdin.equals("standard")) {
                System.out.println("MENU STANDARD");
                stampaMenu();
                break;
            } else if (newOrdin.equals("vegan")) {
                System.out.println("MENU VEGAN");
                stampaMenuVegan();
                break;
            } else System.out.println(("reinserire scelta..."));
        }
    }

    public static boolean aggiuntaPiatti(Scanner scanner, Ordinazione ordine) {
        boolean aggiunto = false;
        int quantita;
        System.out.println("inserire nome del piatto da aggiungere all'ordinazione...");
        String newpiatto = scanner.nextLine();
        System.out.println("aggiungere piatto all' ordinazione?si/no");
        while (true) {
            String newOrdin = scanner.nextLine();
            if (newOrdin.equals("si")) {
                System.out.println("quante porzioni?");
                quantita = scanner.nextInt();
                scanner.nextLine();
                aggiunto = ordine.aggiungi_piatto(newpiatto, quantita);
                return aggiunto;
            } else if (newOrdin.equals("no"))
                break;
            else System.out.println("reinserire scelta...");
        }
        return aggiunto;
    }

    public static void stampaMenu() {
        for (Piatto piatto : Globals.menu) {
            if (piatto.isDisponibile()) {
                System.out.println(piatto.getNome() + ",EURO " + piatto.getPrezzo_piatto());
                System.out.println(piatto.getRicetta());
                System.out.println(".....................................");
            }
        }
    }

    public static void stampaMenuVegan() {
        for (Piatto piatto : Globals.menu) {
            if (piatto.isDisponibile() && piatto.isVegano()) {
                System.out.println(piatto.getNome() + ",EURO " + piatto.getPrezzo_piatto());
                System.out.println(piatto.getRicetta());
                System.out.println(".....................................");
            }
        }
    }

    public static void salvaOrdini()  {
        //il metodo effettua la serizlizzazione e la scrittura su file della lista di ordinazioni effettuate
        if(!Globals.ordinazioni_effettuate.isEmpty()){
            try {
                ObjectOutputStream outstrem = new ObjectOutputStream(new FileOutputStream("ordinazioni"));
                outstrem.writeObject(Globals.ordinazioni_effettuate);
                outstrem.close();
            } catch (IOException e) {
                System.out.println("salvataggio ordinazioni non effettuato...");
            }

        }
    }

    public static void testPersistenza()  {
        //un metodo per recuperare da file(deserializzare) la liste delle ordinazioni effettuate e per la stampa delle stesse
        ObjectInputStream inStream;
        List<Ordinazione>lista;
        try {
            inStream = new ObjectInputStream(new FileInputStream("ordinazioni"));
            lista = (ArrayList<Ordinazione>) inStream.readObject();
            for (Ordinazione ordinazione : lista) {
                    ordinazione.riepilogo();
                    System.out.println(" ");
                }
            inStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("file non trovato...");
        } catch(IOException e){
            System.out.println("impossibile effettuare la lettura...");
        }catch(ClassNotFoundException e){
            System.out.println("incompatibilità tipo oggetto...");
        }

    }
}

