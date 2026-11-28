package pt.ubi.di.pdm.modadebolsa.data;

/**
 * Valores possíveis das características de um tecido.
 * É a ÚNICA fonte de verdade: os dados iniciais (DadosIniciais) e os chips do
 * painel de filtros usam estas constantes, por isso o texto guardado na BD
 * é sempre igual ao texto do chip (a query de filtro compara com "=").
 */
public final class ValoresTecido {

    private ValoresTecido() { }

    // Composição
    public static final String ORGANICA = "Orgânica";
    public static final String SINTETICA = "Sintética";
    public static final String MISTA = "Mista";

    // Caimento
    public static final String FLUIDO = "Fluido";
    public static final String RIGIDO = "Rígido";
    public static final String EQUILIBRADO = "Equilibrado";

    // Elasticidade, transparência, amarrotabilidade e durabilidade
    public static final String ALTA = "Alta";
    public static final String MEDIA = "Média";
    public static final String BAIXA = "Baixa";

    public static final String[] COMPOSICOES = {ORGANICA, SINTETICA, MISTA};
    public static final String[] CAIMENTOS = {FLUIDO, RIGIDO, EQUILIBRADO};
    public static final String[] NIVEIS = {ALTA, MEDIA, BAIXA};
}
