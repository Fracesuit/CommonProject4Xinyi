package cn.xinyi.plugin.model;

/**
 * Created by zhiren.zhang on 2018/11/23.
 */

public class PersonModel {
    private String name;//姓名
    private String sex;//性别
    private String nation;//民族
    private String address;//住址
    private String idcard;//号码
    private String birthday;//出生日期

    public String getName() {
        return name;
    }

    public PersonModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getSex() {
        return sex;
    }

    public PersonModel setSex(String sex) {
        this.sex = sex;
        return this;
    }

    public String getNation() {
        return nation;
    }

    public PersonModel setNation(String nation) {
        this.nation = nation;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public PersonModel setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getIdcard() {
        return idcard;
    }

    public PersonModel setIdcard(String idcard) {
        this.idcard = idcard;
        return this;
    }

    public String getBirthday() {
        return birthday;
    }

    public PersonModel setBirthday(String birthday) {
        this.birthday = birthday;
        return this;
    }

    @Override
    public String toString() {
        return "PersonModel{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", nation='" + nation + '\'' +
                ", address='" + address + '\'' +
                ", idcard='" + idcard + '\'' +
                ", birthday='" + birthday + '\'' +
                '}';
    }
}
