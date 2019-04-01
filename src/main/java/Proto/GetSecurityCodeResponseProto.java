package Proto;
import Enum.ReturnType;
public class GetSecurityCodeResponseProto   {
    private ReturnType returnType;
    private String tipMsg;
    public GetSecurityCodeResponseProto(ReturnType returnType,String tipMsg)
    {
        this.returnType=returnType;
        this.tipMsg=tipMsg;
    }

    /**
     * 将该响应协议对象封装为json数据
     * @param responseProto
     * @return
     */
    public static String packJsonData(GetSecurityCodeResponseProto responseProto){
        // 生成json字符串.手动写
        String jsonData="{ \"returnType\":\""+responseProto.returnType.ordinal()+"\",\"tipMsg\":\""+responseProto.tipMsg+"\"}";

        return jsonData;
    }
}
