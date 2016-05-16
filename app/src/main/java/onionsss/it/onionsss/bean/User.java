package onionsss.it.onionsss.bean;

/**
 * 作者：张琦 on 2016/5/16 20:43
 * 邮箱：759308541@qq.com
 */
public class User {
    private String name;
    private String password;
    private String sex;
    private String registtime;
    private int phone;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User() {
    }

    public User(String name, String password, String sex, String registtime, int phone) {
        this.name = name;
        this.password = password;
        this.sex = sex;
        this.registtime = registtime;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRegisttime() {
        return registtime;
    }

    public void setRegisttime(String registtime) {
        this.registtime = registtime;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }
}
