package br.com.mvp;

import br.com.mvp.model.SegmentoLead;
import br.com.mvp.service.SegmentoAnalyzer;

public class TesteSegmentoAnalyzer {

    public static void main(String[] args) {
        SegmentoAnalyzer analyzer = new SegmentoAnalyzer();

        verificar(analyzer, "Escola pública",
                "contato@gmail.com",
                "Somos uma escola municipal.",
                SegmentoLead.ESCOLA_PUBLICA);

        verificar(analyzer, "Escola particular",
                "contato@gmail.com",
                "Somos uma escola particular.",
                SegmentoLead.ESCOLA_PARTICULAR);

        verificar(analyzer, "Agência",
                "contato@gmail.com",
                "Somos uma agência de turismo.",
                SegmentoLead.B2B);

        verificar(analyzer, "Conflito",
                "contato@gmail.com",
                "Somos uma agência de turismo atendendo uma escola municipal.",
                SegmentoLead.INCERTO);

        verificar(analyzer, "Escola sem rede informada",
                "coordenacao.escola@gmail.com",
                "Sou da coordenação pedagógica.",
                SegmentoLead.INCERTO);

        verificar(analyzer, "Sigla dentro de palavra",
                "contato@gmail.com",
                "Precisamos detectar uma falha.",
                SegmentoLead.INCERTO);

        verificar(analyzer, "Sigla isolada",
                "contato@gmail.com",
                "Somos da ETEC e queremos agendar uma visita.",
                SegmentoLead.ESCOLA_PUBLICA);

        verificar(analyzer, "Domínio cadastrado",
                "diretoria@educacao.sp.gov.br",
                "Gostaria de solicitar um orçamento.",
                SegmentoLead.ESCOLA_PUBLICA);

        verificar(analyzer, "Domínio com sufixo diferente",
                "contato@educacao.sp.gov.br.exemplo.com",
                "Gostaria de solicitar um orçamento.",
                SegmentoLead.INCERTO);

        verificar(analyzer, "Campos ausentes",
                null,
                null,
                SegmentoLead.INCERTO);
    }

    private static void verificar(
            SegmentoAnalyzer analyzer,
            String nomeTeste,
            String remetente,
            String corpo,
            SegmentoLead esperado
    ) {
        SegmentoLead resultado = analyzer.analisar(
                remetente, "", corpo
        );

        if (resultado != esperado) {
            throw new AssertionError(
                    nomeTeste
                            + " | esperado: " + esperado
                            + " | obtido: " + resultado
            );
        }

        System.out.println("OK: " + nomeTeste + " -> " + resultado);
    }
}