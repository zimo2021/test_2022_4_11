package javatest;

import com.sun.source.tree.Tree;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
//网络编程：
/*
网络编程三要素：
IP：每台计算机的唯一标识
端口：计算机中程序的标识
协议：对传输格式，传输效率，传输步骤的同一规定，双方必须同时遵守协议才能完成数据交换
 */
/*
IP地址：
分为两大类：
IPv4:是给每个连接在网络上的主机分配一个32bit地址。按照ICP/IP规定，IP地址用二进制来表示，每个IP地址长32bit，
也就是4个字节。例一个采用二进制形式的IP地址是“11000000 10101000 00000001 01000010”，这么长的地址，
处理起来也太费劲了。为了方便使用，IP地址经常被写成十进制的形式，中间使用符号“”分隔不同的字节。
于是，上面的IP地址可以表示为“192.168.1.66”。IP地址扯的这种表示法叫做“点分十进制表示法”，这显然比1和O容易记忆得多
IPv6:由于互联网的蓬勃发展，IP地址的需求量愈来愈大，但是网络地址资源有限，使得IP的分配越发紧张。
为了扩大地址空间，通过IPv6重新定义地址空间，采用128位地址长度，每16个字节一组，分成8组十六进制数，
这样就解决了网络地址资源数量不够的问题


常用命令：ipconfig：查看本机IP地址
ping+IP地址：检查网络是否联通
使用ipconfig查看本机IP地址
再用ping+本机IP地址 看丢失数据率  如果丢失为0 则网络良好
特殊IP地址：127.0.0.1回送地址 可以代表本机地址 一般用来测试使用
 */

/*
InetAddress  无构造函数  通过函数返回值来创建对象
static InetAddress	getByName(String host)	确定给定主机名称的主机的 IP 地址。
String	getHostName()	获取此 IP 地址的主机名。
String	getHostAddress()	返回文本显示中的 IP 地址字符串。
 */


//public class Test {
//    public static void main(String[] args) throws UnknownHostException {
//        InetAddress iadd = InetAddress.getByName("itheima");
//        String hostName = iadd.getHostName();
//        String hostAddress = iadd.getHostAddress();
//        System.out.println(hostAddress);
//        System.out.println(hostName);
//
//    }
//}

/*
协议分为两种：UDP协议和TCP协议
UDP协议：
用户数据报协议(User Datagram Protocol)
UDP是无连接通信协议，即在数据传输时，数据的发送端和接收端不建立逻辑连接。
简单来说，当一台计算机向另外一台计算机发送数据时，发送端不会确认接收端是否存在，
就会发出数据，同样接收端在收到数据时，也不会向发送端反馈是否收到数据。
由于使用UDP协议消耗资源小，通信效率高，所以通常都会用于音频、视频和普通数据的传输
例如视频会议通常采用UDP协议，因为这种情况即使偶尔丢失一两个数据包，也不会对接收结果产生太大影响。
但是在使用UDP协议传送数据时，由于UDP的面向无连接性，不能保证数据的完整性，因此在传输重要数据时不建议使用UDP协议

TCP协议：
传输控制协议(Transmission Control Protocol)
TCP协议是面向连接的通信协议，即传输数据之前，在发送端和接收端建立逻辑连接，然后再传输数据，
它提供了两台计算机之间可靠无差错的数据传输。在TCP连接中必须要明确客户端与服务器端，由客户端向服务端发出连接请求，
每次连接的创建都需要经讨“三次握手”
三次握手:
TCP协议中，在发送数据的准备阶段，客户端与服务器之间的三次交互，以保证连接的可靠
第一次握手，客户端向服务器端发出连接请求，等待服务器确认
第二次握手，服务器端向客户端回送一个响应，通知客户端收到了连接请求
第三次握手，客户端再次向服务器端发送确认信息，确认连接
 */


/*
UDP发送数据：
发送数据的步骤
1，创建发送端的Socket对象(DatagramSocket)
DatagramSocket() 构造数据报套接字并将其绑定到本地主机上的任何可用端口。
2.创建数据，并把数据打包
DatagramPacket(byte[] buf, int length, InetAddress address, int port)
构造一个数据报数据包，用于将长度为的数据包发送到指定主机上的指定端口号。
3.调用DatagramSocket对象的方法发送数据
void send(DatagramPacket p)	从此套接字发送数据报数据包。
4.关闭发送端
void close();	关闭此数据报套接字
 */

//public class Test {
//    public static void main(String[] args) throws IOException {
//        DatagramSocket ds = new DatagramSocket();//创建一个发送端对象
//        byte[] by = "helloUDP".getBytes();//发送内容
//         创建一个数据包 并将指定长的的数据包发送到指定主机的指定端口 为了方便 我们指定了自己的主机
//        DatagramPacket dp = new DatagramPacket(by, by.length, InetAddress.getByName("HelloWorldcc"), 10086);
//        ds.send(dp);
//        ds.close();
//    }
//}

/*
UDP接收数据：
步骤：
1.创建接收端的Socket对象（DatagramSocket）
DatagramSocket(int port);
2.创建一个数据包，用于接收数据
DatagramPacket(byte[] buf,int length);
3.调用DatagramSocket对象方法接收数据
void receive(DatagramPacket p);
4.解析数据包 并显示
byte[] getData();
int getLength();
5.关闭接收端
void close();
 */
