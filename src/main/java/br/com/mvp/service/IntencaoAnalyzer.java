package br.com.mvp.service;

import br.com.mvp.model.IntencaoEmail;

import java.text.Normalizer;
import java.util.Locale;

public class IntencaoAnalyzer {

    public IntencaoEmail analisar(String assunto, String corpoTexto) {
        String texto = normalizar(
                (assunto == null ? "" : assunto) + " " +
                        (corpoTexto == null ? "" : corpoTexto)
        );

        if (contemAlgum(texto,
                "orcamento", "cotacao", "proposta comercial",
                "valor do ingresso", "preco do ingresso",
                "agendar visita", "visita escolar",
                "excursao", "parceria comercial")) {
            return IntencaoEmail.COMERCIAL;
        }

        if (contemAlgum(texto,
                "cancelar inscricao", "descadastrar",
                "falha na entrega", "mensagem automatica")) {
            return IntencaoEmail.NAO_COMERCIAL;
        }

        return IntencaoEmail.INCERTO;
    }

    private boolean contemAlgum(String texto, String... termos) {
        for (String termo : termos) {
            if (texto.contains(termo)) {
                return true;
            }
        }
        return false;
    }

    private String normalizar(String texto) {
        String semAcentos = Normalizer.normalize(
                texto, Normalizer.Form.NFD
        ).replaceAll("\\p{M}", "");

        return semAcentos.toLowerCase(Locale.ROOT);
    }
}
