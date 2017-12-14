import org.I0Itec.zkclient.IZkDataListener;

/**
 * Created by Administrator on 2017-12-13 0013.
 */
public class MyZkDataListener implements IZkDataListener {
    @Override
    public void handleDataChange(String s, Object o) throws Exception {
        System.out.println("节点数据发生改变，路径="+s+",数据="+o.toString());
    }

    @Override
    public void handleDataDeleted(String s) throws Exception {
       System.out.println("节点被删除，路径="+s);
    }
}
