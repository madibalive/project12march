package com.example.madiba.venualpha.Actions;

/**
 * Created by Madiba on 10/31/2016.
 */

public class ActionCompleteGossip {

   public Boolean error;
   public int chat_id;

   public ActionCompleteGossip(Boolean error, int chat_id) {
      this.error = error;
      this.chat_id = chat_id;
   }


}
