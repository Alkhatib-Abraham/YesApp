package com.yesapp.yesapp;

public class Posts {
    //this class defines a blueprint of the post and the needed setters and getters
        private String cityName;
        private String action;
        private String name;


        public String getCityName() {
            return cityName;
        }

        public void setCityName(String title) {
            this.cityName = title;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String content) {
            this.action = content;
        }

        public String getName(){
            return name;
        }
        public void setName(String name){

           this.name =name ;

        }


}
