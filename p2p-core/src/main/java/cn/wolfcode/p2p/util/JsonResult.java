package cn.wolfcode.p2p.util;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JsonResult {
    private boolean success = true;
    private String msg;

    public void setMsg(String msg){
        this.success = false;
        this.msg = msg;
    }

    public static JsonResult instance(){
        return new JsonResult();
    }

    public static JsonResult instance(String msg){
        JsonResult jsonResult = instance();
        jsonResult.setMsg(msg);
        return jsonResult;
    }
}
