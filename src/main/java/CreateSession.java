import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-12-13 0013.
 */
public class CreateSession {
    public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException {
        //创建连接
      ZkClient zkClient = new ZkClient("192.168.100.128:2181",5000,5000,new SerializableSerializer());
        System.out.println("conneted ok!");
        User user = new User();
        String path = "/node_300000000014";
//        user.setId("1111111");
//        user.setName("张三");
//        //创建节点
         path = zkClient.create("/node_30",user, CreateMode.EPHEMERAL_SEQUENTIAL);
         System.out.println("return path="+path);
//        //获取节点的数据
        User user1 = zkClient.readData(path);
        System.out.println("user1="+user1.toString());
//
//        //获取节点状态信息
        Stat stat = new Stat();
        User user3 = zkClient.readData(path,stat);
        System.out.println("节点状态信息="+stat);

        //修改节点数据
        user.setId("2222222222222");
        user.setName("李思");
        zkClient.writeData(path,user);
        User user2 = zkClient.readData(path);
        System.out.println("修改后的user的数据="+user2.getId()+","+user2.getName());
//
//
        //删除节点
        boolean result = zkClient.delete("/node_2");  //删除无子节点情况
        boolean result2 = zkClient.deleteRecursive("/node_2"); //删除存在子节点的情况
        System.out.println(result+"----"+result2);
//
        //检测节点
        boolean result3 = zkClient.exists("/node_3");
        System.out.println("检测节点node_3是否存在："+result3);
//
        //获取子节点列表
        List<String> childrenList = zkClient.getChildren("/");
        System.out.println("子节点列表是="+childrenList.toString());
//
        //订阅子节点变化
        zkClient.subscribeChildChanges("/node_30",new MyZkChildListener());
//
//
        //订阅节点内容变化,测试方法如果生效，需要改变序列化器new SerializableSerializer() --> new BytesPushThroughSerializer()
        zkClient.subscribeDataChanges("/node_30",new MyZkDataListener());


        //权限相关
//        基于ip白名单的权限
        ACL aclIp = new ACL(ZooDefs.Perms.READ,new Id("ip","192.168.100.128")); //单个权限
        ACL aclIps = new ACL(ZooDefs.Perms.READ| ZooDefs.Perms.WRITE,new Id("ip","192.168.100.128")); //组合权限
        ACL aclAll = new ACL(ZooDefs.Perms.ALL,new Id("ip","192.168.100.128")); //完全权限
        //基于用户名和密码的权限
        ACL aclDigest = new ACL(ZooDefs.Perms.READ | ZooDefs.Perms.WRITE,new Id("digest", DigestAuthenticationProvider.generateDigest("user:123")));
        List<ACL> aclList = new ArrayList<ACL>();
        aclList.add(aclIp);
        aclList.add(aclDigest);
        String path2 = zkClient.create("/node_acl",user,aclList, CreateMode.EPHEMERAL);
        System.out.println("path2="+path2);
        zkClient.addAuthInfo("digest","user:123".getBytes());
        User user4 = zkClient.readData("/node_acl");
        System.out.println(user4.toString());
        String path3 = zkClient.create(path2+"/node_1",user,CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(path3);

        Thread.sleep(Integer.MAX_VALUE);
        zkClient.close();
    }
}
