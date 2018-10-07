package com.chinjiaxiong.hongleongchan;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Account account = new Account();

    final Handler handler2 = new Handler();
    Runnable runnable;

    private static final String TAG = "query_result";//MainActivity
    public static final int DELAY_MILLIS = 5000;
    public static int flag = 0;
    String[] commands = {"what is my balance", "list my account type", "I want to do transaction", "Show me my last transaction", "How much is my cash flow for this month"};
    String[] favAcc;
    String target = "";
    double targetAmount = 0;

    TextToSpeech t1;
    private static volatile boolean mIsListening = false;
    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;

    private void displayText(String text){
        ImageView myView = (ImageView) findViewById(R.id.imageView2);
        final TextView tw = (TextView) findViewById(R.id.tv);
        final AnimatorSet mAnimationSet = new AnimatorSet();
        final ObjectAnimator fadeOut, fadeIn, fadeOutText, fadeInText;
        fadeOut = ObjectAnimator.ofFloat(myView, "alpha",  0.5f, 0f);
        fadeOut.setDuration(2000);
        fadeIn = ObjectAnimator.ofFloat(myView, "alpha", 0f, 0.5f);
        fadeIn.setDuration(2000);
        fadeOutText = ObjectAnimator.ofFloat(tw, "alpha",  1f, 0f);
        fadeOut.setDuration(1000);
        fadeInText = ObjectAnimator.ofFloat(tw, "alpha", 0f, 1f);
        fadeIn.setDuration(1000);
        mAnimationSet.play(fadeIn).after(fadeInText);
        mAnimationSet.start();
        // type writer write

        tw.setText(text);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                tw.setText("");
                mAnimationSet.play(fadeOut).after(fadeOutText);
                mAnimationSet.start();
            }
        }, 5000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        checkPermission();
        checkVoiceRecognition();
        favAcc = new String[]{"john", "andre", "lee yee run"};
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                    t1.setPitch(0.25f);
                }
            }
        });

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());
        SpeechRecognitionListener listener = new SpeechRecognitionListener();
        mSpeechRecognizer.setRecognitionListener(listener);

        final ImageView img = (ImageView) findViewById(R.id.imageView1);
        img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:

                        if(!mIsListening){
                            mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                            ImageView myView = (ImageView) findViewById(R.id.imageView1);
                            myView.setAlpha(0.5f);
                            stopCharacter();
                            mIsListening = true;
                        }

                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:

                        mSpeechRecognizer.stopListening();
                        mIsListening = false;
                        ImageView myView = (ImageView) findViewById(R.id.imageView1);
                        myView.setAlpha(1f);

                        return true;
                }

                return false;
            }
        });
        final int[] imageArray = {R.drawable.man1, R.drawable.man2, R.drawable.man3, R.drawable.man4};
        runnable = new Runnable() {
            int i=0;
            public void run() {
                img.setImageResource(imageArray[i]);
                i++;
                if(i>imageArray.length-1)
                {
                    i=0;
                }
                handler2.postDelayed(this, 50);  //for interval...
            }
        };
    }

    public void moveCharacter(){
        handler2.postDelayed(runnable, 100);
    }

    public void stopCharacter(){
        try {
            handler2.removeCallbacks(runnable);
        } catch(Exception e){

        }
    }

    public void getVoiceInput(){
        if(!mIsListening){
            mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
            ImageView myView = (ImageView) findViewById(R.id.imageView1);
            myView.setAlpha(0.5f);
            stopCharacter();
            mIsListening = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSpeechRecognizer.stopListening();
                    mIsListening = false;
                    ImageView myView = (ImageView) findViewById(R.id.imageView1);
                    myView.setAlpha(1f);
                }
            }, DELAY_MILLIS);
        }
    }

    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (mSpeechRecognizer != null)
        {
            mSpeechRecognizer.destroy();
        }
    }

    public void checkVoiceRecognition() {
        // Check if voice recognition is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
        RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
            Toast.makeText(this, "Voice recognizer not present", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }

    void showToastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

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

    protected class SpeechRecognitionListener implements RecognitionListener
    {

        @Override
        public void onBeginningOfSpeech()
        {
            Log.d(TAG, "onBeginingOfSpeech");
        }

        @Override
        public void onBufferReceived(byte[] buffer)
        {

        }

        @Override
        public void onEndOfSpeech()
        {
            Log.d(TAG, "onEndOfSpeech");
        }

        @Override
        public void onError(int error)
        {
            Log.d(TAG, "error = " + error);
        }

        @Override
        public void onEvent(int eventType, Bundle params)
        {

        }

        @Override
        public void onPartialResults(Bundle partialResults)
        {

        }

        @Override
        public void onReadyForSpeech(Bundle params)
        {
            Log.d(TAG, "onReadyForSpeech"); //$NON-NLS-1$
        }

        @Override
        public void onResults(Bundle results)
        {
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            try{
                // init everything
                PriorityQueue<AccuracyQueue> queue = new PriorityQueue<>();
                String output = null;
                String input = matches.get(0);

                queue.add(new AccuracyQueue(0, StringSimilarity.similarity(input, commands[0])));
                queue.add(new AccuracyQueue(1, StringSimilarity.similarity(input, commands[1])));
                queue.add(new AccuracyQueue(2, StringSimilarity.similarity(input, commands[2])));
                queue.add(new AccuracyQueue(3, StringSimilarity.similarity(input, commands[3])));
                queue.add(new AccuracyQueue(4, StringSimilarity.similarity(input, commands[4])));
                int index = 0;
                if(!queue.isEmpty()) index = queue.peek().index;

                if(flag == 1){
                    target = StringSimilarity.similarity(input, "john") >= 0.4? "john":"andre";
                    double tmp = StringSimilarity.similarity(input, target);
                    target = StringSimilarity.similarity("lee yee run", input) > tmp? "lee yee run":target;
                    output = "How much would you like to send?";
                    displayText(output);
                    t1.speak(output, TextToSpeech.QUEUE_FLUSH, null);
                    moveCharacter();
                    flag = 2;
                }

                else if(flag == 2){
                    targetAmount = Double.parseDouble(input);
                    output = "Successfully transferred " + targetAmount + " to " + target;
                    account.send_amount(target, targetAmount);
                    displayText(output);
                    t1.speak(output, TextToSpeech.QUEUE_FLUSH, null);
                    moveCharacter();
                    flag = 0;
                }

                else{
                    if(queue.peek().result < 0.4){
                        String[] notUnderstand = {"I cannot understand", "uh huh?", "You must be kidding me.", "Can you please repeat again?", "I was not listening just now."};
                        output = notUnderstand[new Random().nextInt(5)];
                        displayText(output);
                        t1.speak(output, TextToSpeech.QUEUE_FLUSH, null);
                        moveCharacter();
                    }
                    // Get balance
                    else if(index == 0){
                        output = String.format("Your balance is %.2f", account.getBalance());
                        displayText(output);
                        t1.speak(output, TextToSpeech.QUEUE_FLUSH, null);
                        moveCharacter();
                    }
                    // Get Account type
                    else if(index == 1) {
                        output = "Your account type is " + account.getAccount_type();
                        displayText(output);
                        t1.speak(output, TextToSpeech.QUEUE_FLUSH, null);
                        moveCharacter();
                    }
                    // Do transaction
                    else if(index == 2) {
                        output = "Okay. Who would you like to send to?";
                        displayText(output);
                        t1.speak(output, TextToSpeech.QUEUE_FLUSH, null);
                        moveCharacter();
                        flag = 1;
                        // Nested calling
                    }
                    // Show the last transaction
                    else if(index == 3) {
                        output = account.last_transaction();
                        displayText(output);
                        t1.speak(output, TextToSpeech.QUEUE_FLUSH, null);
                        moveCharacter();
                    }

                    // Show cashflow
                    else if(index == 4) {
                        output = account.get_cashflow();
                        displayText(output);
                        t1.speak(output, TextToSpeech.QUEUE_FLUSH, null);
                        moveCharacter();
                    }
                    else{
                        output = "I cannot understand. Please try again.";
                        displayText(output);
                        t1.speak(output, TextToSpeech.QUEUE_FLUSH, null);
                        moveCharacter();
                    }
                }


            } catch(Exception e){
                Log.d(TAG, "error occurred");
                displayText("Sorry, transaction failed. Fail to validate the amount.");
                t1.speak("Sorry, transaction failed. Fail to validate the amount.", TextToSpeech.QUEUE_FLUSH, null);
                flag = 0;
            }
        }

        @Override
        public void onRmsChanged(float rmsdB)
        {
        }
    }
}