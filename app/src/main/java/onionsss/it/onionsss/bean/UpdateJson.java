package onionsss.it.onionsss.bean;

/**
 * 作者：张琦 on 2016/5/17 18:10
 */
public class UpdateJson {
    private String versionName;
    private int versionCode;
    private String desc;
    private String url;

    public UpdateJson(String versionName, int versionCode, String desc, String url) {
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.desc = desc;
        this.url = url;
    }

    @Override
    public String toString() {
        return "UpdateJson{" +
                "versionName='" + versionName + '\'' +
                ", versionCode=" + versionCode +
                ", desc='" + desc + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