//DatagramSocket:数据电报端口
//DatagramPacket:数据电报包  用于接收数据
//public class Test {
//    public static void main(String[] args) throws IOException{
//        //DatagramSocket(int port)构造数据报套接字并将其绑定到本地主机上的指定端口。
//        DatagramSocket ds=new DatagramSocket(10086);//创建一个端口接通10086（非电话号码）
//        byte[] bys=new byte[1024];//用于接收缓冲区数据
//        DatagramPacket dp=new DatagramPacket(bys,bys.length);//创建一个接收对象
//        ds.receive(dp);//将接受的数据放入接收对象中
//        System.out.println(new String(dp.getData(),0,dp.getLength()));//将接收对象中的数据解析 并打印
//        ds.close();
//        /*
//       先打开接收端  即先运行 Test1 再打开发送端 即运行send 然后关闭send Test1的运行窗口中就打印出了 接收的数据
//         */
//    }
//}

//UDP通信程序练习
/*
UDP发送数据：数据来自键盘输入直到输入886停止输入
UDP接收数据：因为不知道发送端什么时候停止发送 故采用死循环接收
 */
//public class Test {
//    public static void main(String[] args) throws IOException{
//        DatagramSocket ds=new DatagramSocket(12345);//注意端口别越界  port out of range
//        while(true){
//            byte[] by=new byte[1024];
//            DatagramPacket dp=new DatagramPacket(by,by.length);
//            ds.receive(dp);
//            System.out.println(new String(dp.getData(),0,dp.getLength()));
//        }
//        //因为是死循环  所以不需要关闭
//        //ds.close();
//    }
//}


//TCP发送和接收数据
/*
Socket(String host, int port)	创建流套接字并将其连接到指定主机上的指定端口号。
OutputStream getOutputStream()	返回此套接字的输出流。
ServerSocket(int port)	创建绑定到指定端口的服务器套接字。
Socket accept()侦听要与此套接字建立的连接并接受它。在接收端通过这个得到一个Socket对象调用getInputStream()函数
InputStream	getInputStream()	返回此套接字的输入流。
 */
//public class Test {
//    public static void main(String[] args) throws IOException{
//        ServerSocket ss=new ServerSocket(10000);
//        Socket so = ss.accept();
//        InputStream is = so.getInputStream();
//        byte[] by=new byte[1024];
//        int len =is.read(by);
//        String s=new String(by,0,len);
//        System.out.println(s);
//        ss.close();
//        so.close();
//    }
//}


//TCP通讯程序练习（服务器给出反馈）
//public class Test {
//    public static void main(String[] args) throws IOException{
//        ServerSocket ss=new ServerSocket(10000);
//        Socket sk = ss.accept();//监听客户端连接
//        InputStream is = sk.getInputStream();
//        byte[] by=new byte[1024];
//        int len = is.read(by);
//        String s=new String(by,0,len);
//        System.out.println("用户接收数据："+s);
//        OutputStream os = sk.getOutputStream();
//        os.write("数据已接收".getBytes());
//        ss.close();
//        sk.close();
//        os.close();
//        is.close();
//    }
//}


//TCP通信程序练习（客户端数据来自键盘输入  一直输入直到输入886结束）

//public class Test {
//    public static void main(String[] args) throws IOException {
//        //服务端接收数据
//        ServerSocket ss=new ServerSocket(10000);//创建接受流对象
//        //监听数据接收
//        Socket s = ss.accept();
//        BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));
//        String line;
//        while((line=br.readLine())!=null){
//            System.out.println(line);
//        }
//        ss.close();
//        s.close();
//        br.close();
//    }
//}


//服务器将接收到的数据写入文本文件

//public class Test {
//    public static void main(String[] args) throws IOException{
//        ServerSocket ss=new ServerSocket(10000);
//        //监听客户端连接
//        Socket s = ss.accept();
//        //创建接收对象并把接收对象转换为BufferedReader类型
//        //接收数据
//        //读数据尽量将读取的数据转为BufferedReader类型 写数据尽量用BufferedWriter来写 因为字符流写数据和接收数据更快
//        BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));
//        BufferedWriter bw=new BufferedWriter(new FileWriter("D:\\java\\test\\java.txt"));//不知道为什么 我这里要指明
//        //全路径  不能直接模块名加文件名（test//java.txt）  要写（D:\\java\\tets\\java.txt）;
//        String line;
//        while((line=br.readLine())!=null){
//            bw.write(line);
//            bw.newLine();
//            bw.flush();
//        }
//        //释放资源
//        bw.close();//这个一定要关闭  因为他指向文件
//        ss.close();
//    }
//}


//客户端发送文本文件 从文本中获取内容并将内容发送到服务端 并存入另一个文件中去

//public class Test {
//    public static void main(String[] args) throws IOException {
//        ServerSocket ss=new ServerSocket(10000);
//        //监听服务器
//        Socket s = ss.accept();
//        BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));
//        BufferedWriter bw=new BufferedWriter(new FileWriter("D:\\java\\test\\copy.txt"));
//        String line;
//        while ((line=br.readLine())!=null){
//            bw.write(line);
//            bw.newLine();
//            bw.flush();
//        }
//        ss.close();
//        s.close();
//        br.close();
//        bw.close();
//    }
//}


