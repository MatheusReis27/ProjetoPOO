package br.inatel.Chamado;

import br.inatel.Excecao.ChamadoResolvidoException;

public class Status_chamado

{
    public boolean resolvido;

    public Status_chamado()
    {
        this.resolvido = false;
    }

    public void Resolver() throws ChamadoResolvidoException
    {
        if(this.resolvido)
        {
            throw new ChamadoResolvidoException("O chamado já está resolvido.");
        }
        resolvido = true;
    }
    public String stringStatus() {
        return resolvido ? "RESOLVIDO" : "ABERTO";
    }

}