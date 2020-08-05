public class Nod {
    private String cuvant;
    private double probabilitate;
    Nod left;
    Nod right;


    public Nod(String cuvant, double probabilitate){
        this.cuvant = cuvant;
        this.probabilitate = probabilitate;
        this.left = null;
        this.right = null;
    }

    public Nod(double probabilitate , Nod left , Nod right){
        this.probabilitate = probabilitate;
        this.left = left;
        this.right = right;
    }
    public void afisare(){
        System.out.println(cuvant + " " + probabilitate);
    }

    public String getCuvant() {
        return cuvant;
    }

    public void setCuvant(String cuvant) {
        this.cuvant = cuvant;
    }

    public double getProbabilitate() {
        return probabilitate;
    }

    public void setProbabilitate(double probabilitate) {
        this.probabilitate = probabilitate;
    }

    public Nod getLeft() {
        return left;
    }

    public Nod getRight() {
        return right;
    }

    public static int cmp(Nod ob1, Nod ob2){
        if(ob1.getProbabilitate() < ob2.getProbabilitate()){
            return -1;
        }
        else {
            return 1;
        }
    }

    public static Nod Combina(Nod ob1,Nod ob2){
        Nod nodRezultat = new Nod(ob1.getProbabilitate()+ob2.getProbabilitate(),ob1,ob2);
        return nodRezultat;
    }

}
