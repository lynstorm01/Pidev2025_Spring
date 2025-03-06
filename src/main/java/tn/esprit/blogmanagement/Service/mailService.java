package tn.esprit.blogmanagement.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import tn.esprit.blogmanagement.Entity.Comment;
import tn.esprit.blogmanagement.Entity.User;

import java.util.List;

@Service
public class mailService {

    private final JavaMailSender mailSender;
    private final UserService userService;

    @Value("${spring.mail.username}") // Your email address from application.properties
    private String fromEmail;

    public mailService(JavaMailSender mailSender, UserService userService) {
        this.mailSender = mailSender;
        this.userService = userService;
    }
    // 🔴 Email for Pending Post
    public void sendPostPendingEmail(String toEmail, String postTitle ,String userName) {
        String subject = "Your Post is Pending Approval";
        String content = "<html>" +
                "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                "<h2 style='color: #0056b3;'>Hello " + userName + ",</h2>" +
                "<p>We have received your new post submission titled: <strong>\"" + postTitle + "\"</strong>.</p>" +
                "<p>Our administrators are reviewing your post to ensure it meets our community guidelines. You will receive another email once your post has been approved.</p>" +
                "<p>Thank you for your contribution to our platform!</p>" +
                "<hr style='border: none; border-top: 1px solid #ddd;'/>" +
                "<p style='font-size: 12px; color: #777;'>If you have any questions, feel free to contact our support team.</p>" +
                "</body></html>";


        sendEmail(toEmail, subject, content);
    }

    // ✅ Email for Approved Post
    public void sendPostApprovedEmail(String recipientEmail, String userName, String postTitle, String postLink) {
            String subject ="🎉 Your Post Has Been Approved!";
            String emailContent = "<html>" +
                    "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                    "<h2 style='color: #28a745;'>Congratulations " + userName + "!</h2>" +
                    "<p>Your post titled <strong>\"" + postTitle + "\"</strong> has been approved and is now live.</p>" +
                    "<p>You can view your post here: <a href='" + postLink + "' target='_blank'>" + postTitle + "</a></p>" +
                    "<p>Thank you for being an active member of our community.</p>" +
                    "<hr style='border: none; border-top: 1px solid #ddd;'/>" +
                    "<p style='font-size: 12px; color: #777;'>If you have any questions, feel free to reach out.</p>" +
                    "</body></html>";
            sendEmail(recipientEmail, subject, emailContent);
    }

    // ❌ Email for Rejected Post
    public void sendPostRejectedEmail(String recipientEmail, String userName, String postTitle) {
        String subject="❌ Your Post Has Been Rejected";

            String emailContent = "<html>" +
                    "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                    "<h2 style='color: #d9534f;'>Hello " + userName + ",</h2>" +
                    "<p>Unfortunately, your post titled <strong>\"" + postTitle + "\"</strong> has been rejected.</p>" +
                    "<p>We encourage you to review our guidelines and submit a revised version if applicable.</p>" +
                    "<p>If you have any questions, feel free to contact our support team.</p>" +
                    "<hr style='border: none; border-top: 1px solid #ddd;'/>" +
                    "<p style='font-size: 12px; color: #777;'>Thank you for your understanding.</p>" +
                    "</body></html>";

            sendEmail(recipientEmail, subject, emailContent);
    }

    public void sendMentionNotification(String toEmail, String commentContent,String username) {
        String subject ="You were mentioned in a comment!";
        String emailContent = username +" mentioned you in a comment: \n\n" + commentContent;

sendEmail(toEmail, subject, emailContent);    }

    private void sendEmail(String toEmail, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content, true); // HTML content

            mailSender.send(message);
            System.out.println("Email sent to " + toEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
