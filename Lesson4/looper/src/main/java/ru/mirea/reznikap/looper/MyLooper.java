package ru.mirea.reznikap.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class MyLooper extends Thread {
    public Handler mHandler;
    private Handler mainHandler;

    public MyLooper(Handler mainThreadHandler) {
        mainHandler = mainThreadHandler;
    }

    public void run() {
        Log.d("MyLooper", "run");
        Looper.prepare();
        mHandler = new Handler(Looper.myLooper()) {
            public void handleMessage(Message msg) {
                String ageStr = msg.getData().getString("age");
                String job = msg.getData().getString("job");

                try {
                    int age = Integer.parseInt(ageStr);
                    Thread.sleep(age * 1000);

                    String result = String.format("Ваш возраст: %d лет, ваша профессия: %s", age, job);
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("result", result);
                    message.setData(bundle);
                    mainHandler.sendMessage(message);
                } catch (InterruptedException e) {
                    Log.d("MyLooper", "Ошибка: поток прерван");
                }
            }
        };
        Looper.loop();
    }
}