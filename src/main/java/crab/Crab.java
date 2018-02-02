package crab;


import crab.config.CrabConfiguration;
import crab.config.impl.XmlConfiguration;
import crab.handler.HttpHandler;
import crab.handler.impl.AssetHandler;
import crab.http.HttpRequest;
import crab.http.HttpResponse;
import crab.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Crab {


    private Selector selector;

    /**
     * socket服务
     */
    private ServerSocketChannel server;
    /**
     * 是否已经运行
     */
    private boolean isRunning = false;

    /**
     * debug模式
     */
    private boolean debug = true;


    private List<HttpHandler> handlers = new LinkedList<>();

    private static String contextPath = "/";

    private static String configFilePath = "/crab.xml";

    private static CrabConfiguration crabConfiguration = new XmlConfiguration(configFilePath);


    public Crab() throws IOException {
        this(crabConfiguration.getIntValue("port"));
        addHandler(new AssetHandler(crabConfiguration.getValue("workspace")));
    }

    public Crab(int port) throws IOException {
        selector = Selector.open();
        server = ServerSocketChannel.open();
        this.bind(port);
    }

    private Crab bind(InetSocketAddress address) throws IOException {
        server.socket().bind(address);
        server.configureBlocking(false);
        server.register(selector, SelectionKey.OP_ACCEPT);
        return this;
    }

    private Crab bind(int port) throws IOException {
        return bind(new InetSocketAddress(port));
    }


    /**
     * 增加一个处理器
     *
     * @param httpHandler http处理器
     * @return crab
     */
    public Crab addHandler(HttpHandler httpHandler) {
        handlers.add(httpHandler);
        return this;
    }

    /**
     * 移除一个处理器
     *
     * @param httpHandler http处理器
     */
    public void removeHandler(HttpHandler httpHandler) {
        handlers.remove(httpHandler);
    }

    /**
     * 启动服务
     */
    public void start() {
        isRunning = true;
        System.out.println("Crab is Listening on port :: " + server.socket().getLocalPort());
        while (isRunning) {
            try {
                selector.selectNow();
                Iterator<SelectionKey> i = selector.selectedKeys().iterator();
                while (i.hasNext()) {
                    SelectionKey key = i.next();
                    i.remove();
                    if (!key.isValid()) {
                        continue;
                    }
                    try {
                        //获得新连接
                        if (key.isAcceptable()) {
                            //接受socket
                            SocketChannel client = server.accept();

                            //非阻塞模式
                            client.configureBlocking(false);

                            //注socket到选择器，只读模式
                            client.register(selector, SelectionKey.OP_READ);
                        } else if (key.isReadable()) {
                            //获取socket通道
                            SocketChannel client = (SocketChannel) key.channel();
                            //获取会话
                            HttpSession session = (HttpSession) key.attachment();

                            if (null == session) {
                                session = new HttpSession(client);
                                key.attach(session);
                            }

                            //获取会话数据
                            session.readData();

                            //消息解码
                            String line;
                            while ((line = session.read()) != null) {
                                if (line.isEmpty()) {
                                    this.execute(new CrabExecutor(new HttpRequest(session), handlers));
                                }

                            }


                        }

                    } catch (Exception ex) {
                        System.out.println("Error handing client:" + key.channel());
                        if (isDebug()) {
                            ex.printStackTrace();
                        } else {
                            System.err.println(ex);
                            System.err.println("\tat " + ex.getStackTrace()[0]);
                        }
                        if (key.attachment() instanceof HttpSession) {
                            ((HttpSession) key.attachment()).close();
                        }
                    }
                }
            } catch (Exception ex) {
                //停止服务
                this.shutdown();
                throw new RuntimeException(ex);
            }
        }
    }

    private ExecutorService executor;

    private Future<?> execute(Runnable runnable) {
        if (this.executor == null) {
            this.executor = Executors.newCachedThreadPool();
        }
        return executor.submit(runnable);
    }

    /**
     * 处理请求
     * @param request Httprequest
     * @throws IOException io异常
     */
    protected void handle(HttpRequest request) throws IOException {
        for (HttpHandler httpHandler : handlers) {
            HttpResponse resp = httpHandler.handle(request);
            if (resp != null) {
                request.getSession().sendResponse(resp);
                return;
            }
        }

    }

    /**
     * 停止服务
     */
    public void shutdown() {
        isRunning = false;
        try {
            selector.close();
            server.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public Selector getSelector() {
        return selector;
    }

    public ServerSocketChannel getServer() {
        return server;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isDebug() {
        return debug;
    }

    public List<HttpHandler> getHandlers() {
        return handlers;
    }

    public static String getConfigFilePath() {
        return configFilePath;
    }

    public Crab setConfigFilePath(String configFilePath) {
        Crab.configFilePath = configFilePath;
        return this;
    }
}
