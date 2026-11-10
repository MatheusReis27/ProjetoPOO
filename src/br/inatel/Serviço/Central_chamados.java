package br.inatel.Serviço;

import br.inatel.Chamado.Chamado;
import br.inatel.Lista.Arquivo_chamados;
import br.inatel.Lista.Lista_chamados;
import br.inatel.Excecao.ChamadoResolvidoException;

import java.io.IOException;

public class Central_chamados {
    private Arquivo_chamados arquivo;

    public Central_chamados(Arquivo_chamados arquivo) {
        this.arquivo = arquivo;
    }

    public void cadastrar(Chamado chamado) throws IOException {
        Lista_chamados chamados = arquivo.carregar();
        chamados.Adicionar(chamado);
        arquivo.salvar(chamados.paraArray());
    }

    public Chamado[] listar() throws IOException {
        return arquivo.carregar().paraArray();
    }

    public void resolver(int numero) throws IOException, ChamadoResolvidoException {
        Lista_chamados chamados = arquivo.carregar();
        chamados.buscar(numero).resolver();
        arquivo.salvar(chamados.paraArray());
    }
}