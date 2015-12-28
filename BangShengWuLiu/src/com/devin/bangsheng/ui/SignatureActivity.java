package com.devin.bangsheng.ui;

import java.io.IOException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.devin.bangsheng.R;
import com.devin.bangsheng.common.Const;
import com.devin.bangsheng.view.signature.SignaturePad;
import com.devin.framework.util.DvDateUtils;
import com.devin.framework.util.DvFileUtils;
import com.newland.pay.tools.pensigner.JBigConvert;

public class SignatureActivity extends BaseActivity{
	
	private SignaturePad mSignaturePad;
    private Button mClearButton;
    private Button mSaveButton;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signature);
		initViews();
	}

	private void initViews() {
		mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onSigned() {
                mSaveButton.setEnabled(true);
                mClearButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                mSaveButton.setEnabled(false);
                mClearButton.setEnabled(false);
            }
        });

        mClearButton = (Button) findViewById(R.id.clear_button);
        mSaveButton = (Button) findViewById(R.id.save_button);

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePad.clear();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                Bitmap cpsBitmap = Bitmap.createScaledBitmap(signatureBitmap, 250, 125, true);
                String time = DvDateUtils.getCurrentDate("yyyyMMddHHmmss");
                String outPath = Const.APP_ROOT + "/temp/" + time + ".jbig";
                try {
					JBigConvert.convertToJBIG(cpsBitmap, outPath);
					Intent data = new Intent();
					data.putExtra("jbgPath", outPath);
					setResult(RESULT_OK, data);
					finish();
//					byte[] jbgData = DvFileUtils.getByteArrayFromSD(outPath);
//					System.out.println("JBG签名数据长度：" + jbgData.length);
				} catch (IOException e) {
					e.printStackTrace();
					Toast.makeText(context, "签名保存失败,请重新签名", Toast.LENGTH_LONG).show();
					mSignaturePad.clear();
				}
            }
        });
	}

	@Override
	public void onResponse(Message msg) {
		
	}
	
	@Override
	public void onBackPressed() {
	}
}
