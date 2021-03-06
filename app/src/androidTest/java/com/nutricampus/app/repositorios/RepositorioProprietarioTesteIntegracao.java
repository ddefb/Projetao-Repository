package com.nutricampus.app.repositorios;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.nutricampus.app.database.RepositorioProprietario;
import com.nutricampus.app.entities.Proprietario;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class RepositorioProprietarioTesteIntegracao {
    private Context appContext = InstrumentationRegistry.getTargetContext();
    private RepositorioProprietario repositorio = new RepositorioProprietario(appContext);

    @Test
    public void testarInsereProprietarioInexistente() {

        Proprietario proprietario = new Proprietario("XXX.XXX.XXX-XX", "proprietario", "proprietario@email.com", "1234-5678");
        int resultNovoProprietario = repositorio.inserirProprietario(proprietario);

        boolean resultado = resultNovoProprietario > 0;

        assertTrue(resultado);

        repositorio.removerProprietario(repositorio.buscarProprietario("XXX.XXX.XXX-XX"));
    }

    @Test
    public void testarBuscaDeProprietarioPeloCpf() {

        String cpf = "222.222.222-24";

        Proprietario proprietario = new Proprietario(cpf, "proprietario", "proprietario@email.com", "1234-5678");
        repositorio.inserirProprietario(proprietario);

        Proprietario proprietarioEncontrado = repositorio.buscarProprietario(cpf);

        assertNotNull(proprietarioEncontrado);
    }

    @Test
    public void testarBuscaDeProprietarioPeloId() {
        Proprietario proprietario = new Proprietario("777.888.999.000-00", "proprietario", "proprietario@email.com", "1234-5678");

        int id = repositorio.inserirProprietario(proprietario);

        Proprietario proprietarioEncontrado = repositorio.buscarProprietario(id);

        assertNotNull(proprietarioEncontrado);

        repositorio.removerProprietario(proprietario);
    }


    //Verificar

    @Test
    public void testarAtualizacaoDeDadosSemIDProprietario() {
        boolean result;
        Proprietario proprietario = new Proprietario("333.333.333.34", "proprietario", "proprietario@email.com", "1234-5678");
        repositorio.inserirProprietario(proprietario);

        // Testa a atualização em um registro sem passar o id
        Proprietario proprietarioDadosAlterados = new Proprietario("999.999.999-99", "proprietario", "proprietario@email.com", "1234-5678");
        result = repositorio.atualizarProprietario(proprietarioDadosAlterados);

        assertFalse(result);
    }

    @Test
    public void testarAtualizacaoDeDadosComCpf() {

        String cpf = "444.444.444.45";
        boolean result;
        Proprietario proprietario,
                proprietarioBuscado,
                proprietarioEncontrado;

        proprietario = new Proprietario(cpf, "proprietario", "proprietario@email.com", "1234-5678");
        repositorio.inserirProprietario(proprietario);

        proprietarioBuscado = repositorio.buscarProprietario(cpf);
        proprietarioBuscado.setNome("Fulano de tal");
        result = repositorio.atualizarProprietario(proprietarioBuscado);

        // Testa o resultado do método atualizarProprietario
        assertTrue(result);

        proprietarioEncontrado = repositorio.buscarProprietario(cpf);

        // Testa se houver uma atualização no registro no banco
        assertEquals(proprietarioEncontrado.getNome(), "Fulano de tal");
    }

    @Test
    public void testarAtualizacaoDeDadosInexistentes() {

        boolean result;
        Proprietario proprietarioAlterado;

        // Testa a atualização em um registro que não existe
        proprietarioAlterado = new Proprietario("555.555.555.56", "proprietario", "proprietario@email.com", "1234-5678");
        result = repositorio.atualizarProprietario(proprietarioAlterado);

        assertFalse(result);
    }


    @Test
    public void testarRemocaoProprietario() {

        String cpf = "666.666.666.67";
        Proprietario proprietario,
                proprietarioEncontrado;

        proprietario = new Proprietario(cpf, "proprietario", "proprietario@email.com", "1234-5678");
        repositorio.inserirProprietario(proprietario);

        proprietarioEncontrado = repositorio.buscarProprietario(cpf);

        repositorio.removerProprietario(proprietarioEncontrado);

        proprietarioEncontrado = repositorio.buscarProprietario(cpf);

        assertNull(proprietarioEncontrado);
    }


    @Test
    public void testarbuscarTodosProprietarios() {
        Proprietario proprietario = new Proprietario(2, "987.654.321-00", "testarbuscarTodosProprietarios",
                "testarbuscarTodosProprietarios@email.com", "1234-5678");

        repositorio.inserirProprietario(proprietario);

        List<Proprietario> proprietarioList = repositorio.buscarTodosProprietarios();

        boolean found = false;
        for (Proprietario p : proprietarioList) {
            if (p.getCpf().equals("987.654.321-00") &&
                    p.getEmail().equals("testarbuscarTodosProprietarios@email.com") &&
                    p.getNome().equals("testarbuscarTodosProprietarios")) {
                found = true;
            }
        }
        assertTrue(found);
    }
}
