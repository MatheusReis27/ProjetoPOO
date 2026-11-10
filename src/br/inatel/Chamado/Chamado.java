package br.inatel.Chamado;


public abstract class Chamado implements Priorizável
{
    private Status_chamado status;
    private String titulo;

    protected Chamado(String titulo)
    {
        if(titulo == null || titulo.isBlank())
        {
            throw new IllegalArgumentException("O título é obrigatório");
        }
        this.titulo = titulo;
        this.status = new Status_chamado();
    }
    public String getTitulo()
    {
        return titulo;
    }

    public String getStatus()
    {
        return status.stringStatus();
    }

    public void resolver(){
        status.Resolver();
    }
}
