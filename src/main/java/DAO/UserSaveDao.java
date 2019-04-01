package DAO;

import Model.UserSave;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserSaveDao {
    /**
     * 创建用户的基本数据
     * @param conn
     * @param userid
     */
    public static void CreatSaveForUser(Connection conn,int userid)
    {
        String insertSqlStr="INSERT INTO user_save(userid) VALUES (?)";
        try{
            PreparedStatement preparedStatement=conn.prepareStatement(insertSqlStr);
            preparedStatement.setInt(1,userid);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (Exception e)
        {

            System.out.println("创建用户数据usersave时出现异常，异常信息为"+e);
        }
    }

    /**
     * 通过用户的id获得用户的数据usersave
     * @param conn
     * @param userid
     * @return
     */
    public static UserSave GetUserSaveByUserid(Connection conn,int userid)
    {
        String selectSqlStr="SELECT * FROM user_save WHERE userid=?";
        try{
            PreparedStatement preparedStatement=conn.prepareStatement(selectSqlStr);
            preparedStatement.setInt(1,userid);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next())
            {

                int id=resultSet.getInt("id");
                byte baselevel=(byte)resultSet.getInt("baselevel");
                String nickname=resultSet.getString("nickname");
                int coin=resultSet.getInt("coin");
                int diamond=resultSet.getInt("diamond");
                resultSet.close();
                preparedStatement.close();
                UserSave userSave=new UserSave(id,userid,nickname,baselevel,coin,diamond);
                return userSave;
            }
        }catch (Exception e)
        {

            System.out.println("获得用户数据usersave出现异常，异常信息为"+e);

        }
        return  null;
    }

    /**
     * 通过用户的id来修改用户的游戏昵称
     * @param conn
     * @param userid
     * @param newNickName
     * @return
     */
    public static boolean UpdateNickNameByUserid(Connection conn,int userid,String newNickName)
    {
        String updateSqlStr="UPDATE user_save SET nickname=? WHERE userid=?";
        try{
            PreparedStatement preparedStatement=conn.prepareStatement(updateSqlStr);
            preparedStatement.setString(1,newNickName);
            preparedStatement.setInt(2,userid);
            int effectRows=preparedStatement.executeUpdate();
            preparedStatement.close();
            if(effectRows>0)
            {
                return true;
            }else {
                return false;
            }
        }catch (Exception e)
        {
            System.out.println("更新玩家昵称时发生异常，异常信息为:"+e);
            return false;
        }
    }
}
