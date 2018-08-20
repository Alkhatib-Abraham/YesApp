package com.yesapp.yesapp.classes;

public class Posts {
    //this class defines a blueprint of the post and the needed setters and getters
        private String cityName;
        private String action;
        private String name;
        private String description;
        private String postId;
        private String AuthorsEmail;



    public String getPostId() {
        return this.postId;
    }
    public void setPostId(String postId) {
        this.postId = postId;
    }


    public String getCityName() {
            return this.cityName;
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



    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getAuthorsEmail() {
        return AuthorsEmail;
    }

    public void setAuthorsEmail(String authorsEmail) {
        this.AuthorsEmail = authorsEmail;
    }
}
