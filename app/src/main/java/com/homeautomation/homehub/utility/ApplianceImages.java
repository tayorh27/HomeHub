package com.homeautomation.homehub.utility;

import com.homeautomation.homehub.R;

/**
 * Created by Control & Inst. LAB on 19-Aug-16.
 */
public class ApplianceImages {

    public static int Appliance_image(String app_name){
        int drawable = 0;
        if("fan".contentEquals(app_name.toLowerCase())){
            drawable = R.raw.fan;
        }else if("air conditioner".contentEquals(app_name.toLowerCase()) || "ac".contentEquals(app_name.toLowerCase())){
            drawable = R.raw.air_conditioners;
        }else if("bulb".contentEquals(app_name.toLowerCase()) || "light".contentEquals(app_name.toLowerCase())){
            drawable = R.raw.bulb;
        }else if("dishwasher".contentEquals(app_name.toLowerCase())){
            drawable = R.raw.dishwasher;
        }else if("hair dryer".contentEquals(app_name.toLowerCase())){
            drawable = R.raw.hair_dryer;
        }else if("heater".contentEquals(app_name.toLowerCase())){
            drawable = R.raw.heater;
        }else if("iron".contentEquals(app_name.toLowerCase())){
            drawable = R.raw.iron;
        }else if("laptop".contentEquals(app_name.toLowerCase()) || "desktop".contentEquals(app_name.toLowerCase())){
            drawable = R.raw.laptop;
        }else if("micro wave".contentEquals(app_name.toLowerCase()) || "microwave".contentEquals(app_name.toLowerCase())){
            drawable = R.raw.microwave;
        }else if("mobile phone".contentEquals(app_name.toLowerCase()) || "telephone".contentEquals(app_name.toLowerCase())){
            drawable = R.raw.mobile_phone;
        }else if("oven".contentEquals(app_name.toLowerCase())){
            drawable = R.raw.oven;
        }else if("radio".contentEquals(app_name.toLowerCase())){
            drawable = R.raw.radio;
        }else if("refrigerator".contentEquals(app_name.toLowerCase()) || "freezer".contentEquals(app_name.toLowerCase())){
            drawable = R.raw.refrigerator;
        }else if("television".contentEquals(app_name.toLowerCase()) || "tv".contentEquals(app_name.toLowerCase())){
            drawable = R.raw.television;
        }else if("toaster".contentEquals(app_name.toLowerCase()) || "toasting machine".contentEquals(app_name.toLowerCase())){
            drawable = R.raw.toaster;
        }else if("washing machine".contentEquals(app_name.toLowerCase())){
            drawable = R.raw.washing_machine;
        }else if("water dispenser".contentEquals(app_name.toLowerCase())){
            drawable = R.raw.water_dispenser;
        }else {
            drawable = R.raw.error;
        }
        return drawable;
    }
}

//iv.setImageBitmap(BitmapFactory.decodeFile(picturePath));

//uname.setText(picturePath);

//Bitmap b = BitmapFactory.decodeFile(picturePath);
//Drawable d = new BitmapDrawable(b);
//iv.setBackground(d);
