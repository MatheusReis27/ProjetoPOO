package br.inatel;

import br.inatel.Chamado.Chamado;
import br.inatel.Chamado.Chamado_hardware;
import br.inatel.Chamado.Chamado_software;
import br.inatel.Lista.Arquivo_chamados;
import br.inatel.Serviço.Central_chamados;

import java.io.IOException;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Arquivo_chamados arquivo = new Arquivo_chamados(Path.of("chamados.txt"));
        Central_chamados central = new Central_chamados(arquivo);

        try (Scanner entrada = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n1. Cadastrar chamado");
                System.out.println("2. Listar chamados");
                System.out.println("3. Resolver chamado");
                System.out.println("0. Sair");

                String opcao = ler(entrada, "Opção: ");

                try {
                    switch (opcao) {
                        case "1":
                            cadastrar(entrada, central);
                            break;
                        case "2":
                            listar(central);
                            break;
                        case "3":
                            int numero = Integer.parseInt(
                                    ler(entrada, "Número do chamado: ")
                            );
                            central.resolver(numero);
                            System.out.println("Chamado resolvido.");
                            break;
                        case "0":
                            return;
                        default:
                            System.out.println("Opção inválida.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Digite um número inteiro.");
                } catch (IOException | IllegalArgumentException | IllegalStateException e) {
                    System.out.println("Erro: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("\nEntrada encerrada.");
        }
    }

    private static String ler(Scanner entrada, String pergunta) {
        System.out.print(pergunta);
        return entrada.nextLine().strip();
    }

    private static void cadastrar(Scanner entrada, Central_chamados central)
            throws IOException {
        String tipo = ler(entrada, "Tipo (1 = hardware, 2 = software): ");

        if (!tipo.equals("1") && !tipo.equals("2")) {
            throw new IllegalArgumentException("Tipo inválido.");
        }

        String titulo = ler(entrada, "Título: ");
        Chamado chamado;

        if (tipo.equals("1")) {
            String resposta = ler(entrada, "Equipamento parou? (s/n): ");

            if (!resposta.equalsIgnoreCase("s") && !resposta.equalsIgnoreCase("n")) {
                throw new IllegalArgumentException("Responda com s ou n.");
            }

            chamado = new Chamado_hardware(titulo, resposta.equalsIgnoreCase("s"));
        } else {
            int quantidade = Integer.parseInt(ler(entrada, "Usuários afetados: "));
            chamado = new Chamado_software(titulo, quantidade);
        }

        central.cadastrar(chamado);
        System.out.println("Chamado cadastrado.");
    }

    private static void listar(Central_chamados central) throws IOException {
        Chamado[] chamados = central.listar();

        if (chamados.length == 0) {
            System.out.println("Nenhum chamado cadastrado.");
            return;
        }

        for (int i = 0; i < chamados.length; i++) {
            Chamado chamado = chamados[i];
            System.out.printf("%d. %s | %s | %s%n",
                    i + 1,
                    chamado.getTitulo(),
                    chamado.calcularPrioridade(),
                    chamado.getStatus());
        }
    }
}