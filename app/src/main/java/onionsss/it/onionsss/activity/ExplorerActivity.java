package onionsss.it.onionsss.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.lfk.justwetools.View.FileExplorer.FileExplorer;
import com.lfk.justwetools.View.FileExplorer.OnFileChosenListener;
import com.lfk.justwetools.View.FileExplorer.OnPathChangedListener;
import com.lidroid.xutils.util.MimeTypeUtils;

import onionsss.it.onionsss.R;

public class ExplorerActivity extends AppCompatActivity {

    private FileExplorer mFileExplorer;
    private TextView explorer_tv_title;
    private TextView explorer_tv_path;
    private String mMIMEType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);
        initVIew();
        initListener();
        // 打开路径
        mFileExplorer.setCurrentDir(Environment.getExternalStorageDirectory().getPath());
        // 根路径（能到达最深的路径，以此避免用户进入root路径）
        mFileExplorer.setRootDir(Environment.getExternalStorageDirectory().getPath());
        Intent intent = getIntent();
        String title = intent.getData().toString();
        explorer_tv_title.setText(title);
        mMIMEType = intent.getType();
        explorer_tv_path.setText(mFileExplorer.getCurrentPath());

    }

    private void initListener() {
        //覆盖屏蔽原有长按事件
        mFileExplorer.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });
        //选择打开文件
        mFileExplorer.setOnFileChosenListener(new OnFileChosenListener() {
            @Override
            public void onFileChosen(Uri fileUri) {
                Log.d("ExplorerActivity", "mMIMEType:" + mMIMEType + "/current:" + MimeTypeUtils.getMimeType(fileUri.getPath()));
                if (!MimeTypeUtils.getMimeType(fileUri.getPath()).startsWith(mMIMEType)) {
                    Toast.makeText(ExplorerActivity.this, "选择的文件类型不正确,请选择"
                            + mMIMEType + "文件", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent data = new Intent();
                data.setData(fileUri);
                setResult(RESULT_OK, data);
                finish();
            }
        });

        //更新地址栏
        mFileExplorer.setOnPathChangedListener(new OnPathChangedListener() {
            @Override
            public void onPathChanged(String s) {
                explorer_tv_path.setText(mFileExplorer.getCurrentPath());
            }
        });

    }

    private void initVIew() {
        mFileExplorer = (FileExplorer) findViewById(R.id.explorer_explorer);
        explorer_tv_title = (TextView) findViewById(R.id.explorer_tv_title);
        explorer_tv_path = (TextView) findViewById(R.id.explorer_tv_path);
    }
}
