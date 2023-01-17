package com.trogiare;

import com.trogiare.common.enumrate.MailTemplateEnum;
import com.trogiare.model.EmailTemplate;
import com.trogiare.repo.EmailTemplateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class TrogiareApplication {
    @Autowired
    private EmailTemplateRepo mailTemplateRepo;
    public static void main(String[] args) {
        SpringApplication.run(TrogiareApplication.class, args);
    }
////    @Bean
////    public CommandLineRunner commandLineRunner() {
////        return args -> {
////            EmailTemplate emailTemplate = new EmailTemplate();
////            emailTemplate.setCode(MailTemplateEnum.EMAIL_VERIFYING.name());
////            emailTemplate.setContent("<html>\n" +
////                    " <head><title>TraXem</title></head>\n" +
////                    " <body>\n" +
////                    " <p>Hi <strong>${userName}</strong></p>\n" +
////                    " <p>Welcome to <strong>Trogiare</strong></p>\n" +
////                    " <br/>\n" +
////                    " <p>To activate your account and verify your email address, please click the following link:\n" +
////                    " <br/>\n" +
////                    " <a href=\"${verify}\" >${verify}</a>\n" +
////                    " <br/>\n" +
////                    " If clicking the link above does not work, copy and paste the URL in a new browser window instead.\n" +
////                    " </p>\n" +
////                    " <br/>\n" +
////                    " <br/>\n" +
////                    " <p>DO NOT REPLY THIS EMAIL, IT IS AN AUTO SYSTEM SENDING!</p>\n" +
////                    " <p>This is a post-only mailing. Replies to this message are not monitored or answered.</p>\n" +
////                    " <p>For questions or concerns about your account, please visit the Trogiare Help Center at <a href=\"http://trogiare.vn\">http://trogiare.vn</a></p>\n" +
////                    " <br/>\n" +
////                    " <p>Thank you very much!</p>\n" +
////                    " <br/>\n" +
////                    " <hr/>\n" +
////                    " <p><strong>Trogiare</strong></p>\n" +
////                    " <p>Contact: <strong><i><a href=\"mailto:nghien241120000@gmail.com\">trogiare@gmail.com</a></i></strong></p>\n" +
////                    " <p>Site: <strong><i><a href=\"http://trogiare.vn\">http://trogiare.vn</a></i></strong>\n" +
////                    " <br/>\n" +
////                    " <br/>\n" +
////                    " </body>\n" +
////                    " </html>");
////            emailTemplate.setSubject("[Trogiare] Verify email request");
////            emailTemplate.setType(MailTemplateEnum.EMAIL_VERIFYING.name());
////            EmailTemplate emailTemplate1 = new EmailTemplate();
////            emailTemplate1.setCode(MailTemplateEnum.FORGOT_PASSWORD.name());
////            emailTemplate1.setType(MailTemplateEnum.FORGOT_PASSWORD.name());
////            emailTemplate1.setContent("<html>\n" +
////                    " <head><title>Trogiare</title></head>\n" +
////                    " <body>\n" +
////                    " <p>Hi <strong>${userName}</strong></p>\n" +
////                    " <p>Thank you for using the trogiare application</p>\n" +
////                    " <br/>\n" +
////                    " <p>To complete the password reset process.Your OTP code is \n" +
////                    "<strong>${verify}</strong>\n" +
////                    " </p>\n" +
////                    " <br/>\n" +
////                    " <br/>\n" +
////                    " <p>DO NOT REPLY THIS EMAIL, IT IS AN AUTO SYSTEM SENDING!</p>\n" +
////                    " <br/>\n" +
////                    " <p>Thank you very much!</p>\n" +
////                    " <br/>\n" +
////                    " <hr/>\n" +
////                    " <p><strong>Trogiare Team</strong></p>\n" +
////                    " <p>Contact: <strong><i><a href=\"mailto:nghien241120000@gmail.com\">nghien241120000@gmail.com</a></i></strong></p>\n" +
////                    " <p>Site: <strong><i><a href=\"http://trogiare.vn\">http://trogiare.vn</a></i></strong>\n" +
////                    " <br/>\n" +
////                    " <br/>\n" +
////                    " </body>\n" +
////                    " </html>");
////            emailTemplate1.setSubject("[Trogiare] Forgot Password");
////            EmailTemplate emailTemplate2 = new EmailTemplate();
////            emailTemplate2.setSubject("[Trogiare] Welcome to Trogiare");
////            emailTemplate2.setType(MailTemplateEnum.WELCOME_MEMBER.name());
////            emailTemplate2.setCode(MailTemplateEnum.WELCOME_MEMBER.name());
////            emailTemplate2.setContent("<html>\n" +
////                    " <head><title>Trogiare</title></head>\n" +
////                    " <body>\n" +
////                    " <p>Hi <strong>${userName}</strong></p>\n" +
////                    " <p>Welcome to <strong>Trogiare</strong></p>\n" +
////                    " <br/>\n" +
////                    " <p>Following is your account information, Please keep it in saved place. Thanks!\n" +
////                    " <br/>\n" +
////                    " <p> <b>Account Info:</b></p>\n" +
////                    " <p> User name: <strong>${userName}</strong></p>\n" +
////                    " </p>\n" +
////                    " <br/>\n" +
////                    " <br/>\n" +
////                    " <p>DO NOT REPLY THIS EMAIL, IT IS AN AUTO SYSTEM SENDING!</p>\n" +
////                    " <p>This is a post-only mailing. Replies to this message are not monitored or answered.</p>\n" +
////                    " <p>For questions or concerns about your account, please visit the Trogiare Help Center at <a href=\"http://Trogiare.vn\">http://Trogiare.vn</a></p>\n" +
////                    " <br/>\n" +
////                    " <p>Thank you very much!</p>\n" +
////                    " <br/>\n" +
////                    " <hr/>\n" +
////                    " <p><strong>Trogiare Team</strong></p>\n" +
////                    " <p>Contact: <strong><i><a href=\"mailto:Trogiare@gmail.com\">Trogiare@gmail.com</a></i></strong></p>\n" +
////                    " <p>Site: <strong><i><a href=\"http://Trogiare.vn\">http://Trogiare.vn</a></i></strong>\n" +
////                    " <br/>\n" +
////                    " <br/>\n" +
////                    " </body>\n" +
////                    " </html>\n" +
////                    " ");
////            mailTemplateRepo.saveAll(List.of(emailTemplate,emailTemplate1,emailTemplate2));
////
////        };
//    }
}
