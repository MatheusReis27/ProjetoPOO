package br.inatel.Lista;

import br.inatel.Chamado.Chamado;

public class Lista_chamados {
    private Chamado[] chamadosArray = new Chamado[20];
    private int quantidade;
    
    public void Adicionar(Chamado chamado) 
    {
        if (quantidade == chamadosArray.length) {
            Chamado[] chamados_maior = new Chamado[chamadosArray.length * 2];

            for (int i = 0; i < quantidade; i++) {
                chamados_maior[i] = chamadosArray[i];
            }

            chamadosArray = chamados_maior;
        }

        chamadosArray[quantidade] = chamado;
        quantidade++;
    }

    public Chamado buscar(int numero) {
        if (numero < 1 || numero > quantidade) {
            throw new IllegalArgumentException("Número de chamado inválido.");
        }
        return chamadosArray[numero - 1];
    }

    public Chamado[] paraArray() {
        Chamado[] copia = new Chamado[quantidade];

        for (int i = 0; i < quantidade; i++) {
            copia[i] = chamadosArray[i];
        }

        return copia;
    }
}
