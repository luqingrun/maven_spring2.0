package com.gongsibao.common.mvc;


import com.gongsibao.common.util.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;

import java.text.DateFormat;

/**
 * Created by luqingrun on 16/3/31.
 */
public class GongsibaoCustomDateEditor extends CustomDateEditor {

    public GongsibaoCustomDateEditor(DateFormat dateFormat, boolean allowEmpty, int exactDateLength) {
        super(dateFormat, allowEmpty, exactDateLength);
    }

    public GongsibaoCustomDateEditor(DateFormat dateFormat, boolean allowEmpty) {
        super(dateFormat, allowEmpty);
    }


    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if(!StringUtils.isBlank(text)){
            int length = text.length();
            switch (length){
                case 4:
                    text = text +"-01-01 00:00:00";
                    break;
                case 7:
                    text = text +"-01 00:00:00";
                    break;
                case 10:
                    text = text +" 00:00:00";
                    break;
                case 13:
                    text = text +":00:00";
                    break;
                case 16:
                    text = text +":00";
                    break;
            }
        }
        super.setAsText(text);
    }
}
