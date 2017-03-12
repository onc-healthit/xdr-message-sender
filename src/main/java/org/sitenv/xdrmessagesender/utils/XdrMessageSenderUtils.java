package org.sitenv.xdrmessagesender.utils;

import org.sitenv.xdrmessagesender.dto.XdrMessageMetaDataProperties;

import java.net.URL;
import java.util.Calendar;
import java.util.UUID;

/**
 * Created by Brian on 3/1/2017.
 */
public class XdrMessageSenderUtils {
    public static String replaceHeaders(URL endpoint, String src, XdrMessageMetaDataProperties messageProperties) {
        String messageId = UUID.randomUUID().toString();
        src = src.replaceAll("#MESSAGE_ID#", messageId);

        String entryUuid = UUID.randomUUID().toString();
        src = src.replaceAll("#ENTRY_UUID#", entryUuid);

        String documentUuid = UUID.randomUUID().toString();
        src = src.replaceAll("#DOCUMENT_ID#", documentUuid);

        long timestamp = Calendar.getInstance().getTimeInMillis();
        String uniqueId = "2.16.840.1.113883.3.72.7" + "." + timestamp;
        src = src.replaceAll("#UNIQUE_ID_SS#", uniqueId);

        src = src.replaceAll("#DIRECT_TO#", messageProperties.getTo());
        src = src.replaceAll("#DIRECT_FROM#", messageProperties.getFrom());
        src = src.replaceAll("#WSA_TO#", endpoint.toString());

        return src;
    }

    public static String createMtomPackage(String metadata, String attachment) {
        StringBuilder mtom = new StringBuilder();
        mtom.append("--MIMEBoundary_1293f28762856bdafcf446f2a6f4a61d95a95d0ad1177f20\r\n");
        mtom.append("Content-Type: application/xop+xml; charset=UTF-8; type=\"application/soap+xml\"\r\n");
        mtom.append("Content-Transfer-Encoding: binary\r\n");
        mtom.append("Content-ID: <0.0293f28762856bdafcf446f2a6f4a61d95a95d0ad1177f20@apache.org>\r\n");
        mtom.append("\r\n");
        mtom.append(metadata);
        mtom.append("\r\n");
        mtom.append("--MIMEBoundary_1293f28762856bdafcf446f2a6f4a61d95a95d0ad1177f20\r\n");
        mtom.append("Content-Type: text/plain\r\n");
        mtom.append("Content-Transfer-Encoding: binary\r\n");
        mtom.append("Content-ID: <1.3293f28762856bdafcf446f2a6f4a61d95a95d0ad1177f20@apache.org>\r\n");
        mtom.append("\r\n");
        mtom.append(attachment);
        mtom.append("\r\n");
        mtom.append("--MIMEBoundary_1293f28762856bdafcf446f2a6f4a61d95a95d0ad1177f20--\r\n");

        return mtom.toString();


    }

    public static String addHTTPHeadersToPayload(URL endpoint, String payload) {
        StringBuilder httpHeaders = new StringBuilder();

        httpHeaders.append("POST " + endpoint.getPath() + " HTTP/1.1\r\n");
        httpHeaders.append("Content-Type: multipart/related; boundary=\"MIMEBoundary_1293f28762856bdafcf446f2a6f4a61d95a95d0ad1177f20\"; type=\"application/xop+xml\"; start=\"<0.0293f28762856bdafcf446f2a6f4a61d95a95d0ad1177f20@apache.org>\"; start-info=\"application/soap+xml\"; action=\"urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b\"\r\n");
        httpHeaders.append("User-Agent: TempXDRSender\r\n");
        httpHeaders.append("Host: xdr.sitenv.org:80\r\n");
        httpHeaders.append("Content-Length: " + (payload.length()) + "\r\n");
        httpHeaders.append("\r\n");
        httpHeaders.append(payload);
        return httpHeaders.toString();
    }
}
