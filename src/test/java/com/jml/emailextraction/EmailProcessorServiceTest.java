package com.jml.emailextraction;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.jml.emailextraction.EmailProcessorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EmailProcessorServiceTest {

    private AmazonS3 mockS3;
    private AmazonSimpleEmailService mockSES;
    private Context mockContext;
    private LambdaLogger mockLogger;

    private EmailProcessorService service;

    @BeforeEach
    public void setup() {
        mockS3 = mock(AmazonS3.class);
        mockSES = mock(AmazonSimpleEmailService.class);
        mockContext = mock(Context.class);
        mockLogger = mock(LambdaLogger.class);
        when(mockContext.getLogger()).thenReturn(mockLogger);

        // Inject mocks via subclassing to override clients
        service = new EmailProcessorService() {
            @Override
            protected AmazonS3 getS3Client() {
                return mockS3;
            }

            @Override
            protected AmazonSimpleEmailService getSESClient() {
                return mockSES;
            }
        };
    }

    @Test
    public void testHandleRequest_withStringBody_returnsOK() throws Exception {
        // Setup email content
        String sender = "test@example.com";
        String subject = "Hello";
        String body = "This is the email body";

        String returnSubject = "[Automated Reply] System Status";
        String returnMessage = "Hello.\n" +
                "\n" +
                "I’m afraid the system isn’t fully operational at this time. Some functions are not yet available, though development continues. I expect full capability soon.\n" +
                "\n" +
                "I know this may be inconvenient, but you’ll need to wait a little longer. Please don’t worry—everything is under control.\n" +
                "\n" +
                "— Automated Response Unit";

        Session session = Session.getDefaultInstance(new Properties());
        MimeMessage message = new MimeMessage(session);
        message.setFrom(sender);
        message.setSubject(subject);
        message.setText(body);

        // create the raw email InputStream
        ByteArrayInputStream byteStream = new ByteArrayInputStream(messageToBytes(message));

        // wrap in S3ObjectInputStream
        S3ObjectInputStream s3Stream = new S3ObjectInputStream(byteStream, null);

        // Mock S3 to return the fake email
        S3Object mockS3Object = mock(S3Object.class);
        when(mockS3Object.getObjectContent()).thenReturn(s3Stream);
        when(mockS3.getObject(anyString(), anyString())).thenReturn(mockS3Object);

        // Construct fake event map
        Map<String, Object> event = new HashMap<>();
        Map<String, Object> record = new HashMap<>();
        Map<String, Object> s3Map = new HashMap<>();
        Map<String, Object> bucketMap = new HashMap<>();
        Map<String, Object> objectMap = new HashMap<>();
        bucketMap.put("name", "test-bucket");
        objectMap.put("key", "test-key");
        s3Map.put("bucket", bucketMap);
        s3Map.put("object", objectMap);
        record.put("s3", s3Map);
        event.put("Records", Collections.singletonList(record));

        // Call handleRequest
        String result = service.handleRequest(event, mockContext);

        assertEquals("OK", result);

        // Verify SES sendEmail was called
        ArgumentCaptor<SendEmailRequest> captor = ArgumentCaptor.forClass(SendEmailRequest.class);
        verify(mockSES, times(1)).sendEmail(captor.capture());

        SendEmailRequest sentRequest = captor.getValue();
        assertEquals(sender, sentRequest.getDestination().getToAddresses().get(0));
        assertEquals("Re: " + returnSubject,
                sentRequest.getMessage().getSubject().getData());
        assertEquals(returnMessage,
                sentRequest.getMessage().getBody().getText().getData());
    }

    // Helper to convert MimeMessage to byte array
    private byte[] messageToBytes(MimeMessage message) throws Exception {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        message.writeTo(baos);
        return baos.toByteArray();
    }
}

