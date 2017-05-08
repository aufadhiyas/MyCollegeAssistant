package me.citrafa.asistenkuliahku;

import android.os.Bundle;
import android.widget.Toast;

import com.kenumir.materialsettings.MaterialSettings;
import com.kenumir.materialsettings.items.CheckboxItem;
import com.kenumir.materialsettings.items.HeaderItem;
import com.kenumir.materialsettings.items.TextItem;
import com.kenumir.materialsettings.storage.StorageInterface;

/**
 * Created by SENSODYNE on 08/05/2017.
 */

public class SettingActivity extends MaterialSettings{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addItem(new HeaderItem(this).setTitle("Notifikasi"));
        addItem(new CheckboxItem(this,"key1").setTitle("Status Notifikasi").setSubtitle("Notifikasi untuk jadwal kuliah dll.").setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {
            @Override
            public void onCheckedChange(CheckboxItem checkboxItem, boolean b) {
                if (b){
                    Toast.makeText(SettingActivity.this, "Notifikasi DI Aktifkan", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SettingActivity.this, "Notifikasi DI Matikan", Toast.LENGTH_SHORT).show();
                }
            }
        }));
        addItem(new TextItem(this,"Nama").setTitle("Notifikasi Sebelum").setSubtitle("notifikasi akan tampil sebelum").setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem textItem) {
                Toast.makeText(SettingActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    public StorageInterface initStorageInterface() {
        return null;
    }
}
