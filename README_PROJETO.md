# Desenvolvimento do sistema de chamados em Java

## 1. Como faço `Chamado_hardware` reaproveitar a validação do título com `super(titulo)` e retornar `ALTA` se o equipamento parou?

Faça `Chamado_hardware` herdar de `Chamado`. A chamada `super(titulo)` executa o construtor da classe abstrata, que valida e guarda o título. A subclasse guarda apenas o dado específico de hardware e implementa a regra de prioridade.

```java
package br.inatel.Chamado;

public class Chamado_hardware extends Chamado {
    private final boolean equipamentoParado;

    public Chamado_hardware(String titulo, boolean equipamentoParado) {
        super(titulo);
        this.equipamentoParado = equipamentoParado;
    }

    public boolean isEquipamentoParado() {
        return equipamentoParado;
    }

    @Override
    public String calcularPrioridade() {
        return equipamentoParado ? "ALTA" : "NORMAL";
    }
}
```

## 2. Como monto uma `Main` temporária que percorra um `Chamado[]` com hardware e software, chame `calcularPrioridade()` e teste se resolver um chamado afeta apenas o objeto escolhido?

Crie o array com `new Chamado[2]` e preencha cada posição. O laço chama o mesmo método nos dois objetos, e o Java executa a implementação da subclasse correspondente. Depois, resolva apenas o chamado da posição zero e imprima os dois estados para conferir se cada objeto tem seu próprio status.

```java
package br.inatel;

import br.inatel.Chamado.Chamado;
import br.inatel.Chamado.Chamado_hardware;
import br.inatel.Chamado.Chamado_software;

public class Main {
    public static void main(String[] args) {
        Chamado[] chamados = new Chamado[2];
        chamados[0] = new Chamado_hardware("Computador não liga", true);
        chamados[1] = new Chamado_software("Erro no relatório", 2);

        for (Chamado chamado : chamados) {
            System.out.println(chamado.getTitulo() + " | "
                    + chamado.calcularPrioridade() + " | " + chamado.getStatus());
        }

        chamados[0].resolver();
        System.out.println(chamados[0].getStatus());
        System.out.println(chamados[1].getStatus());
    }
}
```

```text
Computador não liga | ALTA | ABERTO
Erro no relatório | NORMAL | ABERTO
RESOLVIDO
ABERTO
```

## 3. Como implemento `Arquivo_chamados.salvar()` com `Files.writeString()`? Quero gravar uma linha por chamado com tipo, título, dado específico e estado.

Monte o conteúdo em um `StringBuilder` e grave uma vez no `Path caminho` guardado pela classe. Cada subtipo fornece um dado específico. Para hardware, grave se o equipamento parou. Para software, grave o número de usuários afetados.

```java
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
        dado = Boolean.toString(hardware.isEquipamentoParado());
    } else if (chamado instanceof Chamado_software) {
        Chamado_software software = (Chamado_software) chamado;
        tipo = "SOFTWARE";
        dado = Integer.toString(software.getUsuariosAfetados());
    } else {
        throw new IllegalArgumentException("Tipo de chamado desconhecido.");
    }

    return tipo + ";" + chamado.getTitulo() + ";" + dado
            + ";" + chamado.getStatus();
}
```

`Files.writeString()` está disponível a partir do Java 11. Por padrão, cria o arquivo se ele ainda não existir e substitui o conteúdo se já existir. Passe o array com todos os chamados para preservar os registros anteriores. A prioridade é calculada novamente a partir do dado salvo. Um registro de hardware resolvido fica assim.

```text
HARDWARE;Computador não liga;true;RESOLVIDO
```

## 4. O que o `-1` faz em `split(";", -1)`? Como valido um título que contém o separador e um campo de hardware que deveria ser `true` ou `false`?

O `-1` mantém campos vazios no final. Por exemplo, `HARDWARE;Computador não liga;true;` produz quatro campos, com `campos[3]` igual a `""`. Isso permite identificar que o status está vazio. Verifique a quantidade de campos antes de acessar qualquer posição.

No construtor de `Chamado`, depois de verificar se o título existe, rejeite o separador e as quebras de linha. Esses caracteres mudariam a estrutura do registro.

```java
if (titulo.contains(";") || titulo.contains("\n") || titulo.contains("\r")) {
    throw new IllegalArgumentException(
            "O título não pode conter ponto e vírgula ou quebra de linha."
    );
}
```

Na leitura de um registro de hardware, valide o texto antes de converter. `Boolean.parseBoolean("sim")`, por exemplo, retorna `false`, então a validação evita interpretar um valor inválido como equipamento funcionando.

```java
String dado = campos[2];

if (!dado.equals("true") && !dado.equals("false")) {
    throw new IllegalArgumentException("O campo de hardware deve ser true ou false.");
}

boolean equipamentoParado = Boolean.parseBoolean(dado);
```

## 5. Como testo se os dois tipos de chamado e seus estados continuam corretos depois de fechar e abrir o programa?

Use um arquivo de teste separado, começando sem registros. Execute o programa duas vezes com o mesmo diretório de trabalho.

1. Na primeira execução, cadastre um chamado de hardware com equipamento parado e um de software com dois usuários afetados.
2. Resolva apenas o chamado de hardware e saia pelo menu.
3. Verifique se o TXT contém os dois registros abaixo.
4. Abra o programa novamente e escolha **Listar chamados**. Confirme que hardware aparece como `ALTA | RESOLVIDO` e software como `NORMAL | ABERTO`. Tente resolver novamente o chamado de hardware e confira se o programa informa que ele já está resolvido.

```text
HARDWARE;Computador não liga;true;RESOLVIDO
SOFTWARE;Erro no relatório;2;ABERTO
```

## 6. Como faço `Central_chamados` receber `Arquivo_chamados` pelo construtor e coordenar o cadastro, a listagem e a resolução? Em que ordem devo carregar, alterar e salvar os dados?

Crie `Arquivo_chamados` na `Main` e passe o objeto para o construtor da central. A central carrega os registros antes de cada operação. Para cadastrar ou resolver, ela altera os objetos carregados e salva o array completo. Para listar, basta carregar e devolver o array.

```java
package br.inatel.Serviço;

import br.inatel.Chamado.Chamado;
import br.inatel.Lista.Arquivo_chamados;
import br.inatel.Lista.Lista_chamados;

import java.io.IOException;

public class Central_chamados {
    private final Arquivo_chamados arquivo;

    public Central_chamados(Arquivo_chamados arquivo) {
        this.arquivo = arquivo;
    }

    public void cadastrar(Chamado chamado) throws IOException {
        Lista_chamados chamados = arquivo.carregar();
        chamados.adicionar(chamado);
        arquivo.salvar(chamados.paraArray());
    }

    public Chamado[] listar() throws IOException {
        return arquivo.carregar().paraArray();
    }

    public void resolver(int numero) throws IOException {
        Lista_chamados chamados = arquivo.carregar();
        Chamado chamado = chamados.buscar(numero);
        chamado.resolver();
        arquivo.salvar(chamados.paraArray());
    }
}
```

Na `Main`, com o import de `java.nio.file.Path`, a ligação entre os objetos fica assim.

```java
Arquivo_chamados arquivo = new Arquivo_chamados(Path.of("chamados.txt"));
Central_chamados central = new Central_chamados(arquivo);
```

Se a busca ou a resolução lançar uma exceção, a execução para antes de `salvar()`. Se ocorrer uma falha de leitura ou gravação, a `IOException` chega ao menu. Mostre a mensagem de sucesso somente depois que a operação da central retornar normalmente.
