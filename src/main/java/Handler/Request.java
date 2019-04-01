package Handler;




public class Request {
    public int requestType;
    public int actionType;
    public String jsonData;
    public Request(){}
    public Request(int requestType, int actionType,String jsonData)
    {
        this.requestType=requestType;
        this.actionType=actionType;
        this.jsonData=jsonData;
    }
}
