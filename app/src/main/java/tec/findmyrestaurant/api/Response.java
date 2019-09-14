package tec.findmyrestaurant.api;

import java.util.List;

public class Response<T>{
    public void onSuccess(T objet){}
    public void onSuccess(List<T> list){}
    public void onSuccess(Message message){}
    public void onFailure(Message message){}
}