//TCP练习：客户端发送文本文件 从文本中获取内容并将内容发送到服务端 并存入另一个文件中去 并且服务端给出接收反馈病并被客户端接收并打印


//public class Test {
//    public static void main(String[] args) throws IOException {
//        ServerSocket ss=new ServerSocket(10000);
//        //监听服务器
//        Socket s = ss.accept();
//        BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));
//        BufferedWriter bw=new BufferedWriter(new FileWriter("D:\\java\\test\\copy.txt"));
//        String line;
//        while ((line=br.readLine())!=null){
//            bw.write(line);
//            bw.newLine();
//            bw.flush();
//        }
//        //void	shutdownOutput()  禁用此套接字的输出流。
//        //用处：结束读取数据 执行下面的代码
//        //没加shutdownOutput之前 可以打印出1111 却不能打印2222  因为程序还在等待接收数据
//        System.out.println(2222);
//        BufferedWriter bww=new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
//        bww.write("文件接收成功！");
//        bww.newLine();
//        bww.flush();
//        ss.close();
//        s.close();
//        br.close();
//        bw.close();
//        bww.close();
//    }
//}


//多线程实现文件上传
//public class Test {
//    public static void main(String[] args) throws IOException {
//        ServerSocket ss = new ServerSocket(10000);
//        while (true) {//while(true)
//            //监听客户端连接:保持接收器一直开通
//            Socket s = ss.accept();
//            //采用多线程接收数据（即模拟多个客户端发送数据）
//            //Thread(Runnable target)分配新对象。Thread
//            new Thread(new ServerThread(s)).start();
//        }
//    }
//}


//体验Lambda（拉姆达）表达式
//如果我们有一个需求：启动一个线程 在控制台输出一句话：多线程程序启动了
/*
方式一：定义一个My_Runnable类 重写run方法 再用My_Runnable创建对象 并用该对象作为实参创建Thread类对象 调用start方法启动线程
方式二：匿名内部类的方式改进
//方式三：Lambda方式改进
 */


//public class Test {
//    public static void main(String[] args) {
//        //方式一不展示
//        //方式二：
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////                System.out.println("多线程启动了");//多线程启动了
////            }
////        }).start();  这种方式形式比较复杂
//        //方式三
//        new Thread(()->{
//            System.out.println("多线程启动了");//多线程启动了 暂时不知道原因
//            形式：(形参)->{方法体}若有多个形参 用“,”隔开 若没有则为空
//        }).start();
//    }
//}

//public class Test {
//    public static void main(String[] args) {
//        int a=10;
//        short b=10;
//        char c='a';
//        float f=10.0f;
//        double d=10.0d;
//        System.out.println("a: "+a+" b :"+b+" c :"+c+" f: "+f+" d: "+d);
//    }
//}


//interface eatapple{
//    void eat();
//}
//
//
//class eeat implements eatapple{
//    @Override
//    public void eat() {
//        System.out.println("一天一苹果，医生远离我！");
//    }
//}
////Lambda表达式练习
//public class Test {
//    public static void main(String[] args) {
//        //首先要定义一个接口 接口中只用一个实现类方法
//        //方式一：
//        eeat e=new eeat();
//        showeat(e);//一天一苹果，医生远离我！
//        //方式二：
//        showeat(new eatapple() {//匿名内部类
//            @Override
//            public void eat() {
//                System.out.println("一天一苹果，医生远离我！");//一天一苹果，医生远离我！
//            }
//        });
//        //方式三： Lambda方法
//        showeat(()->{
//            System.out.println("一天一苹果，医生远离我！");//一天一苹果，医生远离我！
//        });
//
//    }
//    public static void showeat(eatapple e){
//        e.eat();
//    }
//}

//Lambda表达式练习（抽象方法带参）
//interface flyable{
//    void fly(String s);//抽象方法带参
//}
//public class Test {
//    public static void main(String[] args) {
//        //方法一：匿名内部类
//        showfly(new flyable() {
//            @Override
//            public void fly(String s) {
//                System.out.println(s);
//                System.out.println("汽车自驾游！");
//                /*
//                风和日丽，适合出游！
//                汽车自驾游！
//                 */
//            }
//        });
//        //Lumbda方法
//        showfly((String s)->{
//            System.out.println(s);
//            System.out.println("汽车自驾游！");
//              /*
//                风和日丽，适合出游！
//                汽车自驾游！
//                 */
//        });
//    }
//    private static void showfly(flyable f){
//        f.fly("风和日丽，适合出游！");
//    }
//}

//Lamabda表达式的省略模式
/*
1.参数类型可以省略 但是要全部省略 不能只省略一部分
2。如果参数仅有一个 那么小括号可以省略
如果代码块的语句只有一条 那么可以省略大括号和分号 和return
 */
