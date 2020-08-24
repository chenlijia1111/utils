package com.github.chenlijia1111.utils.http.po;

import org.apache.http.entity.ContentType;

import java.io.InputStream;

/**
 * 文件上传对象
 * 参数值为 输入流 数组
 * @author Chen LiJia
 * @since 2020/8/17
 */
public class FileUploadWithInputStreamPo {

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
     * 输入流对象
     */
    private InputStream inputStream;

    /**
     * contentType
     * 可以为空
     */
    private ContentType contentType;

    public FileUploadWithInputStreamPo() {
    }

    public FileUploadWithInputStreamPo(String paramsKey, InputStream inputStream) {
        this.paramsKey = paramsKey;
        this.inputStream = inputStream;
    }

    public FileUploadWithInputStreamPo(String fileName, String paramsKey, InputStream inputStream) {
        this.fileName = fileName;
        this.paramsKey = paramsKey;
        this.inputStream = inputStream;
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

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }
}
