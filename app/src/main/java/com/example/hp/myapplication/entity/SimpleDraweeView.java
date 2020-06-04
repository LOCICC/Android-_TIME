package com.example.hp.myapplication.entity;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.Supplier;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.interfaces.SimpleDraweeControllerBuilder;
import com.facebook.drawee.view.GenericDraweeView;

         public class SimpleDraweeView extends GenericDraweeView {
     private static Supplier<? extends SimpleDraweeControllerBuilder> sDraweeControllerBuilderSupplier;
     private SimpleDraweeControllerBuilder mSimpleDraweeControllerBuilder;

     public static void initialize(Supplier<? extends SimpleDraweeControllerBuilder> draweeControllerBuilderSupplier) {
     }

    public static void shutDown() {
         sDraweeControllerBuilderSupplier = null;
     }
     public SimpleDraweeView(Context context, GenericDraweeHierarchy hierarchy) {
                 super(context, hierarchy);
                 this.init();
             }

     public SimpleDraweeView(Context context) {
                 super(context);
                 this.init();
             }

     public SimpleDraweeView(Context context, AttributeSet attrs) {
         super(context, attrs);
         this.init();
     }

     public SimpleDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
         this.init();
     }

     private void init() {
         if(!this.isInEditMode()) {
             Preconditions.checkNotNull(sDraweeControllerBuilderSupplier, "SimpleDraweeView was not initialized!");
         }
     }

     protected SimpleDraweeControllerBuilder getControllerBuilder() {
         return this.mSimpleDraweeControllerBuilder;
     }

     public void setImageURI(Uri uri) {
         this.setImageURI(uri, (Object)null);
     }

     public void setImageURI(Uri uri,  Object callerContext) {
         DraweeController controller = this.mSimpleDraweeControllerBuilder.setCallerContext(callerContext).setUri(uri).setOldController(this.getController()).build();
         this.setController(controller);
     }
 }
