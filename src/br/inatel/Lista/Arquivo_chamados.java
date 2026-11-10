package br.inatel.Lista;

import br.inatel.Chamado.Chamado;
import br.inatel.Excecao.ChamadoResolvidoException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class Arquivo_chamados {
    private final Path caminho;

    public Arquivo_chamados(Path caminho) {
        this.caminho = caminho.toAbsolutePath();
    }

    public Lista_chamados carregar() throws IOException {
        Lista_chamados chamados = new Lista_chamados();
        String conteudo;

        try {
            conteudo = Files.readString(caminho);
        } catch (NoSuchFileException e) {
            return chamados;
        }

        String[] linhas = conteudo.split("\\r\\n|\\n|\\r");

        for (int i = 0; i < linhas.length; i++) {
            if (linhas[i].isBlank()) {
                continue;
            }

            try {
                chamados.Adicionar(Conversor_chamados.deLinha(linhas[i]));
            } catch (IllegalArgumentException | ChamadoResolvidoException e) {
                throw new IOException(
                        "Linha " + (i + 1) + " do arquivo: " + e.getMessage(), e
                );
            }
        }

        return chamados;
    }

    public void salvar(Chamado[] chamados) throws IOException {
        StringBuilder conteudo = new StringBuilder();

        for (Chamado chamado : chamados) {
            if (chamado != null) {
                conteudo.append(Conversor_chamados.paraLinha(chamado));
                conteudo.append(System.lineSeparator());
            }
        }

        Files.writeString(caminho, conteudo.toString());
    }
}