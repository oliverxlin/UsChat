package com.example.richado.uschat;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

public class MsgAdapter extends ArrayAdapter<Msg>{
    private int resourceId;

    public MsgAdapter(Context context, int textViewResourceId,
                      List<Msg> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    /**
     * 功能描述：这个方法在每个子项滚动到屏幕内的时候被调用
     *         1.首先通过 getItem()获取当前项的ToolBar实例
     *         2.使用LayoutInflater将这个子布局项加载并传入我们的主布局
     *         3.调用View的findViewById()分别获取到左右Layout、左右Msg的实例
     *         4.调用它们的setText()来显示文字
     *         5.最后返回布局
     * */
    public View getView(int position, View convertView, ViewGroup parent){
        Msg msg = getItem(position);
        /*
         * 新增内部类ViewHolder，用于对控件的实例进行缓存。
         * 1.当convertView为空时，创建一个ViewHolder对象，并将控件的实例对象存放在ViewHolder里。
         * 2.然后调用View的setTag()方法，将ViewHolder对象存储在View中。
         * 3.当convertView不为空时，调用View的getTag()方法把ViewHolder重新取出来。
         * */
        View view;
        ViewHolder viewHolder;

        /*加载自定义布局与控件实例*/
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);

            //创建控件实例并进行缓存
            viewHolder = new ViewHolder();
            viewHolder.leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
            viewHolder.rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
            viewHolder.leftMsg = (TextView) view.findViewById(R.id.left_msg);
            viewHolder.rightMsg = (TextView) view.findViewById(R.id.right_msg);

            view.setTag(viewHolder);

        } else {
            //convertView参数用于将之前加载好的布局进行缓存，以便之后可以进行复用（提高效率）
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        /*接受与发送消息的分类处理*/
        //如果为收到的消息，则显示左边的消息布局，将右边的消息布局隐藏
        if(msg.getType() == Msg.TYPE_RECEIVED){
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.leftMsg.setText(msg.getContent());

        } else if(msg.getType() == Msg.TYPE_SENT){
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.rightMsg.setText(msg.getContent());
        }

        return view;
    }

    //新增内部类ViewHolder，用于对控件的实例进行缓存。
    class ViewHolder{

        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
    }

}
