package model.Client;

import java.io.Serializable;

/**
 * Created by Armand on 27/09/2016.
 */
public class MessageType implements Serializable{
    public enum Type {
        SendKey,
        Message
    }
    private Type type;
    private Object data;

    public void setType(Type type){
        this.type = type;
    }

    public void setData(Object data){
        this.data = data;
    }

    public Type getType(){
        return type;
    }

    public Object getData(){
        return data;
    }
}
