package rosehulman.edu.pictochat.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.firebase.database.Exclude;

import java.io.ByteArrayOutputStream;

public class MessageModel {
    private String base64Bitmap;
    private String from;
    private String time;
    private String content;
    private String key;

    public MessageModel() {}

    @Exclude
    public void setBase64Bitmap(Bitmap bitmap){
        ByteArrayOutputStream baos = new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        this.base64Bitmap = Base64.encodeToString(b, Base64.DEFAULT);
    }

    public String getBase64Bitmap() {
        return base64Bitmap;
    }

    public void setBase64Bitmap(String base64Bitmap) {
        this.base64Bitmap = base64Bitmap;
    }


    @Exclude
    public Bitmap getBitmap(){
        try {
            byte [] encodeByte=Base64.decode(base64Bitmap, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setContent(String content){this.content = content;}

    public String getContent(){return content;}
}
