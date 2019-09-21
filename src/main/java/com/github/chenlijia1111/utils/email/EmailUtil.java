package com.github.chenlijia1111.utils.email;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.common.Result;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.list.Lists;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 邮件工具类
 * <p>
 * SMTP 协议用于发送邮件  端口 25  ssl 加密端口 465
 * POP3 协议用于接收邮件  端口 110
 * IMAP 协议用于收发邮件(一般不怎么用)
 * <p>
 * 这里主要是使用 SMTP 发送邮件
 * 默认全部采用 465端口加密 发送邮件
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/12 0012 上午 9:07
 **/
public class EmailUtil {

    /**
     * 发送邮件账号
     **/
    private String account;

    /**
     * 发送邮件密码
     **/
    private String password;

    /**
     * 发送邮件用户名称
     **/
    private String sendUserName;

    /**
     * 发送邮件服务器类型
     **/
    private EmailHostType emailHostType;

    /**
     * @param account       发送邮件账号
     * @param password      发送邮件密码
     * @param emailHostType 发送邮件服务器类型
     * @param sendUserName  发送邮件用户名称 如果不设置,默认为 {@link #account} 发送账户
     * @since 上午 10:47 2019/9/12 0012
     **/
    public EmailUtil(String account, String password, EmailHostType emailHostType, String sendUserName) {

        AssertUtil.isTrue(StringUtils.isNotEmpty(account), "邮箱账户不能为空");
        AssertUtil.isTrue(StringUtils.isNotEmpty(password), "邮箱密码不能为空");
        AssertUtil.isTrue(Objects.nonNull(emailHostType), "邮箱服务类型不能为空");

        this.account = account;
        this.password = password;
        this.emailHostType = emailHostType;
        this.sendUserName = StringUtils.isEmpty(sendUserName) ? account : sendUserName;
    }

    /**
     * 发送邮件
     * 除了收邮件地址 为必传以外 其他的参数都可以不传
     * subject 和 content 不传默认为空字符串
     *
     * @param receiveEmails 收邮件地址 必传
     * @param subject       邮件标题
     * @param content       邮件内容
     * @param fileList      邮件附件集合
     * @return com.github.chenlijia1111.utils.common.CheckResult
     * @since 上午 10:48 2019/9/12 0012
     **/
    public Result sendMassage(List<String> receiveEmails, String subject, String content, List<File> fileList) {

        if (Lists.isEmpty(receiveEmails)) {
            return Result.failure("接收邮件地址集合不能为空");
        }

        //防止为空
        subject = StringUtils.isEmpty(subject) ? "" : subject;
        content = StringUtils.isEmpty(content) ? "" : content;

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.host", this.emailHostType.getHost());
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.port", this.emailHostType.getPort());
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", this.emailHostType.getPort());
        props.setProperty("mail.smtp.ssl.enable", "true");
        Session session = Session.getInstance(props);
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(this.account, this.sendUserName, "utf-8"));
            //批量发送
            ArrayList<Address> addressList = new ArrayList<>();
            for (String receiveEmail : receiveEmails) {
                addressList.add(new InternetAddress(receiveEmail, receiveEmail, "utf-8"));
            }
            //设置批量收件地址
            message.setRecipients(MimeMessage.RecipientType.TO, addressList.toArray(new Address[addressList.size()]));
            message.setSubject(subject);
            //body内容
            MimeBodyPart text = new MimeBodyPart();
            text.setContent(content, "text/html; charset=utf-8");

            //混合内容发送
            MimeMultipart mm = new MimeMultipart();
            //body内容 content
            mm.addBodyPart(text);
            //判断是否有需要发送附件
            if (Lists.isNotEmpty(fileList)) {
                for (File file : fileList) {
                    //附件
                    MimeBodyPart attachment = new MimeBodyPart();
                    DataHandler dh2 = new DataHandler(new FileDataSource((file)));
                    attachment.setDataHandler(dh2);
                    attachment.setFileName(MimeUtility.encodeText(dh2.getName()));
                    mm.addBodyPart(attachment);
                }
            }
            mm.setSubType("mixed");

            message.setContent(mm);
            message.setSentDate(new Date());
            message.saveChanges();

            Transport transport = session.getTransport();
            transport.connect(account, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            return Result.success("发送成功");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return Result.success("发送失败");
    }
}
