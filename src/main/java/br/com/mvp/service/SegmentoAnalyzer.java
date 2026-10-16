package br.com.mvp.service;

import br.com.mvp.model.SegmentoLead;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class SegmentoAnalyzer {

    public SegmentoLead analisar(
            String remetente,
            String assunto,
            String corpoTexto
    ) {
        String dominio = extrairDominio(remetente);

        String texto = normalizar(
                (assunto == null ? "" : assunto) + " " +
                        (corpoTexto == null ? "" : corpoTexto)
        );

        // 1. ESCOLA PÚBLICA
        boolean publica =
                contemAlgum(texto,
                        "escola publica",
                        "escola municipal",
                        "escola estadual",
                        "rede municipal",
                        "rede estadual",
                        "escola tecnica estadual",
                        "centro municipal de educacao infantil",
                        "centro integrado de educacao de jovens e adultos",
                        "diretoria de ensino",
                        "secretaria municipal de educacao",
                        "secretaria estadual de educacao"
                )
                        || contemAlgum(texto,
                        "emei", "emef", "emeb",
                        "cemei", "cmei", "cieja",
                        "emebs", "emefm", "emeief",
                        "etec", "etesp",
                        "ee", "e.e", "e.e.",
                        "e.m", "e.m."
                )
                        || dominioPublico(dominio);

        // 2. ESCOLA PARTICULAR
        // Somente expressões que indicam explicitamente a rede.
        boolean particular =
                contemAlgum(texto,
                        "escola particular",
                        "colegio particular",
                        "escola privada",
                        "colegio privado",
                        "rede particular de ensino",
                        "rede privada de ensino",
                        "instituicao de ensino particular",
                        "instituicao de ensino privada"
                );

        // 3. B2B
        boolean b2b =
                contemAlgum(texto,
                        "agencia de turismo",
                        "agencia de viagens",
                        "operadora de turismo",
                        "operadora de viagens",
                        "sindicato",
                        "associacao",
                        "apae",
                        "grupo empresarial",
                        "grupo corporativo",
                        "grupo de funcionarios",
                        "grupo de colaboradores",
                        "grupo de associados",
                        "empresa parceira",
                        "grupo de excursao",
                        "grupo organizado",
                        "excursao em grupo",
                        "caravana"
                );

        // 4. CONTAGEM DAS EVIDÊNCIAS
        int segmentosEncontrados = 0;

        if (publica) {
            segmentosEncontrados++;
        }

        if (particular) {
            segmentosEncontrados++;
        }

        if (b2b) {
            segmentosEncontrados++;
        }

        // Nenhum segmento ou conflito entre segmentos.
        if (segmentosEncontrados != 1) {
            return SegmentoLead.INCERTO;
        }

        // Aqui sabemos que apenas um segmento foi identificado.
        if (publica) {
            return SegmentoLead.ESCOLA_PUBLICA;
        }

        if (particular) {
            return SegmentoLead.ESCOLA_PARTICULAR;
        }

        return SegmentoLead.B2B;
    }

    // Compatibilidade com chamadas antigas.
    public SegmentoLead analisar(
            String assunto,
            String corpoTexto
    ) {
        return analisar("", assunto, corpoTexto);
    }

    // Compara somente os domínios cadastrados, por igualdade.
    private boolean dominioPublico(String dominio) {
        return dominio.equals("educacao.sp.gov.br")
                || dominio.equals("cps.sp.gov.br")
                || dominio.equals("etec.sp.gov.br");
    }

    // Espera um endereço simples, como nome@dominio.com.br.
    private String extrairDominio(String remetente) {
        if (remetente == null) {
            return "";
        }

        String endereco = remetente
                .trim()
                .toLowerCase(Locale.ROOT);

        int posicaoArroba = endereco.indexOf('@');

        if (posicaoArroba <= 0
                || posicaoArroba != endereco.lastIndexOf('@')
                || posicaoArroba == endereco.length() - 1
                || Pattern.compile("\\s").matcher(endereco).find()
                || endereco.contains("<")
                || endereco.contains(">")) {
            return "";
        }

        return endereco.substring(posicaoArroba + 1);
    }

    // Protege tanto palavras quanto siglas.
    private boolean contemAlgum(
            String texto,
            String... termos
    ) {
        for (String termo : termos) {
            String regex =
                    "(?<![\\p{L}\\p{N}_])"
                            + Pattern.quote(normalizar(termo))
                            + "(?![\\p{L}\\p{N}_])";

            if (Pattern.compile(regex).matcher(texto).find()) {
                return true;
            }
        }

        return false;
    }

    private String normalizar(String texto) {
        if (texto == null) {
            return "";
        }

        return Normalizer
                .normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ")
                .trim();
    }
}