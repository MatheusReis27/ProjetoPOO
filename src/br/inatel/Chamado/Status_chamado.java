package br.inatel.Chamado;

public class Status_chamado

{
    public boolean resolvido;

    public Status_chamado()
    {
        this.resolvido = false;
    }

    public void Resolver()
    {
        if(this.resolvido)
        {
            throw new IllegalStateException("O chamado já está resolvido.");
        }
        resolvido = true;
    }
    public String stringStatus() {
        return resolvido ? "RESOLVIDO" : "ABERTO";
    }

}
