package com.navigasi.budi.kasibi.IIID_View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.navigasi.budi.kasibi.IIID_Controller.TouchController;
import com.navigasi.budi.kasibi.R;
import com.navigasi.budi.kasibi.coba;

@SuppressLint("ViewConstructor")
public class ModelSurfaceView extends GLSurfaceView {

    private coba parent;
    private ModelRenderer mRenderer;
    private TouchController touchHandler;

    //diganti dari model activity parent ke main activity parent
    public ModelSurfaceView(coba parent, AttributeSet attributeSet) {
        super(parent, attributeSet);

        // parent component
        this.parent = parent;

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // This is the actual renderer of the 3D space
        mRenderer = new ModelRenderer(this);
        setRenderer(mRenderer);

        //nanti dikembalikan
        touchHandler = new TouchController(this, mRenderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return touchHandler.onTouchEvent(event);
    }

    public coba getModelActivity() {
        return parent;
    }

    public ModelRenderer getModelRenderer(){
        return mRenderer;
    }
}