//interface addnum{
//    int add(int x,int y);
//}
//
//interface  flyable{
//    void fly(String s);
//}
//public class Test {
//    public static void main(String[] args) {
////       addnums((int x,int y)->{
////           return x+y;
////       });
//       //省略参数类型
////        addnums(( x, y)->{
////            return x+y;
////        });
//      //对于只有一个参数的Lamabda形式
////        ffly((String s)->{
////            System.out.println(s);
////        });
//        //省略参数类型和小括号
////        ffly(s->{
////            System.out.println(s);
////        });
//        //省略大括号
//       // ffly(s-> System.out.println(s));//真简洁
//
//        //对于多个参数 省略return
//        addnums((x,y)->x+y);//40  真简洁
//
//    }
//    public static void addnums(addnum a){
//        //此时的a只是一个接口  要想调用的其中的方法  只能通过实现类来实现
//        //那么就有三种方法：1，声明一个类并实现这个接口
//        //2.匿名内部类的方法 在调用addnums方法形参时 new一个内部类并实现抽象方法
//        //3.使用Lamabda形式实现对接口中抽象方法的实现
//        int ret= a.add(10,30);
//        System.out.println(ret);
//    }
//    public static void ffly(flyable f){
//        f.fly("风和日丽，适合出行！");
//
//    }
//}

//Lambda表达式的注意事项
/*
1.使用Lanbda表达式必须要有接口 并且接口中有且仅有一个抽象给方法
2.必须要有上下文环境  才能推导出Lambda对应的接口
 可以根据局部变量的赋值得知Lambda的接口：Runnable=()->System.out.println();
 或者根据调用方法的参数来得知Lambda对应的接口 ：new Thread(()->System.out.println()).start();
 */
//public class Test {
//    public static void main(String[] args) {
//        Runnable r=()-> System.out.println("Lambda调用方法");
//        new Thread(r).start();//Lambda调用方法
//
//        new Thread(()-> System.out.println("Lambda调用方法")).start();//Lambda调用方法
//    }
//}


/*
Lambda方法和匿名内部类的区别
1，所需类型不同：匿名内部类:可以是接口，也可以是抽象类，还可以是具体类
Lambda表达式:只能是接口
2.使用限制不同：如果接口中有且仅有一个抽象方法，可以使用Lambda表达式，也可以使用匿名内部类
如果接口中多于一个抽象方法，只能使用匿名内部类，而不能使用Lambda表达式
3.实现原理不同：匿名内部类:编译之后，产生一个单独的.class字节码文件
Lambda表达式:编译之后，没有一个单独的.class字节码文件。对应的字节码会在运行的时候动态生成
 */
//interface flyable{
//    void fly();
//}
//abstract class stu{
//    abstract void show();
//}
//class stuu{
//    void show(){
//        System.out.println("具体类");
//    }
//}
//public class Test {
//    public static void main(String[] args) {
//        //匿名内部类：
//        //接口
////        flyy(new flyable() {
////            @Override
////            public void fly() {
////                System.out.println("接口");//接口
////            }
////        });
////        //抽象类
////        stushow(new stu() {
////            @Override
////            void show() {
////                System.out.println("抽象类");//抽象类
////            }
////        });
////        //具体类
////        stuushow(new stuu());//具体类
//
//        //
//        //flyy(()->System.out.println("接口"));
//
//        //但是如果接口中有两个抽象方法的话  就不能使用Lambda方法 可以使用匿名内部类
//
//    }
//    public static void flyy(flyable f){
//        f.fly();
//    }
//    public static void stushow(stu s){
//        s.show();
//    }
//    public static void stuushow(stuu s){
//        s.show();
//    }
//}


//接口的组成
/*
常量：默认修饰符：public static final
抽象方法：public abstract
默认方法
静态方法
私有方法
 */


//接口中的默认方法（default关键字）
/*
格式：public default +返回值类型+方法名
注意事项：
默认方法不是抽象方法，所以不强制被重写 但也可以被重写 重写时去掉default关键字default
写默认方法时public 可以省略 但default不能被省略
 */


//interface Myinterface{
//    void show1();
//    void show2();
//    //但是我们临时想完善接口 添加一个show3 如果按原来的方法添加show3那么以前每个接口的实现类都要重新实现show3 非常麻烦
//    //所以就有了我们的默认方法
//    public default void show3(){
//        System.out.println("show3");
//    }
//}
//
//class Myimple implements Myinterface{
//    @Override
//    public void show1() {
//        System.out.println("one show1");
//    }
//
//    @Override
//    public void show2() {
//        System.out.println("one show2");
//    }
//
//    @Override
//    public void show3() {
//        System.out.println("one show3");
//    }
//}
//
//public class Test {
//    public static void main(String[] args) {
//        Myimple my=new Myimple();
//        my.show1();//one show1
//        my.show2();//one show2
//        //show3没有被重写之前
//        my.show3();//show3
//        //show3被重写之后
//        my.show3();//one show3
//    }
//}


//接口中的静态方法
/*
格式：public static 返回值类型+方法名
接口中的静态方法的注意事项：
静态方法只能通过接口名调用 不能通过实现实现类名或者对象名调用
public可以省略 static不能省略
 */
//interface inter{
//    void show();
//    public default void show1(){
//        System.out.println("接口中的默认方法的调用");
//    }
//    public static void show2(){
//        System.out.println("接口中的静态方法的调用");
//    }
//}
//
//class interimpl implements inter{
//    @Override
//    public void show() {
//        System.out.println("接口中的show方法的调用");
//    }
//}
//
//public class Test {
//    public static void main(String[] args) {
//        inter i=new interimpl();
//        i.show();//接口中的show方法的调用
//        i.show1();//接口中的默认方法的调用
//        //i.show2();//报错  不能通过实现类对象来调用接口中的静态方法 稚嫩通过接口名调用
//        inter.show2();//接口中的静态方法的调用  这样就可以了
//    }
//}

