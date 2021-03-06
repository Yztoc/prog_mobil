package tj.project.esir.progmobproject.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import tj.project.esir.progmobproject.models.CustomPair;
import tj.project.esir.progmobproject.models.Question;

public class QuestionManager {

    private static final String TABLE_NAME = "question";
    public static final String ID_QUESTION="id_question";
    public static final String TITLE_QUESTION="title_question";
    public static final String TEXT_RESPONSE1="text_response1";
    public static final String TEXT_RESPONSE2="text_response2";
    public static final String TEXT_RESPONSE3="text_response3";
    public static final String VALUE_RESPONSE1="value_response1";
    public static final String VALUE_RESPONSE2="value_response2";
    public static final String VALUE_RESPONSE3="value_response3";

    public static final String CREATE_TABLE_QUESTION = "CREATE TABLE "+TABLE_NAME+
            " (" +
            " "+ID_QUESTION+" INTEGER primary key,"+
            " "+TITLE_QUESTION+" TEXT" +
            " "+TEXT_RESPONSE1+" TEXT" +
            " "+TEXT_RESPONSE2+" TEXT" +
            " "+TEXT_RESPONSE3+" TEXT" +
            " "+VALUE_RESPONSE1+" BOOLEAN" +
            " "+VALUE_RESPONSE2+" BOOLEAN" +
            " "+VALUE_RESPONSE3+" BOOLEAN" +
            ");";

    private MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    public QuestionManager(Context context) {
        maBaseSQLite = MySQLite.getInstance(context);
    }

    public void open() {
        //on ouvre la table en lecture/écriture
        db = maBaseSQLite.getWritableDatabase();
    }

    public void close() {
        //on ferme l'accès à la BDD
        db.close();
    }

    public long addQuestion(Question question) {
        // Ajout d'un enregistrement dans la table
        ContentValues values = new ContentValues();
        values.put(ID_QUESTION, question.getId());
        values.put(TITLE_QUESTION, question.getTitle());
        values.put(TEXT_RESPONSE1, question.getResponse1().getFirst());
        values.put(TEXT_RESPONSE2, question.getResponse2().getFirst());
        values.put(TEXT_RESPONSE3, question.getResponse3().getFirst());
        values.put(VALUE_RESPONSE1, question.getResponse1().getSecond());
        values.put(VALUE_RESPONSE2, question.getResponse2().getSecond());
        values.put(VALUE_RESPONSE3, question.getResponse3().getSecond());

        // insert() retourne l'id du nouvel enregistrement inséré, ou -1 en cas d'erreur
        return db.insert(
                TABLE_NAME,null,values);
    }


    public int updateQuestion(Question question) {
        // modification d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la requête

        ContentValues values = new ContentValues();
        values.put(TITLE_QUESTION, question.getTitle());
        values.put(TEXT_RESPONSE1, question.getResponse1().getFirst());
        values.put(TEXT_RESPONSE2, question.getResponse2().getFirst());
        values.put(TEXT_RESPONSE3, question.getResponse3().getFirst());
        values.put(VALUE_RESPONSE1, question.getResponse1().getSecond());
        values.put(VALUE_RESPONSE2, question.getResponse2().getSecond());
        values.put(VALUE_RESPONSE3, question.getResponse3().getSecond());

        String where = ID_QUESTION+" = ?";
        String[] whereArgs = {question.getId()+""};

        return db.update(TABLE_NAME, values, where, whereArgs);
    }

    public int deleteQuestion(int id) {
        // suppression d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la clause WHERE, 0 sinon

        String where = ID_QUESTION+" = ?";
        String[] whereArgs = {id+""};

        return db.delete(TABLE_NAME, where, whereArgs);
    }


