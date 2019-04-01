package EmailVerifySystem;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * 用于对邮箱验证码的处理
 */
public class Email {

    private static Email instance;
    public static Email Instance(){
        if(instance==null)
        {
            instance=new Email();
        }
        return instance;
    }
    private Session session;

    private Email()
    {
        //初始化邮件发送设置
        Properties properties=new Properties();
        properties.setProperty("mail.host","smtp.qq.com");
        properties.setProperty("mail.transport.protocol","smtp");
        properties.setProperty("mail.smtp.auth","true");
        session=Session.getInstance(properties);
        session.setDebug(true);
    }

    /**
     * 发送邮件
     * @param receiverEmailAddress
     * @param emailTitle
     * @param emailContent
     * @throws Exception
     */
    public void SendEmail(String receiverEmailAddress,String emailTitle,String emailContent )
    {
        try {


            Transport transport = session.getTransport();
            //发件者邮箱
            transport.connect("smtp.qq.com", "1274659231", "rghkciatuinhbabh");
            //创建邮件
            Message message = CreateMessage(session, receiverEmailAddress, emailTitle, emailContent);
            //发送邮件
            transport.sendMessage(message, message.getAllRecipients());
            //关闭流
            transport.close();
        }catch (Exception e)
        {
            System.out.println("发送邮件时出现异常，异常信息为"+e);
        }
    }

    /**
     * 创建邮件
     * @param session
     * @param receiverEmail
     * @param emailTitle
     * @param emailContent
     * @return
     * @throws Exception
     */
    private Message CreateMessage(Session session,String receiverEmail,String emailTitle,String emailContent) throws Exception
    {
        //创建邮件对象
        MimeMessage message=new MimeMessage(session);
        //指定邮件发件人
        message.setFrom(new InternetAddress("1274659231@qq.com"));
        //指明邮件收件人
        message.setRecipient(Message.RecipientType.TO,new InternetAddress(receiverEmail));
        //设置邮件标题
        message.setSubject(emailTitle);
        //设置内容
        message.setContent(emailContent, "text/html;charset=UTF-8");
        return message;
    }
}
