package com.example.hp.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.hp.myapplication.adapter.SelectionAdapter;
import com.example.hp.myapplication.entity.Selection;

import java.util.ArrayList;
import java.util.List;

public class Personfragment extends Fragment {
    private List<Selection> selectionList1 = new ArrayList<>();
    private List<Selection> selectionList2 = new ArrayList<>();
    private List<Selection> selectionList3 = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View messageLayout=inflater.inflate(R.layout.activity_personal,container,false);

        initSelections1();
        initSelections2();
        initSelections3();

        SelectionAdapter adapter1 = new SelectionAdapter(
                getActivity(), R.layout.selection_item,
                selectionList1
        );
        SelectionAdapter adapter2 = new SelectionAdapter(
                getActivity(), R.layout.selection_item,
                selectionList2
        );
        SelectionAdapter adapter3 = new SelectionAdapter(
                getActivity(), R.layout.selection_item,
                selectionList3
        );
        ListView listView1 = messageLayout.findViewById(R.id.per_list1);
        ListView listView2 = messageLayout.findViewById(R.id.per_list2);
        ListView listView3 = messageLayout. findViewById(R.id.per_list3);
        listView1.setAdapter(adapter1);
        listView2.setAdapter(adapter2);
        listView3.setAdapter(adapter3);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Selection selection = selectionList1.get(position);

                if (selection.getName().equals("个人资料")) {
                    Intent intent = new Intent(getActivity(), PersActivity.class);
                    startActivity(intent);
                }
                if (selection.getName().equals("团队")) {
                    Intent intent = new Intent(getActivity(), TeamsListActivity.class);
                    startActivity(intent);
                }
            }
        });
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Selection selection = selectionList2.get(position);

            }
        });
        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Selection selection = selectionList3.get(position);

                if (selection.getName().equals("使用帮助")) {
                    Intent intent = new Intent(getActivity(), UseHelpActivity.class);
                    startActivity(intent);
                }
            }
        });
        return  messageLayout;
    }

    //初始化列表数据
    private void initSelections1() {

        Selection account = new Selection("个人资料", R.drawable.account_icon);
        selectionList1.add(account);
        Selection team = new Selection("团队", R.drawable.team_icon);
        selectionList1.add(team);
    }
    private void initSelections2() {

        Selection exchange = new Selection("兑换", R.drawable.exchange_icon);
        selectionList2.add(exchange);
    }

    private void initSelections3() {

        Selection restTime = new Selection(5, "休息时间");
        selectionList3.add(restTime);
        Selection help = new Selection("使用帮助");
        selectionList3.add(help);
    }
}
