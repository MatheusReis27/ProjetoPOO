package br.inatel.Chamado;

public class Chamado_hardware extends Chamado{
    private boolean Equipamento_parado;
    
    public Chamado_hardware(String titulo, boolean Equipamento_parado)
    {
        super(titulo);
        this.Equipamento_parado = Equipamento_parado;
    }
    
    public boolean getEquipamento_parado()
    {
        return Equipamento_parado;
    }

    @Override
    public String calcularPrioridade() {
        return Equipamento_parado ? "Alta" : "Normal";
    }
}
