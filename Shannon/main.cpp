#include <iostream>
#include <fstream>
#include <stdio.h>
#include <cstring>
#include<bits/stdc++.h>
#include <string.h>
#include <cmath>
#include <vector>
#include <algorithm>
#include <utility>
using namespace std;

///Aici citim toate cuvintele pentru care dorim sa calculam probabilitatile
int sirCuvinte(char * numeFisier,string *aux){

  ifstream myfile(numeFisier);
  int i=0;
  while(myfile){
     myfile>>aux[i];
     i++;
  }
  myfile.close();
  return i;
}

// Functia verifica daca al doilea string este substring al primului.
int esteSubString(string s1, string s2)
{
    int Primul = s1.length();
    int Aldoilea = s2.length();
    // Aici este tratat cazul in care se compara un cuvant de genul "in" cu "interpretare" si nu sunt aceleasi cuvinte
    // Si cuvantul pus pe pozitia de substring este mai mare ca si lungime decat stringul pentru care se verifica, verificarea nu mai are sens.
    if(Primul >= Aldoilea + 3 || Aldoilea > Primul){
        return 0;
    }


    for(int i=0;i<s2.length();i++){
        if(s1[i]!=s2[i]) return 0;
    }
   return 1;

}

///Functia calculeaza probabilitatile(intr-un vector de probabilitati de dimensiune egala cu numarul de cuvinte)
/// pentru fiecare cuvant parcurgand fisierul "numeFisier"
/// primul parametru reprezinta cuvintele pentru care calculam probabilitatea , al doilea vectorul de probabilitati, n - numarul de cuvinte
void calculProbabilitate(string * cuvintele,double *probabilitati,int n,char * numeFisier){
    string cuvant;

    ifstream sursa (numeFisier);

    int numarCuvinte = 0;


    while(sursa>>cuvant){

        numarCuvinte++;
        transform(cuvant.begin(), cuvant.end(), cuvant.begin(), ::tolower); // am transformat literele cuvinteleor citite in litere mici

        for(int i=0;i<n-1;i++){ // daca cuvantul pentru care calculam probabilitatea este un substring al cuvantului citit ,
                                // am folosit aceasta analogie pentru ca atunci cand se citeste din fisier un string este citit cu tot cu caracterele .,/?!
            if (esteSubString(cuvant,cuvintele[i])){
                probabilitati[i]++;
            }
        }
    }

    for(int i=0;i<n;i++){
        if(probabilitati[i] != 0){
            probabilitati[i] /= numarCuvinte;
        }
    }
    sursa.close();

}

double EntropieShannon(double * p, int N){
  double Info = 0.0;
  for (int i=0;i<N-1;i++) {
       if (p[i]!=0.0) {
           Info = Info - p[i]*(log(p[i])/log(2.0));
       }
  }
  return Info;
}

int main()
{
    //variabile si vectori
    string * listaCuvinte = new string[30000];
    int lungime = sirCuvinte("Cuvinte.txt",listaCuvinte);
    double *probabilitati = new double[lungime+1];

    // initializare vectori
    for(int i=0;i<=lungime;i++){
        probabilitati[i]=0.00;
    }

    //aplicare functii
    calculProbabilitate(listaCuvinte,probabilitati,lungime,"TextSursa.txt");

   cout<<"\n\n Entropia este : "<<EntropieShannon(probabilitati,lungime);
    //afisare rezultate
    ofstream probFisier("out.txt");
    probFisier<<EntropieShannon(probabilitati,lungime)<<"\n";
    for(int i=0;i<lungime-1;i++){
        if(probabilitati[i] != 0.0){
        probFisier<<listaCuvinte[i]<<" "<<probabilitati[i]<<"\n";
        cout<<listaCuvinte[i]<<" "<<probabilitati[i]<<".\n";
        }
    }
    probFisier.close();

    return 0;
}
