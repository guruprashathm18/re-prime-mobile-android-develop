package com.radaee.util;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RDFilesView extends ListView
{
    public interface OnFilesListener
    {
        void OnItemClick(RDFilesItem item, int idx);
        void OnItemMore(RDFilesItem item, int idx);
    }
    class RDFilesAdt implements ListAdapter
    {
        private ArrayList<RDFilesItem> m_items;
        private DataSetObserver m_dset;
        public RDFilesAdt(Context ctx)
        {
            RDGridView.Init(ctx);
        }
        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }
        @Override
        public boolean isEnabled(int position) {
            return true;
        }
        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
            m_dset = observer;
        }
        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            m_dset = null;
        }
        @Override
        public int getCount() {
            if (m_items == null) return 0;
            return m_items.size();
        }
        @Override
        public Object getItem(int position) {
            return m_items.get(position);
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public boolean hasStableIds() {
            return false;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return m_items.get(position).m_view;
        }
        @Override
        public int getItemViewType(int position) {
            return 0;
        }
        @Override
        public int getViewTypeCount() {
            return 1;
        }
        @Override
        public boolean isEmpty() {
            if (m_items == null) return true;
            return m_items.isEmpty();
        }
        public void ListDir(final File dir)
        {
            String sPath = dir.getAbsolutePath();
            String sName = dir.getName();
            if (sName.charAt(0) == '.') return;
            if (sPath.compareTo("/mnt/sdcard/Android") == 0) return;
            File[] files = dir.listFiles();
            if (files == null || files.length <= 0) return;

            File[] tmp = new File[files.length];
            int cnt = 0;
            for(File file : files)
            {
                if (file.isFile())
                {
                    String sname = file.getName();
                    //if (sname.length() > 5)
                    //{
                    //    sname = sname.substring(sname.length() - 5).toLowerCase();
                    //    if(sname.compareTo(".docx") == 0) tmp[cnt++] = file;
                    //}
                    if (sname.length() > 4)
                    {
                        sname = sname.substring(sname.length() - 4).toLowerCase();
                        if(sname.compareTo(".pdf") == 0) tmp[cnt++] = file;
                    }
                }
            }
            if (cnt > 0)
            {
                final File[] tmp2 = new File[cnt];
                System.arraycopy(tmp, 0, tmp2, 0, cnt);
                post(new Runnable() {
                    @Override
                    public void run() {
                        m_items.add(new RDFilesItem(getContext(), m_lset, dir, tmp2, m_listener));
                        m_dset.onChanged();
                    }
                });
            }

            for (File file : files)
            {
                if (file.isDirectory()) ListDir(file);
            }
        }
        void Update()
        {
            m_items = new ArrayList<RDFilesItem>();
            new Thread(){
                @Override
                public void run() {
                    ListDir(new File("/mnt"));
                }
            }.start();
            m_dset.onChanged();
        }
        void Remove(RDFilesItem item)
        {
            m_items.remove(item);
            m_dset.onChanged();
        }
    }
    private RDLockerSet m_lset;
    private RDFilesAdt m_adt;
    private OnFilesListener m_listener;
    public RDFilesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_adt = new RDFilesAdt(context);
        setAdapter(m_adt);
    }
    public void Update(RDLockerSet lset, OnFilesListener listener)
    {
        m_lset = lset;
        m_listener = listener;
        m_adt.Update();
    }
    public void Update()
    {
        m_adt.Update();
    }
    public void Remove(RDFilesItem item)
    {
        m_adt.Remove(item);
    }
}
