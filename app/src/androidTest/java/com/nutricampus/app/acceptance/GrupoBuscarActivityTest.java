package com.nutricampus.app.acceptance;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import com.nutricampus.app.R;
import com.nutricampus.app.database.RepositorioGrupo;
import com.nutricampus.app.entities.Grupo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


@SuppressWarnings("squid:S2925") //  SonarQube ignora o sleep())
@RunWith(AndroidJUnit4.class)
@LargeTest


public class GrupoBuscarActivityTest extends AbstractPreparacaoTestes{
    private RepositorioGrupo repositorioGrupo;
    private Grupo grupos[];
    public void criarGrupo() {
        repositorioGrupo = new RepositorioGrupo(InstrumentationRegistry.getTargetContext());
        grupos = new Grupo[5];
        grupos[0] = new Grupo("Especial", "Grupo especial de animais", 1);
        grupos[1] = new Grupo("Tratamento", "Grupo em tratamento", 1);
        grupos[2] = new Grupo("Vacinacao", "Grupo em vacinação", 1);
        grupos[3] = new Grupo("Subnutrido", "Grupo subnutrido", 1);
        grupos[4] = new Grupo("Acima do peso", "Grupo acima do peso", 1);
        for (int i = 0; i < 5; i++) {
            repositorioGrupo.inserirGrupo(grupos[i]);
        }
    }
    @Before
    public void setUp() throws Exception {
        criarGrupo();
        realizaLogin();
        abrirMenu();
        clicarItemMenu(4);
    }
    @After
    public void deletaDadosPosTestes() {
        repositorioGrupo.removerGrupoUsuario(1);
    }
    @Test
    public void buscarGruposCadastrados() throws Exception {
        espera(500);
        ViewInteraction listView = onView(
                allOf(withId(R.id.listaGrupo),
                        childAtPosition(
                                allOf(withId(R.id.resultado_busca_prole),
                                        childAtPosition(
                                                withId(R.id.telaListaDadosCompl),
                                                0)),
                                2),
                        isDisplayed()));
        listView.check(matches(isDisplayed()));
        espera(500);

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.text_quantidade_encontrados), withText("9 registros encontrados"),
                        childAtPosition(
                                allOf(withId(R.id.resultado_busca_prole),
                                        childAtPosition(
                                                withId(R.id.telaListaDadosCompl),
                                                0)),
                                0),
                        isDisplayed()));
        textView7.check(matches(withText("9 registros encontrados")));
    }
}