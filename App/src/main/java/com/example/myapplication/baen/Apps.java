package com.example.myapplication.baen;

import java.io.Serializable;
import java.util.List;

/**
 * 2 * Copyright (C), 2018, 宁波瑞泽西医疗科技有限公司
 * 3 * FileName: 应用详情实体类
 * 4 * Author: dell 许格
 * 5 * Date: 2018/6/19 10:06
 * 6 * Description: ${DESCRIPTION}
 * 7 * History:
 * 8 * desc :获取json数据应用实体类
 */
public class Apps {

    private int start;
    private int count;
    private int type;
    private List<message> message;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getCount() {
        return count;
    }

    public List<Apps.message> getMessage() {
        return message;
    }

    public void setMessage(List<Apps.message> message) {
        this.message = message;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public  static  class message implements Serializable {
        private String icon;
        private String name;
        private String version;
        private String label;
        private String packagename;
        private String newfunction;
        private int oldfunction;
        private String time;
        private String image1;
        private String image2;
        private String image3;
        private int video;
        private String developers;
        private double size;
        private int category;
        private String difficulty;
        public String urls;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getPackagename() {
            return packagename;
        }

        public void setPackagename(String packagename) {
            this.packagename = packagename;
        }

        public String getNewfunction() {
            return newfunction;
        }

        public void setNewfunction(String newfunction) {
            this.newfunction = newfunction;
        }

        public int getOldfunction() {
            return oldfunction;
        }

        public void setOldfunction(int oldfunction) {
            this.oldfunction = oldfunction;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getVideo() {
            return video;
        }

        public void setVideo(int video) {
            this.video = video;
        }

        public String getDevelopers() {
            return developers;
        }

        public void setDevelopers(String developers) {
            this.developers = developers;
        }

        public double getSize() {
            return size;
        }

        public void setSize(double size) {
            this.size = size;
        }

        public int getCategory() {
            return category;
        }

        public void setCategory(int category) {
            this.category = category;
        }

        public String getDifficulty() {
            return difficulty;
        }

        public void setDifficulty(String difficulty) {
            this.difficulty = difficulty;
        }

        public String getUrls() {
            return urls;
        }

        public void setUrls(String urls) {
            this.urls = urls;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getImage1() {
            return image1;
        }

        public void setImage1(String image1) {
            this.image1 = image1;
        }

        public String getImage2() {
            return image2;
        }

        public void setImage2(String image2) {
            this.image2 = image2;
        }

        public String getImage3() {
            return image3;
        }

        public void setImage3(String image3) {
            this.image3 = image3;
        }
    }

}
