package utils;

import java.util.Properties;

import jakarta.mail.MessagingException;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class MailUtil {
	public static void sendMail(String to, String from, String subject, String body, boolean isBodyHtml) throws MessagingException {
		// 1 - Get a email session
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host", "localhost");
		props.put("mail.smtp.port", 25);
		
		Session session = Session.getDefaultInstance(props);
		session.setDebug(true);
		
		// 2 - Create a message
		Message message = new MimeMessage(session);
		message.setSubject(subject);
		if (isBodyHtml) {
			message.setContent(body, "text/html");
		} else {
			message.setText(body);
		}
		
		// 3 - Address the message
		Address fromAddress = new InternetAddress(from);
		Address toAddress = new InternetAddress(to);
		message.setFrom(fromAddress);
		message.setRecipient(Message.RecipientType.TO, toAddress);
		
		// 4 - Send the message
		Transport.send(message);
	}
	
	public static void sendGmail(String to, String from, String subject, String body, boolean isBodyHtml) throws MessagingException {
		// 1 - Get an email session
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtps");
		props.put("mail.smtps.host", "smtp.gmail.com");
		props.put("mail.smtps.port", 465);
		props.put("mail.smtps.auth", "true");
		props.put("mail.smtps.quitwait", "false");
		
		Session session = Session.getDefaultInstance(props);
		session.setDebug(true);
		
		// 2 - Create a message
		Message message = new MimeMessage(session);
		message.setSubject(subject);
		if (isBodyHtml) {
			message.setContent(body, "text/html");
		} else {
			message.setText(body);
		}
		
		// 3 - Address the message
		Address fromAddress = new InternetAddress(from);
		Address toAddress = new InternetAddress(to);
		message.setFrom(fromAddress);
		message.setRecipient(Message.RecipientType.TO, toAddress);
		
		// 4 - Send the message
		Transport transport = session.getTransport();
		transport.connect("nguyenquanghuy070306@gmail.com", "qrey rgsm lnhi ulxx");
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
	}
}
