package tk.onlinesilkstore.videochatdemo;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements Session.SessionListener, PublisherKit.PublisherListener {
    private static String API_KEY="RANJITH46231722";
    private static String SESSION_ID="RANJITH-1_MX40NjIzMTcyMn5-MTU0NDAzMDU0NDE2Mn5NdmgremlGY0NWOHF1KzdPQ0VIZlVuSWV-fg";
    private static String TOKEN="T1==ranjithcGFydG5lcl9pZD00NjIzMTcyMiZzaWc9ODdjN2VlZTkzYjk0MDBlNDkyNjA3ZTM1YjYwYjBjMDkzOTE0ZDIyYjpzZXNzaW9uX2lkPTFfTVg0ME5qSXpNVGN5TW41LU1UVTBOREF6TURVME5ERTJNbjVOZG1ncmVtbEdZME5XT0hGMUt6ZFBRMFZJWmxWdVNXVi1mZyZjcmVhdGVfdGltZT0xNTQ0MDMwNjIyJm5vbmNlPTAuMDkwMjg4NDcxMDczNDI4NjMmcm9sZT1wdWJsaXNoZXImZXhwaXJlX3RpbWU9MTU0NjYyMjYyMiZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==";
    private static final String TAG = "MainActivity";
    private static  final int RC_SETTINGS=123;
    private Session session;
    private FrameLayout subscriber, publisher;
    private Publisher mPublisher;
    private Subscriber mSubscriber;
    private ImageButton close_btn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();
        publisher=(FrameLayout)findViewById(R.id.publisher_container);
        subscriber=(FrameLayout)findViewById(R.id.subscriber_container);
        close_btn=findViewById(R.id.close);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions,grantResults,this);

    }

    @AfterPermissionGranted(RC_SETTINGS)
    private void requestPermissions()
    {
        String[] perms={Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        if(EasyPermissions.hasPermissions(this,perms))
        {
            session= new Session.Builder(this,API_KEY,SESSION_ID).build();
            session.setSessionListener(this);
            session.connect(TOKEN);
        }else
        {
            EasyPermissions.requestPermissions(this,"This App needs Acess for Camera, Mike and Internet",RC_SETTINGS,perms);
        }

    }

    @Override
    public void onConnected(Session session) {

        mPublisher=new Publisher.Builder(this).build();
        mPublisher.setPublisherListener(this);
        publisher.addView(mPublisher.getView());
        session.publish(mPublisher);
    }

    @Override
    public void onDisconnected(Session session) {

        session.disconnect();
        subscriber.removeAllViews();
        publisher.removeAllViews();
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        if(mSubscriber==null)
        {
            mSubscriber=new Subscriber.Builder(this,stream).build();
            session.subscribe(mSubscriber);
            subscriber.addView(mSubscriber.getView());
        }

    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
if(mSubscriber!=null)
{
    subscriber=null;
    subscriber.removeAllViews();
}

    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        session.disconnect();
        subscriber.removeAllViews();
        publisher.removeAllViews();

    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }

    public void close(View view) {
        session.disconnect();
        subscriber.removeAllViews();
        publisher.removeAllViews();
        finish();
    }
}
