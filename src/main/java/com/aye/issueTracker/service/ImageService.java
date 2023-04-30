package com.aye.issueTracker.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.aye.issueTracker.model.Attachment;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageService {

    @Autowired
    private GridFsTemplate template;

    @Autowired
    private GridFsOperations operations;

    public String addFile(MultipartFile upload) throws IOException {

        DBObject metadata = new BasicDBObject();
        metadata.put("fileSize", upload.getSize());

        Object fileID = template.store(upload.getInputStream(), upload.getOriginalFilename(), upload.getContentType(), metadata);

        return fileID.toString();
    }


    public Attachment downloadFile(String id) throws IOException {

        GridFSFile gridFSFile = template.findOne( new Query(Criteria.where("_id").is(id)) );

        Attachment loadFile = new Attachment();

        if (gridFSFile != null && gridFSFile.getMetadata() != null) {
            loadFile.setFileName( gridFSFile.getFilename() );

            loadFile.setContentType( gridFSFile.getMetadata().get("_contentType").toString() );

            loadFile.setSize((Long) gridFSFile.getMetadata().get("fileSize"));

            loadFile.setContent( IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream()));
        }

        return loadFile;
    }
}
