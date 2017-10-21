package com.CSU.Syx.model;

public class response {
    public class signUp {
        //注册回复
        private String msg;
        private String uid;

        public signUp(String msg, String uid){
            this.msg = msg;
            this.uid = uid;
        }
    }

    public class login {
        //登陆回复
        private String msg;
        private String uid;
        private boolean isAdmin;

        public login(String msg, String uid, boolean isAdmin){
            this.msg = msg;
            this.uid = uid;
            this.isAdmin = isAdmin;
        }
    }
}
