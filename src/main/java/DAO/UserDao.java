package DAO;

import Model.User;
import sun.dc.pr.PRError;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDao {
    /**
     * 判断所输入的邮箱是否已经存在
     * @param conn
     * @param email
     * @return
     */
    public static boolean JudgeEmailExist(Connection conn,String email)
    {
        String selectSqlStr="SELECT * FROM user WHERE email=?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(selectSqlStr);
            preparedStatement.setString(1,email);
            ResultSet resultSet= preparedStatement.executeQuery();
            if(!resultSet.next())
            {
                resultSet.close();
                preparedStatement.close();
                //说明不存在当前这个邮箱的记录
                return false;

            }else{
                resultSet.close();
                preparedStatement.close();
                //说明存在当前这个邮箱的记录
                return true;
            }



        }catch (Exception e)
        {

            System.out.println("判断输入邮箱是否存在时出现异常，异常信息为："+e);
        }
        return true;//如果该处出现了错误，返回为真，保证后面的逻辑执行不下去
    }

    /**
     * 加入一条用户记录
     * @param conn
     * @param email
     * @param password
     * @return
     */
    public static boolean InsertUser(Connection conn,String email,String password,String idcard)
    {
        //先检查是否存在
        boolean isExist=JudgeEmailExist(conn,email);
        if(!isExist)
        {
            String insertSqlStr="INSERT INTO user(email,password,idcard) VALUES (?,?,?)";
            try {
                PreparedStatement preparedStatement = conn.prepareStatement(insertSqlStr);
                preparedStatement.setString(1,email);
                preparedStatement.setString(2,password);
                preparedStatement.setString(3,idcard);
                int effectRows= preparedStatement.executeUpdate();
                preparedStatement.close();
                if(effectRows>0)
                {

                    //说明加入玩家记录成功
                    //为这个玩家创建UserSave记录-先查到这个玩家的id
                    String selectSqlStr="SELECT * FROM user WHERE email=?";
                    PreparedStatement preparedStatement1=conn.prepareStatement(selectSqlStr);
                    preparedStatement1.setString(1,email);
                    ResultSet resultSet=preparedStatement1.executeQuery();

                    if(resultSet.next())
                    {
                      int uid=resultSet.getInt("id");
                      //创建这个用户对应的usersave
                      UserSaveDao.CreatSaveForUser(conn,uid);
                    }
                    resultSet.close();
                    preparedStatement1.close();

                }
            }catch (Exception e)
            {
                System.out.println("插入记录时发生异常，异常信息为："+e);
            }
        }else{
            //该记录存在，插入失败
            return false;
        }
        return false;

    }

    /**
     * 用户登录
     * @param conn
     * @param email
     * @param password
     * @return
     */
    public static User UserLogin(Connection conn,String email,String password)
    {
        String selectSqlStr="SELECT * FROM user WHERE email=? AND password=?";
        try{
            PreparedStatement preparedStatement=conn.prepareStatement(selectSqlStr);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);
            ResultSet resultSet=preparedStatement.executeQuery();
            User user=null;
            if(resultSet.next())
            {
                int uid=resultSet.getInt("id");
                String idcard=resultSet.getString("idcard");
                 user=new User(uid,email,password,idcard);
            }
            resultSet.close();
            preparedStatement.close();
            return user;
        }catch (Exception e)
        {
            System.out.println("用户登录时发生异常，异常信息为:"+e);
            return null;
        }
    }
}
