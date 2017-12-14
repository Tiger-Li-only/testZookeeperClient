import org.I0Itec.zkclient.IZkChildListener;

import java.util.List;

/**
 * Created by Administrator on 2017-12-13 0013.
 */
public class MyZkChildListener implements IZkChildListener {
    @Override
    public void handleChildChange(String s, List<String> strings) throws Exception {
        System.out.println("节点列表发生改变，parentPath="+s+",当前子节点列表="+strings);
    }
}
