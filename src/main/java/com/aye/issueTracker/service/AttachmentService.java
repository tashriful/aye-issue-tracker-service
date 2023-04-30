package com.aye.issueTracker.service;

import com.aye.issueTracker.model.Attachment;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AttachmentService {

    String addFile(MultipartFile upload) throws IOException;
    Attachment downloadFile(String id) throws IOException;
}
