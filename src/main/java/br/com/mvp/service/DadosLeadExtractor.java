package br.com.mvp.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DadosLeadExtractor {

    private static final Pattern PADRAO_ALUNOS =
            Pattern.compile(
                    "(?<![\\p{L}\\p{N}.,])([0-9]+)\\s+alunos?\\b",
                    Pattern.CASE_INSENSITIVE
                            | Pattern.UNICODE_CHARACTER_CLASS
            );

    public Integer extrairQuantidadeAlunos(String texto) {

        if (texto == null || texto.isBlank()) {
            return null;
        }

        Matcher matcher = PADRAO_ALUNOS.matcher(texto);

        if (matcher.find()) {

            String quantidadeEncontrada = matcher.group(1);

            try {
                int quantidade = Integer.parseInt(
                        quantidadeEncontrada
                );

                return quantidade > 0 ? quantidade : null;

            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }
}