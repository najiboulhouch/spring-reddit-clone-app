package net.najiboulhouch.redditcloneapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class MailContentBuilder {

    private final TemplateEngine templateEngine;

    public String buildEmail(String msg){
        Context context = new Context();
        context.setVariable("message" , msg);
        return templateEngine.process("mailTemplate" , context);
    }
}
