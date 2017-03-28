package org.sitenv.xdrmessagesender.services;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.sitenv.xdrmessagesender.dto.MessageSenderResult;
import org.sitenv.xdrmessagesender.dto.XdrMessageMetaDataProperties;
import org.sitenv.xdrmessagesender.services.enums.XdrMessageType;
import org.sitenv.xdrmessagesender.utils.XdrMessageSenderUtils;
import org.sitenv.xdrmessagesender.utils.XdrSocket;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Brian on 1/19/2017.
 */
@Service
public class MessageSenderService {

    public MessageSenderResult sendMessageWithAttachment(URL endpoint, byte[] attachmentFile, XdrMessageMetaDataProperties messageProperties, XdrMessageType xdrMessageType){
        MessageSenderResult messageSenderResult = new MessageSenderResult();
        String payload = null;
        try {
            String metaDataFileContents = getMetaData(xdrMessageType);
            String xdrMessageMetaData = prepareXdrMessageMetaData(endpoint, metaDataFileContents, messageProperties);
            String mtomPackage = XdrMessageSenderUtils.createMtomPackage(xdrMessageMetaData, new String(attachmentFile));
            payload = XdrMessageSenderUtils.addHTTPHeadersToPayload(endpoint, mtomPackage);
            String response = XdrSocket.sendMessage(endpoint, payload);
            setMessageSenderResult(messageSenderResult, true, response, payload);
        } catch (IOException e) {
            setMessageSenderResult(messageSenderResult, false, ExceptionUtils.getStackTrace(e), payload);
        }
        return messageSenderResult;
    }

    private void setMessageSenderResult(MessageSenderResult messageSenderResult, boolean success, String response, String payload){
        messageSenderResult.setSuccess(success);
        messageSenderResult.setMessage(response);
        messageSenderResult.setPayload(payload);
    }

    private String getEncodedFile(byte[] ccdaFile) throws IOException {
        return Base64.encodeBase64String(ccdaFile);
    }

    private String prepareXdrMessageMetaData(URL endpoint, String metaData, XdrMessageMetaDataProperties messageProperties){
        return XdrMessageSenderUtils.replaceHeaders(endpoint, metaData, messageProperties);
    }

    private String getMetaData(XdrMessageType messageType) throws IOException {
        String metadata = null;
        switch (messageType){
            case MINIMAL:
                metadata = FileUtils.readFileToString(ResourceUtils.getFile("classpath:Xdr_minimal_metadata_only.xml"));
                break;
            case FULL:
                metadata = FileUtils.readFileToString(ResourceUtils.getFile("classpath:Xdr_full_metadata_only.xml"));
                break;
        }
        return metadata;
    }
}
