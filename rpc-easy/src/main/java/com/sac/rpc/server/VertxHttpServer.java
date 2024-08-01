package com.sac.rpc.server;

import com.sun.net.httpserver.HttpHandler;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

public class VertxHttpServer implements HttpServer {


    @Override
    public void dostart(int port) {
        //创建实例
        Vertx vertx = Vertx.vertx();
        //创建Http服务器
        io.vertx.core.http.HttpServer Server = vertx.createHttpServer();

        Server.requestHandler(new HttpServerHandler());

        Server.listen(port, result ->{
            if (result.succeeded()) {
                System.out.println("Server started on port " + port);
            }else {
                System.out.println("Server failed to start on port " + result.cause());
            }
        });

    }
}
