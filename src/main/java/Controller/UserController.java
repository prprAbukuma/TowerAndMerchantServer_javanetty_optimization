package Controller;

import DAO.UserDao;
import DAO.UserSaveDao;
import EmailVerifySystem.Email;
import Handler.Request;
import Model.User;
import Model.UserSave;
import Proto.GetSecurityCodeRequestProto;
import Proto.GetSecurityCodeResponseProto;

import Servers.Client;
import Servers.Server;
import io.netty.channel.Channel;
import Enum.ReturnType;
import Enum.ActionType;
import java.util.Random;
import Enum.RequestType;

public class UserController extends BaseController {
    private String emailSecurityCode;
    private String emailAddress;

    public UserController()
    {
        requestType= RequestType.User;
    }
    /**
     * 获得邮箱验证码
     * @param jsonData
     * @param channel
     * @param server
     * @return
     */
    public String GetSecurityCode(String jsonData, Channel channel, Server server)
    {
        Client client=server.clientDictionary.get(channel);
        //解析json,并生成GetSecurityCodeRequestProto对象，然后从这个对象中取得数据
        GetSecurityCodeRequestProto getSecurityCodeRequestProto=new GetSecurityCodeRequestProto(jsonData);

        emailAddress=getSecurityCodeRequestProto.getEmailAddress();
        System.out.println("发起请求的电子邮箱是"+emailAddress);
        // 先查询该邮箱是否已经注册过
        boolean isExist= UserDao.JudgeEmailExist(client.getMysqlConn(),emailAddress);
        if(!isExist)
        {
            //可以注册
            // 返回邮箱验证码
            //随机生成一个6位数的验证码
            emailSecurityCode= String.valueOf((new Random().nextInt(899999) + 100000));
            //发送邮件
            String emailTitle="你正在注册剑与商人，这是你的验证码";
            String emailContent="你的验证码是"+emailSecurityCode;
            Email.Instance().SendEmail(emailAddress,emailTitle,emailContent);
            //返回成功-构造这个请求对应的responseProto对象,封为json后生成protoData对象返回

            GetSecurityCodeResponseProto getSecurityCodeResponseProto=new GetSecurityCodeResponseProto(ReturnType.Successful,"验证码已下发到邮箱");

            return GetSecurityCodeResponseProto.packJsonData(getSecurityCodeResponseProto);
        }else{
            //不可注册-返回失败
            GetSecurityCodeResponseProto getSecurityCodeResponseProto=new GetSecurityCodeResponseProto(ReturnType.Failed,"错误：邮箱已存在");
            return GetSecurityCodeResponseProto.packJsonData(getSecurityCodeResponseProto);
        }
    }

    /**
     * 注册请求
     * @param data
     * @param channel
     * @param server
     * @return
     */
    public String Register(String data,Channel channel,Server server)
    {
        Client client=server.clientDictionary.get(channel);
        //解析data
        String[] strArr=data.split("#");
        String securityCode=strArr[0];//获得用户所输入的邮箱验证码
        String email=strArr[1];
        String password=strArr[2];
        String idCard=strArr[3];
        if(securityCode.equals(emailSecurityCode))
        {
            // 验证码输入成功
            //进行注册
            UserDao.InsertUser(client.getMysqlConn(),email,password,idCard);
            return ReturnType.Successful.ordinal()+"#"+"注册成功";
        }else{
            // 验证码输入失败
            return ReturnType.Failed.ordinal()+"#"+"邮箱验证码错误";
        }
    }

    /**
     * 用户登录
     * @param data
     * @param channel
     * @param server
     * @return
     */
    public String Login(String data,Channel channel,Server server)
    {
        Client client=server.clientDictionary.get(channel);
        String[] dataStrArr=data.split("#");
        String email=dataStrArr[0];
        String password=dataStrArr[1];
        User user=UserDao.UserLogin(client.getMysqlConn(),email,password);
        if(user!=null)
        {
            //说明登录成功
            //查询他的记录
            UserSave userSave= UserSaveDao.GetUserSaveByUserid(client.getMysqlConn(),user.getId());
            //System.out.println("该游戏账户的昵称为："+userSave.getNickname());

            if(userSave.getNickname().equals("") ||userSave.getNickname()==null)
            {
                //说明角色名字为空，没有创建
                //发生一个响应，去让客户端创建游戏昵称
                server.SendResponseToClient(String.valueOf(user.getId()),ActionType.SetNickName,channel);
                return null;
            }else{
                //说明角色有名字，将登录成功的信息进行返回到客户端
                String usersaveStr=userSave.getNickname()+"#"+userSave.getBaselevel()+"#"+userSave.getCoin()+"#"+userSave.getDiamond();
                return String.valueOf(ReturnType.Successful.ordinal())+"*"+usersaveStr;
            }

        }else{
            //登录失败
            return String.valueOf(ReturnType.Failed.ordinal())+"*"+"用户名或密码不正确";
        }
    }

    /**
     * 更新游戏内昵称
     * @param data
     * @param channel
     * @param server
     * @return
     */
    public String UpdateNickName(String data,Channel channel,Server server)
    {
        Client client=server.clientDictionary.get(channel);
        String []dataStrArr=data.split("#");
        int useid=Integer.parseInt(dataStrArr[0]);
        String nickname=dataStrArr[1];
        boolean isSuccessful= UserSaveDao.UpdateNickNameByUserid(client.getMysqlConn(),useid,nickname);
        if(isSuccessful)
        {
            return String.valueOf(ReturnType.Successful.ordinal());
        }else {
            return String.valueOf(ReturnType.Failed.ordinal());
        }
    }
}
