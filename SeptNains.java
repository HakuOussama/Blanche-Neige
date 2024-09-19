// -*- coding: utf-8 -*-

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class SeptNains {
    final static LinkedList<String> fileAttente=new LinkedList<>();
    final static BlancheNeige bn = new BlancheNeige();
    final static int nbNains = 7;
    final static String noms [] = {"Simplet", "Dormeur",  "Atchoum", "Joyeux", "Grincheux",
                                   "Prof", "Timide"};
  
        public static void main(String[] args) throws InterruptedException {
        final Nain nain [] = new Nain [nbNains];
        for(int i = 0; i < nbNains; i++) nain[i] = new Nain(noms[i]);
        for(int i = 0; i < nbNains; i++) {
            nain[i].start();
        }
        Thread.sleep(5000);
        for(int i = 0; i < nbNains; i++) {
            nain[i].interrupt();
        }
        while(true){
            boolean done=true;
            for(Nain nain1 : nain){
                done =done && nain1.isInterrupted();
            }
            if(done)
                break;
        }
        affiche("Programme Terminer");
    }

    
    static class Nain extends Thread {
        public Nain(String nom) {
            this.setName(nom);
        }
        public void run() {


            while(true) {
                bn.requérir();
                try {
                    bn.accéder();
                } catch (InterruptedException e) {
                    affiche("adios amongous");
                    currentThread().interrupt();
                    break;
                }
                Long start = System.currentTimeMillis();
                try {
                    sleep(2000);

                } catch (InterruptedException e) {

                    try {
                        sleep(System.currentTimeMillis() - start);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }


                    affiche("Adieux");

                    currentThread().interrupt();
                    break;
                }
                bn.relâcher();
            }
        }
    }
    
    static void affiche(String message) {
        SimpleDateFormat sdf=new SimpleDateFormat("'['hh'h 'mm'mn 'ss','SSS's] '");  
        Date heure = new Date(System.currentTimeMillis());
        System.out.println(sdf.format(heure) + "\"" + Thread.currentThread().getName() + "\" "
                           + message + ".");        
    }

    static class BlancheNeige {
        private volatile boolean libre = true;     // Initialement, Blanche-Neige est libre.

        public synchronized void requérir() {
            fileAttente.add(Thread.currentThread().getName());
            affiche("veut un accès exclusif");
        }
        
        public synchronized void accéder() throws InterruptedException {
            System.out.println("la file d'attente : "+fileAttente);
            while ( ! libre || !Thread.currentThread().getName().equals(fileAttente.get(0))) wait() ;                // Le nain attend passivement son tour
            libre = false;
            affiche("accède à la ressource");
        }
        
        public synchronized void relâcher() {
            affiche("relâche la ressource");
            fileAttente.remove(0);
            notifyAll();
            libre = true;
        }
    }
}    



/*
  % java SeptNains.java
  [10h 51mn 04,406s] "Simplet" veut un accès exclusif.
  [10h 51mn 04,409s] "Simplet" accède à la ressource.
  [10h 51mn 04,409s] "Timide" veut un accès exclusif.
  [10h 51mn 04,409s] "Prof" veut un accès exclusif.
  [10h 51mn 04,409s] "Grincheux" veut un accès exclusif.
  [10h 51mn 04,409s] "Joyeux" veut un accès exclusif.
  [10h 51mn 04,409s] "Atchoum" veut un accès exclusif.
  [10h 51mn 04,409s] "Dormeur" veut un accès exclusif.
  [10h 51mn 06,414s] "Simplet" relâche la ressource.
  [10h 51mn 06,416s] "Simplet" veut un accès exclusif.
  [10h 51mn 06,416s] "Simplet" accède à la ressource.
  [10h 51mn 06,416s] "Timide" accède à la ressource.
  [10h 51mn 06,416s] "Dormeur" accède à la ressource.
  [10h 51mn 06,417s] "Atchoum" accède à la ressource.
  [10h 51mn 06,417s] "Joyeux" accède à la ressource.
  [10h 51mn 06,417s] "Grincheux" accède à la ressource.
  [10h 51mn 06,417s] "Prof" accède à la ressource.
  [10h 51mn 08,417s] "Timide" relâche la ressource.
  [10h 51mn 08,418s] "Timide" veut un accès exclusif.
  [10h 51mn 08,418s] "Timide" accède à la ressource.
  [10h 51mn 08,418s] "Atchoum" relâche la ressource.
  [10h 51mn 08,418s] "Atchoum" veut un accès exclusif.
  [10h 51mn 08,418s] "Atchoum" accède à la ressource.
  ...
*/


/* En remplaçant if par while :
  $ java SeptNains
  ...
*/
