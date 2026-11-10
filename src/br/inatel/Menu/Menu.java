package br.inatel.menu;

import br.inatel.Chamado.Chamado;
import br.inatel.Chamado.Chamado_hardware;
import br.inatel.Chamado.Chamado_software;
import br.inatel.Serviço.Central_chamados;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Menu {
    private Central_chamados central;
    private Scanner entrada;

    public Menu(Central_chamados central) {
        this.central = central;
        this.entrada = new Scanner(System.in);
    }

    public void executar() {
        try {
            while (true) {
                exibirOpcoes();

                try {
                    switch (ler("Opção: ")) {
                        case "1" -> cadastrar();
                        case "2" -> listar();
                        case "3" -> resolver();
                        case "0" -> { return; }
                        default -> System.out.println("Opção inválida.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Digite um número inteiro.");
                } catch (IOException | IllegalArgumentException | IllegalStateException e) {
                    System.out.println("Erro: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("\nEntrada encerrada.");
        } finally {
            entrada.close();
        }
    }

    private void exibirOpcoes() {
        System.out.println("\n1. Cadastrar chamado\n2. Listar chamados\n3. Resolver chamado\n0. Sair");
    }

    private String ler(String pergunta) {
        System.out.print(pergunta);
        return entrada.nextLine().strip();
    }

    private void cadastrar() throws IOException {
        String tipo = ler("Tipo (1 = hardware, 2 = software): ");

        if (!tipo.equals("1") && !tipo.equals("2")) {
            throw new IllegalArgumentException("Tipo inválido.");
        }

        String titulo = ler("Título: ");
        Chamado chamado;

        if (tipo.equals("1")) {
            String resposta = ler("Equipamento parou? (s/n): ").toLowerCase();

            if (!resposta.equals("s") && !resposta.equals("n")) {
                throw new IllegalArgumentException("Responda com s ou n.");
            }

            chamado = new Chamado_hardware(titulo, resposta.equals("s"));
        } else {
            chamado = new Chamado_software(titulo, Integer.parseInt(ler("Usuários afetados: ")));
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

    private void resolver() throws IOException {
        central.resolver(Integer.parseInt(ler("Número do chamado: ")));
        System.out.println("Chamado resolvido.");
    }
}