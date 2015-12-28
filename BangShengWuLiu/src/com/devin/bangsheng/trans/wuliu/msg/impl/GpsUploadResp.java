package com.devin.bangsheng.trans.wuliu.msg.impl;

import com.devin.bangsheng.trans.wuliu.msg.AbstractWlResponse;
import com.devin.bangsheng.trans.wuliu.msg.WlBody;


public class GpsUploadResp extends AbstractWlResponse{
	
	public static class GpsUploadRespWlBody extends WlBody {
	}

	@Override
	public Class getBodyClass() {
		return GpsUploadRespWlBody.class;
	}
}
