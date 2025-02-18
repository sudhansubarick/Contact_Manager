package com.scm.services;

import org.springframework.web.multipart.MultipartFile;

public interface imageService {

    public String uploadImage(MultipartFile contactImage,String fileName);

    public String  getUrlFromPublicId(String publicId);

}
