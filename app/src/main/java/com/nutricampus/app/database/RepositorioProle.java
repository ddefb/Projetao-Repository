package com.nutricampus.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nutricampus.app.entities.Proprietario;
import com.nutricampus.app.entities.Prole;
import com.nutricampus.app.utils.Conversor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Diego Bezerra on 15/06/17.
 * For project NutriCampus.
 * Contact: <diego.defb@gmail.com>
 */

public class RepositorioProle {

    private SQLiteManager gerenciador;
    private SQLiteDatabase bancoDados;

    public RepositorioProle(Context context) {
        gerenciador = new SQLiteManager(context);
    }

    public int inserirProle(Prole prole) {
        bancoDados = gerenciador.getWritableDatabase();

        ContentValues dados = new ContentValues();
        dados.put(SQLiteManager.PROLE_ID_MATRIZ, prole.getMatriz());
        dados.put(SQLiteManager.PROLE_DATA_DE_NASCIMENTO, prole.getDataDeNascimento().toString());
        dados.put(SQLiteManager.PROLE_PESO_DE_NASCIMENTO, prole.getPesoDeNascimento());
        dados.put(SQLiteManager.PROLE_IS_NATIMORTO, prole.isNatimorto());

        long retorno = bancoDados.insert(SQLiteManager.TABELA_PROPRIETARIO, null, dados);
        bancoDados.close();

        // retorna o id do elemento inserido
        return (int) retorno;
    }

    public Prole buscarProle(int id_animal) {
        bancoDados = gerenciador.getReadableDatabase();

        String colunas_where = SQLiteManager.PROLE_ID_MATRIZ + "= ?";
        String[] valores_where = new String[]{String.valueOf(id_animal)};

        Cursor cursor = bancoDados.query(SQLiteManager.TABELA_PROLE, new String[]{
                        SQLiteManager.PROLE_ID,
                        SQLiteManager.PROLE_ID_MATRIZ,
                        SQLiteManager.PROLE_DATA_DE_NASCIMENTO,
                        SQLiteManager.PROLE_PESO_DE_NASCIMENTO,
                        SQLiteManager.PROLE_IS_NATIMORTO},
                colunas_where,
                valores_where, null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return new Prole(
                    cursor.getInt(cursor.getColumnIndex(SQLiteManager.PROLE_ID)),
                    cursor.getInt(cursor.getColumnIndex(SQLiteManager.PROLE_ID_MATRIZ)),
                    Date.valueOf(cursor.getString(cursor.getColumnIndex(SQLiteManager.PROLE_DATA_DE_NASCIMENTO))),
                    cursor.getFloat(cursor.getColumnIndex(SQLiteManager.PROLE_PESO_DE_NASCIMENTO)),
                    Conversor.StringToBoolean(cursor.getString(cursor.getColumnIndex(SQLiteManager.PROLE_IS_NATIMORTO))));

        }
        cursor.close();
        return null;
    }

    public List<Prole> buscarTodasProles() {

        bancoDados = gerenciador.getReadableDatabase();

        ArrayList<Prole> proles = new ArrayList<>();
        String getProles = "SELECT * FROM " + SQLiteManager.TABELA_PROLE;

        try {
            Cursor c = bancoDados.rawQuery(getProles, null);

            if (c.moveToFirst()) {
                do {
                    Prole p = new Prole();
                    p.setId(c.getInt(c.getColumnIndex(SQLiteManager.PROLE_ID)));
                    p.setMatriz(c.getInt(c.getColumnIndex(SQLiteManager.PROLE_ID_MATRIZ)));
                    p.setDataDeNascimento(Date.valueOf(c.getString(c.getColumnIndex(SQLiteManager.PROLE_DATA_DE_NASCIMENTO))));
                    p.setPesoDeNascimento(c.getFloat(c.getColumnIndex(SQLiteManager.PROLE_PESO_DE_NASCIMENTO)));
                    p.setNatimorto(Conversor.StringToBoolean(c.getString(c.getColumnIndex(SQLiteManager.PROLE_IS_NATIMORTO))));
                    proles.add(p);
                } while (c.moveToNext());
                c.close();
            }

        } catch (Exception e) {
            Log.i("RepositorioProle", e.toString());
            return Collections.emptyList();
        } finally {
            bancoDados.close();
        }

        return proles;
    }

    public boolean atualizarProle(Prole prole) {
        bancoDados = gerenciador.getWritableDatabase();

        ContentValues dados = new ContentValues();

        dados.put(SQLiteManager.PROLE_ID_MATRIZ, prole.getMatriz());
        dados.put(SQLiteManager.PROLE_DATA_DE_NASCIMENTO, prole.getDataDeNascimento().getTime());
        dados.put(SQLiteManager.PROLE_PESO_DE_NASCIMENTO, prole.getPesoDeNascimento());
        dados.put(SQLiteManager.PROLE_IS_NATIMORTO, prole.isNatimorto());

        int retorno = bancoDados.update(SQLiteManager.TABELA_PROLE,
                dados, SQLiteManager.PROLE_ID + " = ?",
                new String[]{String.valueOf(prole.getId())});

        bancoDados.close();

        return (retorno > 0);

    }

    public void removerProle(Prole prole) {
        bancoDados = gerenciador.getWritableDatabase();
        bancoDados.delete(SQLiteManager.TABELA_PROLE,
                SQLiteManager.PROLE_ID + " = ? ",
                new String[]{String.valueOf(prole.getId())});

        bancoDados.close();
    }


}

