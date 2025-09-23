package com.jml.emailextraction;

import java.util.List;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.Message;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.Properties;
import java.util.Map;
import com.jml.emailextraction.constants.EmailProcessorConstants;

public class EmailProcessorService implements RequestHandler<Map<String, Object>, String> {
    private final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();

    @Override
    public String handleRequest(Map<String, Object> event, Context context) {
        // Extract bucket + object key from event (if S3 trigger)
        Map<String, Object> record = ((Map<String, Object>) ((List<Object>) event.get("Records")).get(0));
        // Grab the S3 section
        Map<String, Object> s3Map = (Map<String, Object>) record.get("s3");

        // Extract bucket info
        Map<String, Object> bucketMap = (Map<String, Object>) s3Map.get("bucket");
        String bucket = (String) bucketMap.get("name");

        // Extract object info
        Map<String, Object> objectMap = (Map<String, Object>) s3Map.get("object");
        String key = (String) objectMap.get("key");

        AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.defaultClient();

        try (InputStream rawEmail = s3.getObject(bucket, key).getObjectContent()) {
            Session session = Session.getDefaultInstance(new Properties());
            MimeMessage message = new MimeMessage(session, rawEmail);

            String subject = message.getSubject();
            String from = message.getFrom()[0].toString();
            Object content = message.getContent();

            context.getLogger().log("Subject: " + subject + " From: " + from);

            // Handle text/plain vs multipart
            if (content instanceof String) {
                context.getLogger().log("Body: " + content);
            } else if (content instanceof Multipart) {
                Multipart mp = (Multipart) content;
                for (int i = 0; i < mp.getCount(); i++) {
                    BodyPart part = mp.getBodyPart(i);
                    if (part.isMimeType("text/plain")) {
                        context.getLogger().log("Text: " + part.getContent());
                    }
                }
            }

            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(new Destination().withToAddresses(from))
                    .withMessage(new Message()
                            .withSubject(new Content().withCharset("UTF-8").withData("Re: " + EmailProcessorConstants.RETURN_SUBJECT))
                            .withBody(new Body().withText(new Content().withCharset("UTF-8").withData(EmailProcessorConstants.AUTO_REPLY_MESSAGE))))
                    .withSource(EmailProcessorConstants.EMAIL_DOMAIN);
            client.sendEmail(request);
        } catch (Exception e) {
            context.getLogger().log("Error processing email: " + e.getMessage());
        }

        return "OK";
    }
}


