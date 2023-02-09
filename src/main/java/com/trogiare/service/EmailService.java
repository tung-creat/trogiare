package com.trogiare.service;


import com.trogiare.common.enumrate.MailTemplateEnum;
import com.trogiare.config.ServerProperties;
import com.trogiare.model.EmailTemplate;
import com.trogiare.model.User;
import com.trogiare.repo.EmailTemplateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

// Annotation
@Service
// Class
// Implementing EmailService interface
public class EmailService {
    private static Map<String, EmailTemplate> map = new HashMap<>();
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private EmailTemplateRepo emailTemplateRepo;
    @Autowired
    private ServerProperties props;
    @Value("${spring.mail.username}")
    private String sender;

    public void sendVerifyingReq(User user, String token,String refer) throws MessagingException, URISyntaxException {
        String uripath = refer+"confirm/" +token;
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        Map<String, String> query = new HashMap<>();
        query.put("userName", user.getFirstName());
        query.put("verify", uripath);
        String emailTemplate = handleTemplateEmail(query, MailTemplateEnum.EMAIL_VERIFYING);
        helper.setText(emailTemplate, true); // Use this or above line.
        helper.setFrom(sender);
        helper.setTo(user.getEmail());
        helper.setSubject(map.get(MailTemplateEnum.EMAIL_VERIFYING.name()).getSubject());
        mailSender.send(mimeMessage);
    }

    public void sendWelcomeMember(User user) throws MessagingException {
        Map<String, String> query = new HashMap<>();
        query.put("userName", user.getFirstName());
        String emailTemplate = handleTemplateEmail(query, MailTemplateEnum.WELCOME_MEMBER);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setText(emailTemplate, true); // Use this or above line.
        helper.setFrom(sender);
        helper.setTo(user.getEmail());
        helper.setSubject(map.get(MailTemplateEnum.WELCOME_MEMBER.name()).getSubject());
        mailSender.send(mimeMessage);
    }

    public void sendForgotPassword(User user, String otp) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        Map<String, String> query = new HashMap<>();
        query.put("userName", user.getFirstName());
        query.put("verify", otp);
        String emailTemplate = handleTemplateEmail(query, MailTemplateEnum.FORGOT_PASSWORD);
        helper.setText(emailTemplate, true); // Use this or above line.
        helper.setFrom(sender);
        helper.setTo(user.getEmail());
        helper.setSubject(map.get(MailTemplateEnum.FORGOT_PASSWORD.name()).getSubject());
        mailSender.send(mimeMessage);

    }

    public String handleTemplateEmail(Map<String, String> information, MailTemplateEnum mailTemplateEnum) {
        EmailTemplate emailTemplate = getEmailTemplate(mailTemplateEnum.name());
        if (emailTemplate == null) {
            return null;
        }
       EmailTemplate emailResult = emailTemplate;
        String emailContent = emailResult.getContent();
        for (var x : information.entrySet()) {
            emailContent = emailContent.replace("${" + x.getKey() + "}", x.getValue());
        }

      return emailContent;
    }

    public  EmailTemplate getEmailTemplate(String typeEmailTemplate) {
        if (map.size() > 0 && !map.isEmpty()) {
          return  map.get(typeEmailTemplate);
        }
        List<EmailTemplate> emailTemplateList = (List<EmailTemplate>) emailTemplateRepo.findAll();
        map = emailTemplateList.stream()
                .collect(Collectors.toMap(EmailTemplate::getCode, Function.identity()));
        return map.get(typeEmailTemplate);
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
