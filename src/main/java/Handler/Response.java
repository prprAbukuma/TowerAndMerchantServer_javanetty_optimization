package Handler;




public class Response {
    public int actionType;
    public String jsonData;
    public Response(){}
    public Response(int actionType,String jsonData)
    {
        this.actionType=actionType;
        this.jsonData=jsonData;
    }

}
