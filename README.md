服务端开发：

创建并实现处理器逻辑。 应用程序根据需求扩展ChannelHandler。 针对不同类型的事件来调用ChannelHandler。
创建ServerBootstrap实例来引导和绑定服务器。
创建并分配一个NioEventLoopGroup实例来进行请求处理。
创建并分配一个NioEventLoopGroup实例来进行事件处理。
指定服务器绑定的本地InetSocketAddress。
使用处理器实例初始化每一个新的channel。
调用ServerBootstrap.bind()方法来绑定服务器。

客户端开发：

创建并实现处理器逻辑。 应用程序根据需求扩展ChannelHandler。 针对不同类型的事件来调用ChannelHandler。
创建Bootstrap实例来初始化客户端。
创建并分配一个NioEventLoopGroup实例来进行事件处理。
为服务器创建InetSocketAddress实例。
当连接被建立时，一个Handler会被安装到该channel的ChannelPipline上。
调用Bootstrap.connect()方法连接远程节点 。


Netty客户端:实例一个客户端，连接对应ip和端口【实例一个EventLoopGroup线程组，
加入到Bootstrap（用来引导和绑定服务器的对象< 内有线程组、通道、处理器、引导通道的配置option>）、
连接远程节点->阻塞等待直到连接完成ChannelFuture->阻塞到channel关闭】

Netty服务端：实例一个服务端，start一个端口【实例2个EventLoopGroup线程组来处理tcp连接请求和一个处理IO事件、ServerBootstrap实例用于
引导和绑定服务器（将之前二个实例加入进来、异步绑定服务器，调用 sync() 方法阻塞等待直到绑定完成；
获取 channel 的 closeFuture，并且阻塞直到它完成。）】
