package com.projet.kilian.mathieu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ImagesBDD {
    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "images.db";

    private static final String TABLE_IMAGES = "table_images";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_PATH = "PATH";
    private static final int NUM_COL_PATH = 1;
    private static final String COL_NAME = "NAME";
    private static final int NUM_COL_NAME = 2;

    private SQLiteDatabase bdd;

    private MaBaseSQLite maBaseSQLite;

    public ImagesBDD(Context context){
        //On crée la BDD et sa table
        maBaseSQLite = new MaBaseSQLite(context, NOM_BDD, null, VERSION_BDD);
    }

    public void open(){
        //on ouvre la BDD en écriture
        bdd = maBaseSQLite.getWritableDatabase();
    }

    public void close(){
        //on ferme l'accès à la BDD
        bdd.close();
    }

    public SQLiteDatabase getBDD(){
        return bdd;
    }

    public long insertLivre(Images images){
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associée à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
        values.put(COL_PATH, images.getPath());
        values.put(COL_NAME, images.getName());
        //on insère l'objet dans la BDD via le ContentValues
        return bdd.insert(TABLE_IMAGES, null, values);
    }

    public int updateLivre(int id, Images images){
        //La mise à jour d'un livre dans la BDD fonctionne plus ou moins comme une insertion
        //il faut simplement préciser quel livre on doit mettre à jour grâce à l'ID
        ContentValues values = new ContentValues();
        values.put(COL_PATH, images.getPath());
        values.put(COL_NAME, images.getName());
        return bdd.update(TABLE_IMAGES, values, COL_ID + " = " +id, null);
    }

    public int removeLivreWithID(int id){
        //Suppression d'un livre de la BDD grâce à l'ID
        return bdd.delete(TABLE_IMAGES, COL_ID + " = " +id, null);
    }

    public ArrayList<Images> getAllImages() {
        ArrayList<Images> movieDetailsList = new ArrayList();
        String selectQuery = "SELECT * FROM " + TABLE_IMAGES
                + " ORDER BY " + COL_ID + " ASC";
        SQLiteDatabase db = this.bdd;
        Cursor cursor = db.rawQuery(selectQuery, null);

        //if TABLE has rows
        if (cursor.moveToFirst()) {
            //Loop through the table rows
            do {
                Images image = new Images();
                image.setId(cursor.getInt(0));
                image.setPath(cursor.getString(1));
                image.setName(cursor.getString(2));
                //Add movie details to list
                movieDetailsList.add(image);
            } while (cursor.moveToNext());
        }
        db.close();

        return movieDetailsList;
    }

    public Images getImageWithName(String name){
        //Récupère dans un Cursor les valeurs correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
        Cursor c = bdd.query(TABLE_IMAGES, new String[] {COL_ID, COL_PATH, COL_NAME}, COL_NAME + " = \"" + name +"\"", null, null, null, null);
        return cursorToLivre(c);
    }

    //Cette méthode permet de convertir un cursor en un livre
    private Images cursorToLivre(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        //Sinon on se place sur le premier élément
        c.moveToFirst();
        //On créé un livre
        Images img = new Images();
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        img.setId(c.getInt(NUM_COL_ID));
        img.setPath(c.getString(NUM_COL_PATH));
        img.setName(c.getString(NUM_COL_NAME));
        //On ferme le cursor
        c.close();

        //On retourne le livre
        return img;
    }

}
