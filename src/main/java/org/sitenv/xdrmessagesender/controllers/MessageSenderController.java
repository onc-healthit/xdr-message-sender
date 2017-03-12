package org.sitenv.xdrmessagesender.controllers;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.sitenv.xdrmessagesender.dto.MessageSenderResult;
import org.sitenv.xdrmessagesender.dto.XdrMessageMetaDataProperties;
import org.sitenv.xdrmessagesender.services.MessageSenderService;
import org.sitenv.xdrmessagesender.services.enums.XdrMessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

;

/**
 * Created by Brian on 1/19/2017.
 */
@RestController
public class MessageSenderController {
    private MessageSenderService messageSenderService;
    @Resource
    private List<String> ccdaFileList;

    @Autowired
    public MessageSenderController(MessageSenderService messageSenderService) {
        this.messageSenderService = messageSenderService;
    }

    @RequestMapping(value = "/sendmessagewithattachmentfilepath", method = RequestMethod.POST)
    public MessageSenderResult sendMessageWithAttachment(@RequestParam(value = "endpoint", required = true) URL endpoint,
                                                         @RequestParam(value = "attachmentFilePath", required = true) String attachmentFilePath, @RequestParam(value = "directFromAddress", required = false)String directFromAddress, @RequestParam(value = "directToAddress", required = false) String directToAddress, @RequestParam(value = "messageType", required = true)XdrMessageType messageType) throws IOException {
        XdrMessageMetaDataProperties xdrMessageMetaDataProperties = setXdrMessageDataProperties(directFromAddress,directToAddress);
        return messageSenderService.sendMessageWithAttachment(endpoint, FileUtils.readFileToByteArray(new File(attachmentFilePath)), xdrMessageMetaDataProperties, messageType);
    }

    @RequestMapping(value = "/sendmessagewithattachmentfile", headers = "content-type=multipart/*", method = RequestMethod.POST)
    public MessageSenderResult sendMessageWithAttachment(@RequestParam(value = "endpoint", required = true) URL endpoint,
                                                 @RequestParam(value = "attachment", required = true) MultipartFile attachment, @RequestParam(value = "directFromAddress", required = false)String directFromAddress, @RequestParam(value = "directToAddress", required = false) String directToAddress, @RequestParam(value = "messageType", required = true)XdrMessageType messageType) throws IOException {
        XdrMessageMetaDataProperties xdrMessageMetaDataProperties = setXdrMessageDataProperties(directFromAddress,directToAddress);
        return messageSenderService.sendMessageWithAttachment(endpoint, attachment.getBytes(), xdrMessageMetaDataProperties, messageType);
    }

    @RequestMapping(value = "/listsampleccdafiles", method = RequestMethod.GET)
    public List<String> getSampleCcdaList(){
        return ccdaFileList;
    }

    @RequestMapping(value = "/xdrmessageproperties", method = RequestMethod.GET)
    public XdrMessageMetaDataProperties getXdrMessageProperties(){
        return new XdrMessageMetaDataProperties();
    }

    @RequestMapping(value = "/xdrmessagetypes", method = RequestMethod.GET)
    public XdrMessageType[] getXdrMessageTypes(){
        return XdrMessageType.values();
    }

    private boolean hasXdrMessageProperties(String directFromAddress, String directToAddress) {
        return !(StringUtils.isBlank(directFromAddress) || StringUtils.isBlank(directToAddress));
    }

    private XdrMessageMetaDataProperties setXdrMessageDataProperties(String directFromAddress, String directToAddress){
        XdrMessageMetaDataProperties xdrMessageMetaDataProperties = new XdrMessageMetaDataProperties();
        if(hasXdrMessageProperties(directFromAddress, directToAddress)){
            xdrMessageMetaDataProperties.setFrom(directFromAddress);
            xdrMessageMetaDataProperties.setTo(directToAddress);
        }
        return xdrMessageMetaDataProperties;
    }
}
