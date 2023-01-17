package com.trogiare.service;


import com.trogiare.common.enumrate.MailTemplateEnum;
import com.trogiare.config.ServerProperties;
import com.trogiare.model.EmailTemplate;
import com.trogiare.model.User;
import com.trogiare.repo.EmailTemplateRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// Annotation
@Service
// Class
// Implementing EmailService interface
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private EmailTemplateRepo emailTemplateRepo;
    @Autowired
    private ServerProperties props;
    @Value("${spring.mail.username}")
    private String sender;
    private final String URI_CONFIRM_EMAIL = "/confirm";


    public void verifyEmail(User user,String token) throws MessagingException, URISyntaxException {
        Map<String, String> param = new HashMap<>();
        param.put("token",token);
        String uripath = props.getURIPath(URI_CONFIRM_EMAIL,param);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        Map<String, String> query = new HashMap<>();
        query.put("userName", user.getFirstName());
        query.put("verify",uripath);
        EmailTemplate emailTemplate = handleTemplateEmail(query,MailTemplateEnum.EMAIL_VERIFYING);
        helper.setText(emailTemplate.getContent(), true); // Use this or above line.
        helper.setFrom(sender);
        helper.setTo(user.getEmail());
        helper.setSubject(emailTemplate.getSubject());
        mailSender.send(mimeMessage);
    }

    public EmailTemplate handleTemplateEmail(Map<String, String> information, MailTemplateEnum mailTemplateEnum) {
        Optional<EmailTemplate> emailTemplateOP = emailTemplateRepo.findByCode(mailTemplateEnum.name());
        EmailTemplate emailTemplate;
        if (emailTemplateOP.isPresent()) {
            emailTemplate = emailTemplateOP.get();
            String emailContent = emailTemplate.getContent();
            for (var x : information.entrySet()) {
                emailContent = emailContent.replace("${" +x.getKey() + "}", x.getValue());
            }
            emailTemplate.setContent(emailContent);
            return emailTemplate;
        }
        return null;
    }


    // Method 2
    // To send an email with attachment
//        public String
//        sendMailWithAttachment(EmailDetails details)
//        {
//            // Creating a mime message
//            MimeMessage mimeMessage
//                    = javaMailSender.createMimeMessage();
//            MimeMessageHelper mimeMessageHelper;
//
//            try {
//                mimeMessageHelper
//                        = new MimeMessageHelper(mimeMessage, true);
//                mimeMessageHelper.setFrom(sender);
//                mimeMessageHelper.setTo(details.getRecipient());
//                mimeMessageHelper.setText(details.getMsgBody());
//                mimeMessageHelper.setSubject(
//                        details.getSubject());
//
//                // Adding the attachment
//                FileSystemResource file
//                        = new FileSystemResource(
//                        new File(details.getAttachment()));
//
//                mimeMessageHelper.addAttachment(
//                        file.getFilename(), file);
//
//                // Sending the mail
//                javaMailSender.send(mimeMessage);
//                return "Mail sent Successfully";
//            }
//
//            // Catch block to handle MessagingException
//            catch (MessagingException e) {
//
//                // Display message when exception occurred
//                return "Error while sending mail!!!";
//            }
//        }
}
