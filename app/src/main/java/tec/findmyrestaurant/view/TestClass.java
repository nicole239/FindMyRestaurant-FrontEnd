package tec.findmyrestaurant.view;

import android.content.Context;

import java.util.List;

import tec.findmyrestaurant.api.CalificationRequest;
import tec.findmyrestaurant.api.CommentRequest;
import tec.findmyrestaurant.api.Message;
import tec.findmyrestaurant.api.PhotoRequest;
import tec.findmyrestaurant.api.Response;
import tec.findmyrestaurant.api.RestaurantRequest;
import tec.findmyrestaurant.api.SessionManager;
import tec.findmyrestaurant.api.UserRequest;
import tec.findmyrestaurant.model.Calification;
import tec.findmyrestaurant.model.Comment;
import tec.findmyrestaurant.model.FoodType;
import tec.findmyrestaurant.model.Restaurant;
import tec.findmyrestaurant.model.User;

public class TestClass {

    public static void test(final Context context){
        final User user = new User();
        user.setPassword("123");
        user.setEmail("steven@gmail.com");
        UserRequest.authUser(context,user,new Response<User>(){
            @Override
            public void onSuccess(Message message) {
                SessionManager.saveToken(context,user,message.getToken());
                super.onSuccess(message);
                test15(context);
            }

            @Override
            public void onFailure(Message message) {
                super.onFailure(message);
            }
        });

    }
    public static void test2(Context context){
        final User user = new User();
        user.setPassword("123");
        user.setEmail("steven2@gmail.com");
        user.setName("Steven");
        user.setType(User.REGULAR);
        UserRequest.registerUser(context,user,new Response<User>(){
            @Override
            public void onSuccess(Message message) {
                super.onSuccess(message);
            }

            @Override
            public void onFailure(Message message) {
                super.onFailure(message);
            }
        });
    }
    public static void test3(Context context){
        UserRequest.getUsers(context,new Response<User>(){
            @Override
            public void onSuccess(List<User> list) {
                super.onSuccess(list);
            }

            @Override
            public void onFailure(Message message) {
                super.onFailure(message);
            }
        });
    }
    public static void test4(Context context){
        RestaurantRequest.getRestaurants(context,new Response<Restaurant>(){
            @Override
            public void onSuccess(List<Restaurant> list) {
                super.onSuccess(list);
            }

            @Override
            public void onFailure(Message message) {
                super.onFailure(message);
            }
        });
    }
    public static void test5(final Context context){
        User user = new User();
        user.setEmail("steven@gmail.com");
        final Restaurant restaurant = new Restaurant();
        restaurant.setLatitude(3.5f);
        restaurant.setLongitude(9.2f);
        restaurant.setName("McDonals");
        restaurant.setPhoneNumber(1234);
        restaurant.setWebsite("mc.com");
        restaurant.setPrice('H');
        restaurant.addPhoto("a-photo.com");
        restaurant.addPhoto("b-photo1.com");
        restaurant.setFoodType(new FoodType(1,""));
        restaurant.setUsrCreator(user);
        restaurant.setSchedule("");
        RestaurantRequest.registerRestaurant(context,restaurant,new Response<Restaurant>(){
            @Override
            public void onSuccess(Message message) {
                super.onSuccess(message);
                test4(context);
            }

            @Override
            public void onFailure(Message message) {
                super.onFailure(message);
            }
        });
    }
    public static void test6(final Context context){
        RestaurantRequest.getRestaurant(context,6,new Response<Restaurant>(){
            @Override
            public void onSuccess(Restaurant objet) {
                super.onSuccess(objet);
                test7(context);
            }

            @Override
            public void onFailure(Message message) {
                super.onFailure(message);
            }
        });
    }
    public static void test7(Context context){
        RestaurantRequest.getRestaurant(context,9,new Response<Restaurant>(){
            @Override
            public void onSuccess(Restaurant objet) {
                super.onSuccess(objet);
            }

            @Override
            public void onFailure(Message message) {
                super.onFailure(message);
            }
        });
    }
    public static void test8(final Context context){
        PhotoRequest.getPhotos(context,6,new Response<String>(){
            @Override
            public void onSuccess(List<String> list) {
                super.onSuccess(list);
                test9(context);
            }

            @Override
            public void onFailure(Message message) {
                super.onFailure(message);
            }
        });
    }
    public static void test9(Context context){
        PhotoRequest.getPhotos(context,886,new Response<String>(){
            @Override
            public void onSuccess(List<String> list) {
                super.onSuccess(list);
            }

            @Override
            public void onFailure(Message message) {
                super.onFailure(message);
            }
        });
    }
    public static void test10(final Context context){
        Calification calification = new Calification();
        calification.setCalification(5);
        calification.setIdRestaurant(6);
        User user = new User();
        user.setEmail("steven@gmail.com");
        calification.setUser(user);
        CalificationRequest.insertCalification(context,calification,new Response<Calification>(){
            @Override
            public void onSuccess(Message message) {
                super.onSuccess(message);
                test11(context);
            }

            @Override
            public void onFailure(Message message) {
                super.onFailure(message);
                test11(context);
            }
        });
    }
    public static void test11(final Context context){
        Calification calification = new Calification();
        calification.setCalification(3);
        calification.setIdRestaurant(6);
        User user = new User();
        user.setEmail("nicole@gmail.com");
        calification.setUser(user);
        CalificationRequest.insertCalification(context,calification,new Response<Calification>(){
            @Override
            public void onSuccess(Message message) {
                super.onSuccess(message);
                test12(context);
            }

            @Override
            public void onFailure(Message message) {
                super.onFailure(message);
            }
        });
    }
    public static void test12(final Context context){
        CalificationRequest.getCalifications(context,6,new Response<Calification>(){
            @Override
            public void onSuccess(List<Calification> list) {
                super.onSuccess(list);
                test13(context);
            }

            @Override
            public void onFailure(Message message) {
                super.onFailure(message);
            }
        });
    }
    public static void test13(Context context){
        CalificationRequest.getRestaurantCalification(context,6,new Response<Calification>(){
            @Override
            public void onSuccess(Calification objet) {
                super.onSuccess(objet);
            }

            @Override
            public void onFailure(Message message) {
                super.onFailure(message);
            }
        });
    }
    public static void test14(final Context context){
        User user = new User();
        user.setEmail("steven@gmail.com");
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setIdRestaurant(6);
        comment.setComment("comment");
        CommentRequest.registerComment(context,comment,new Response<Restaurant>(){
            @Override
            public void onSuccess(Message message) {
                super.onSuccess(message);
                test15(context);
            }

            @Override
            public void onFailure(Message message) {
                super.onFailure(message);
            }
        });
    }
    public static void test15(Context context){
        CommentRequest.getComments(context,6,new Response<Comment>(){
            @Override
            public void onSuccess(List<Comment> list) {
                super.onSuccess(list);
            }

            @Override
            public void onFailure(Message message) {
                super.onFailure(message);
            }
        });
    }

}