//接口中的私有方法：
/*
格式一：private+返回值类型+方法名
格式二：private static +返回值类型+方法名
注意事项：默认方法可以调用私有静态方法和非静态方法
静态方法只能调用私用静态方法
 */
//interface inter{
//    public default void show1(){
//        System.out.println("show1开始执行");
////        System.out.println("初级工程师");
////        System.out.println("中级工程师");
////        System.out.println("高级工程师");
//        show();
//        System.out.println("show1执行结束");
//    }
//    public default void show2(){
//        System.out.println("show2开始执行");
////        System.out.println("初级工程师");
////        System.out.println("中级工程师");
////        System.out.println("高级工程师");
//        show();
//        System.out.println("show2执行结束");
//    }
//    private void show(){
//        System.out.println("初级工程师");
//        System.out.println("中级工程师");
//        System.out.println("高级工程师");
//    }
//    public static void show3(){
//        System.out.println("show3开始执行");
////        System.out.println("初级工程师");
////        System.out.println("中级工程师");
////        System.out.println("高级工程师");
//        showw();
//        System.out.println("show3执行结束");
//    }
//
//    public static void show4(){
//        System.out.println("show4开始执行");
////        System.out.println("初级工程师");
////        System.out.println("中级工程师");
////        System.out.println("高级工程师");
//        showw();
//        System.out.println("show4执行结束");
//    }
//    private static void showw(){
//        System.out.println("初级工程师");
//        System.out.println("中级工程师");
//        System.out.println("高级工程师");
//    }
//}
//class interimpl implements inter{
//
//}
//
//
//public class Test {
//    public static void main(String[] args) {
//            inter i=new interimpl();
//            i.show1();//就算实现类中什么都不要做  也要通过实现类来调用默认方法
//        System.out.println("----------------");
//            i.show2();
//        System.out.println("----------------");
//            inter.show3();
//        System.out.println("----------------");
//            inter.show4();
//
//    }
//}


//方法引用
//interface printtable{
//    void show(String s);
//}
//public class Test {
//    public static void main(String[] args) {
//        //方式一 Lambda方法
//        showmag(s-> System.out.println(s));//爱生活，爱java
//        //方式二：方法引用
//        showmag(System.out::println);//爱生活，爱java  这里编译器可以通过推导
//        // 推导出我们想实现的内容  而可推导的就可以省略
//    }
//    private static void showmag(printtable p){
//        p.show("爱生活，爱java");
//    }
//}

//Lambda支持的方法引用
/*
常见的引用方法：
1.引用类方法
2.引用对象的实例方法
3.引用类的实例方法
4.引用构造器
 */
//1.引用类方法：其实就是引用类的静态方法
//格式：类名::静态方法
//interface parseint{
//    int to_int(String s);
//}
//public class Test {
//    public static void main(String[] args) {
//        //Lambda方法
//        useparseint(s->Integer.parseInt(s));//666
//        //方法引用(引用静态类方法)
//        useparseint(Integer::parseInt);//666
//        //注意：Lambda表达式被类方法替代的时候 它的形式参数全部传递给了静态方法作为参数
//    }
//    private static void useparseint(parseint p){
//        int i = p.to_int("666");
//        System.out.println(i);
//    }
//}


//引用对象的实例方法：其实就是引用类中的成员方法（非static）
/*
格式：对象::成员方法名
 */
//interface toupper{
//    void target(String s);
//}
//class to_upper{
//    public void func(String s){
//        String result = s.toUpperCase();
//        System.out.println(result);
//    }
//}
//public class Test {
//
//    public static void main(String[] args) {
//        //Lambda方法
//        funcimp(s-> System.out.println(s.toUpperCase()));//HELLOWORLD
//
//        //引用对象的实例方法
//        to_upper per=new to_upper();//先要创建对象
//        funcimp(per::func);//HELLOWORLD
//        //注意：Lambda表达式被实例方法替代的时候 它的形式参数全部传递给了实例方法作为参数
//
//    }
//    private static void funcimp(toupper t){
//        t.target("HelloWorld");
//    }
//}


//引用构造器:其实就是引用构造法方法
//格式：类名::new

//class student{
//    private String name;
//    private int age;
//
//    public student() {
//    }
//
//    public student(String name, int age) {
//        this.name = name;
//        this.age = age;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public int getAge() {
//        return age;
//    }
//
//    public void setAge(int age) {
//        this.age = age;
//    }
//}
//interface inter{
//    student build(String name,int age);
//}
//
//public class Test {
//    public static void main(String[] args) {
//        //Lambda方法：
//        testinter((String name,int age)->{
//            student s=new student(name,age);
//            return s;
//        });
//       //简化
//        testinter((n,a)->new student(n,a));
//        //方法引用
//        testinter(student::new);
//        //Lambda表达式被构造器替代的时候，它的形式参数全部传递给构造器作为参数  相当于new xxx(形参a,形参b...)
//    }
//    private static void testinter(inter i){
//        student s = i.build("林青霞", 30);
//        System.out.println(s.getName()+","+s.getAge());
//
//    }
//}


