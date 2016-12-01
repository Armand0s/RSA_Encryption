package model.Common;

import java.io.Serializable;

/**
 * Created by Armand on 27/09/2016.
 */
public class MessageType implements Serializable{
    public enum Type {
        RSAKeys,
        RSAPublicKey,
        Pseudo,
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageType that = (MessageType) o;

        if (type != that.type) return false;
        return data != null ? data.equals(that.data) : that.data == null;

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }
}
