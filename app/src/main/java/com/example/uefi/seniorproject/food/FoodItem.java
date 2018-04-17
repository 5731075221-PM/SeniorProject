package com.example.uefi.seniorproject.food;

/**
 * Created by palida on 17-Apr-18.
 */

public class FoodItem {
    public int id,label;
    public String thai,energy,protein,fat,carbohydrate,fibre,vitE,thiamine,vitC;

    public FoodItem(int id,String thai,String energy,String protein,String fat,String carbohydrate,String fibre,String vitE,String thiamine,String vitC,int label){
        this.id = id;
        this.thai = thai;
        this.energy = energy;
        this.protein = protein;
        this.fat =fat;
        this.carbohydrate = carbohydrate;
        this.fibre = fibre;
        this.vitE = vitE;
        this.thiamine = thiamine;
        this.vitC = vitC;
        this.label = label;
    }
}