/*
函数式接口：有且仅有一个抽象方法的接口
java中函数式编程体现的就是Lambda表达式，所以函数式接口就是可以适用于Lambda使用的接口
只有确保接口中有且仅有一个抽象方法,java中的Lambda才能顺利的推导

如何检测一个接口是不是函数式接口呢
@FunctionalInterface
放在接口的上方：如果接口是函数式接口，编译通过 如果不是 编译失败

注意：我们自己定义函数式接口的时候，@Functionallnterface是可选的，就算我不写这个注解，
只要保证满足函数式接口定义的条件，也照样是函数式接口。但是，建议加上该注解

 */

//函数式接口作为方法的参数  这里以Runnable为例
//public class Test {
//    public static void main(String[] args) {
//        //匿名内部类的方式
////        testRunnable(new Runnable() {
////            @Override
////            public void run() {
////                System.out.println(Thread.currentThread().getName()+"线程启动了");//Thread-0线程启动了
////            }
////        });
//        //函数式接口作为参数的方式
//        testRunnable(()-> System.out.println(Thread.currentThread().getName()+"线程启动了"));//Thread-0线程启动了
//    }
//
//    public static void testRunnable(Runnable r){
//        new Thread(r).start();
//    }
//}

//函数式接口作为方法的返回值
/*
如果方法的返回值是一个函数式接口 我们可以使用Lambda表达式作为结果返回
 */
//public class Test {
//    public static void main(String[] args) {
//        ArrayList<String>al=new ArrayList<>();
//        al.add("cccc");
//        al.add("bb");
//        al.add("a");
//        al.add("ddd");
//        System.out.println("排序前:"+al);//排序前:[cccc, bb, a, ddd]
//        Collections.sort(al);
//        System.out.println("排序后："+al);//按字符串的自然数排序  排序后：[a, bb, cccc, ddd]
//
//        //用指定的排序器
//        Collections.sort(al,get_compara());
//        System.out.println("按字符串长度排序后："+al);//按字符串长度排序后：[a, bb, ddd, cccc]
//    }
//    public static Comparator<String>get_compara(){
//        //匿名内部类的方法
////        Comparator<String>co=new Comparator<String>() {
////            @Override
////            public int compare(String s1, String s2) {
////                return s1.length()-s2.length();//指定按字符串的长度排
////            }
////        };
////        return co;
//
//        //Lambda方法
//        return (s1,s2)->s2.length()-s2.length();//可以
//    }
//}


/*
常用函数式接口：
Supplier接口
Consumer接口
predicate接口
Function接口
 */
/*
Supplier接口：这是一个函数接口，因此可以用作 lambda 表达式或方法引用的赋值目标。
T	get()	获取结果 T:泛型 根据传递的值的类型确定
 */
//public class Test {
//    public static void main(String[] args) {
//        //匿名内部类
////       String s= getString(new Supplier<String>() {
////            @Override
////            public String get() {
////                return "林青霞";
////            }
////        });
////        System.out.println(s);//林青霞
//        //Lambda方法
//        String s = getString(() -> "林青霞");
//        System.out.println(s);//林青霞
//
//        int i=getint(()->100);
//        System.out.println(i);//100
//
//    }
//    public static Integer getint(Supplier<Integer>s){
//       return s.get();
//    }
//    public static String getString(Supplier<String> s) {
//        return s.get();//返回接收到的字符串
//    }
//}


/*
常用函数式接口之 Consumer
Consumer:这是一个函数接口，因此可以用作 lambda 表达式或方法引用的赋值目标。
void	accept(T t)	对给定参数执行此操作。
default Consumer<T>	andThen(Consumer<? super T> after)返回一个组合，该组合按顺序执行此操作，后跟该操作。Consumerafter
 */
//public class Test {
//    public static void main(String[] args) {
////        //Lambda方法
////        testconsumer("林青霞",s-> System.out.println(s));
////        //方法引用
////        testconsumer("林青霞",System.out::println);//林青霞
//        testconsumer("林青霞",s-> System.out.println(s),s-> System.out.println(new StringBuilder(s).reverse().toString()));
//        //林青霞  霞青林
//    }
//    //有两个消费函数
//    public static void testconsumer(String s,Consumer<String>con1,Consumer<String>con2){
////        con1.accept(s);
////        con2.accept(s);
//        //以上两步可以合成一步
//        con1.andThen(con2).accept(s);
//    }
//
//    //只有一个消费函数
//    public static void testconsumer(String s, Consumer<String> con){
//        con.accept(s);
//    }
//}


//用Consumer将字符串按指定格式输出
//public class Test {
//    public static void main(String[] args) {
//        String[] arr = {"林青霞,20", "张曼玉,18", "柳岩,21"};
//        testconsumer(arr, s -> System.out.print("姓名：" + s.split(",")[0]),
//                s -> System.out.println(",年龄：" + Integer.parseInt(s.split(",")[1])));
//    }
//
//    public static void testconsumer(String[] s, Consumer<String> con1, Consumer<String> con2) {
//        for (String t : s) {
//            con1.andThen(con2).accept(t);
//        }
//    }
//}

