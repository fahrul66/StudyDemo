package com.example.wulei.train;

/**
 * Created by wulei on 2016/11/8.
 */
public class TrainTimeForm {
    private String trainStation; //站点
    private String arriveTime;  //到达时间
    private String startTime;   //开车时间
    private String km;   //行驶公里

    /**
     * @param trainStation
     * @param arriveTime
     * @param startTime
     * @param km
     */
    public TrainTimeForm(String trainStation, String arriveTime,
                         String startTime, String km) {
        super();
        this.trainStation = trainStation;
        this.arriveTime = arriveTime;
        this.startTime = startTime;
        this.km = km;
    }
    public String getTrainStation() {
        return trainStation;
    }
    public void setTrainStation(String trainStation) {
        this.trainStation = trainStation;
    }
    public String getArriveTime() {
        return arriveTime;
    }
    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getKm() {
        return km;
    }
    public void setKm(String km) {
        this.km = km;
    }

}
