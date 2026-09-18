package br.com.mvp;

import br.com.mvp.service.EmailProcessor;
import br.com.mvp.service.FolderProcessor;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {

        try {

            Path entrada =
                    Path.of("C:/MVP-Leads/entrada");


            Path processados =
                    Path.of("C:/MVP-Leads/processados");


            Path erros =
                    Path.of("C:/MVP-Leads/erros");

            System.out.println(
                    "Pasta de entrada: "
                            + entrada.toAbsolutePath()
            );
            System.out.println(
                    "Pasta existe? "
                            + Files.exists(entrada)
            );
            System.out.println(
                    "É uma pasta? "
                            + Files.isDirectory(entrada)
            );

            EmailProcessor emailProcessor =
                    new EmailProcessor();

            FolderProcessor folderProcessor =
                    new FolderProcessor(
                            entrada,
                            processados,
                            erros,
                            emailProcessor
                    );

            System.out.println(
                    "Iniciando processamento..."
            );

            folderProcessor.processarFila();

            System.out.println(
                    "Processamento finalizado."
            );

        } catch (Exception e) {

            System.err.println(
                    "Erro geral da aplicação."
            );

            e.printStackTrace();
        }
    }
}