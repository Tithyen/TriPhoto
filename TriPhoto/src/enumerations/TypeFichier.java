package enumerations;

public enum TypeFichier {

	PHOTO (new String[] {".jpeg", ".jpg"}),
	PHOTO_SANS_EXIF (new String[] {}),
	VIDEO (new String[] {".avi"}),
	INCONNU (new String[] {});
	
	String[] extensionOk= {};
	
	TypeFichier(String[] pExtensionOk) {
		this.extensionOk = pExtensionOk;
	}
	
	public String[] getExtensionOk() {
		return this.extensionOk;
	}
	
	public void setExtensionOk(String[] pExtension) {
		this.extensionOk = pExtension;
	}
}