    public Question getRandomQuestion() {
        // Retourne l'animal dont l'id est passé en paramètre

        Question question=new Question();

        Cursor c = db.rawQuery(
                "SELECT * FROM "+TABLE_NAME+" ORDER BY RANDOM() "+
                        "LIMIT 1", null);

        if (c.moveToFirst()) {
            question.setId(c.
                    getInt(c.getColumnIndex(ID_QUESTION)));
            question.setTitle(c.
                    getString(c.getColumnIndex(TITLE_QUESTION)));
            question.setResponse1(new CustomPair<String, Integer>(c.getString(c.getColumnIndex(TEXT_RESPONSE1)),c.getInt(c.getColumnIndex(VALUE_RESPONSE1))));
            question.setResponse2(new CustomPair<>(c.getString(c.getColumnIndex(TEXT_RESPONSE2)),c.getInt(c.getColumnIndex(VALUE_RESPONSE2))));
            question.setResponse3(new CustomPair<>(c.getString(c.getColumnIndex(TEXT_RESPONSE3)),c.getInt(c.getColumnIndex(VALUE_RESPONSE3))));

            c.close();
        }
        return question;
    }

    public List<Question> get5randomQuestions(){
        List<Question> res = new ArrayList<>();
        Cursor c = db.rawQuery(
                "SELECT * FROM "+TABLE_NAME+" ORDER BY RANDOM() "+
                        "LIMIT 5", null);
        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.
                        getInt(c.getColumnIndex(ID_QUESTION)));
                question.setTitle(c.
                        getString(c.getColumnIndex(TITLE_QUESTION)));
                question.setResponse1(new CustomPair<String, Integer>(c.getString(c.getColumnIndex(TEXT_RESPONSE1)),c.getInt(c.getColumnIndex(VALUE_RESPONSE1))));
                question.setResponse2(new CustomPair<String, Integer>(c.getString(c.getColumnIndex(TEXT_RESPONSE2)),c.getInt(c.getColumnIndex(VALUE_RESPONSE2))));
                question.setResponse3(new CustomPair<String, Integer>(c.getString(c.getColumnIndex(TEXT_RESPONSE3)),c.getInt(c.getColumnIndex(VALUE_RESPONSE3))));
                res.add(question);
            } while (c.moveToNext());
            c.close();
        }
        return res;
    }

    public Question getQuestion(int id) {

        Question question=new Question();

        Cursor c = db.rawQuery(
                "SELECT * FROM "+TABLE_NAME+" WHERE "+
                        ID_QUESTION+"="+id, null);

        if (c.moveToFirst()) {
            question.setId(c.
                    getInt(c.getColumnIndex(ID_QUESTION)));
            question.setTitle(c.
                    getString(c.getColumnIndex(TITLE_QUESTION)));
            question.setResponse1(new CustomPair<String, Integer>(c.getString(c.getColumnIndex(TEXT_RESPONSE1)),c.getInt(c.getColumnIndex(VALUE_RESPONSE1))));
            question.setResponse2(new CustomPair<String, Integer>(c.getString(c.getColumnIndex(TEXT_RESPONSE2)),c.getInt(c.getColumnIndex(VALUE_RESPONSE2))));
            question.setResponse3(new CustomPair<String, Integer>(c.getString(c.getColumnIndex(TEXT_RESPONSE3)),c.getInt(c.getColumnIndex(VALUE_RESPONSE3))));

            c.close();
        }
        return question;
    }


    public int getLastInsertedId(){
        int res =-1;
        Cursor c = db.rawQuery("SELECT max("+ID_QUESTION+") FROM "+TABLE_NAME,null);
        if (c.moveToFirst()) {
           res =  c.getInt(0);
            c.close();
        }
        return res;
    }

    public String getAllQuestionsIds(){
        String res ="";
        Cursor c = db.rawQuery(
                "SELECT "+ID_QUESTION+" FROM "+TABLE_NAME, null);
        if (c.moveToFirst()) {
            do {
                res += c.getInt(c.getColumnIndex(ID_QUESTION))+",";
            } while (c.moveToNext());
            c.close();
        }
        if(res != "")
        res = res.substring(0,res.length()-1);
        return res;
    }

    public Cursor getQuestions() {
        // sélection de tous les enregistrements de la table
        return db.rawQuery("SELECT * FROM "+TABLE_NAME,
                null);
    }
}
