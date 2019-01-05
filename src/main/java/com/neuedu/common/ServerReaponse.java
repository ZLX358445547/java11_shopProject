package com.neuedu.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


/**
 *
 *响应前端的高复用对象
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerReaponse<T> {

    //状态码，0表示成功；非0表示失败
    private Integer status;
    //当status=0视乎，data对应的接口响应的数据
    private T data;
    //提示信息
    private String msg;



    //生成对应的构造方法,构造方法私有，其他类无法调用

    private ServerReaponse() {
    }
    private ServerReaponse(Integer status) {
         this.status = status;
    }
    private ServerReaponse(Integer status, String msg) {
         this.status = status;
         this.msg = msg;
    }
    private ServerReaponse(Integer status,  String msg,T data) {
         this.status = status;
         this.msg = msg;
         this.data = data;
    }
    /*
    * 判断接口是否访问成功
    * */
    @JsonIgnore
    public boolean isSuccess(){
        return this.status==ResponseCode.SUCESS;
    }



    //写公有成功即状态码为0的方法
    /*
    * {status*:0}
    * */
    public static ServerReaponse createServerResponseBySucess(){
          return new ServerReaponse(ResponseCode.SUCESS);
    }
    /*
    * {status*:0,"msg":"aaaa"}
    * */
    public static ServerReaponse createServerResponseBySucess(String msg){
          return new ServerReaponse(ResponseCode.SUCESS,msg);
     }
     /*
     * {status*:0,"msg":"aaaa","data":{}}
     * */
     public static <T> ServerReaponse createServerResponseBySucess(String msg, T data){
          return  new ServerReaponse(ResponseCode.SUCESS,msg,data);
     }

     //写公有的失败即状态码为非0的方法
     /*
     * {status*:1}
     * */
     public static ServerReaponse createServerResponseByError(){
           return new ServerReaponse(ResponseCode.ERROR);
     }
     /*
     * {"status":custom}
     * */
     public static ServerReaponse createServerResponseByError(Integer status){
           return new ServerReaponse(status);
      }
     /*
       * {"status":1,msg:"aaa"}
       *
       * */
     public static ServerReaponse createServerResponseByError(String msg){
             return new ServerReaponse(ResponseCode.ERROR,msg);
     }

     /*
       * {"status":custom,msg:"aaa"}
       *
       * */
     public static ServerReaponse createServerResponseByError(Integer status ,String msg){
             return new ServerReaponse(status,msg);
     }





    //生成getset方法

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
