package com.pngcui.skyworth.dlna.center;

public class DlnaMediaModel {

	private String uri = "";
	private String title = "";
	private String artist = "";
	private String album = "";
	private String albumiconuri = "";
	private String objectclass = "";
	

	public String getTitle() {
		return unicode2Chinese(title);
	}
	public void setTitle(String title) {
		this.title = (title != null ? title : "");
	}
	
	public String getArtist() {
		return unicode2Chinese(artist);
	}
	public void setArtist(String artist) {
		this.artist = (artist != null ? artist : "");
	}
	
	public void setAlbum(String album) {
		this.album = (album != null ? album : "");
	}
	public String getAlbum() {
		return unicode2Chinese(album);
	}
	
	public void setObjectClass(String objectClass) {
		this.objectclass = (objectClass != null ? objectClass : "");
	}
	public String getObjectClass() {
		return objectclass;
	}

	public void setUrl(String uri) {
		this.uri = (uri != null ? uri : "");
	}
	public String getUrl() {
		return uri;
	}
	
	public String getAlbumUri(){
		return albumiconuri;
	}
	public void setAlbumUri(String albumiconuri){
		this.albumiconuri = (albumiconuri != null ? albumiconuri : "");
	}

	//add by pngcui
	public String unicode2Chinese(String unicodeString) {

		String chineseString="";
		char[] unicodeChar = unicodeString.toCharArray();
		int i =0;
		
		for(i = 0 ; i < unicodeChar.length ;i++){
			
			if((i+7) < unicodeChar.length){

				if(unicodeChar[i]=='&' && unicodeChar[i+1] == '#' && unicodeChar[i+7] == ';'){
					String code = unicodeString.substring(i+2, i+7);
					try{
						chineseString +=(char)Integer.parseInt(code);
						i+=7;
					}catch(Exception e){
						e.printStackTrace();
					}
				}else{
					chineseString +=unicodeChar[i];
				}
				
			}else{
				chineseString +=unicodeChar[i];
			}
		}
		return chineseString;
	}
	
}