/*
常用函数式接口之Predicate
Predicate中常用的四个方法
boolean	test(T t)	根据给定参数评估此谓词。
default Predicate<T>	and(Predicate<? super T> other)	返回一个组合谓词，该谓词表示此谓词和另一个谓词的短路逻辑 AND。 逻辑与
default Predicate<T>	or(Predicate<? super T> other)	返回一个组合谓词，该谓词表示此谓词和另一个谓词的短路逻辑 OR。  逻辑或
static <T> Predicate<T>	not(Predicate<? super T> target)返回一个谓词，该谓词是对所提供谓词的否定。                  逻辑非
 */

//前两个方法
//public class Test {
//    public static void main(String[] args) {
//       boolean b= testpredicate("hello",s->s.length()>8);
//        System.out.println(b);//false hello的长度小于8
//
//    }
//    public static boolean testpredicate(String s, Predicate<String> pre){
//       // return pre.test(s);//正常的判定
//        //return !pre.test(s);//取反
//        return pre.negate().test(s);//也是取反（通过取反函数）
//    }
//}

//predicate接口中另外两个方法 and 和or方法(组合判断)
//public class Test {
//    public static void main(String[] args) {
//        boolean b=testpredicate("hello",s->s.length()>5,s->s.length()<10);
//        System.out.println(b);//false  因为hello的的长度等于5
//        boolean b1=testpredicate("hello",s->s.length()>4,s->s.length()<10);
//        System.out.println(b1);//true
//    }
//    public static boolean testpredicate(String s,Predicate<String>p1,Predicate<String>p2){
////        boolean b1=p1.test(s);
////        boolean b2=p2.test(s);
////        boolean b3=b1&&b2;//与计算
////        return b3;
//        //上面的额可以直接用and替代
////        return p1.and(p2).test(s);
//        //或计算
//        return p1.or(p2).test(s);//或计算  有一个成立就为真
//    }
//}


//常用函数式接口之Function
/*
Function<T,R>:用于对函数进行处理，转换 然后返回一个新的值 其中T为传入的参数类型 R为要返回的参数类型
Function中常用的两个方法：qpply和andThen
 */
//public class Test {
//    public static void main(String[] args) {
//        //testfunc("100",s->Integer.parseInt(s));
//
//        testfunc("100", s -> Integer.parseInt(s), i -> String.valueOf(i + 20));
//    }
//
//    //    public static void testfunc(String s, Function<String,Integer>f){
////        Integer a = f.apply(s);
////        System.out.println(a);//100
////    }
//    public static void testfunc(String s, Function<String, Integer> f1, Function<Integer, String> f2) {
////        Integer a = f1.apply(s);//将s转为int
////        String ss = f2.apply(a);//将a做处理后转为string
////        System.out.println(ss);//120
//        //以上可以用andThen代替
//        String ss = f1.andThen(f2).apply(s);
//        System.out.println(ss);//120
//
//    }
//}

//体验Stream流
/*
我们有一个集合  集合中有很多姓名 我们要从中提取出姓张的并且长度为三的姓名
 */
//public class Test {
//    public static void main(String[] args) {
//        ArrayList<String>list=new ArrayList<>();
//        list.add("张曼玉");
//        list.add("柳岩");
//        list.add("张无忌");
//        list.add("刑天");
//        list.add("林青霞");
//        list.stream().filter(s->s.startsWith("张")).filter(s->s.length()==3).forEach(System.out::println);
//        //张曼玉  张无忌
//    }
//}


//Stream流的常见生成方式
/*
1.Collection体系可以直接生成Stream流
2.Map体系的集合间接生成流
3.数组可以通过Stream接口的静态方法of生成流
 */

//public class Test {
//    public static void main(String[] args) {
//        //1.Collection体系可以直接生成Stream流
//        List<String>list=new ArrayList<>();
//        Stream<String> stringStream = list.stream();
//
//        Set<String>set=new HashSet<>();
//        Stream<String> setstream = set.stream();
//
//        //2.Map体系的集合间接生成流
//        Map<String,String>map=new HashMap<String, String>();
//        Stream<String> mapstring = map.keySet().stream();//通过Keyset间接生成Stream流
//        Stream<String> stringStream1 = map.values().stream();//通过values间接生成Stream流
//        Stream<Map.Entry<String, String>> entryStream = map.entrySet().stream();//通过entrySet来生成Stream流
//
//        //3.数组可以通过Stream接口的静态方法of生成流
//        String[] s={"hello","world","java"};
//        Stream<String> s1 = Stream.of(s);
//        Stream<String> hello = Stream.of("hello", "world", "java");
//        Stream<Integer> intg = Stream.of(10, 20);
//    }
//}



//Stream流中的中间方法之filter
//public class Test {
//    public static void main(String[] args) {
//        List<String>list=new ArrayList<String>();
//        list.add("张曼玉");
//        list.add("张敏");
//        list.add("张无忌");
//        list.add("林青霞");
//        list.stream().filter(s->s.startsWith("张")).filter(s->s.length()==3).forEach(System.out::println);
//        //张曼玉  张无忌
//    }
//}

//Stream流中间操作之limit和skip
/*
limit:截取前指定参数个数据
skip:跳过指定参数个是数据，返回剩余元素的流
 */
//public class Test {
//    public static void main(String[] args) {
//        List<String>list=new ArrayList<String>();
//        list.add("张曼玉");
//        list.add("张敏");
//        list.add("张无忌");
//        list.add("林青霞");
//        list.add("张三丰");
//        //输出前俩个数据
//        list.stream().limit(2).forEach(System.out::println);
//        System.out.println("---------");
//        //输出后三个数据
//        list.stream().skip(2).forEach(System.out::println);
//        System.out.println("---------");
//        //输出后三个中前两个数据
//        list.stream().skip(2).limit(2).forEach(System.out::println);
//    }
//}


