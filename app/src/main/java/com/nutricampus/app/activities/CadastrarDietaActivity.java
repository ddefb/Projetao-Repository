package com.nutricampus.app.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nutricampus.app.R;
import com.nutricampus.app.database.RepositorioAnimal;
import com.nutricampus.app.database.RepositorioCompostosAlimentares;
import com.nutricampus.app.database.RepositorioDieta;
import com.nutricampus.app.database.RepositorioPropriedade;
import com.nutricampus.app.entities.Animal;
import com.nutricampus.app.entities.CompostosAlimentares;
import com.nutricampus.app.entities.Dieta;
import com.nutricampus.app.entities.Propriedade;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paulo Mateus on 20/08/17.
 * For project NutriCampus.
 * Contact: <paulomatew@gmail.com>
 */
@SuppressWarnings({"squid:S1172", "squid:MaximumInheritanceDepth"})
public class CadastrarDietaActivity extends AppCompatActivity {
    public EditText identificador = null;
    public EditText pb = null;
    public TextView detalhes = null;
    public Button btnAddAnimal = null;
    public Button btnAddComposto = null;
    public Button btnCalcular = null;
    public Spinner spinnerProprietarios = null;
    public List<Propriedade> propriedadesBD = null;
    public List<CompostosAlimentares> compostosBD = null;
    public List<Animal> animaisBD = null;

    public ArrayList<Animal> arrAnimais = new ArrayList<>();
    public ArrayList<CompostosAlimentares> arrCompostos = new ArrayList<>();

    public ArrayList<Integer> mSelectedItemsAnimal = new ArrayList<>();
    public ArrayList<Integer> mSelectedItemsComposto = new ArrayList<>();

