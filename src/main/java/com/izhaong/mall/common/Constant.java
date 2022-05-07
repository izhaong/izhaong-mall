package com.izhaong.mall.common;


import org.springframework.beans.factory.annotation.Value;

public class Constant {
    public static final String MALL_USER = "mall_user";
    public static final String SAIT = "dfa@fs1231";

    public static String FILE_UPLOAD_DIR;

    @Value("${file.upload.dir}")
    public void setFileUploadDir(String fileUploadDir) {
        FILE_UPLOAD_DIR = fileUploadDir;
    }
}
