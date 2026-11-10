package br.inatel;

import br.inatel.Lista.Arquivo_chamados;
import br.inatel.menu.Menu;
import br.inatel.Serviço.Central_chamados;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Arquivo_chamados arquivo = new Arquivo_chamados(Path.of("chamados.txt"));
        Central_chamados central = new Central_chamados(arquivo);
        Menu menu = new Menu(central);
        menu.executar();
    }
}