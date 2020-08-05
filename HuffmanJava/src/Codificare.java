import java.util.ArrayList;

public class Codificare {
    private String cuvant;
    private ArrayList<Integer> cod;

    public Codificare(String cuvant, ArrayList<Integer> cod) {
        this.cuvant = cuvant;
        this.cod = cod;
    }

    public String getCuvant() {
        return cuvant;
    }

    public void setCuvant(String cuvant) {
        this.cuvant = cuvant;
    }

    public ArrayList<Integer> getCod() {
        return cod;
    }

    public void setCod(ArrayList<Integer> cod) {
        this.cod = cod;
    }

    @Override
    public String toString() {
        return  cuvant  + " = " + cod ;
    }

    public static int cmp(Codificare ob1, Codificare ob2){
        if(ob1.getCuvant().compareTo(ob2.getCuvant()) < 0){
            return -1;
        }
        else {
            return 1;
        }
    }
}
