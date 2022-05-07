package com.warden.notebook;

import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

public class MMKVUtil {

    public static void addNote(NoteBean bean) {
        ArrayList<NoteBean> list = getNotes();
        list.add(bean);
        setNotes(list);
    }

    public static Boolean setNotes(List<NoteBean> list) {
//        kv.encode(1.toString(), "去跟人类伟大的灵魂对话");
//        kv.encode(2.toString(), "去超越浅薄与平庸");
        return setArray(list, "notes");
    }

    public static <T> ArrayList<T> getNotes() {
        return getArray("notes", NoteBean.class);
    }

    public static <T> Boolean setArray(List<T> list, String name) {
        MMKV kv = MMKV.defaultMMKV();
        if (list == null || list.size() == 0) { //清空
            kv.putInt(name + "size", 0);
            int size = kv.getInt(name + "size", 0);
            for (int i = 0; i < size; i++) {
                if (kv.getString(name + i, null) != null) {
                    kv.remove(name + i);
                }
            }
        } else {
            kv.putInt(name + "size", list.size());
            if (list.size() > 20) {
                list.remove(0);   //只保留后20条记录
            }
            for (int i = 0; i < list.size(); i++) {
                kv.remove(name + i);
                kv.remove(new Gson().toJson(list.get(i)));//删除重复数据 先删后加
                kv.putString(name + i, new Gson().toJson(list.get(i)));
            }
        }
        return kv.commit();
    }

    public static <T> ArrayList<T> getArray(String name, Class<?> clazz) {
        MMKV kv = MMKV.defaultMMKV();
        ArrayList<T> list = new ArrayList<T>();
        int size = kv.getInt(name + "size", 0);
        for (int i = 0; i < size; i++) {
            if (kv.getString(name + i, null) != null) {
                try {
                    list.add((T) new Gson().fromJson(kv.getString(name + i, null), clazz));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return list;
    }

    public boolean put(@NonNull Object object) {
        MMKV mmkv = MMKV.defaultMMKV();
        if (object instanceof Parcelable) {
            return mmkv.encode(object.getClass().getName(), (Parcelable) object);
        } else {
            return mmkv.encode(object.getClass().getName(), new Gson().toJson(object));
        }
    }

    public <T> T getObject(@NonNull Class<T> key) {
        MMKV mmkv = MMKV.defaultMMKV();
        if (Parcelable.class.isAssignableFrom(key)) {
            return (T) mmkv.decodeParcelable(key.getName(), (Class<? extends Parcelable>) key);
        } else {
            String v = mmkv.decodeString(key.getName(), (String) null);
            if (!TextUtils.isEmpty(v)) {
                return new Gson().fromJson(v, key);
            }
        }
        return null;
    }
}
