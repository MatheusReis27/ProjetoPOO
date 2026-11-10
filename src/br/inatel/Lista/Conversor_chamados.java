package br.inatel.Lista;

import br.inatel.Chamado.Chamado;
import br.inatel.Chamado.Chamado_hardware;
import br.inatel.Chamado.Chamado_software;
import br.inatel.Excecao.ChamadoResolvidoException;

// Converte um chamado em linha de texto e uma linha de texto em chamado.
// Classe utilitaria: so tem metodos static, nao precisa criar objeto.
public class Conversor_chamados {

    public static Chamado deLinha(String linha) throws ChamadoResolvidoException {
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

    public static String paraLinha(Chamado chamado) {
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