import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static int nivelanterior = 0;
    public static ArrayList<Codificare> coduri = new ArrayList<>(10000);

    /// Am folosit o parcurgere de arbore ca sa generez codul pentru fiecare cuvant
    /// In momentul in care s-a ajuns intr-un cuvant ( frunza) este retinut in vectorui de Codificari ( coduri , cel de mai sus) cuvantul respectiv codul
    /// Nivelele in arbore sunt numerotate incepand cu 0 de la radacina.

    public static void AfisareArboreSRD(Nod radacina,int poz, ArrayList<Integer> aux,int nivelactual){
       /// In momentul in care nivelul anterior este mai mic decat cel actual inseamna ca ne-am deplasat in jos pe arbore si avem 2 verificari
        if(nivelanterior < nivelactual) {
            // daca am intrat cu poz == 0 in functie inseamna ca ne-am deplasat in stanga, deci punem in vector 0
            if (poz == 0 ) {
                aux.add(0);
            }
            // daca poz == 1 inseamna ca ne-am deplasat in dreapta, deci punem in vector 1
            if (poz == 1 ) {
                aux.add(1);

            }
        }
        else if(nivelanterior > nivelactual){
            // In cazul in care nivelul anterior este mai mare decat cel actual , inseamna ca ne-am deplasat in sus pe arbore
            /// Actualizarile in vectorul de codificari au avut loc, deci vectorul folosit in recursivitate aux ( in care se genereaza codificarile)
                // //    trebuie modificat stergandu-i ultima valoare
            aux.remove(aux.size()-1);
        }
        // nivelul anterior devine cel actual in momentul in care urmeaza verificarea unui nou nod pe partea stanga sau intrarea pe un nod null
        nivelanterior = nivelactual;
        if(radacina != null ){

            AfisareArboreSRD(radacina.getLeft(),0,aux,nivelactual +1);

            if(nivelanterior > nivelactual && aux.size() > 0){
                // aceasta reducere este repetata pentru evitarea cazurilor in care suntem pe fiul din dreapta al unui nod din extremitatea din dreapta
                // si recursivitatea sare inapoi mai multe nivele la nodul care a pornit apelul spre stanga
                aux.remove(aux.size()-1);
            }

            if(radacina.getCuvant() != null) {
                coduri.add(new Codificare(radacina.getCuvant(),new ArrayList<>(aux)));
            }
            // nivelul anterior devine cel actual si inaintea inceperii parcurgerii spre dreapta
            nivelanterior = nivelactual;
            AfisareArboreSRD(radacina.getRight(),1,aux,nivelactual+1);

            /// aceasta modificare are loc in momentul in care ne intoarcem in radacina ( 100)
            /// si nu vrem sa ramana reziduuri in aux ... de obicei ramane un 0 din partea stanga
            if(nivelanterior > nivelactual){
                aux.remove(aux.size()-1);
            }
        }

    }

    public static void AfisareLista(ArrayList<Nod> lista){
        System.out.println("Afisam lista :\n");
        for (Nod n : lista) {
            n.afisare();
        }
    }
    public static void CombinaDouaMinime(ArrayList<Nod> lista){
        // sortam lista de noduri si le combina pe primele 2 cele mai mici
        lista.sort(Nod::cmp);
        Nod auxiliar = Nod.Combina(lista.get(0),lista.get(1));
        // le stergem din lista
        lista.remove(0);
        lista.remove(0);
        // adaugam nodul nou format in lista
        lista.add(auxiliar);

    }
    public static Nod CreeazaArbore(ArrayList<Nod> lista){
        // se creeaza arborele cat timp in lista exista mai mult de 1 nod
        while(lista.size() > 1){
            CombinaDouaMinime(lista);
        }
        return lista.get(0);
    }

    public static int[] ReturneazaCodul(ArrayList<Integer> cod){
        // functia asta am folosita pentru printarea cifrelor
        // atunci cand printez in fisier doar ArrayList se vor folosi caractere de genul [1,2,5,6] astfel se va printa 1256
        int a[] = new int[100];
        for(int i=0;i<cod.size();i++){
            a[i] = cod.get(i);
        }
        return a;
    }

    public static void main(String[] args) {

        /* ------- CALEA CATRE FISIERE TREBUIE MODIFICATA ---- */
        /*----------- file - reprezinta fisierul din care se vor citi cuvintele cu probabilitatile ( este creeat de programul din CodeBlocks) -- */
        /* ---------- sursa  -  reprezinta fisierul care contine textul ce trebuie compresat ---- */
        /* ---------- destinatie - fisierul care contine ce s-a generat in urma compresiei ---- */

        // citim din fisier cuvintele cu probabilitatile corespunzatoare.
        ArrayList<Nod> lista = new ArrayList<>(20000);
        ArrayList<Nod> listaPentruHuffman = new ArrayList<>(20000);
        File file = new File("D:\\Calculatoare Numerice notite\\TemaShannon\\Shannon\\out.txt");
        Double EntropiaShannon = null;
        Scanner sc = null;
        try {
            sc = new Scanner(file);
            EntropiaShannon = Double.parseDouble(sc.nextLine());
            while (sc.hasNextLine()) {
                String aux = sc.nextLine();
                String prim[] = aux.split(" ");
                lista.add(new Nod(prim[0], Double.parseDouble(prim[1])));
                listaPentruHuffman.add(new Nod(prim[0], Double.parseDouble(prim[1])));

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        
        // Creem arborele pentru generarea codurilor

        Nod radacina = CreeazaArbore(lista);
        ArrayList<Integer> aux =  new ArrayList<>(20);
        int nivel = 0;
        AfisareArboreSRD(radacina,-1,aux,nivel);

        coduri.sort(Codificare::cmp);
        for (Codificare c: coduri) {
            System.out.println(c.getCuvant() + " " + c.getCod());
        }


        // Se citeste din sursa string cu string si se scrie in destinatie, in cazul in care
        // se gaseste cuvant care poate fi codificat, este scris codul sau

        File sursa = new File("D:\\Calculatoare Numerice notite\\TemaShannon\\Shannon\\TextSursa.txt");
        PrintWriter destinatie;
        try{
            destinatie = new PrintWriter("D:\\Calculatoare Numerice notite\\TemaShannon\\Shannon\\Destinatie.txt");
            sc = new Scanner(sursa);
            while (sc.hasNext()){
                String cuvant = sc.next();
                int findCode = 0;
                for (Codificare c: coduri) {
                    if(Objects.equals(cuvant,c.getCuvant())){
                        for (int i =0;i<c.getCod().size();i++){
                            destinatie.print(ReturneazaCodul(c.getCod())[i]);
                        }
                        findCode = 1;
                    }
                }
                if (findCode == 0){
                    destinatie.print(cuvant);
                }
            }
         destinatie.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }




        // Determinam InformatiaMedieHuffman prin suma(de la 1 la nr de cuvinte ) de prob(cuvant) * lungime(cod)
        Double InformatieMedieHuffman = 0.0;
        for( int i = 0 ; i < coduri.size() ; i++){
            InformatieMedieHuffman += (listaPentruHuffman.get(i).getProbabilitate() * coduri.get(i).getCod().size());
        }

        System.out.println("Informatia medie calculata utilizand Huffman este : " +  InformatieMedieHuffman);
        System.out.println("Informatia medie din Entropia Shannon este :" + EntropiaShannon);
    }
}

