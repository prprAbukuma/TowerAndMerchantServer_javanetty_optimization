package Proto;

import org.json.JSONObject;

public class GetSecurityCodeRequestProto   {
    public String getEmailAddress() {
        return emailAddress;
    }

    private String emailAddress;

    /**
     * 通过json字符串构造该请求协议对象
     * @param jsonData
     */
    public GetSecurityCodeRequestProto(String jsonData)
    {
        // 解析json 获得邮箱地址，然后构造proto对象并返回
        JSONObject json=new JSONObject(jsonData);
        String email=json.getString("email");
        this.emailAddress= email;
    }

}
