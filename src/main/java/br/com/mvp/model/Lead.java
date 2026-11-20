package br.com.mvp.model;

public class Lead {

    private final String emailContato;
    private final IntencaoEmail intencao;
    private final SegmentoLead segmento;
    private final Integer quantidadeAlunos;

    public Lead(
            String emailContato,
            IntencaoEmail intencao,
            SegmentoLead segmento,
            Integer quantidadeAlunos
    ) {
        this.emailContato = emailContato;
        this.intencao = intencao;
        this.segmento = segmento;
        this.quantidadeAlunos = quantidadeAlunos;
    }

    public String getEmailContato() {
        return emailContato;
    }

    public IntencaoEmail getIntencao() {
        return intencao;
    }

    public SegmentoLead getSegmento() {
        return segmento;
    }

    public Integer getQuantidadeAlunos() {
        return quantidadeAlunos;
    }
}