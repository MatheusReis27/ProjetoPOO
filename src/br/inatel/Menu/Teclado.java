package br.inatel.Menu;

import java.util.Scanner;

// Encapsula o Scanner: toda leitura do teclado passa por aqui.
public class Teclado {
    private Scanner entrada;

    public Teclado() {
        this.entrada = new Scanner(System.in);
    }

    public String lerTexto(String pergunta) {
        System.out.print(pergunta);
        return entrada.nextLine().strip();
    }

    public int lerInteiro(String pergunta) {
        return Integer.parseInt(lerTexto(pergunta));
    }

    public boolean lerSimNao(String pergunta) {
        String resposta = lerTexto(pergunta).toLowerCase();

        if (!resposta.equals("s") && !resposta.equals("n")) {
            throw new IllegalArgumentException("Responda com s ou n.");
        }

        return resposta.equals("s");
    }

    public void fechar() {
        entrada.close();
    }
}