package xingyingyue.com.goexplore.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xingyingyue.com.goexplore.R;

/**
 * Created by huanghaojian on 17/6/28.
 */

public class FragmentTab3 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab3, container, false); //解析布局，此处第三个参数应为false,否则会返回父视图的布局，导致OOM
        return v;
    }

}
