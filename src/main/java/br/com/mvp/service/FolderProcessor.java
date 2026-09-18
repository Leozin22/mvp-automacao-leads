package br.com.mvp.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FolderProcessor {

    private final Path pastaEntrada;
    private final Path pastaProcessados;
    private final Path pastaErros;

    private final EmailProcessor emailProcessor;

    public FolderProcessor(
            Path pastaEntrada,
            Path pastaProcessados,
            Path pastaErros,
            EmailProcessor emailProcessor) {

        this.pastaEntrada = pastaEntrada;
        this.pastaProcessados = pastaProcessados;
        this.pastaErros = pastaErros;
        this.emailProcessor = emailProcessor;
    }

    public void processarFila() throws IOException {

        criarPastasSeNecessario();

        try (Stream<Path> arquivos =
                     Files.list(pastaEntrada)) {

            arquivos
                    .filter(Files::isRegularFile)
                    .filter(this::arquivoJson)
                    .forEach(this::processarArquivo);
        }
    }

    private boolean arquivoJson(Path arquivo) {

        return arquivo
                .getFileName()
                .toString()
                .toLowerCase()
                .endsWith(".json");
    }

    private void processarArquivo(Path arquivo) {

        try {

            System.out.println(
                    "Arquivo encontrado: "
                            + arquivo.getFileName()
            );

            emailProcessor.processar(arquivo);

            moverArquivo(
                    arquivo,
                    pastaProcessados
            );

            System.out.println(
                    "Arquivo processado com sucesso."
            );

        } catch (Exception e) {

            System.err.println(
                    "Erro ao processar "
                            + arquivo.getFileName()
            );

            System.err.println(
                    e.getMessage()
            );

            try {

                moverArquivo(
                        arquivo,
                        pastaErros
                );

            } catch (IOException erroMovimentacao) {

                System.err.println(
                        "Também não foi possível mover "
                                + "o arquivo para a pasta de erros."
                );

                erroMovimentacao.printStackTrace();
            }
        }
    }

    private void moverArquivo(
            Path origem,
            Path pastaDestino) throws IOException {

        Path destino =
                pastaDestino.resolve(
                        origem.getFileName()
                );

        if (Files.exists(destino)) {

            destino =
                    gerarNomeAlternativo(
                            pastaDestino,
                            origem
                    );
        }

        Files.move(
                origem,
                destino
        );
    }

    private Path gerarNomeAlternativo(
            Path pastaDestino,
            Path origem) {

        String nomeOriginal =
                origem
                        .getFileName()
                        .toString();

        int indicePonto =
                nomeOriginal.lastIndexOf('.');

        String nome;
        String extensao;

        if (indicePonto > 0) {

            nome =
                    nomeOriginal.substring(
                            0,
                            indicePonto
                    );

            extensao =
                    nomeOriginal.substring(
                            indicePonto
                    );

        } else {

            nome = nomeOriginal;
            extensao = "";
        }

        String novoNome =
                nome
                        + "-"
                        + System.currentTimeMillis()
                        + extensao;

        return pastaDestino.resolve(novoNome);
    }

    private void criarPastasSeNecessario()
            throws IOException {

        Files.createDirectories(
                pastaEntrada
        );

        Files.createDirectories(
                pastaProcessados
        );

        Files.createDirectories(
                pastaErros
        );
    }
}