    protected void init() {
        setContentView(R.layout.activity_cadastrar_dieta);

        identificador = (EditText) findViewById(R.id.input_dieta_identificador);
        pb = (EditText) findViewById(R.id.input_dieta_pb);
        btnAddAnimal = (Button) findViewById(R.id.btn_add_animais_dieta);
        btnAddComposto = (Button) findViewById(R.id.btn_add_compostos_dieta);
        btnCalcular = (Button) findViewById(R.id.btn_calcular_dieta);
        spinnerProprietarios = (Spinner) findViewById(R.id.spinner_proprietario);
        detalhes = (TextView) findViewById(R.id.textview_detalhes_dieta);

        RepositorioPropriedade repositorioPropriedade = new RepositorioPropriedade(this);
        RepositorioCompostosAlimentares repositorioCompostos = new RepositorioCompostosAlimentares(this);
        propriedadesBD = repositorioPropriedade.buscarTodasPropriedades();
        compostosBD = repositorioCompostos.buscarTodosCompostos("");

        if (propriedadesBD.size() < 1) {
            Toast.makeText(this, "Cadastre pelo menos uma propriedade", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        if (compostosBD.size() < 2) {
            Toast.makeText(this, "Cadastre pelo menos dois compostos", Toast.LENGTH_SHORT).show();
            this.finish();
        }

        //Faço array só com string de nomes
        ArrayList<String> arr = new ArrayList<>();
        for (int i = 0; i < propriedadesBD.size(); i++) {
            arr.add(propriedadesBD.get(i).getNome());
        }

        ArrayAdapter<String> arr_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arr);
        spinnerProprietarios.setAdapter(arr_adapter);
        spinnerProprietarios.setSelection(0);
        /*spinnerProprietarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //mSelectedItemsAnimal.clear();
                //arrAnimais.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void addAnimais(View view) {
        Propriedade p = propriedadesBD.get(spinnerProprietarios.getSelectedItemPosition());

        RepositorioAnimal repositorioAnimal = new RepositorioAnimal(this);
        animaisBD = repositorioAnimal.buscarPorPropridade(p.getId());

        String[] animais = new String[animaisBD.size()];
        boolean[] ativos = new boolean[animaisBD.size()];
        for (int i = 0; i < animaisBD.size(); i++) {
            animais[i] = animaisBD.get(i).getIndentificador();
            ativos[i] = false;
        }

        for (int i = 0; i < mSelectedItemsAnimal.size(); i++) {
            int j = mSelectedItemsAnimal.get(i);
            ativos[j] = true;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set the dialog title
        builder.setTitle("Selecione os Animais")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(animais, ativos,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    mSelectedItemsAnimal.add(which);
                                    arrAnimais.add(animaisBD.get(which));
                                } else if (mSelectedItemsAnimal.contains(which)) {
                                    mSelectedItemsAnimal.remove(Integer.valueOf(which));
                                    Animal a = animaisBD.get(which);

                                    for (int i = 0; i < arrAnimais.size(); i++) {
                                        if (arrAnimais.get(i).getId() == a.getId()) {
                                            arrAnimais.remove(i);
                                        }
                                    }
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton("Pronto", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        /*for (int i = 0; i < arrAnimais.size(); i++) {
                            System.out.println(arrAnimais.get(i).getIndentificador());
                        }*/

                    }
                }).setNegativeButton("Resetar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                arrAnimais.clear();
                mSelectedItemsAnimal.clear();
            }
        });
        builder.create().show();
    }

    public void addCompostos(View view) {
        final String[] compostos = new String[compostosBD.size()];
        boolean[] ativos = new boolean[compostosBD.size()];
        for (int i = 0; i < compostosBD.size(); i++) {
            compostos[i] = compostosBD.get(i).getIdentificador();
            ativos[i] = false;
        }

        for (int i = 0; i < mSelectedItemsComposto.size(); i++) {
            int j = mSelectedItemsComposto.get(i);
            ativos[j] = true;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set the dialog title
        builder.setTitle("Selecione os Compostos")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(compostos, ativos,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    mSelectedItemsComposto.add(which);
                                    arrCompostos.add(compostosBD.get(which));
                                } else if (mSelectedItemsComposto.contains(which)) {
                                    mSelectedItemsComposto.remove(Integer.valueOf(which));
                                    arrCompostos.remove(compostosBD.get(which));
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton("Pronto", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        /*for (int i = 0; i < arrCompostos.size(); i++) {
                            System.out.println(arrCompostos.get(i).getIdentificador());
                        }*/

                    }
                }).setNegativeButton("Resetar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                arrCompostos.clear();
                mSelectedItemsComposto.clear();
            }
        });
        builder.create().show();
    }

    public void calcularDieta(View view) {
        if (identificador.getText().toString().isEmpty()) {
            Toast.makeText(this, "Adicione um nome a dieta", Toast.LENGTH_SHORT).show();
            return;
        }
        double pbD = 0;
        if (pb.getText().toString().isEmpty()) {
            Toast.makeText(this, "Adicione a quantidade de PB da dieta", Toast.LENGTH_SHORT).show();
            return;
        } else {
            try {
                pbD = Double.parseDouble(pb.getText().toString());
                if (pbD <= 0) {
                    throw new Exception();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Adicione um valor maior que zero", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (arrAnimais.size() < 1) {
            Toast.makeText(this, "Adicione pelo menos 1 Animal", Toast.LENGTH_SHORT).show();
            return;
        }
        if (arrCompostos.size() < 2) {
            Toast.makeText(this, "Adicione pelo menos 2 Compostos", Toast.LENGTH_SHORT).show();
            return;
        }

        Dieta d = new Dieta(identificador.getText().toString(), pbD, arrAnimais, arrCompostos);
        d.propriedade = propriedadesBD.get(spinnerProprietarios.getSelectedItemPosition());


        /*for (int i = 0; i < d.arrayObjetoDieta.size(); i++) {
            d.arrayObjetoDieta.get(i).print();
        }*/
        showDieta(d);
    }

    public void showDieta(final Dieta dieta) {
        String str = "";
        for (int i = 0; i < dieta.arrayObjetoDieta.size(); i++) {
            str += dieta.arrayObjetoDieta.get(i).composto.getIdentificador() +
                    ": " + dieta.arrayObjetoDieta.get(i).porcentagem + "%\n";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Detalhes");
        builder.setMessage(str)
                .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RepositorioDieta repositorioDieta = new RepositorioDieta(CadastrarDietaActivity.this);
                        repositorioDieta.inserirDieta(dieta);

                        //dieta.print();
                        CadastrarDietaActivity.this.finish();
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        // Create the AlertDialog object and return it
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }
}
