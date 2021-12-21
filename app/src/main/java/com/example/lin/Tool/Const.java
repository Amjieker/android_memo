package com.example.lin.Tool;

/**
 * 整个常量类
 * 方便改一些全局的东西，比如我服务器的ip地址
 */
public final class Const {
    public static final String // 服务端ip地址
        IP = "101.43.66.34";
    public static final Integer // 服务端端口
        Port = 8086;
    public static final String // 注册账户接口
        Add = "http://101.43.66.34:8084/add";
    public static final String // 校验账户接口
        Check = "http://101.43.66.34:8084/check";
    public static final String // 连接socket需要的校验,简单对比,过滤一下其他乱连接
        Key = "hash_1645dads2";
    public static String //全局昵称
        name = "";
    private Const(){}
}
