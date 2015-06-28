    package com.threebrothers.scorestrip;

    import android.content.Context;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.hardware.Camera;
    import android.os.Bundle;
    import android.os.Environment;
    import android.support.v7.app.ActionBarActivity;
    import android.util.DisplayMetrics;
    import android.util.Log;
    import android.view.Display;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.SurfaceHolder;
    import android.view.SurfaceView;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.FrameLayout;
    import android.widget.Toast;

    import java.io.File;
    import java.io.FileNotFoundException;
    import java.io.FileOutputStream;
    import java.io.IOException;
    import java.util.Date;
    import java.util.List;


    public class data_capture extends ActionBarActivity {

        double PM3_RATIO = 0.98;
        double X_OVER_Y_RATIO = PM3_RATIO;
        double SQRT5 = 2.2360679775;
        double GRATIO = ((SQRT5 - 1) / 2);
        CameraPreview cameraControl;
        View rect;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_data_capture);
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_surface);
            setRect();
            cameraControl = new CameraPreview(this);
            preview.addView(cameraControl);
        }


        public void toast_capture(View view){

            if(cameraControl.hasCamera()) {
                cameraControl.mCamera.takePicture(null, null, cameraControl);
            }
            Toast toast = Toast.makeText(getApplicationContext(),"Picture Taken",Toast.LENGTH_SHORT);
            toast.show();
        }

        private String uniqueErgName(){
            Date d = new Date();
            String unique = Long.toString(d.getTime());
            return ("ergStrip-"+unique+".jpg");
        }


        private void setRect(){
            DisplayMetrics metrics = this.getResources().getDisplayMetrics();
            double width = (double) metrics.widthPixels;
            double Sx = width*GRATIO;
            double Sy = width*PM3_RATIO*GRATIO;
            rect = findViewById(R.id.ergo_rect);
            rect.getLayoutParams().width = (int) Sx;
            rect.getLayoutParams().height = (int) Sy;
        }

            @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            //getMenuInflater().inflate(R.menu.menu_data_capture, menu);
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

        public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, Camera.PictureCallback {
            private SurfaceHolder mHolder;
            private Camera mCamera = null;
            private Camera.Parameters param;
            int CamH;
            int CamW;



            public CameraPreview(Context context) {
                super(context);

                // Install a SurfaceHolder.Callback so we get notified when the
                // underlying surface is created and destroyed.
                mHolder = getHolder();
                mHolder.addCallback(this);
                // deprecated setting, but required on Android versions prior to 3.0
                mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            }

            public Camera getCameraInstance(){
                Camera c = null;
                try {
                    c = Camera.open(0); // attempt to get a Camera instance
                }
                catch (Exception e){
                    Toast noBackCameraToast = Toast.makeText(getApplicationContext(),"Device has no back facing camera",Toast.LENGTH_SHORT);
                    noBackCameraToast.show();
                }

                return c;
                // returns null if camera is unavailable
            }

            public void cmon(String name,int num){
                Log.e(name,Integer.toString(num));
            }

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                int height = this.getHeight();
                int width = this.getWidth();

                int rX = (int) rect.getX();
                int rY = (int) rect.getY();
                int rH = rect.getHeight();
                int rW = rect.getWidth();

                float wF = (float) CamW / width;
                float hF = (float) CamH / height;

                int imagerX = (int) (wF * rX);
                int imagerY = (int) (hF * rY);
                int imagerW = (int) (rW * wF);
                int imagerH = (int) (rH * hF);

                Bitmap whole = BitmapFactory.decodeByteArray(data,0,data.length);
                Bitmap rect = Bitmap.createBitmap(whole,imagerX,imagerY,imagerW,imagerH);

                FileOutputStream picFile;
                String fileName = uniqueErgName();
                File picFileName = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES),fileName);
                try {
                    picFile = new FileOutputStream(picFileName);
                    rect.compress(Bitmap.CompressFormat.JPEG, 100, picFile);

                    picFile.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.e("opening", "failed");
                } catch (IOException e) {
                    Log.e("writing","failed");
                    e.printStackTrace();
                }
                //ViewGroup parent = (ViewGroup) findViewById(R.id.camera_surface);

                //parent.removeAllViews();


            }

            public void setPrevPic() {
                int height = this.getHeight();
                int width = this.getWidth();
                double vR = (double) width / height;
                Camera.Parameters paramC = param;
                List<Camera.Size> prevSizes = paramC.getSupportedPreviewSizes();
                double pR;
                double epsilon = 0;
                boolean set = false;
                int cCamWidth =0;
                int cCamHeight=0;
                int bestW=0;
                int bestH=0;
                double bestAspDiff = 100;

                //TODO: THIS CAN BE IMPROVED BY WRITING THE internal loop as a while
                    for (Camera.Size s : prevSizes) {
                            //NOTICE HERE, THE CAMERA IS HARDCODED 90CC ROTATION, SO THIS IS OK
                            cCamHeight = s.width;
                            cCamWidth = s.height;

                            pR = (double) cCamWidth / cCamHeight;
                            if (Math.abs(1 - (vR / pR)) < bestAspDiff) {
                                bestH = cCamHeight;
                                bestW = cCamWidth;
                                bestAspDiff = Math.abs(1 - (vR/pR));
                            }
                    }

                CamH = bestH;
                CamW = bestW;
                paramC.setPreviewSize(bestH, bestW);
                paramC.setPictureSize(bestW, bestH);
                param = paramC;
                mCamera.setParameters(param);
            }

            public void surfaceCreated(SurfaceHolder holder) {
                // The Surface has been created, now tell the camera where to draw the preview.
                if (mCamera == null){
                    mCamera = getCameraInstance();
                    param = mCamera.getParameters();
                    orientDisplay();
                    setPrevPic();
                }

                try {
                    mCamera.reconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mCamera.setPreviewDisplay(holder);
                    mCamera.startPreview();
                } catch (IOException e) {
                    Log.d("START",e.toString());
                }
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
                  // empty. Take care of releasing the Camera preview in your activity.
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
                // If your preview can change or rotate, take care of those events here.
                // Make sure to stop the preview before resizing or reformatting it.

                if (mHolder.getSurface() == null){
                    // preview surface does not exist
                    return;
                }
                // stop preview before making changes
                try {
                    mCamera.stopPreview();
                } catch (Exception e){
                    // ignore: tried to stop a non-existent preview
                }
                //ADD CODE HERE
                // start preview with new settings
                try {
                    mCamera.setPreviewDisplay(mHolder);
                    mCamera.startPreview();

                } catch (Exception e){

                }
            }

            public boolean hasCamera() {
                return (mCamera != null);

            }
            //Currently not used properly (It doesnt work, some problem with MOTO rotation a 90CC turn returns a 270CC or 90AC turn
            //POssibly a rotation relative to front of device....
            public void orientDisplay() {
                Display display = getWindowManager().getDefaultDisplay();
                Camera.CameraInfo cI = new Camera.CameraInfo();
                Camera.getCameraInfo(0,cI);
                int baseCRot = cI.orientation;
                int screenRot = display.getRotation();
                int RotOut = ((screenRot)*90 + baseCRot) % 360;

                //(baseCRot + (screenRot*90)) % 360;
                //param.setRotation(RotOut);
                //mCamera.setParameters(param);
                mCamera.setDisplayOrientation(RotOut);
                param.setRotation(RotOut);
                mCamera.setParameters(param);
            }
        }

    }

