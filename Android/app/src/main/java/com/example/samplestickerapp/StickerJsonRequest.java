package com.example.samplestickerapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class StickerJsonRequest {

    public static final String STICKER_PACK_IDENTIFIER_IN_QUERY = "sticker_pack_identifier";
    public static final String STICKER_PACK_NAME_IN_QUERY = "sticker_pack_name";
    public static final String STICKER_PACK_PUBLISHER_IN_QUERY = "sticker_pack_publisher";
    public static final String STICKER_PACK_ICON_IN_QUERY = "sticker_pack_icon";
    public static final String ANDROID_APP_DOWNLOAD_LINK_IN_QUERY = "android_play_store_link";
    public static final String IOS_APP_DOWNLOAD_LINK_IN_QUERY = "ios_app_download_link";
    public static final String PUBLISHER_EMAIL = "sticker_pack_publisher_email";
    public static final String PUBLISHER_WEBSITE = "sticker_pack_publisher_website";
    public static final String PRIVACY_POLICY_WEBSITE = "sticker_pack_privacy_policy_website";
    public static final String LICENSE_AGREENMENT_WEBSITE = "sticker_pack_license_agreement_website";

    public String req_url;
    private StickerPackDetailsActivity stickerPackDetailsActivity = null;
    private StickerPackListActivity stickerPackListActivity = null;

    public StickerJsonRequest(String url){
        this.req_url = url;
    }

    public void getResult(StickerPackDetailsActivity ins){

        stickerPackDetailsActivity = ins;
        MyTask myTask = new MyTask();
        myTask.execute();
    }

    public void refreshView(StickerPackListActivity ins){
        stickerPackListActivity = ins;
        RefreshTask refreshTask = new RefreshTask();
        refreshTask.execute();
    }

    private class RefreshTask extends AsyncTask<Void, Void, Void>{
        String textResult;

        public RefreshTask(){
            textResult = "";
        }

        @Override
        protected Void doInBackground(Void... params) {
            URL txturl;

            try{
                txturl = new URL(req_url);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(txturl.openStream()));
                String stringBuffer;
                String stringText = "";
                while ((stringBuffer = bufferedReader.readLine()) != null){
                    stringText += stringBuffer;
                }
                bufferedReader.close();
                textResult = stringText;

            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            Log.e("StickerJsonRequest",textResult);
            try {

                JSONObject obj = new JSONObject(textResult);

                if (obj.getJSONArray("sticker_packs").length()>0){
                    ArrayList<StickerPack> stickerPackList = new ArrayList<>();
                    stickerPackList.clear();

                    for (int i = 0 ; i < obj.getJSONArray("sticker_packs").length() ; i ++ ){
                        JSONObject tmp = obj.getJSONArray("sticker_packs").getJSONObject(i);
                        final String identifier = tmp.getString("identifier");
                        final String name = tmp.getString("name");
                        final String publisher = tmp.getString("publisher");
                        final String trayImage = tmp.getString("tray_image_file");
                        final String androidPlayStoreLink = "";
                        final String iosAppLink = "";
                        final String publisherEmail = tmp.getString("publisher_email");
                        final String publisherWebsite = tmp.getString("publisher_website");
                        final String privacyPolicyWebsite = tmp.getString("privacy_policy_website");
                        final String licenseAgreementWebsite = tmp.getString("license_agreement_website");
                        final StickerPack stickerPack = new StickerPack(identifier, name, publisher, trayImage, publisherEmail, publisherWebsite, privacyPolicyWebsite, licenseAgreementWebsite);
                        stickerPack.setAndroidPlayStoreLink(androidPlayStoreLink);
                        stickerPack.setIosAppStoreLink(iosAppLink);
                        List<Sticker> stickers = new ArrayList<>();
                        stickers.clear();
                        JSONArray stickerArray = tmp.getJSONArray("stickers");
                        if (stickerArray != null && stickerArray.length()>0){
                            for (int k = 0 ; k < stickerArray.length() ; k ++){
                                Sticker mtmp_sticker = new Sticker();
                                mtmp_sticker.setImageFileName(stickerArray.getJSONObject(k).getString("image_file"));
                                stickers.add(mtmp_sticker);
                            }
                        }
                        stickerPack.setStickers(stickers);
                        stickerPackList.add(stickerPack);
                    }
                    //Identifiers must be unique
                    HashSet<String> identifierSet = new HashSet<>();
                    for (StickerPack stickerPack : stickerPackList) {
                        if (identifierSet.contains(stickerPack.identifier)) {
                            throw new IllegalStateException("sticker pack identifiers should be unique, there are more than one pack with identifier:" + stickerPack.identifier);
                        } else {
                            identifierSet.add(stickerPack.identifier);
                        }
                    }
                    if (stickerPackList.isEmpty()) {
                        throw new IllegalStateException("There should be at least one sticker pack in the app");
                    }
                    stickerPackListActivity.showStickerPackList(stickerPackList);
                }

            } catch (Throwable tx) {
                Log.e("My App", "Could not parse malformed JSON: \"" + textResult + "\"");
            }
            super.onPostExecute(aVoid);
        }
    }

    private class MyTask extends AsyncTask<Void, Void, Void>{
        String textResult;

        public MyTask(){
            textResult = "";
        }

        @Override
        protected Void doInBackground(Void... params) {
            URL txturl;

            try{
                txturl = new URL(req_url);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(txturl.openStream()));
                String stringBuffer;
                String stringText = "";
                while ((stringBuffer = bufferedReader.readLine()) != null){
                    stringText += stringBuffer;
                }
                bufferedReader.close();
                textResult = stringText;

            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            Log.e("StickerJsonRequest",textResult);
            try {

                JSONObject obj = new JSONObject(textResult);

                if (obj.getJSONArray("sticker_packs").length()>0){
                    ArrayList<StickerPack> stickerPackList = new ArrayList<>();
                    stickerPackList.clear();

                    for (int i = 0 ; i < obj.getJSONArray("sticker_packs").length() ; i ++ ){
                        JSONObject tmp = obj.getJSONArray("sticker_packs").getJSONObject(i);
                        final String identifier = tmp.getString("identifier");
                        final String name = tmp.getString("name");
                        final String publisher = tmp.getString("publisher");
                        final String trayImage = tmp.getString("tray_image_file");
                        final String androidPlayStoreLink = "";
                        final String iosAppLink = "";
                        final String publisherEmail = tmp.getString("publisher_email");
                        final String publisherWebsite = tmp.getString("publisher_website");
                        final String privacyPolicyWebsite = tmp.getString("privacy_policy_website");
                        final String licenseAgreementWebsite = tmp.getString("license_agreement_website");
                        final StickerPack stickerPack = new StickerPack(identifier, name, publisher, trayImage, publisherEmail, publisherWebsite, privacyPolicyWebsite, licenseAgreementWebsite);
                        stickerPack.setAndroidPlayStoreLink(androidPlayStoreLink);
                        stickerPack.setIosAppStoreLink(iosAppLink);
                        List<Sticker> stickers = new ArrayList<>();
                        stickers.clear();
                        JSONArray stickerArray = tmp.getJSONArray("stickers");
                        if (stickerArray != null && stickerArray.length()>0){
                            for (int k = 0 ; k < stickerArray.length() ; k ++){
                                Sticker mtmp_sticker = new Sticker();
                                mtmp_sticker.setImageFileName(stickerArray.getJSONObject(k).getString("image_file"));
                                stickers.add(mtmp_sticker);
                            }
                        }
                        stickerPack.setStickers(stickers);
                        stickerPackList.add(stickerPack);
                    }
                    //Identifiers must be unique
                    HashSet<String> identifierSet = new HashSet<>();
                    for (StickerPack stickerPack : stickerPackList) {
                        if (identifierSet.contains(stickerPack.identifier)) {
                            throw new IllegalStateException("sticker pack identifiers should be unique, there are more than one pack with identifier:" + stickerPack.identifier);
                        } else {
                            identifierSet.add(stickerPack.identifier);
                        }
                    }
                    if (stickerPackList.isEmpty()) {
                        throw new IllegalStateException("There should be at least one sticker pack in the app");
                    }
                    final Intent intent = new Intent(stickerPackDetailsActivity, StickerPackListActivity.class);
                    intent.putParcelableArrayListExtra("sticker_pack_list", stickerPackList);
                    stickerPackDetailsActivity.startActivity(intent);
                    stickerPackDetailsActivity.finish();
                }

            } catch (Throwable tx) {
                Log.e("My App", "Could not parse malformed JSON: \"" + textResult + "\"");
            }
            super.onPostExecute(aVoid);
        }
    }
}
