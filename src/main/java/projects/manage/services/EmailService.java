package projects.manage.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import projects.manage.entities.Users;
import java.util.concurrent.CompletableFuture;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendRestPassword(Users user){
        CompletableFuture.runAsync(() -> {
            SimpleMailMessage message = new SimpleMailMessage();
            String tokenRest = user.getTokenRest();
            message.setTo(user.getEmail());
            message.setSubject("Rest Password");
            message.setText("Hello " + user.getFullName() + ", \n\n"
                    +"We send you confirm code to rest your password:  " + tokenRest + ". \n\n"
                    +"Please do not share this code to anyone !, this code valid in 5 minutes\n"
                    +"Best regards, \n"
                    +"Dev Team"
            );
            message.setFrom("nhanphmhoang@gmail.com");
            mailSender.send(message);
        });
    }
}
