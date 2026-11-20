package br.com.mvp.service;

import br.com.mvp.model.EmailMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import java.nio.file.Path;
import br.com.mvp.model.SegmentoLead;
import br.com.mvp.model.IntencaoEmail;
import br.com.mvp.model.Lead;

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

        IntencaoAnalyzer intencaoAnalyzer = new IntencaoAnalyzer();

        IntencaoEmail intencao = intencaoAnalyzer.analisar(
                email.getSubject(),
                corpoTexto
        );

        SegmentoAnalyzer segmentoAnalyzer = new SegmentoAnalyzer();

        SegmentoLead segmento = segmentoAnalyzer.analisar(
                email.getSenderEmail(),
                email.getSubject(),
                corpoTexto
        );

        DadosLeadExtractor dadosExtractor = new DadosLeadExtractor();

        Integer quantidadeAlunos =
                dadosExtractor.extrairQuantidadeAlunos(corpoTexto);

        Lead lead = new Lead(
                email.getSenderEmail(),
                intencao,
                segmento,
                quantidadeAlunos
        );


        System.out.println("Intenção: " + lead.getIntencao());
        System.out.println("Segmento: " + lead.getSegmento());

        System.out.println(
                "Quantidade de alunos: "
                        + (lead.getQuantidadeAlunos() == null
                        ? "Não informada"
                        : lead.getQuantidadeAlunos())
        );

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