//Stream流的中间操作之Concat和distinct
/*
    static <T> Stream<T>concat(Stream<? extends T> a, Stream<? extends T> b)
        创建一个懒惰连接的流，其元素是第一个流的所有元素，后跟第二个流的所有元素。
       Stream<T>distinct()返回由此流的不同元素（根据 Object.equals（Object））组成的流。
 */
//public class Test {
//    public static void main(String[] args) {
//        List<String>list=new ArrayList<String>();
//        list.add("张曼玉");
//        list.add("张敏");
//        list.add("张无忌");
//        list.add("林青霞");
//        list.add("张三丰");
//        //截取前两个前三个元素
//        Stream<String> s1 = list.stream().limit(3);
//        //截取后三个元素
//        Stream<String> s2 = list.stream().skip(2);
//        //合并
//        //Stream.concat(s1,s2).forEach(System.out::println);//中间元素张无忌出现了两次
//        //System.out.println("--------");
//        //去重
//        Stream.concat(s1,s2).distinct().forEach(System.out::println);//这样就可以去重了
//
//    }
//}


//Stream中间流之sorted
/*
Stream<T>sorted():返回由此流的元素组成的流，该流按自然顺序排序。
Stream<T>sorted(Comparator<? super T> comparator):返回由此流的元素组成的流，该流根据提供的 排序。Comparator
 */
//public class Test {
//    public static void main(String[] args) {
//        List<String>list=new ArrayList<String>();
//        list.add("zhangmanyu");
//        list.add("zhangmin");
//        list.add("zhangwuji");
//        list.add("linqingxia");
//        list.add("zhangsanfeng");
//        //list.stream().sorted().forEach(System.out::println);//按自然顺序排列
//        list.stream().sorted((s1,s2)->{
//            int num1=s1.length()-s2.length();//先按长度来排序
//            int num2=num1==0?s1.compareTo(s2):num1;//长度相同则按自然顺序排列
//            return num2;
//        }).forEach(System.out::println);
//
//    }
//}

//Stream流中间操作之map 和 mapToint
/*
<R> Stream<R>map(Function<? super T,? extends R> mapper)返回一个流，该流由将给定函数应用于此流的元素的结果组成。
IntStream mapToInt(ToIntFunction<? super T> mapper)返回一个，其中包含将给定函数应用于此流的元素的结果。IntStream
IntStream:表示原始int流
 */

//public class Test {
//    public static void main(String[] args) {
//        List<String>list=new ArrayList<String>();
//        list.add("10");
//        list.add("20");
//        list.add("30");
//        list.add("40");
//        list.add("50");
//        //list.stream().map(Integer::parseInt).forEach(System.out::println);//将字符串转换为int类型并输出
//
//        list.stream().mapToInt(Integer::parseInt).forEach(System.out::println);
//        int result = list.stream().mapToInt(Integer::parseInt).sum();
//        System.out.println(result);//150
//    }
//}


//Stream流中的收集操作
/*
static <T> Collector<T,?,List<T>> toList()返回 将输入元素累加到新 .CollectorList
static <T> Collector<T,?,Set<T>> toSet()返回 将输入元素累加到新 .CollectorSet
static <T,K,U> Collector<T,?,Map<K,U>>toMap(Function<? super T,? extends K>
keyMapper, Function<? super T,? extends U> valueMapper)返回 将元素累加到 a 中，
其键和值是将提供的映射函数应用于输入元素的结果。CollectorMap
 */
//public class Test {
//    public static void main(String[] args) {
//                List<String>list=new ArrayList<String>();
////        list.add("张曼玉");
////        list.add("张敏");
////        list.add("张无忌");
////        list.add("林青霞");
////        list.add("张三丰");
////        //得到名字为三个字的人收集到List集合并遍历
////        List<String> collect = list.stream().filter(s -> s.length() == 3).collect(Collectors.toList());
////        for(String s: collect){
////            System.out.println(s);
////        }
//
//        list.add("张曼玉，33");
//        list.add("张小敏，28");
//        list.add("张无忌，40");
//        list.add("林青霞，30");
//        list.add("张三丰，100");
//        //得到姓名为三个字并且年龄大于等于30的人收集到set集合中并遍历
////        Set<String> collect = list.stream().filter(s -> s.split("，")[0].length() == 3).filter
////                (s -> Integer.parseInt(s.split("，")[1]) >= 30).collect(Collectors.toSet());
////        for(String s:collect){
////            System.out.println(s);
////        }
//
//        //将上面的年龄大于等于30的人收集到Map集合中
//        Stream<String> ss = list.stream().filter(s -> Integer.parseInt(s.split("，")[1]) >= 30);
//        Map<String, Integer> map = ss.collect(Collectors.toMap(s -> s.split("，")[0], s -> Integer.parseInt(s.split("，")[1])));
//        Set<String> strings = map.keySet();//获取键值对集合
//        for(String s:strings){
//            Integer i = map.get(s);//通过键值获取value值
//            System.out.println(s+","+i);
//        }
//
//    }
//}
























