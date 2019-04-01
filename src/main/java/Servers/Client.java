package Servers;

import Model.User;
import Model.UserSave;
import io.netty.channel.socket.SocketChannel;

import java.sql.Connection;

public class Client {
    private Connection mysqlConn;//该客户端与Mysql数据库的连接
    private SocketChannel socketChannel;
    private Server currentServer;

    //Client对象的玩家信息
    public User user;//当前用户
    public UserSave userSave;//当前玩家的游戏信息
    public int CurrentHP;//当前血量


    public SocketChannel getSocketChannel(){
        return socketChannel;
    }
    public Connection getMysqlConn(){return mysqlConn; }
    private boolean isDie=false;

    public Client(){}

    public Client(SocketChannel socketChannel, Connection mysqlConn,Server currentServer)
    {
        this.mysqlConn=mysqlConn;
        this.socketChannel=socketChannel;
        this.currentServer=currentServer;

    }

    /**
     * 扣血函数，返回bool，为真时代表死亡，游戏结束
     * @param deltaHp
     * @return
     */
    public boolean DecreaseHP(int deltaHp)
    {
        CurrentHP-=deltaHp;
        if(CurrentHP<=0)
        {
            CurrentHP=0;
            isDie=true;
            return true;
        }
        return false;
    }

    public void Close()
    {
        try{
            //释放mysql连接和socketChannel
            mysqlConn.close();
            //socketChannel.close();
        }catch (Exception e)
        {
            System.out.println("关闭客户端时发生异常，异常信息为:"+e);
        }

    }

}
