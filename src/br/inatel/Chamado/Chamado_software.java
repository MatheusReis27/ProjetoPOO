package br.inatel.Chamado;

public class Chamado_software extends Chamado
{
    private int clientes_afetados;

    public Chamado_software(String titulo, int clientes_afetados)
    {
        super(titulo);

        if (clientes_afetados < 0) {
            throw new IllegalArgumentException(
                    "Informe um numero válido de usuários."
            );
        }

        this.clientes_afetados = clientes_afetados;
    }

    public int getClientes_afetados()
    {
        return clientes_afetados;
    }

    @Override
    public String calcularPrioridade() {
        if(clientes_afetados >= 10)
        {
            return "Alta";
        }
        else
            return "Baixa";
    }
}
