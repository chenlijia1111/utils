package com.github.chenlijia1111.utils.http.po;

import org.apache.http.entity.ContentType;

import java.io.File;

/**
 * 文件上传参数
 * @author Chen LiJia
 * @since 2020/8/17
 */
public class FileUploadPo {

    /**
     * 文件名称
     * 可以为空
     */
    private String fileName;

    /**
     * 参数名称
     */
    private String paramsKey;

    /**
     * 文件
     */
    private File file;

    /**
     * contentType
     * 可以为空
     */
    private ContentType contentType;

    public FileUploadPo() {
    }

    public FileUploadPo(String paramsKey, File file) {
        this.paramsKey = paramsKey;
        this.file = file;
    }

    public FileUploadPo(String fileName, String paramsKey, File file) {
        this.fileName = fileName;
        this.paramsKey = paramsKey;
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getParamsKey() {
        return paramsKey;
    }

    public void setParamsKey(String paramsKey) {
        this.paramsKey = paramsKey;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }
}
