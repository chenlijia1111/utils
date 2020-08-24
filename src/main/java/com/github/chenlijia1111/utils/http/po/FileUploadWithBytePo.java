package com.github.chenlijia1111.utils.http.po;

import org.apache.http.entity.ContentType;

/**
 * 文件上传对象
 * 参数值为 byte 数组
 * @author Chen LiJia
 * @since 2020/8/17
 */
public class FileUploadWithBytePo {

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
     * 文件字节数组
     */
    private byte[] bytes;

    /**
     * contentType
     * 可以为空
     */
    private ContentType contentType;

    public FileUploadWithBytePo() {
    }

    public FileUploadWithBytePo(String paramsKey, byte[] bytes) {
        this.paramsKey = paramsKey;
        this.bytes = bytes;
    }

    public FileUploadWithBytePo(String fileName, String paramsKey, byte[] bytes) {
        this.fileName = fileName;
        this.paramsKey = paramsKey;
        this.bytes = bytes;
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

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }
}
