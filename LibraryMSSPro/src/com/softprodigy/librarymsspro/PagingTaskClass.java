package com.softprodigy.librarymsspro;

import org.json.JSONArray;
import org.json.JSONObject;



import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
/*
 * 
 * json format for response required
 *
 {

"returnCode":{"result":"0","resultText":"success","maxVersion":"1292.2532958984375","totalRecords":12859}


"details":

[]

}
 * 
 * 
 * 
 * 
 */

public class PagingTaskClass extends AsyncTask<Void, String, Void> {
	
	
	float startingVersionNo;
	JSONObject params;
	int startingPageNo;
	String url;
	Context context;
	String versionNoParamKey,pageNoParamKey;
	int recordInOnePage;
	
	
	float currentVersionNo;
	int currentPageNo;
	
	private PagingResponseListener paginResponseListener;
	public interface PagingResponseListener{
		
		public boolean onPageResult(int currentPageProcessedNo, JSONArray dataJsonArray) throws Exception;
		
		public void error(String result,String resultText);
		
		public void onFinishPagingProcess(String maxVersion);
		
	}

	public PagingTaskClass(Context context,String url,JSONObject params,
			int startingPageNo, float startingVersionNo
			,int recordInOnePage,
			String pageNoParamKey,String versionNoParamKey
			,PagingResponseListener paginResponseListener
			
			)
	{
		this.params=params;
		this.startingPageNo=startingPageNo;
		this.currentPageNo=startingPageNo;
		this.startingVersionNo=startingVersionNo;
	this.recordInOnePage=recordInOnePage;
		this.url=url;
		this.context=context;
		this.pageNoParamKey=pageNoParamKey;
		this.versionNoParamKey=versionNoParamKey;
		this.paginResponseListener=paginResponseListener;
	}
	
	
	public void processAPIFirstTimeToGetTotalPagesAvailable()
	{
		try{
		JSONObject mParms=new JSONObject(params.toString());
		mParms.put(pageNoParamKey, startingPageNo);
		mParms.put(versionNoParamKey, startingVersionNo);
		System.out.println("paging url*********"+url);
		System.out.println("mParms *********"+mParms);
		String  response = Webservices.ApiCall(url,mParms,context);
		System.out.println("response*******"+response);
		JSONObject responseObj=new JSONObject(response);
		
		JSONObject returnCodeObj=responseObj.getJSONObject("response");
		
		String result=returnCodeObj.getString("result");
		String resultText=returnCodeObj.getString("resultText");
		//if result=0 means success else error
		if(result.equals("0")){
			String maxVersion=returnCodeObj.getString("maxVersion");
			String totalRecords=returnCodeObj.getString("totalRecords");
			
			JSONArray detailsJsonArray=responseObj.getJSONArray("details");
			
			boolean continueProcessing=paginResponseListener.onPageResult(currentPageNo,detailsJsonArray);
			if(continueProcessing)
			{
			currentPageNo=startingPageNo+1;
			currentVersionNo=startingVersionNo;
			
			// now start the paging provess for next pages bcoz we have total no of pages now
			
			int totalPages=(Integer.parseInt(totalRecords)/recordInOnePage);
			while(currentPageNo<=totalPages)
			{
				
				continueProcessing=processAfterFirstPage();
				if(!continueProcessing)
				{
					break;
				}
				
				currentPageNo++;
			}
			if(currentPageNo>totalPages)
			{
				paginResponseListener.onFinishPagingProcess(maxVersion);
			}
			}else
			{
				return;
			}
		}else
		{
			//error
			paginResponseListener.error(result,resultText);
		}
		}catch(Exception er)
		{
			er.printStackTrace();
			paginResponseListener.error("1",er.getMessage());
		}
	}
	
	
	
	boolean processAfterFirstPage()
	{
		try{
		JSONObject mParms=new JSONObject(params.toString());
		mParms.put(pageNoParamKey, currentPageNo);
		mParms.put(versionNoParamKey, currentVersionNo);
		
		String  response = Webservices.ApiCall(url,mParms,context);
		JSONObject responseObj=new JSONObject(response);
		
		JSONObject returnCodeObj=responseObj.getJSONObject("returnCode");
		
		String result=returnCodeObj.getString("result");
		String resultText=returnCodeObj.getString("resultText");
		//if result=0 means success else error
		if(result.equals("0")){
			String maxVersion=returnCodeObj.getString("maxVersion");
			String totalRecords=returnCodeObj.getString("totalRecords");
			
			JSONArray detailsJsonArray=responseObj.getJSONArray("details");
			
			boolean stopProcessing=paginResponseListener.onPageResult(currentPageNo,detailsJsonArray);
			if(stopProcessing)
			{
//			currentPageNo=startingPageNo+1;
//			currentVersionNo=startingVersionNo;
			return true;
			}else
			{
				currentPageNo=startingPageNo+1;
				currentVersionNo=startingVersionNo;
				return false;
			}
		}
		
		}catch(Exception er)
		{
			er.printStackTrace();
			paginResponseListener.error("1",er.getMessage());
		}
		return false;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		try{


			processAPIFirstTimeToGetTotalPagesAvailable();


		 }catch (Exception e) {
			// TODO: handle exception
		}
    	return null;

	}
	
	@Override
	protected void onPostExecute(Void unused) {
		


	}
//}
	
	
	
	
	    
	
}




