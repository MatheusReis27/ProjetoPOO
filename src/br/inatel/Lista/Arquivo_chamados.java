package br.inatel.Lista;

import br.inatel.Chamado.Chamado;
import br.inatel.Chamado.Chamado_hardware;
import br.inatel.Chamado.Chamado_software;

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
                chamados.Adicionar(interpretar(linhas[i]));
            } catch (IllegalArgumentException e) {
                throw new IOException(
                        "Linha " + (i + 1) + " do arquivo: " + e.getMessage(), e
                );
            }
        }

        return chamados;
    }

    private Chamado interpretar(String linha) {
        String[] campos = linha.split(";", -1);

        if (campos.length != 4) {
            throw new IllegalArgumentException("Esperados quatro campos.");
        }

        String tipo = campos[0];
        String titulo = campos[1];
        String dado = campos[2];
        String status = campos[3];
        Chamado chamado;

        switch (tipo) {
            case "HARDWARE":
                if (!dado.equals("true") && !dado.equals("false")) {
                    throw new IllegalArgumentException(
                            "O campo de hardware deve ser true ou false."
                    );
                }
                chamado = new Chamado_hardware(titulo, Boolean.parseBoolean(dado));
                break;

            case "SOFTWARE":
                chamado = new Chamado_software(titulo, Integer.parseInt(dado));
                break;

            default:
                throw new IllegalArgumentException("Tipo de chamado desconhecido.");
        }

        if (status.equals("RESOLVIDO")) {
            chamado.resolver();
        } else if (!status.equals("ABERTO")) {
            throw new IllegalArgumentException("Estado desconhecido.");
        }

        return chamado;
    }

    public void salvar(Chamado[] chamados) throws IOException {
        StringBuilder conteudo = new StringBuilder();

        for (Chamado chamado : chamados) {
            if (chamado != null) {
                conteudo.append(formatarLinha(chamado));
                conteudo.append(System.lineSeparator());
            }
        }

        Files.writeString(caminho, conteudo.toString());
    }

    private String formatarLinha(Chamado chamado) {
        String tipo;
        String dado;

        if (chamado instanceof Chamado_hardware) {
            Chamado_hardware hardware = (Chamado_hardware) chamado;
            tipo = "HARDWARE";
            dado = Boolean.toString(hardware.getEquipamento_parado());
        } else if (chamado instanceof Chamado_software) {
            Chamado_software software = (Chamado_software) chamado;
            tipo = "SOFTWARE";
            dado = Integer.toString(software.getClientes_afetados());
        } else {
            throw new IllegalArgumentException("Tipo de chamado desconhecido.");
        }

        return tipo + ";" + chamado.getTitulo() + ";" + dado
                + ";" + chamado.getStatus();
    }
}