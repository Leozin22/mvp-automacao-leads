package br.com.mvp.service;

import br.com.mvp.model.EmailMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;

import java.nio.file.Path;

public class EmailProcessor {

    private final ObjectMapper mapper;

    public EmailProcessor() {
        this.mapper = new ObjectMapper();
    }

    public void processar(Path arquivo) throws Exception {

        EmailMessage email =
                mapper.readValue(
                        arquivo.toFile(),
                        EmailMessage.class
                );

        String corpoTexto =
                Jsoup.parse(
                        email.getBodyHtml()
                ).text();

        System.out.println();
        System.out.println("==============================");
        System.out.println("PROCESSANDO E-MAIL");
        System.out.println("==============================");

        System.out.println(
                "Arquivo: " + arquivo.getFileName()
        );

        System.out.println(
                "Remetente: " + email.getSenderEmail()
        );

        System.out.println(
                "Assunto: " + email.getSubject()
        );

        System.out.println(
                "Recebido: " + email.getReceivedAt()
        );

        System.out.println("Mensagem:");

        System.out.println(corpoTexto);

        System.out.println("==============================");
    }
}