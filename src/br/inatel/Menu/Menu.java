package br.inatel.Menu;

import br.inatel.Chamado.Chamado;
import br.inatel.Chamado.Chamado_hardware;
import br.inatel.Chamado.Chamado_software;
import br.inatel.Serviço.Central_chamados;
import br.inatel.Excecao.ChamadoResolvidoException;

import java.io.IOException;
import java.util.NoSuchElementException;

public class Menu {
    private Central_chamados central;
    private Teclado teclado;

    public Menu(Central_chamados central) {
        this.central = central;
        this.teclado = new Teclado();
    }

    public void executar() {
        try {
            while (true) {
                exibirOpcoes();

                try {
                    switch (teclado.lerTexto("Opção: ")) {
                        case "1" -> cadastrar();
                        case "2" -> listar();
                        case "3" -> resolver();
                        case "0" -> { return; }
                        default -> System.out.println("Opção inválida.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Digite um número inteiro.");
                } catch (IOException | ChamadoResolvidoException | IllegalArgumentException e) {
                    System.out.println("Erro: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("\nEntrada encerrada.");
        } finally {
            teclado.fechar();
        }
    }

    private void exibirOpcoes() {
        System.out.println("\n1. Cadastrar chamado\n2. Listar chamados\n3. Resolver chamado\n0. Sair");
    }

    private void cadastrar() throws IOException {
        String tipo = teclado.lerTexto("Tipo (1 = hardware, 2 = software): ");

        if (!tipo.equals("1") && !tipo.equals("2")) {
            throw new IllegalArgumentException("Tipo inválido.");
        }

        String titulo = teclado.lerTexto("Título: ");
        Chamado chamado;

        if (tipo.equals("1")) {
            chamado = new Chamado_hardware(titulo, teclado.lerSimNao("Equipamento parou? (s/n): "));
        } else {
            chamado = new Chamado_software(titulo, teclado.lerInteiro("Usuários afetados: "));
        }

        central.cadastrar(chamado);
        System.out.println("Chamado cadastrado.");
    }

    private void listar() throws IOException {
        Chamado[] chamados = central.listar();

        if (chamados.length == 0) {
            System.out.println("Nenhum chamado cadastrado.");
            return;
        }

        for (int i = 0; i < chamados.length; i++) {
            System.out.printf("%d. %s | %s | %s%n", i + 1, chamados[i].getTitulo(),
                    chamados[i].calcularPrioridade(), chamados[i].getStatus());
        }
    }

    private void resolver() throws IOException, ChamadoResolvidoException {
        central.resolver(teclado.lerInteiro("Número do chamado: "));
        System.out.println("Chamado resolvido.");
    }
}