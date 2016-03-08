
package br.com.tiago.utilitarios;

import br.com.tiago.model.Model;
import br.com.tiago.model.ModelContador;
import br.com.tiago.model.ModelUsuario;
import java.io.File;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class AvisoEmail {

    public static void main(String[] args){
    }
    public void enviaAlerta(Model model, ModelUsuario user, String mensagem, String arquivo, ModelContador contador, String margem){
    
    HtmlEmail email = new HtmlEmail();
    email.setHostName( "smtp.prolinkcontabil.com.br" );
    email.setSmtpPort(587);//tentei com o gmail mas nao consegui
//    email.setContent("mail.smtp.socketFactory.port", "465");
//    email.setContent("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//    email.setSslSmtpPort( "465" );
//    email.setStartTLSRequired(true);
//    email.setSSLOnConnect(true);
    
    email.setAuthenticator( new DefaultAuthenticator( "documentos@prolinkcontabil.com.br" ,  "plkc2004" ) );
    
    try {
        email.setFrom( "documentos@prolinkcontabil.com.br" , "Robozinho");
        email.setDebug(true); 
        email.setSubject( "Prolink: Oi "+user.getNome()+", tenho um aviso para você" );

        String id = email.embed(new File("imagens/robot.png"));
        
        StringBuilder builder = new StringBuilder();

        builder.append("<p style=\"color: blue;\"><span style=\"font-size:20px;\"><strong>Ol&aacute ").append(user.getNome()).append(";</strong></span><br />"
        //+"<span style=\"color:#000000;\"><img alt=\"\" height=\"210\" src=\"http://img.actualidadgadget.com/wp-content/uploads/2015/04/robot-question-300x300.jpg\" style=\"float: right;\" width=\"210\" /></span><br />"
            +"<br />"
            +"<span style=\"color:#000000;\"><em><strong>Eu sou um Rob&ocirc;</strong></em>, </span></p>"
            +"<p style=\"color: blue;\">"
            +"<span style=\"color:#000000;\">Fui programado para verificar documentos f&iacute;sicos que clientes enviaram para a </span><u><strong>Prolink</strong></u><span style=\"color:#000000;\">,</span><br />"
            +"<span style=\"color:#000000;\">Sabe, minha fun&ccedil;&atilde;o &eacute; n&atilde;o deixar que nada se perca.<br />"
            +"<br />"
            +"Estava olhando aqui e percebi que tem alguma coisa mas acho que voc&ecirc; ainda n&atilde;o viu,</span></p>"
            +"<p style=\"color: blue;\">"
            +"<span style=\"color:#000000;\"><em>Olha s&oacute; isso aqui embaixo...</em></span><br />"
            +"<br />"
            +"<span style=\"color:#000000;\">").append(mensagem).append("&nbsp;</p>"
            +"<p style=\"color: blue;\">"
            +"<span style=\"color:#000000;\"><em>Estranho...</em><br />"
            +"Ent&atilde;o! Poderia fazer um favor pra mim?<br />"
            +"Veja o que &eacute; voc&ecirc; recebeu e finalize no sistema <u><em><strong>Controle de Processos!</strong></em></u><br />"
            +"Ah &eacute;! Ele fica bem na sua area de trabalho,<br />"
            +"<em>Alias, tudo aqui est&aacute; relacionado a voc&ecirc;,</em> <span style=\"background-color:#ffff00;\">se n&atilde;o recebeu ou era pra outra pessoa voc&ecirc; pode contestar atraves da op&ccedil;&atilde;o Contestar</span>.<br />"
            +"<br />"
            +"<span style=\"color:#000000;\"><img alt=\"\" height=\"210\" src=\"cid:").append(id).append("\" style=\"float: right;\" width=\"210\" /></span><br />"
            +"Percebi que voc&ecirc; tem uma margem de fechamento de <span style=\"background-color:#ffff00;\">").append(contador.getMedia()).append("%</span>. Isso &eacute; ").append(margem).append("!<br />"
            +"<br />"
            +"Se precisar de mais detalhes tem anexo viu? Muito obrigado e bom trabalho :)</span><br />"
            +"<br />"
            +"<span style=\"color:#808080;\"><em>***Ah! Outra coisa...n&atilde;o responde esse e-mail n&atilde;o, por favor.***</em></span></p>");
        
        EmailAttachment anexo = new EmailAttachment();
        anexo.setPath(arquivo);//pegando o anexo
        anexo.setDisposition(EmailAttachment.ATTACHMENT);
        anexo.setName("Relatorio.pdf");//renomeando o arquivo pdf
        
        email.attach(anexo);
        
        email.setHtmlMsg( builder.toString()+"\n");
        
        email.addTo("suporte.ti@prolinkcontabil.com.br");
        email.send();
        
        System.out.println("Sucesso!");
        model.setMensagem(user.getNome()+"=Email enviado com sucesso!");
        
    } catch (EmailException e) {
        model.setMensagem(user.getNome()+"=Falha ao enviar o email, problema no arquivo gerado!");
    } 
    }
}