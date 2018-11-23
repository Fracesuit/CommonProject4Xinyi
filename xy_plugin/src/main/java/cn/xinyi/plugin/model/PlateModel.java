package cn.xinyi.plugin.model;

/**
 * Created by zhiren.zhang on 2018/11/23.
 */

public class PlateModel {
    private String plateNumber;//车牌号码
    private String color;//车牌颜色
    private String score;//可信度


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "PlateModel{" +
                "plateNumber='" + plateNumber + '\'' +
                ", color='" + color + '\'' +
                ", score='" + score + '\'' +
                '}';
    }
}
