package jp.akita.conchan.cameratest;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SurfaceView mySurfaceView;
    private Camera myCamera; //hardware


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // SurfaceView
        mySurfaceView = (SurfaceView)findViewById(R.id.camera_surfaceView);
        //リスナ追加 //STEP2で追加
        mySurfaceView.setOnClickListener(onSurfaceClickListener);

        // SurfaceHolder(SVの制御に使うInterface）
        SurfaceHolder holder = mySurfaceView.getHolder();
        // setting callback
        holder.addCallback(callback);

    }

    // callback
    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {

            //CameraOpen
            myCamera = Camera.open();

            //出力をSurfaceViewに設定
            try{
                myCamera.setPreviewDisplay(holder);
            }catch(Exception e){
                e.printStackTrace();
            }

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            //プレビュースタート（Changedは最初にも1度は呼ばれる）
            myCamera.startPreview();

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

            //片付け
            myCamera.release();
            myCamera = null;

        }
    };


    //以下、STEP2で追加

    //Surfaceをクリックした時
    private View.OnClickListener onSurfaceClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            if(myCamera != null){
                //AutoFocusを実行
                myCamera.autoFocus(autoFocusCallback);
            }
        }

    };

    //AutoFocusした時
    private Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback(){
        @Override
        public void onAutoFocus(boolean b, Camera camera) {

            //ただ写真を撮るだけならここにtakePicture()を入れても良い
            //しかし、Pixelを弄りたいのでもうワンクッション（画像処理やバーコード処理をしたいので）
            //Previewを1枚切り取る（そしてイベント発生）
            camera.setOneShotPreviewCallback(previewCallback);
        }
    };

    //切り取った時（ここで撮影、各種画像処理を行う）
    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback(){

        //OnShotPreview時のbyte[]が渡ってくる
        @Override
        public void onPreviewFrame(byte[] bytes, Camera camera) {

            Toast.makeText(getApplicationContext(),"フォーカスが合い画像を切り取りました。", Toast.LENGTH_SHORT).show();

        }
    };















    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
